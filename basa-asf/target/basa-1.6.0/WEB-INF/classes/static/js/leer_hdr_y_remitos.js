
app.controller('hojaRutaRemitoCtrl', function ($scope,
                                               $http,
                                               $window,
                                               CONST,
                                               hojaRutaRemito,
                                               ambienteFactory,
                                               operacionFactory,
                                               notifications)
{

    function warnAlert(text, sound){ notifications.showWarning({message: text}); if(sound){ soundError.play(); } }

    function successAlert(text, sound){ notifications.showSuccess({message: text}); }

    function errorAlert(text){ notifications.showError({message: text}); }

    //Building "items" list and "leidos" list to check items
    let totalRegistros      = 0;
    $scope.disableAceptar   = false;
    $scope.elementosList    = {};
    $scope.leidosList       = {};
    $scope.leidos           = [];

    $scope.configAmbiente = ambienteFactory;

    /* Set dialog option */
    $scope.dialog = {
        title: 'Leer HDR y Remitos',
        subtitle: 'Controlar Remito (Verificación del Remito firmado por el Cliente)',
        columnOne: {
            title   : 'Asignados',
            subtitle: '(Remito Asignado)'
        },
        columnTwo: {
            title   : 'Controlados',
            subtitle: '(Remito Controlado)'
        },
    };

    angular.forEach($scope.$parent.row, (fila) => {
        if (fila.remito !== null){
            $scope.elementosList[fila.remito] = {
                id    : fila.remito,
                numReq: fila.num_req,
            };

            $scope.leidosList[fila.remito]    = {
                elemento: "",
            };
        }
    });

    totalRegistros = Object.keys($scope.elementosList).length;

    $scope.flagLimpiar = false;
    let timestampIn    = 0;
    let keyPressedIn   = 0;

    $scope.quitarFoco = false;

    $scope.agregarElemento = function(keyEvent) {

        keyPressedIn = keyEvent.which;

        if (timestampIn === 0) timestampIn = performance.now();

        const timestampOut = performance.now();

        if (keyPressedIn === 13) {
            $scope.showAlert = false;

            if ( ($scope.elementoLeido ? $scope.elementoLeido.length : 0) == 0 ) {
                warnAlert('No puede ingresar un elemento vacío', true);
                $scope.flagLimpiar  = true;
                soundError.play();
            }else{
                if((timestampOut - timestampIn) < 1500  || $scope.configAmbiente.ambiente === 'DEV') { //Si el tiempo NO supera el límite impuesto
                    if ($scope.elementoLeido.length < 8) { //Remito 8 caracteres. Elementos: 12 caracteres
                        warnAlert('No está permitido el ingreso manual. Debe utilizar el lector de código de barras', true);
                        $scope.flagLimpiar  = true;
                        soundError.play();
                    } else {
                        $scope.elementoLeido = parseInt($scope.elementoLeido).toString().substring(0,9);

                        var $element =  $("div.md-list-item-text h4:contains(" + $scope.elementoLeido + ")");

                        if ($element.length === 1) {
                            if($element.html().trim()==$scope.elementoLeido) {
                                const index = $element.html().trim();
                                $scope.leidosList[index] = {elemento: $scope.elementoLeido};
                                $scope.leidos.push($scope.elementoLeido);
                                $element.css('color', 'green');
                                soundOk.play();
                                totalRegistros--;
                            } else {
                                warnAlert(`El remito N° ${$scope.elementoLeido} no pertenece a la Hoja de Ruta actual.`, true);
                                $scope.quitarFoco   = true;
                            }

                        } else {
                            warnAlert(`El remito N° ${$scope.elementoLeido} no pertenece a la Hoja de Ruta actual.`, true);
                            $scope.quitarFoco   = true;
                        }
                        $scope.elementoLeidoBkp = $scope.elementoLeido;
                        if (totalRegistros === 0) $scope.disableAceptar = false;
                        $scope.flagLimpiar = true; //Levanta bandera. En el evento keyUp se procederá a limpiar el input
                    }
                }else{
                    warnAlert('No está permitido el ingreso manual. Debe utilizar el lector de código de barras', true);
                    $scope.flagLimpiar = true; //Levanta bandera. En el evento keyUp se procederá a limpiar el input
                }
            }
        }else if ( (timestampOut - timestampIn) > 1500 && $scope.configAmbiente.ambiente === "PRD") {	//Si el tiempo supera el límite impuesto
            warnAlert('No está permitido el ingreso manual. Debe utilizar el lector de código de barras', true);
            $scope.flagLimpiar = true; //Levanta bandera. En el evento keyUp se procederá a limpiar el input
        }
    };

    $scope.limpiarElemento = function(keyEvent) {
        keyPressedIn = keyEvent.which;	//8 Backspace //46 Supr
        if ($scope.flagLimpiar || keyPressedIn === 8 || keyPressedIn === 46) {
            $scope.elementoLeido = '';
            $scope.flagLimpiar = false; //Bajo bandera
            timestampIn = 0;
        }

        if ($scope.quitarFoco){
            $('#elementoLeido').blur();
            $scope.quitarFoco = false;
        }
    }

    /**
     * Service call
     */
    $scope.ejecutarOperacion = function(){

        let flagContinue = false;

        if ($scope.leidos.length === 0){
            warnAlert('No se registró ningún remito',false);
        }else{

            if ($scope.$parent.row.length === $scope.leidos.length){
                flagContinue = true;
            }else{
                if ($window.confirm('Faltan remitos. Los requerimientos sin lectura de remito volverán a la operación anterior. ¿Desea continuar?')){
                    flagContinue = true;
                }else{
                    flagContinue = false;
                }
            }

            if (flagContinue){
                let data = {
                    nro_remitos_list : $scope.leidos,
                    nro_hdr          : $scope.$parent.selectedHdR,
                };

                operacionFactory.siguienteOperacion(data, CONST.LEER_HDR_Y_REMITOS)
                    .then(() => {
                        $scope.$parent.answer('aceptar');
                    })
                    .catch(() => {
                        switch (operacionFactory.codigo) {
                            case 500:
                                errorAlert(operacionFactory.respuesta);
                                break;
                            case 405:
                                errorAlert('Acceso denegado');
                                break;
                            default:
                                errorAlert('Ha ocurrido un error. Revisar log.');
                                console.error(`Código de error: ${operacionFactory.codigo}`);
                                break;
                        }
                    });
            }
        }
    }
});

app.factory('hojaRutaRemito', function () {
    return {
        dialog: {
            title: '',
            subtitle: '',
            columnOne: {title: '', subtitle  : ''},
            columnTwo: {title: '', subtitle  : ''}
        }
    }
});