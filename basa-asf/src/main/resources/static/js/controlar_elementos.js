app.config(['notificationsConfigProvider', function(notificationsConfigProvider){
    notificationsConfigProvider.setAutoHide(false);
}]);

app.controller('controlarElementosCtrl', function ($scope,
                                                   $http,
                                                   $window,
                                                   CONST,
                                                   hojaRuta,
                                                   ambienteFactory,
                                                   operacionFactory,
                                                   notifications)
{

    //Building "elementos" list and "leidos" list to check items
    $scope.elementosList    = {};
    $scope.leidosList       = {};
    $scope.disableControlar = !$scope.$parent.noControlar;
    $scope.procesarTodos    = false;

    $scope.mostrarNumeroReq = false;
    $scope.configAmbiente   = ambienteFactory;

    function errorAlert(text){
        notifications.showError({
            message: text,
            hideDelay: 2000,
            hide: true
        });
    }

    function warnAlert(text, sound){
        notifications.showWarning({message: text});
        if(sound){soundError.play();}
    }

    function successAlertSound(text){
        notifications.showSuccess({message: text});
        soundOk.play();
    }

    function warnAlertSoundCleanFlag(text){
        notifications.showWarning({message: text});
        soundError.play();
        $scope.flagLimpiar  = true;
    }

    $scope.provieneDeHdR = function(nroHdR){
        return nroHdR !== undefined;
    };

    //when the value is an array it means that the app is processing the entire hdr
    if($scope.provieneDeHdR($scope.$parent.selectedHdR)){
        $scope.mostrarNumeroReq = true;

        angular.forEach($scope.$parent.row, (requerimiento) =>{
            if (requerimiento.tipo_movimiento === CONST.RQ_TIPO_MOV_EGRESO){
                angular.forEach(requerimiento.elementos, (elemento) => {
                    $scope.elementosList[elemento.codigo] = {
                        codigo       : elemento.codigo,
                        requerimiento: requerimiento.num_req,
                    };
                    $scope.leidosList[elemento.codigo] = {
                        codigo: '',
                    };
                });
            }else {
                $scope.elementosList[requerimiento.remito] = {
                    codigo       : requerimiento.remito,
                    requerimiento: requerimiento.num_req,
                    remito       : 's',
                };
                $scope.leidosList[requerimiento.remito] = {
                    codigo: '',
                };
            }
        });

        $scope.procesarTodos = true;

        /*$scope.$parent.row.forEach(row=>{
            const fila = row;
            row.relacionOpEl.forEach(function (item) {
                //only available "elementos"
                if (item.estado === "Pendiente") {
                    $scope.elementosList[item.codigoElemento.codigo] = {id: item.codigoElemento.codigo, requerimiento:fila.requerimiento.numero};
                    $scope.leidosList[item.codigoElemento.codigo] = {elemento: ""};
                }
            });
        });
        $scope.procesarTodos = true;*/
    } else {
        $scope.$parent.row.entity.relacionOpEl.forEach(function (item) {
            //only available "elementos"
            $scope.mostrarNumeroReq = false;
            if (item.estado === "Pendiente") {
                $scope.elementosList[item.codigoElemento.codigo] = {
                    codigo: item.codigoElemento.codigo
                };
                $scope.leidosList[item.codigoElemento.codigo] = {
                    codigo: ""
                };
            }
        });
    }
    $scope.totalRegistros = Object.keys($scope.elementosList).length;

    //verifies if "tipo de requerimiento" is related to "consulta de legajo" to disable validations
    const validateElements = !$scope.procesarTodos;

    $scope.disableAceptar = validateElements;

    $scope.flagLimpiar = false;
    let timestampIn    = 0;
    let keyPressedIn   = 0;

    $scope.quitarFoco  = false;

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
            }else {

                if((timestampOut - timestampIn) < 1500 || $scope.configAmbiente.ambiente === 'DEV'){ //Si el tiempo NO supera el límite impuesto
                    if ($scope.elementoLeido.length < 9){ //Elementos: 12 caracteres + caracter validador
                        warnAlertSoundCleanFlag("No está permitido el ingreso manual. Debe utilizar el lector de código de barras");
                    }else{
                        //looks for matches to turn green the displayed element and adds the found ones
                        $scope.elementoLeido = $scope.elementoLeido.substring(0,2) === '00'
                                                    ? $scope.elementoLeido.substring(2,11)
                                                    : $scope.elementoLeido.substring(0,12);

                        var $element = $("div.md-list-item-text h4:contains(" + $scope.elementoLeido + ")");

                        if ($element.length === 1 && $scope.elementosList[$scope.elementoLeido]) {
                            if($element.html().trim()==$scope.elementoLeido) {

                                const index = $element.html().trim();
                                //process multiple "requerimientos"
                                if ($scope.procesarTodos) {
                                    $scope.leidosList[index] = {elemento: $scope.elementoLeido, requerimiento:$scope.elementosList[$scope.elementoLeido].requerimiento};
                                    delete $scope.elementosList[$scope.elementoLeido];
                                } else {
                                    $scope.leidosList[index] = {elemento: $scope.elementoLeido};
                                    $element.css('color', 'green');
                                }

                                soundOk.play();
                                $scope.totalRegistros--;

                            } else {
                                warnAlert("El elemento " + $scope.elementoLeido + " no pertenece al Requerimiento actual.", true);
                            }

                        } else if (!validateElements && !$scope.procesarTodos) {
                            // if validations are disabled adds a new element
                            $scope.leidosList[$scope.elementoLeido + 1000000000000] = {elemento: $scope.elementoLeido};
                        } else if ($scope.leidosList[$scope.elementoLeido]) {
                            warnAlert("El elemento " + $scope.elementoLeido + " ya fue ingresado", true);
                            $scope.quitarFoco    = true;
                        } else {
                            warnAlert("El elemento " + $scope.elementoLeido + " no pertenece al Requerimiento actual.", true);
                            $scope.quitarFoco   = true;
                        }
                        $scope.elementoLeidoBkp = $scope.elementoLeido;
                        //timestampIn = 0;
                        $scope.flagLimpiar = true; //Levanta bandera. En el evento keyUp se procederá a limpiar el input

                        if ($scope.totalRegistros === 0) {
                            $scope.disableAceptar = false;
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
    $scope.operacionSiguiente = function(sinControl){
        let leidosToSend        = [];
        $scope.elementoToSend   = {};
        let listaIgnorados      = {};

        $scope.disableAceptar   = true;
        $scope.disableDividir   = true;
        $scope.disableControlar = true;

        if ($scope.procesarTodos) {
            $.each($scope.leidosList, function (index, elemento) {
                if(elemento.requerimiento !== undefined) {
                    if (!$scope.elementoToSend[elemento.requerimiento])
                        $scope.elementoToSend[elemento.requerimiento] = [];

                    $scope.elementoToSend[elemento.requerimiento].push(elemento.elemento);
                }
            });

            $.each($scope.elementosList, function (index, item) {
                delete $scope.elementoToSend[item.requerimiento];
                listaIgnorados[item.requerimiento] = item.requerimiento;
            });

            $.each($scope.elementoToSend,function (index, requerimiento) {
                requerimiento.forEach(item=>{
                    leidosToSend.push(item);
                });
            });

            if (leidosToSend.length > 0) {
                if (Object.keys(listaIgnorados).length > 0) {
                    /*if ($window.confirm('Lectura incompleta para Requerimiento/s ' + Object.values(listaIgnorados).join(", ") + ". Continuar e ignorarlos?")) {
                        //$scope.regenerarHdR(Object.values(listaIgnorados), leidosToSend);
                    }*/
                    warnAlert('Debe completar la lectura con todos los elementos',false);
                } else {
                    let elemLeidosReq = []; //Elementos por requerimiento

                    $.each(($scope.provieneDeHdR($scope.$parent.selectedHdR) ? $scope.$parent.row : $scope.$parent.row.entity),function (index, row) {
                        elemLeidosReq = $scope.elementoToSend[($scope.provieneDeHdR($scope.$parent.selectedHdR) ? row.num_req : row.requerimiento.numero)];
                        if (!$scope.provieneDeHdR($scope.$parent.selectedHdR)) $scope.serviceCall(elemLeidosReq, row);
                    });

                    if ($scope.provieneDeHdR($scope.$parent.selectedHdR)){
                        let data = {
                            nro_hdr         : $scope.$parent.selectedHdR,
                            elementos_leidos: leidosToSend,
                            salta_control   : false,
                        };

                        operacionFactory.siguienteOperacion(data, CONST.CONTROLAR_ELEMENTOS)
                            .then(function(){
                                $scope.$parent.answer('aceptar');
                            })
                            .catch(function(){
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
            } else {
                notifications.showWarning("Debe completar la lectura de al menos un Requerimiento.", false);
            }

        } else {
            //Cuando proviene de HdR siempre debe procesar todos los requerimientos. Por lo tanto no llega
            if (sinControl) {
                $.each($scope.elementosList, function (index, item) {
                    leidosToSend.push(item.codigo);
                });
                $scope.$parent.row.entity.fechaEntrega = new Date($scope.$parent.row.entity.fechaEntrega + "T" + $scope.$parent.row.entity.horaEntrega + "-03:00");

                $http({
                    method: 'POST',
                    url: "/siguiente_operacion/saltar_control",
                    data: JSON.stringify($scope.$parent.row.entity),
                    headers: {'Content-Type': 'application/json'}
                }).success(function () {

                })

            } else {
                $.each($scope.leidosList, function (index, item) {
                    leidosToSend.push(item.codigo);
                });
            }
            $scope.serviceCall(leidosToSend, $scope.$parent.row.entity);
        }

    };

    $scope.serviceCall = function (leidosToSend, row) {
        let url = "";
        let data = [];
        if (!row.fechaEntrega instanceof Date)
            row.fechaEntrega = new Date(row.fechaEntrega + "T" + row.horaEntrega + "-03:00");
        if(validateElements){
            url = "/siguiente_operacion/siguiente";
            data.push(row.id);

        } else {
            url = '/siguiente_operacion/legajo/siguiente';
            data = {elementosLeidos: leidosToSend, operacion: row};
        }

        $http({
            method: 'POST',
            url: url,
            data:  JSON.stringify(data),
            headers: {'Content-Type': 'application/json'}
        }).success(function (data) {
            // handle success things
            if(Object.entries(data).length !== 0) { //ameo
                notifications.showInfo("Elementos: " + data + " no encontrados");
            }
            const retorno = $scope.newHdR?$scope.newHdR:"aceptar";
            $scope.$parent.answer(retorno);
        }).error(function (data) {
            // handle error things
            $scope.disableAceptar = false;
            $scope.disableDividir = false;
            $scope.disableControlar = false;
            if (data.status === 405) {
                errorAlert("Acceso denegado.");
            }
        });
    };

    $scope.regenerarHdR = function (requerimientos, leidosToSend) {
        const nroHdR = $scope.$parent.selectedHdR != null ? $scope.$parent.selectedHdR : 0;
        $scope.leidosToSend = leidosToSend;

        $scope.filteredRows = $scope.$parent.row.entity.filter(function (el) {
            return requerimientos.indexOf(el.requerimiento.numero) === -1;
        });

        $scope.filteredRows.forEach(function (item) {
            item.fechaEntrega = new Date(item.fechaEntrega + "T" + item.horaEntrega+"-03:00");
        });

        const jsonToSend = $scope.filteredRows;
        $scope.disableAceptar = true;

        $http({

            method: 'POST',
            url: '/hoja_de_ruta/'+nroHdR+'/save/',
            data: JSON.stringify(jsonToSend),
            headers: {'Content-Type': 'application/json'}
        }).success(function (data) {
            if (data != "") {
                let source = "";
                data.Hdr = data.Hdr.toFixed(2);
                $scope.newHdR = data.Hdr;
                $scope.filteredRows.forEach(function (row) {
                    $scope.leidosToSend = $scope.elementoToSend[row.requerimiento.numero];
                    $scope.serviceCall($scope.leidosToSend, row);
                });

                localStorage.removeItem("dataHdR");
                localStorage.removeItem("stateHdR");
                printHojaRuta($scope.filteredRows, data, "");
                const w = window.open();
                source = data.http.replace("[","").replace("]","");
                $(w.document.body).html('<img src=' + source + '>');
            }
        }).error(function (data) {
            $scope.disableAceptar = false;
            if (data.status === 405) {
                errorAlert("Acceso denegado.");
            }
        });
    }

    $scope.mostrarDescripcionRemito = function(item){
        if (item.remito){
            return '[Remito]';
        }
    };
});

//------ Directiva para deshabilitar el drag and drop en los ítems. Usar como drop-disable=""
app.directive('dropDisable', function() {
    return {
        restrict: 'A',
        link: function($scope, element, attrs) {

            if ($scope.configAmbiente.ambiente == 'PRD'){
                var handler = function (event) {
                    event.preventDefault();
                    return false;
                };
                element.on('dragenter', handler);
                element.on('dragover', handler);
                element.on('drop', handler);
            }
        }
    };
});