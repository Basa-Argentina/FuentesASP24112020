app.controller('marcarElementosCtrl', function ($scope,
                                                $http,
                                                $mdDialog,
                                                notifications,
                                                ambienteFactory,
                                                elementoFactory,
                                                operacionFactory
) {

    //Building "elementos" list and "leidos" list to check items
    // noinspection JSAnnotator
    let totalRegistros = 0;
    // noinspection JSAnnotator
    let registrosRestantes = 0;
    // noinspection JSAnnotator
    let registrosLeidos = 0;
    $scope.disableControlar = !$scope.$parent.noControlar;

    $scope.configAmbiente = ambienteFactory;

    $scope.showProgress = false;

    $scope.elementosList = {};

    $scope.leidosList = {};
    $scope.tipoRequerimientoId = $scope.$parent.row.entity.requerimiento.tipoRequerimiento_id;

    function warnAlert(text, sound){
        notifications.showWarning({message: text});
        if(sound){soundError.play();}
    }

    function successAlert(text, sound){
        notifications.showSuccess({message: text});
    }

    function warnAlertSoundCleanFlag(text){
        notifications.showWarning({message: text});
        soundError.play();
        $scope.flagLimpiar  = true;
    }

    $scope.$parent.row.entity.relacionOpEl.forEach(item => {
        /* Si la operacion es Busqueda de Documentacion, en el marcado de elementos, se elimina la visualizacion del codigo del Contenedor */
        var tipoREquerimiento = $scope.$parent.row.entity.requerimiento.tipoRequerimiento.codigo;
        var tipoOperacion = $scope.$parent.row.entity.tipoOperaciones.codigo;

        if (tipoREquerimiento == '16' && ['66', '22', '12'].includes(tipoOperacion)) return;

        if (item.estado === "{Pendiente}" || item.estado === "Digitalizando") {
            $scope.elementosList[item.codigoElemento.codigo] = {id: item.codigoElemento.codigo};
            $scope.leidosList[item.codigoElemento.codigo] = {elemento: ""};
        }
    });

    totalRegistros = Object.keys($scope.elementosList).length;
    registrosRestantes = totalRegistros;
    if(totalRegistros<=0){
        $scope.disableDividir = true;
    }

    //verifies if "tipo de requerimiento" is related to "consulta de legajo" to disable validations
    // noinspection JSAnnotator
    let validateElements = true;
    if (['2', '3', '4', '5', '6', '8', '10', '16', '18'].includes($scope.tipoRequerimientoId)) {
        validateElements = false;
    }

    $scope.disableAceptar = true;
    $scope.disableDividir = true;

    $scope.flagLimpiar    = false;
    let timestampIn       = 0;
    let keyPressedIn      = 0;

    /**
     *  Adds elements to "leidos" list to check the elements
     * @param keyEvent
     */
    $scope.agregarElemento = function(keyEvent) {

        keyPressedIn = keyEvent.which;

        if (timestampIn === 0) timestampIn = performance.now();

        // prevents manual entries
        const timestampOut = performance.now();

        if (keyPressedIn === 13) {
            $scope.showAlert = false;

            if ( ($scope.elementoLeido ? $scope.elementoLeido.length : 0) == 0 ) {
                warnAlertSoundCleanFlag("No puede ingresar un elemento vacío");
            }else{
                if((timestampOut - timestampIn) < 1500 || $scope.configAmbiente.ambiente === 'DEV'){ //Si el tiempo NO supera el límite impuesto
                    if ($scope.elementoLeido.length < 12){ //Elementos: 12 caracteres + caracter validador
                        $scope.showAlert    = true;
                        $scope.alertType    = "danger";
                        $scope.alertMessage = "No está permitido el ingreso manual. Debe utilizar el lector de código de barras";
                        $scope.flagLimpiar  = true;
                        soundError.play();
                    }else{
                        //looks for matches to turn green the displayed element and adds the found ones
                        $scope.elementoLeido = $scope.elementoLeido.substring(0,12);

                        var $element        = $("div.md-list-item-text h4:contains(" + $scope.elementoLeido + ")");
                        var $elementMarked  = $("md-list-item.marked div.md-list-item-text h4:contains(" + $scope.elementoLeido + ")");

                        //* Si el elemento esta Marcado Se muestra alert
                        if ($elementMarked.length >= 1 && $scope.elementoLeido.length === 12) {
                            if ($elementMarked.html().trim() == $scope.elementoLeido) {
                                warnAlertSoundCleanFlag("Elemento actualmente Ingresado.");
                                return;
                            }
                        }

                        if ($element.length >= 1 && $scope.elementoLeido.length === 12) {
                            if ($element.html().trim() == $scope.elementoLeido) {
                                $scope.verificarElemento($scope.elementoLeido)
                                    .then(() => {
                                        if (elementoFactory.codigo === 'contenedor'){
                                            warnAlert(`El elemento ${$scope.elementoLeido} ingresado posee legajos que no se encuentran "EN SALIDA". No puede continuar`, true);
                                            //$scope.$parent.answer('cancelar');
                                            $scope.disableAceptar = true;
                                            $scope.disableDividir = true;
                                            $scope.disableControlar = true;
                                            $scope.elementoLeido = '';
                                        } else{
                                            const index = $element.html().trim();
                                            if(index in $scope.leidosList && $scope.leidosList[index].elemento === "")
                                                registrosRestantes--;

                                            $scope.leidosList[index] = {elemento: $scope.elementoLeido};
                                            $element.css('color', 'green');
                                            soundOk.play();

                                            $scope.$emit('evMarcarElemento', {
                                                totalRegistros : totalRegistros ,
                                                validateElements : validateElements ,
                                                registrosRestantes: registrosRestantes
                                            });

                                        }
                                    }, () => { });
                            } else {
                                warnAlert("El elemento no pertenece al Requerimiento actual.", true);
                            }
                            // if validations are disabled adds a new element
                        } else if (!validateElements) {

                            $scope.verificarElemento($scope.elementoLeido)
                                .then(() => {
                                    if (elementoFactory.codigo === 'contenedor'){
                                        warnAlert(`El elemento ${$scope.elementoLeido} ingresado posee legajos que no se encuentran "EN SALIDA". No puede continuar`, true)
                                        //$scope.$parent.answer('cancelar');
                                        $scope.disableAceptar = true;
                                        $scope.disableDividir = true;
                                        $scope.disableControlar = true;
                                        $scope.elementoLeido = '';
                                    } else{
                                        $scope.leidosList[$scope.elementoLeido + 1000000000000] = {elemento: $scope.elementoLeido};
                                        soundOk.play();
                                        $scope.$emit('evMarcarElemento', {
                                            totalRegistros : totalRegistros ,
                                            validateElements : validateElements ,
                                            registrosRestantes: registrosRestantes
                                        });
                                    }
                                }, () => { });
                        } else {
                            warnAlert("El elemento no pertenece al Requerimiento actual.", true);
                        }
                    }
                }else{
                    warnAlertSoundCleanFlag("No está permitido el ingreso manual. Debe utilizar el lector de código de barras");
                }
            }
        }else if ( (timestampOut - timestampIn) > 1500 && $scope.configAmbiente.ambiente === "PRD") {	//Si el tiempo supera el límite impuesto
            warnAlertSoundCleanFlag("No está permitido el ingreso manual. Debe utilizar el lector de código de barras");
        }
    };

    $scope.$on('evMarcarElemento', (event, data) => {
        $scope.elementoLeidoBkp = $scope.elementoLeido;
        $scope.flagLimpiar = true; //Levanta bandera. En el evento keyUp se procederá a limpiar el input

        if (data.totalRegistros === 0 && data.validateElements) {
            $scope.disableAceptar = true;
            $scope.disableDividir = true;

        } else if (data.registrosRestantes < data.totalRegistros) {
            if (data.validateElements) {
                if(data.registrosRestantes <= 0) {
                    $scope.disableAceptar = false;
                    $scope.disableDividir = true;
                } else {
                    $scope.disableAceptar = true;
                    if($scope.tipoRequerimientoId !== "1" && data.totalRegistros > data.registrosRestantes)
                        $scope.disableDividir = false;
                }
            } else {
                $scope.realReadedList = {},
                    $.each($scope.leidosList, function (index, item) {
                        if (item.elemento != "") {
                            $scope.realReadedList[index] = {elemento: item.elemento};
                        }
                    });

                // when type is "legajo" it enable or disable "dividir" or "aceptar" according new elements where readed
                if(data.totalRegistros >= data.registrosRestantes && data.registrosRestantes !== 0) {
                    $scope.disableAceptar = true;
                    $scope.disableDividir = false;
                } else {
                    $scope.disableAceptar = false;
                    $scope.disableDividir = true;
                }
            }
        }else if (['1','2', '3', '4', '5', '6','8', '16'].includes($scope.tipoRequerimientoId)) {
            $scope.disableAceptar = false;
            $scope.disableDividir = true;
        } else {
            $scope.disableAceptar = true;
            $scope.disableDividir = true;
        }

        $scope.elementoLeido = '';

    });

    $scope.limpiarElemento = function(keyEvent) {
        keyPressedIn = keyEvent.which;	//8 Backspace //46 Supr
        if (keyPressedIn === 8 || keyPressedIn === 46) {
            $scope.elementoLeido = '';
            //$scope.flagLimpiar = false; //Bajo bandera
            timestampIn = 0;
        }
    };

    /**
     * Service call
     */
    $scope.operacionSiguiente = function(sinControl){
        let url = "";
        let leidosToSend = [];
        let elementosVerificar = [];
        $scope.disableAceptar = true;
        $scope.disableDividir = true;
        $scope.disableControlar = true;

        $scope.$parent.row.entity.fechaEntrega = new Date($scope.$parent.row.entity.fechaEntrega + "T" + $scope.$parent.row.entity.horaEntrega+"+03:00");

        let data = {};

        if(validateElements || (totalRegistros > registrosRestantes && registrosRestantes != 0)){
            if(registrosRestantes == 0)
                url = '/siguiente_operacion/legajo/siguiente';
            else
                url = "/siguiente_operacion/caja/siguiente";
        } else {
            url = '/siguiente_operacion/legajo/siguiente';
        }

        if (sinControl){
            angular.forEach($scope.elementosList, elemento => {
                leidosToSend.push(elemento.id);
            });
            if (leidosToSend.length > 0){
                $scope.verificarElemento(leidosToSend)
                    .then(() => {
                        if (elementoFactory.codigo === 'contenedor'){
                            $scope.mostrarMensaje(elementoFactory.respuesta);
                            soundError.play();
                        }else{
                            $scope.$emit('dispatchRequest', {
                                elementosLeidos: leidosToSend,
                                operacion: $scope.$parent.row.entity,
                                sinControl: sinControl ? sinControl : false,
                                url: url
                            })
                        }
                    }, () => {
                    });
            }
        } else {
            $.each($scope.leidosList, function (index, item) {
                if (item.elemento != "") {
                    leidosToSend.push(item.elemento);
                }
            });
            $scope.$emit('dispatchRequest', {
                elementosLeidos: leidosToSend,
                operacion: $scope.$parent.row.entity,
                sinControl: sinControl ? sinControl : false,
                url: url
            })
        }
    };

    $scope.$on('dispatchRequest', (event, data) => {
        operacionFactory.siguienteOperacionMarcarElementos(data)
            .then(() => {
                if(operacionFactory.respuesta) {
                    if (!$scope.disableDividir) {
                        successAlert(`Nuevo requerimiento creado: ${operacionFactory.respuesta}. Click en "Cerrar" para continuar.`, false);
                    } else if (Object.keys(operacionFactory.respuesta).length > 0) {
                        if(operacionFactory.respuesta.notExist)
                            $scope.alertMessage += `Elementos: ${operacionFactory.respuesta.notExist} no encontrados.`;
                        if(operacionFactory.respuesta.notClient)
                            $scope.alertMessage += `Elementos: ${operacionFactory.respuesta.notClient} no pertenecen al cliente.`;

                        $scope.alertMessage += `Click en "Cerrar" para continuar.`;
                        warnAlert($scope.alertMessage, false);
                    } else {
                        $scope.$parent.answer('aceptar');
                    }
                } else {
                    $scope.$parent.answer('aceptar');
                }
            }, () => {
                $scope.disableAceptar = true;
                $scope.disableDividir = true;
                $scope.disableControlar = true;
                if (operacionFactory.respuesta && operacionFactory.respuesta.data.status === 405) {
                    warnAlert("Acceso denegado.", false);
                } else {
                    warnAlert("Se ha producido un error. Intente nuevamente mas tarde.", false);
                }
            });

        /*$http({
            method: 'POST',
            url: url,
            data: JSON.stringify(data),
            headers: {'Content-Type': 'application/json'}
        }).success(function (data) {
            // handle success things
            if(data) {
                if (!$scope.disableDividir) {
                    successAlert("Nuevo requerimiento creado: " + data + ". Click en \"Cerrar\" para continuar.", false);
                } else if (Object.keys(data).length > 0) {
                    if(data.notExist) $scope.alertMessage += "Elementos: " + data.notExist + " no encontrados. ";
                    if(data.notClient) $scope.alertMessage += "Elementos: " + data.notClient + " no pertenecen al cliente. ";
                    $scope.alertMessage += "Click en \"Cerrar\" para continuar.";
                    warnAlert($scope.alertMessage, false);
                } else {
                    $scope.$parent.answer('aceptar');
                }
            } else {
                $scope.$parent.answer('aceptar');
            }
        }).error(function (data) {
            // handle error things
            $scope.disableAceptar = true;
            $scope.disableDividir = true;
            $scope.disableControlar = true;
            if (data && data.status === 405) {
                warnAlert("Acceso denegado.", false);
            } else {
                warnAlert("Se ha producido un error. Intente nuevamente mas tarde.", false);
            }
        });*/
    });

    $scope.verificarElemento = function(codigo){
        return elementoFactory.verificarElementoContenedor(codigo, $scope.getClienteAsp());
    };

    $scope.getClienteAsp = function () {
        return $scope.$parent.row.entity.clienteAsp_id;
    };

    function MensajeController($rootScope, $mdDialog, $scope){
        $scope.elementosContenedores = elementoFactory.respuesta;
    }

    $scope.mostrarMensaje = function(){

        $mdDialog.show({
            controller: MensajeController,
            templateUrl: 'dialogs/dialog_message',
            parent: angular.element(document.body),
            locals: {
                elementosContenedores: $scope.elementosContenedores
            },
            clickOutsideToClose:true,
            escapeToClose: true,
            fullscreen: true
        }).then(function(answer) {

        });
    };

});

//------ Directiva para deshabilitar el drag and drop en los ítems. Usar como drop-disable=""
app.directive('dropDisable', function() {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {

            if (scope.configAmbiente.ambiente == 'PRD'){
                var handler = function (event) {
                    event.preventDefault();
                    return false;
                };
                //element.on('dragenter', handler);
                //*element.on('dragover' , handler);
                element.on('drop'     , handler);
                //element.on('keydown'  , handler);
            }
        }
    };
});
