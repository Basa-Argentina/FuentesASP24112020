app.config(['notificationsConfigProvider', function(notificationsConfigProvider){
    notificationsConfigProvider.setAutoHide(false);
}]);
app.controller('cambiarTipoCtrl', function ($scope, $http, ambienteFactory, notifications) {
    const ERROR_GENERAL = 'Ocurrio un error en la aplicacion.';
    const ERROR_CONNECTION = 'No se puede conectar al servidor.';
    const ERROR_SERVER = 'Ocurrio un error en el servidor.';
    const ERROR_NOT_PRIVILEGE = 'No posee privilegios para realizar esta acción.';

    function typeError(status) {
        switch (status) {
            case -1:
                return ERROR_CONNECTION;
                break;
            case 403:
                return ERROR_NOT_PRIVILEGE;
                break;
            case 500:
                return ERROR_SERVER;
                break;
            default:
                return ERROR_GENERAL;
        }
    }

    function errorAlert(text){
        notifications.showError({
            message: text,
            hideDelay: 2000,
            hide: true
        });
    }

    function warnAlertSound(text){
        notifications.showWarning({message: text});
        soundError.play();
    }

    let totalRegistros   = 0;
    $scope.elementosList = {};
    $scope.leidosList    = {};

    $scope.configAmbiente = ambienteFactory;

    /* Los tipos de requerimientos disponibles para el cambio de tipo son:
        2 CONSULTA NORMAL DE LEGAJOS
        4 CONSULTA NORMAL DE CAJA
        8 CONSULTA DIGITAL
    */
    $scope.tiposReqPermitido = ["02","04","06","07","08"];

    $scope.tipoRequerimientos = $scope.$parent.tipoRequerimientos.filter(tipoRequerimiento => ($scope.tiposReqPermitido.includes(tipoRequerimiento.codigo)));

    $scope.$parent.row.entity.relacionOpEl.forEach(item => {
        if (item.estado === "Pendiente") {
            $scope.elementosList[item.codigoElemento.codigo] = {id: item.codigoElemento.codigo}
            $scope.leidosList[item.codigoElemento.codigo] = {elemento: ""};
        }
    });
    totalRegistros = Object.keys($scope.elementosList).length;

    //verifies if "tipo de requerimiento" is related to "consulta de legajo" to disable validations
    let validateElements  = false;

    $scope.disableAceptar = false;

    $scope.flagLimpiar    = false;
    let timestampIn       = 0;
    let keyPressedIn      = 0;

    $scope.agregarElemento = function(keyEvent) {

        keyPressedIn = keyEvent.which;

        if (timestampIn===0) timestampIn = performance.now();

        const timestampOut = performance.now();

        if (keyPressedIn === 13) {

            if ( ($scope.elementoLeido ? $scope.elementoLeido.length : 0) == 0 ) {
                errorAlert("No puede ingresar un elemento vacío");
            }else{
                if((timestampOut - timestampIn) < 1500 || $scope.configAmbiente.ambiente === 'DEV'){ //Si el tiempo NO supera el límite impuesto
                    if ($scope.elementoLeido.length < 12){ //Elementos: 12 caracteres + caracter validador
                        errorAlert("No está permitido el ingreso manual. Debe utilizar el lector de código de barras");
                    }else{
                        $scope.checkElementClient($scope.elementoLeido, $scope.row.entity.clienteEmp_id)
                            .then(result=>{
                                if (result.data) {
                                    $scope.leidosList[$scope.elementoLeido + 1000000000000] = {elemento: $scope.elementoLeido};
                                } else {
                                    warnAlertSound(`El elemento ${$scope.elementoLeido} no pertenece al cliente.`);
                                }
                                $scope.elementoLeidoBkp = $scope.elementoLeido;
                                $scope.flagLimpiar = true; //Levanta bandera. En el evento keyUp se procederá a limpiar el input

                                if (totalRegistros === 0) {
                                    $scope.disableAceptar = false;
                                }
                                $scope.elementoLeido = "";
                        }).then(err=>{
                            console.error(err);
                        });

                    }
                }else{
                    errorAlert("No está permitido el ingreso manual. Debe utilizar el lector de código de barras");
                }
            }
        }else if ( (timestampOut - timestampIn) > 1500 && $scope.configAmbiente.ambiente === "PRD") {	//Si el tiempo supera el límite impuesto
            errorAlert("No está permitido el ingreso manual. Debe utilizar el lector de código de barras");
        }
    };

    $scope.limpiarElemento = function(keyEvent) {
        keyPressedIn = keyEvent.which;	//8 Backspace //46 Supr
        if ($scope.flagLimpiar || keyPressedIn === 8 || keyPressedIn === 46) {
            $scope.elementoLeido = '';
            $scope.flagLimpiar = false; //Bajo bandera
            timestampIn = 0;
            console.log('Input cleaned');
        }
    }

    $scope.checkElementClient = function (codigo, idClient) {
        let url = `/siguiente_operacion/${codigo}/${idClient}/verificar_elemento`;

        return $http({
            method: 'GET',
            url: url,
            headers: {'Content-Type': 'application/json'}
        }).success((data) => data).
        error((err, status) => {
            notifications.showError({message: typeError(status)});
        });
    }

    $scope.operacionSiguiente = function() {
        if ($scope.selectedTipoRequerimiento) {
            const url = '/siguiente_operacion/legajo/siguiente';
            let leidosToSend = [];
            $.each($scope.leidosList, function (index, item) {
                if (item.elemento != "") {
                    leidosToSend.push(item.elemento);
                }
            });
            $scope.$parent.row.entity.fechaEntrega = new Date($scope.$parent.row.entity.fechaEntrega + "T" + $scope.$parent.row.entity.horaEntrega + "-03:00");
            const data = {
                elementosLeidos: leidosToSend,
                operacion: $scope.$parent.row.entity,
                tipoRequerimiento: $scope.selectedTipoRequerimiento
            };

            $http({
                method: 'POST',
                url: url,
                data: JSON.stringify(data),
                headers: {'Content-Type': 'application/json'}
            }).success(function (data) {
                if(data){
                    $scope.showAlert = true;
                    $scope.alertType = "info";
                    $scope.alertMessage = "Elementos: " + data + " no encontrados";
                }
                // handle success things
                $scope.$parent.answer('aceptar');
            }).error(function (data) {
                // handle error things
                if (data.status === 405) {
                    errorAlert("Acceso denegado.");
                }
            });
        } else {
            errorAlert("Debe seleccionar un tipo de requerimiento.");
        }
    }
});