var app = angular.module('BasaApp', [ 'ui.router',
                                      'ngMaterial',
                                      'ngMessages',
                                      'ui.bootstrap',
                                      'schemaForm',
                                      'ngAnimate',
                                      'ui.grid',
                                      'ui.grid.grouping',
                                      'ui.grid.edit',
                                      'ui.grid.selection',
                                      'ui.grid.exporter',
                                      'ui.grid.saveState',
                                      'ui.grid.moveColumns',
                                      'ui.grid.autoFitColumns',
                                      'ui.grid.pagination',
                                      'ui.grid.resizeColumns',
                                      'ngNotificationsBar',
                                      'BasaApp.filtroReqCtrl',
                                      'BasaApp.remitoCtrl'
]);

app.config(['notificationsConfigProvider', function(notificationsConfigProvider){
    notificationsConfigProvider.setAcceptHTML(true);
    notificationsConfigProvider.setAutoHide(false);
}]);

app.controller( 'AppCtrl',
                function ($scope,
                    $window,
                    $http,
                    $mdDialog,
                    $element,
                    $timeout,
                    $q,
                    $interval,
                    CONST,
                    listenerHdR,
                    listaHdR,
                    hojaRuta,
                    hojaRutaRemito,
                    ambienteFactory,
                    requerimientoFactory,
                    grillaHojaRutaFactory,
                    hojaRutaFactory,
                    reportFactory, reportFactory1,
                    notifications
) {
    /**
     *  Ver la acción de "ELiminar registro" de la grilla. No está funcionando correctamente
     */
    // filters init
    $scope.filtro = { };

    // defined "jefe de planta" group_id should be set here to allow or deny specific functionality.
    $scope.jefePlanta = 2;
    $scope.ambiente = undefined;

    $scope.showButtonHdR = false;

    $scope.$on('filtroAplicado',function (event,data) {
        $scope.fillGrid(data.operaciones);
        $scope.showButtonHdR = data.showButtonHdR;
        $scope.tipoRequerimientos = data.tipoRequerimientos;
        $('#requerimientosGrid').show();
    });

    $scope.$on('ocultarGrillaReq', function () {
        $('#requerimientosGrid').hide();
    });

    $("#requerimiento").click(function(){
        $scope.aplicarFiltro();
    });

    ambienteFactory.getAmbiente()
        .then(function(){
            $scope.ambiente = ambienteFactory.ambiente;
        });

    function warnAlert(text, sound){
        notifications.showWarning({message: text});
        if(sound){soundError.play();}
    }
    function successAlert(text, sound){
        notifications.showSuccess({message: text});
    }
    function errorAlert(text){
        notifications.showError({
            message: text,
            hideDelay: 2000,
            hide: true
        });
    }

    $scope.filtroReq = CONST.TEMPLATE.filtro_requerimiento;

    /* Tabs initialization */
    $scope.tabs = [
        {
            title  : 'Requerimientos',
            content: 'requerimientos content',
            url    : 'requerimientos',
            id     : "requerimiento"
        },
        {
            title  : 'Hoja de Ruta',
            content: 'Hoja de Ruta content',
            url    : 'hoja_ruta',
            id     : "hdr"
        },
        {
            title  : 'Remitos',
            content: 'Remito content',
            url    : 'remitos',
            id     : 'remito'
        },
    ];
    $scope.mensaje = {};
    $scope.dialog = $scope.dialog || { columnOne: {title: '', subtitle: ''}, columnTwo: {title: '', subtitle: ''}};
    const self = this;

    loadAll();

    function loadAll() {
        ambienteFactory.getAmbiente()
            .then(function(){
                $scope.ambiente = ambienteFactory.ambiente;
            });
        $scope.clienteIdNumero = {};
        const getPersona = function () {
            return $http.get('/personas_juridicas/read').success(function (data) {
                return data;
            });
        };
        const myDataPromise = getPersona();
        myDataPromise.then(function (result) {
            for (let i = 0; i < result.data.length; i++) {
                //Building JS object to have "numero de cliente" available instead id
                $scope.clienteIdNumero[result.data[i][0].id] = result.data[i][1];
            }
        });
        if (navigator.userAgent.indexOf("Chrome")=== -1){
            $scope.showAlert = true;
            $scope.ctrl.alertType = "info";
            $scope.ctrl.alertMessage = "Es recomendable utlizar Google Chrome para ejecutar esta aplicación";
        }
    }

    $scope.validateSession = function () {
        $http.get("/usuarios/check_session/").success(function () {
        }).error(function (data) {
            if (data.message==="Sesión expirada."){
                this.location.reload();
            }
        });
    };

    $scope.fillGrid = function(operaciones) {
        $scope.gridOptions.totalItems = operaciones.totalElements;
        $scope.gridOptions.data       = operaciones.content;
    };

    $scope.getMessageUser = function() {
        $http.get('/mensajes/usuario').success(function (data) {
            $scope.mensaje = [];
            data.forEach(nuevo => {
                $scope.mensaje[nuevo] = "no_leido";
        });
        }).error(function () {

        });
    };

    $scope.init = function() {
        $('body').css('background-color', 'blue !important');
        // const clienteId = $scope.selectedCliente != null ? $scope.selectedCliente.value : null;
        $http.defaults.headers.post['Content-Type'] = 'application/json';
        this.showAlert = false;
        if($scope.filtro.selectedTipoOperacion != 0) {
            $scope.filtro.sinHDR = false;
            $scope.hideButton    = true;
        }
        //$scope.fillGrid();
        $scope.getMessageUser();
    };

    $scope.$on('$viewContentLoaded', function(){
        $scope.init();
    });

    $scope.aplicarFiltro = function () {
        $scope.$broadcast('aplicarFiltro', function () { });
        $http.get('/mensajes/usuario').success(function (data) {
            $scope.mensaje = [];
            data.forEach(nuevo=>{
                $scope.mensaje[nuevo] = "no_leido";
            })
        }).error(function () { });
    };

     $scope.printRemitoReport = function (data) {
         let data_report = {}
         data_report  = {
             report_name : CONST.REPORT_REMITO,
             params      : {
                 param1: data
             }
         };

         reportFactory.print(data_report)
             .then(() => {
                 console.log('OK');
             }, () => {
                 console.log('FAIL');
             });
                    };

    /**
     * Adds "requerimientos" to a temporal "Hoja de Ruta"
     */
    $scope.agregarHdR = function () {
        $scope.validateSession();

        if($scope.gridApi.selection.getSelectedRows().length === 0){
            warnAlert("Debe seleccionar al menos un Requerimiento.", false);
        } else {
            let filasSeleccionadas = $scope.gridApi.selection.getSelectedRows();
            let reqSeleccionados   = [];

            angular.forEach(filasSeleccionadas, function (fila) { reqSeleccionados.push(fila.requerimiento.numero); });

            $scope.mostrarProgreso();

            requerimientoFactory.getEstadoRequerimientos(reqSeleccionados)
                .then(function () {
                    $scope.$emit('ocultarProgreso', function () {});

                    if (requerimientoFactory.respuesta.length > 0){
                        if (grillaHojaRutaFactory.cargada){
                            if (grillaHojaRutaFactory.numero_hdr_seleccionada !== '0'){

                                hojaRutaFactory.getEstadoHojaRuta(grillaHojaRutaFactory.numero_hdr_seleccionada)
                                    .then(function () {
                                        switch (hojaRutaFactory.respuesta.toLowerCase()) {
                                            case CONST.HDR_ESTADO_PENDIENTE:
                                                $mostrarDialogoConfirmacion(`Asignar a Hoja de Ruta`,                                                                   //Titulo
                                                    `La hoja de ruta N° ${grillaHojaRutaFactory.numero_hdr_seleccionada} se encuentra cargada. ¿Que desea realizar?`,   //Pregunta
                                                    `Vaciar y crear nueva hoja de ruta`,                                                                                //Option 'aceptar'
                                                    `Agregar a hoja de ruta`                                                                                            //Opción 'cancelar
                                                ).then(
                                                    function () {   //aceptar
                                                        grillaHojaRutaFactory.vaciar = true;
                                                        $scope.realizarPasaje(requerimientoFactory.respuesta, true, false);
                                                        $scope.gridApi.selection.clearSelectedRows();
                                                    },
                                                    function () {   //cancelar
                                                        $scope.realizarPasaje(requerimientoFactory.respuesta, false, false);
                                                        $scope.gridApi.selection.clearSelectedRows();
                                                    }
                                                );
                                                break;
                                            case CONST.HDR_ESTADO_DESPACHADA:
                                                $mostrarDialogoConfirmacion(`Asignar a Hoja de Ruta`,                                                                   //Titulo
                                                    `La hoja de ruta N° ${grillaHojaRutaFactory.numero_hdr_seleccionada} se encuentra cargada y despachada. Únicamente se pueden agregar retiros. ¿Que desea realizar?`,   //Pregunta
                                                    `Vaciar y crear nueva hoja de ruta`,                                                                                //Option 'aceptar'
                                                    `Agregar a hoja de ruta`                                                                                            //Opción 'cancelar
                                                ).then(
                                                    function () {   //aceptar
                                                        grillaHojaRutaFactory.vaciar = true;
                                                        $scope.realizarPasaje(requerimientoFactory.respuesta, true, false);
                                                        $scope.gridApi.selection.clearSelectedRows();
                                                    },
                                                    function () {   //cancelar
                                                        $scope.realizarPasaje(requerimientoFactory.respuesta, false, true);
                                                        $scope.gridApi.selection.clearSelectedRows();
                                                    }
                                                );
                                                break;
                                            case CONST.HDR_ESTADO_ANULADA:
                                                warnAlert(`Se detectó una Hoja de ruta pre cargada en estado ${hojaRutaFactory.respuesta}. Se anula asignación de requerimiento`,false);
                                                break;
                                            case CONST.HDR_ESTADO_FINALIZADA:
                                                warnAlert(`Se detectó una Hoja de ruta pre cargada en estado ${hojaRutaFactory.respuesta}. Se anula asignación de requerimiento`,false);
                                                break;
                                            default:
                                                errorAlert(`No se puede detectar el estado de la hoja de ruta abierta`);
                                                break;
                                        }
                                    })
                                    .catch(function () {
                                        errorAlert(`Ha ocurrido un error interno al verificar el estado de la Hoja de Ruta N°(${grillaHojaRutaFactory.numero_hdr_seleccionada}) pre cargada.`);
                                    });

                            }else{
                                $scope.realizarPasaje(requerimientoFactory.respuesta, true, false);
                                $scope.gridApi.selection.clearSelectedRows();   //Se limpian desde los if por las promesas del diálogo de confirmación
                            }

                        }else{
                            $scope.realizarPasaje(requerimientoFactory.respuesta, true, false);
                            $scope.gridApi.selection.clearSelectedRows();   //Se limpian desde los if por las promesas del diálogo de confirmación
                        }
                    }else{
                        errorAlert(`Ha ocurrido un error. No se pueden asignar a Hoja de Ruta ${(reqSeleccionados.length > 1) ? `los` : `el`} requerimiento${(reqSeleccionados.length > 1) ? `s`: ``}.`);
                    }

                })
                .catch(function () {
                    errorAlert(`Se ha producido un error interno al validar ${(reqSeleccionados.length > 1) ? `los` : `el`} requerimiento${(reqSeleccionados.length > 1) ? `s`: ``}.`);
                    $scope.$emit('ocultarProgreso', function () {});
                });
        }

    };

    $scope.realizarPasaje = function(pRequerimientos, flagNuevaHdR = true, flagReqIngreso = false){
        let reqFails = [];
        let mensaje = ``;
        $scope.requerimientoToHojaRuta(requerimientoFactory.respuesta, reqFails, flagReqIngreso);

        if (reqFails.length > 0){
            angular.forEach(reqFails, fail => {
                mensaje += `${fail.error}<br>`;
            });
        }

        if (requerimientoFactory.respuesta.length === grillaHojaRutaFactory.data.length){
            if (flagNuevaHdR)
                notifications.showInfo(`Requerimiento${(grillaHojaRutaFactory.data.length > 1 ? `s` : ``)} agregado${(grillaHojaRutaFactory.data.length > 1 ? `s` : ``)} a nueva hoja de ruta`);
            else
                notifications.showSuccess(`Requerimiento${(grillaHojaRutaFactory.data.length > 1 ? `s` : ``)} agregado${(grillaHojaRutaFactory.data.length > 1 ? `s` : ``)} a hoja de ruta N°${grillaHojaRutaFactory.numero_hdr_seleccionada}`);
        }else if(requerimientoFactory.respuesta.length === reqFails.length) {
            notifications.showError(`${reqFails.length > 1 ? `Los` : `El`} requerimiento${reqFails.length > 1 ? `s` : ``} no ha${reqFails.length > 1 ? `n` : ``} sido asignado a Hoja de Ruta.`);
            $scope.mostrarAlert(`Detalle`, mensaje);
        }else{
            if (flagNuevaHdR)
                notifications.showWarning(`Algunos requerimientos fueron asignados a la hoja de ruta`);
            else
                notifications.showWarning(`Algunos requerimientos fueron asignados a la hoja de ruta N°${grillaHojaRutaFactory.numero_hdr_seleccionada}`);
            $scope.mostrarAlert(`Detalle`, mensaje);
        }
    };

    $scope.requerimientoToHojaRuta = function(pReqsResponse, pReqFailsList, pOnlyIngreso = false){
        let reqToHdRList = [];
        angular.forEach(pReqsResponse, (reqResponse) => {
            if (reqResponse.codigo === 'OK'){
                if (pOnlyIngreso){
                    if (reqResponse.respuesta.tipo_movimiento === CONST.RQ_TIPO_MOV_INGRESO){
                        reqToHdRList.push(reqResponse.respuesta);
                    }else {
                        pReqFailsList.push({error: `Req N° ${reqResponse.respuesta.num_req} no asignado. Motivo: <i>No es de RETIRO</i>`});
                        //console.error(`Requerimiento N° ${reqResponse.respuesta.num_req}. Motivo: No es de RETIRO`);
                    }
                }else{
                    reqToHdRList.push(reqResponse.respuesta);
                }
            }else{
                pReqFailsList.push({error: `Req N° ${reqResponse.respuesta} no asignado. <i>Motivo: ${reqResponse.dato_adicional}</i>`});
                //console.error(`Requerimiento N° ${reqResponse.respuesta}. Motivo: ${reqResponse.dato_adicional}`);
            }
        });
        if (reqToHdRList.length > 0) grillaHojaRutaFactory.data = reqToHdRList;
    };

    $scope.mostrarAlert = function(pTitulo, pMensaje){
        $mdDialog.show(
            $mdDialog.alert()
                .clickOutsideToClose(true)
                .title(pTitulo)
                .htmlContent(pMensaje)
                .ok('Aceptar')
        );
    };

    function WaitController($rootScope, $mdDialog){
        $rootScope.$on('ocultarProgreso', function () {
            $mdDialog.hide();
        });
    }

    $scope.mostrarProgreso = function(){
        $mdDialog.show({
            controller: WaitController,
            template: `<md-dialog id="plz_wait" style="background-color:transparent;box-shadow:none">
                            <div layout="row" layout-sm="column" layout-align="center center" aria-label="wait">
                                <md-progress-circular md-mode="indeterminate" ></md-progress-circular>
                            </div>
                        </md-dialog>`,
            parent: angular.element(document.body),
            clickOutsideToClose:false,
            escapeToClose: false,
            fullscreen: true
        }).then(function(answer) {

        });
    };

    $mostrarDialogoConfirmacion = function(pTitulo, pPregunta, pOptionOk, pOptionCancelar){
        var confirm = $mdDialog.confirm()
            .title(pTitulo)
            .textContent(pPregunta)
            .ariaLabel('Lucky day')
            .ok(pOptionOk)
            .cancel(pOptionCancelar);

        return $mdDialog.show(confirm);
    }

    $scope.crearRemito = function (ev,row) {
        const elementsIds = [];
        row.entity.relacionOpEl.forEach(function(entry) {
            if(entry.elemento_id==null || entry.estado!="Pendiente")
                return;
            elementsIds.push(entry.elemento_id);
        });
        row.entity.elementos = {};
        $http({
            method: "POST",
            url: "/elementos/por_lista",
            data: elementsIds,
            headers: {"Content-Type": "application/json"}
        }).success(function (data) {
            row.entity.elementos.elemento = [];
            row.entity.elementos.tipoElemento = [];
            row.entity.elementos.clienteId = [];
            row.entity.elementos.estado = [];

            if(data != "") {
                data.forEach(function (elemento) {
                    row.entity.elementos.elemento += elemento.codigo + " [" + elemento.estado + "]" + "\t";
                    row.entity.elementos.tipoElemento = elemento.tipoElemento.descripcion;

                    if (elemento.clienteEmp!==null)
                        row.entity.elementos.clienteId = elemento.clienteEmp.codigo;
                    row.entity.elementos.estado = elemento.estado;
                });
            }
            $http.get("/mensajes/descripcion_remito/" + row.entity.requerimiento_id).success(function (data) {
                row.entity.mensajes=data.mensajes.replace("[","").replace("]","").split(",").join("\n");
                row.entity.numeroCliente = $scope.clienteIdNumero[row.entity.clienteEmp.personasJuridicas.id];
                $scope.renderDynamicForm(ev, row, formRemito, remitoSchema)
            }).error(function () {
            });

        }).error(function () {
        });
    };

    $scope.showOperacion = function (ev, row) {
        $scope.validateSession();

        row.entity.action = 'showOperacion';

        const tipoOperacion = row.entity.tipoOperaciones.descripcion.toLowerCase().split(" ").join("_");

        $scope.getDialogTitle(row.entity);
        $scope.getDialogSubtitle(row.entity);

        let existeHDR;

        switch (tipoOperacion) {
            case "crear_remito":
                remitoSchema.title      = $scope.dialog.title;
                remitoSchema.subtitle   = $scope.dialog.subtitle;

                $scope.crearRemito(ev,row);
                break;
            case "cargar_datos":
            case "digitalizar":
                if ( [ '59','99'].includes(row.entity.tipoOperaciones.codigo)){
                    formDigitalizar["0"].items["0"].items["0"].readonly=true;
                    formDigitalizar["2"].items["0"].items["0"].readonly=true;
                    digitalizarSchema.required = ["horasArchivistas"];
                }

                digitalizarSchema.title     = $scope.dialog.title;
                digitalizarSchema.subtitle  = $scope.dialog.subtitle;

                $scope.renderDynamicForm(ev, row, formDigitalizar, digitalizarSchema);
                break;

            case "asignar_hdr":
                row.isSelected = true;
                $scope.agregarHdR();
                break;
            case "leer_hdr_y_remitos":

                grillaHojaRutaFactory.dialog = {
                    title    : $scope.dialog.title,
                    subtitle : $scope.dialog.subtitle,
                    columnOne: {
                        title   : $scope.dialog.columnOne.title,
                        subtitle: $scope.dialog.columnOne.subtitle},
                    columnTwo: {
                        title   : $scope.dialog.columnTwo.title,
                        subtitle: $scope.dialog.columnTwo.subtitle}
                };

                requerimientoFactory.getExisteHDR(row.entity.requerimiento.id)
                    .then(function () {
                        switch (requerimientoFactory.codigo) {
                            case "ERR":
                                errorAlert('No se ha encontrado la Hoja de Ruta');
                                break;
                            case "OK":
                                grillaHojaRutaFactory.numero_hdr_a_cargar = requerimientoFactory.respuesta;
                                grillaHojaRutaFactory.event               = ev;
                                $timeout(function() {
                                    $("#hdr").children().trigger("click");  //Redirecciona a pestaña HdR
                                });
                                break;
                            default:
                                errorAlert("Ha ocurrido un error al verificar Hoja de Ruta");
                                break;
                        }
                    })
                    .catch(function () {
                        if (requerimientoFactory.codigo === "DB_100")
                            errorAlert("Error. El requerimiento se encuentra asignado a varias hojas de rutas");
                        else
                            errorAlert("Ha ocurrido un error al verificar requerimiento en Hoja de Ruta")
                    });
                break;
            case "esperar_cliente":
                $scope.renderDynamicForm(ev, row, formEnPlanta, enPlantaSchema);
                const data = [];
                data.push(row.entity.id);
                $http({
                    method: "POST",
                    url: "/siguiente_operacion/siguiente",
                    data: JSON.stringify(data),
                    headers: {"Content-Type": "application/json"}
                }).success(function () {
                    $scope.aplicarFiltro();
                }).error(function (data) {
                    if (data.status === 405) {
                        $scope.showAlert = true;
                        $scope.ctrl.alertType = "danger";
                        $scope.ctrl.alertMessage = "Acceso denegado.";
                    }
                });
                break;
            case "digitalizar_remitos":
                $scope.digitalizarRemitos(row.entity);
                break;
            case "ingresar_etiquetas":
            case "ingresar_precintos":
                formCantidad["0"].items["0"].items["0"].title   = row.entity.tipoOperaciones.descripcion;
                cantidadSchema.title                            = $scope.dialog.title;
                cantidadSchema.subtitle                         = $scope.dialog.subtitle;

                $scope.renderDynamicForm(ev, row, formCantidad, cantidadSchema);
                break;
            case "imprimir_etiquetas" :
                const dataSet = [];
                const rowToSend = [];
                dataSet.push(row.entity);
                rowToSend.push(row.entity.id);
                $scope.imprimirEtiquetas(dataSet);
                $http({
                    method: "POST",
                    url: "/siguiente_operacion/siguiente",
                    data: JSON.stringify(rowToSend),
                    headers: {"Content-Type": "application/json"}
                }).success(function () {
                    // handle success things
                    $scope.aplicarFiltro();
                }).error(function (data) {
                    // handle error things
                    if (data.status === 405) {
                        $scope.showAlert = true;
                        $scope.ctrl.alertType = "danger";
                        $scope.ctrl.alertMessage = "Acceso denegado.";
                    } else {
                        $scope.showAlert = true;
                        $scope.ctrl.alertType = "danger";
                        $scope.ctrl.alertMessage = "Error al procesar etiquetas.";
                    }
                    //$scope.showProgress = false;
                    $('#requerimientosGrid').show();
                });
                break;

            case "adjuntar_hdr":
                $scope.renderDynamicForm(ev, row, formAdjuntarHDR, adjuntarHDRSchema);
                break;
            case "verificar_digitales":
            case "correo_enviado":
                $http({
                    method: "POST",
                    url: "/siguiente_operacion/verificar_digitales",
                    data: JSON.stringify(row.entity),
                    headers: {"Content-Type": "application/json"}
                }).success(function (data) {
                    $scope.aplicarFiltro();
                    $scope.showAlert = true;
                    $scope.ctrl.alertType = "success";
                    $scope.ctrl.alertMessage = data.mensaje;
                }).error(function (data) {
                    // handle error things
                    if (data.status === 405) {
                        $scope.showAlert = true;
                        $scope.ctrl.alertType = "danger";
                        $scope.ctrl.alertMessage = "Acceso denegado.";
                    }
                });
                break;
            default:

                var option = {
                    dialog: {
                        title: $scope.dialog.title,
                        subtitle: $scope.dialog.subtitle,
                        columnOne: {title: $scope.dialog.columnOne.title, subtitle  : $scope.dialog.columnOne.subtitle},
                        columnTwo: {title: $scope.dialog.columnTwo.title, subtitle  : $scope.dialog.columnTwo.subtitle}
                    },
                };

                if (tipoOperacion === 'controlar_elementos'){
                    requerimientoFactory.getExisteHDR(row.entity.requerimiento.id)
                        .then(function () {
                            switch (requerimientoFactory.codigo) {
                                case "ERR":
                                    $scope.mostrarModal(tipoOperacion, ev, row, option);
                                    break;
                                case "OK":
                                    warnAlert("El requerimiento se encuentra asignado a la hoja de ruta N° " + requerimientoFactory.respuesta);
                                    break;
                                default:
                                    errorAlert("Ha ocurrido un error al verificar requerimiento en Hoja de Ruta");
                                    break;
                            }
                        })
                        .catch(function () {
                            if (requerimientoFactory.codigo === "DB_100")
                                errorAlert("Error. El requerimiento se encuentra asignado a varias hojas de rutas");
                            else
                                errorAlert("Ha ocurrido un error al verificar requerimiento en Hoja de Ruta")
                    });
                }else {
                    $scope.mostrarModal(tipoOperacion, ev, row, option);
                }

                break;
        }

    };

    $scope.mostrarModal = function(pTipoOperacion, pEv, pRow, pOption){
        $mdDialog.show({
            controller: DialogController,
            templateUrl: pTipoOperacion,
            parent: angular.element(document.body),
            targetEvent: pEv,
            clickOutsideToClose: false,
            locals: {
                tipoRequerimientos: $scope.tipoRequerimientos,
                operacion: $scope.items,
                row: pRow,
                checkRole: $scope.checkRole,
                jefePlanta: $scope.jefePlanta,
                option: pOption
            },
            fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
        }).then(function (answer) {
            if (answer)
                $scope.aplicarFiltro();
        }, function () {
            $scope.aplicarFiltro();
        });
    };

    /* Get Dialog Title according tipoOperaciones.codigo */
    $scope.getDialogTitle = function(entity){
        $scope.dialog.title     = entity.tipoOperaciones.descripcion.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
        var codigoTipoOperacion = entity.tipoOperaciones.codigo;

        if (['23', '55', '16'].includes(codigoTipoOperacion)){
            $scope.dialog.title = $scope.dialog.title;
        }

        if(entity.action === 'showMensaje') $scope.dialog.title = " Mensaje";
    };

    /* Get Dialog Subtitle according tipoOperaciones.codigo */
    $scope.getDialogSubtitle = function(entity){
        var codigoTipoRequerimiento = entity.requerimiento.tipoRequerimiento.codigo;
        var codigoTipoOperacion     = entity.tipoOperaciones.codigo;

        $scope.dialog.subtitle  = entity.requerimiento.tipoRequerimiento.descripcion.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
        $scope.dialog.columnOne = {title: '', subtitle: ''};
        $scope.dialog.columnTwo = {title: '', subtitle: ''};

        /* Asignar Tarea */
        if (['21', '24', '47', '34', '38'].includes(codigoTipoOperacion)){
            $scope.dialog.subtitle += " (Asignar Personal de Desposito)";
        }

        /* Crear Remito */
        if (['13', '23'].includes(codigoTipoOperacion)){
            $scope.dialog.subtitle += " (Crear Remito)";
        }

        /* Conceptos Facturables */
        if (['51', '25'].includes(codigoTipoOperacion)){
            if(codigoTipoOperacion === '25') $scope.dialog.subtitle += " (Conceptos Facturables del Elemento)";
            else if(codigoTipoOperacion === '51') $scope.dialog.subtitle += " (Conceptos Facturables del Contenedor)";
        }

        /* Cambiar Tipo de Requerimiento */
        if (['35'].includes(codigoTipoOperacion)){
            $scope.dialog.subtitle              += " (Cambiar Tipo de Requerimiento)";
            $scope.dialog.columnOne.title       += "Asignados";
            $scope.dialog.columnOne.subtitle    += "(Ingresar Contenedor)";
            $scope.dialog.columnTwo.title       += "Ingresados";
            $scope.dialog.columnTwo.subtitle    += "(Contenedor Ingresado)";
        }

        /* Ingresar Etiqueta */
        if (['67'].includes(codigoTipoOperacion)){
            $scope.dialog.subtitle += " (Generación de Etiquetas del Elemento)";
        }

        if (['22', '12', '14', '66', '69', '55', '17'].includes(codigoTipoOperacion)){
            if(['16'].includes(codigoTipoRequerimiento)) {
                if(['22', '66', '55'].includes(codigoTipoOperacion)){
                    $scope.dialog.subtitle              += " (Ingresar Elemento)";
                }
                if(['69'].includes(codigoTipoOperacion)){
                    $scope.dialog.subtitle              += " (Verificación del Elemento)";
                }
                // else $scope.dialog.subtitle += " (Verificación de los Elementos)";
                $scope.dialog.columnOne.title       += "Asignados";
                $scope.dialog.columnOne.subtitle    += "(Ingresar Elemento)";
                $scope.dialog.columnTwo.title       += "Marcados";
                $scope.dialog.columnTwo.subtitle    += "(Elemento Marcado)";
            } else {
                if(['55'].includes(codigoTipoOperacion)) $scope.dialog.subtitle += " (Verificación del Personal del Camión)";
                else $scope.dialog.subtitle              += " (Verificación del Elemento por el Personal de Deposito)";

                $scope.dialog.columnOne.title       += "Asignados";
                $scope.dialog.columnOne.subtitle    += "(Elemento Asignado)";

                if(['14', '55'].includes(codigoTipoOperacion)) {
                    $scope.dialog.columnTwo.title += "Controlados";
                    $scope.dialog.columnTwo.subtitle += "(Elemento Controlado)";
                } else {
                    $scope.dialog.columnTwo.title += "Marcados";
                    $scope.dialog.columnTwo.subtitle += "(Elemento Marcado)";
                }
            }
        }

        if (['57'].includes(codigoTipoOperacion)){
            $scope.dialog.subtitle              += " (Controlar Remito)";
            $scope.dialog.columnOne.title       += "Asignados";
            $scope.dialog.columnOne.subtitle    += "(Remito Asignado)";
            $scope.dialog.columnTwo.title       += "Controlados";
            $scope.dialog.columnTwo.subtitle    += "(Remito Controlado)";
        }

        if (['16'].includes(codigoTipoOperacion)){
            $scope.dialog.subtitle += " (Verificación del Remito por el Personal del Camión)";
        }

        if (['17'].includes(codigoTipoOperacion)){
            $scope.dialog.subtitle += " (Verificación del Remito firmado por el Cliente)";
        }

        /* Mensaje */
        if(entity.action == 'showMensaje') $scope.dialog.subtitle = entity.requerimiento.tipoRequerimiento.descripcion.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});

    };

    $scope.showMensaje = function (ev, row) {
        if(ev.currentTarget.src.indexOf('leido.png') !== -1) row.entity.action = 'showMensaje';

        $scope.getDialogTitle(row.entity);
        $scope.getDialogSubtitle(row.entity);

        var option = {
            dialog: {
                title: $scope.dialog.title,
                subtitle: $scope.dialog.subtitle,
                columnOne: {title : $scope.dialog.columnOne.title, subtitle: $scope.dialog.columnOne.subtitle},
                columnTwo: {title: $scope.dialog.columnTwo.title, subtitle  : $scope.dialog.columnTwo.subtitle}
            },
        };

        $mdDialog.show({
            controller: DialogController,
            templateUrl: "mensajes_dialog",
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            locals: {
                tipoRequerimientos: $scope.tipoRequerimientos,
                operacion: $scope.items,
                row: row,
                checkRole: $scope.checkRole,
                jefePlanta: $scope.jefePlanta,
                option: option
            },
            fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
        })
            .then(function () {
                $scope.aplicarFiltro();
            }, function () {
                $scope.status = "You cancelled the dialog.";
            });
    };

    function DialogController($scope, $mdDialog, row, tipoRequerimientos, checkRole, jefePlanta, option) {
        $scope.row = row;
        $scope.tipoRequerimientos = tipoRequerimientos;

        $scope.dialog = {title: option.dialog.title, subtitle: option.dialog.subtitle};
        if('columnOne' in option.dialog) $scope.dialog.columnOne = {title: option.dialog.columnOne.title, subtitle: option.dialog.columnOne.subtitle};
        if('columnTwo' in option.dialog) $scope.dialog.columnTwo = {title: option.dialog.columnTwo.title, subtitle  : option.dialog.columnTwo.subtitle};

        /* only jefe de planta is allowed */
        $scope.noControlar = checkRole(jefePlanta);

        $scope.hide = function () {$mdDialog.hide();};
        $scope.cancel = function () {$mdDialog.cancel();};
        $scope.answer = function (answer) {$mdDialog.hide(answer);};
    }

    $scope.isAnySubscription = function () {
        const result = false;
        for (let i = 0; i < $scope.subscriptions.length; i++) {
            if ($scope.subscriptions[i].active) {
                return true;
            }
        }
        return result;
    };

    $scope.getClientesAsp = function () {
        $http.get("/clientes_asp/read").success(function (data) {
            $scope.clientes_asp = data;
        }).error(function () {
        });
    };

    /*getOperaciones();

    function getOperaciones() {
        $http.get("/operaciones/tipos").then(function (result) {
            //return data;
            const obj = [];
            $scope.tipoOperacion = [];
            for (let i = 0; i < result.data.length; i++) {
                obj.push(
                    {
                        descripcion: result.data[i]
                    }
                );
            }
            $scope.operaciones = obj;
        })
    };*/

    $scope.clearSearchTerm = function () {
        $scope.searchTerm = '';
    };

    /*getTipoRequerimiento();

    function getTipoRequerimiento() {
        $http.get("/requerimiento/tipo_view").then(function (result) {
            //return data;
            const obj = [];
            for (let i = 0; i < result.data.length; i++) {
                obj.push(
                    {
                        value: result.data[i].id,
                        descripcion: result.data[i].descripcion
                    }
                );
            }
            $scope.tipoRequerimientos = obj;
        })
    };*/

    $http.get("/sucursales/depositos").success(function (data) {
        $scope.sucursales = data;
    })


    // The md-select directive eats keydown events for some quick select
    // logic. Since we have a search input here, we don"t need that logic.
    $element.find("input").on("keydown", function (ev) {
        ev.stopPropagation();
    });

    $scope.getElements = function ($http, $q, elementsIds) {
        const defered = $q.defer();
        const promise = defered.promise;

        $http({
            method: "POST",
            url: "/elementos/por_lista",
            data: elementsIds,
            headers: {"Content-Type": "application/json"}
        }).success(function(data) {
            defered.resolve(data);
        }).error(function(err) {
            defered.reject(err)
        });

        return promise;
    };

    $scope.checkRole = function (role) {
        return $scope.user.gruposRoles.filter(function (item) {
            return (item.group_id === role);
        }).length !== 0;
    }

    $scope.menuOptions = [
        ["Actualizar", function ($itemScope) {
            $itemScope.aplicarFiltro();
        }],
        null,
        ["Imprimir Etiqueta", function ($itemScope) {
            let dataSet = [];
            //only jefe de planta is allowed
            const autorizado = $scope.checkRole($scope.jefePlanta);

            if(autorizado) {
                if ($itemScope.gridApi.selection.getSelectedRows().length > 0) {
                    dataSet = $itemScope.gridApi.selection.getSelectedRows();
                }
                else {
                    dataSet.push($itemScope.selectedRow.entity);
                }

                $scope.imprimirEtiquetas(dataSet)
            } else {
                $scope.showAlert        = true;
                this.ctrl.alertType     = "danger";
                this.ctrl.alertMessage  = "No se encontraron etiquetas.";
            }
        }],
        null,
        ["Cargar Datos", function ($itemScope,ev) {
        let dataSet = [];
        let row = $itemScope.selectedRow;


        //only jefe de planta is allowed
        const autorizado = $scope.checkRole($scope.jefePlanta);

        if(autorizado) {

            modificarSchema.title     = $scope.dialog.title;
            modificarSchema.subtitle  = $scope.dialog.subtitle;

            $scope.renderDynamicForm( ev,row, formModificarSiempre, modificarSchema);

        } else {
            $scope.showAlert        = true;
            this.ctrl.alertType     = "danger";
            this.ctrl.alertMessage  = "Usted no puede Cargar Datos";
        }
    }],
        null,
        ["Imprimir Tareas Seleccionadas", function ($itemScope) {
            let dataSet = [];
            if($itemScope.gridApi.selection.getSelectedRows().length>0)
                dataSet = $itemScope.gridApi.selection.getSelectedRows();
            else
                dataSet.push($itemScope.selectedRow.entity);

            const promise = [];
            let tieneAsignado = false;
            let tieneElementos = false;

            dataSet.forEach(function (row) {
                const elementsIds = [];
                if(row.usrAsignado) {
                    tieneAsignado = true;
                    row.relacionOpEl.forEach(function (entry) {
                        if (entry.elemento_id==null || entry.estado!="Pendiente")
                            return;
                        elementsIds.push(entry.elemento_id);
                        tieneElementos = true;
                    });
                }

                if(tieneAsignado) promise[row.id] = $scope.getElements($http, $q, elementsIds)
            });

            if (tieneAsignado) {
                $q.all(promise)
                    .then(function (elementos) {
                        $scope.validateSession();
                        dataSet.clienteIdNumero = $scope.clienteIdNumero;
                        printTareas(dataSet, elementos);
                    });
            } else if (!tieneAsignado) {
                $scope.showAlert = true;
                this.ctrl.alertType = "danger";
                this.ctrl.alertMessage = "No se encontraron usuarios asignados.";
            } else {
                $scope.showAlert = true;
                this.ctrl.alertType = "danger";
                this.ctrl.alertMessage = "No se encontraron elementos.";
            }

        }],
        null,
        ["Reimprimir Remitos", function ($itemScope) {
                            let data_report = {}

                            if ($itemScope.gridApi.selection.getSelectedRows().length === 1) {
                                if ($itemScope.selectedRow.entity.requerimiento.remito !== null){
                                    //dataSet.requerimiento.remito.numero
                                    data_report  = {
                                        report_name : CONST.REPORT_REMITO,
                        params      : {
                            param1: $itemScope.selectedRow.entity.requerimiento.remito.id
                        }
                    };

                    reportFactory.print(data_report)
                        .then(() => {
                            console.log('OK');
                        }, () => {
                            console.log('FAIL');
                        });
                    //printRemito(dataSet, 20004, dataSet.requerimiento.remito.numero);
                }else{
                    warnAlert('Requerimiento sin remito generado.',false);
                }
            }else if ($itemScope.gridApi.selection.getSelectedRows().length === 0){
                if ($itemScope.selectedRow.entity.requerimiento.remito !== null){
                    //dataSet.requerimiento.remito.numero
                    data_report = {
                        report_name : CONST.REPORT_REMITO,
                        params      : {
                            param1: $itemScope.selectedRow.entity.requerimiento.remito.id
                        }
                    };

                    reportFactory.print(data_report)
                        .then(() => {
                            console.log('OK');
                        }, () => {
                            console.log('FAIL');
                        });
                    //printRemito(dataSet, 20004, dataSet.requerimiento.remito.numero);
                }else{
                    warnAlert('Requerimiento sin remito generado.',false);
                }
            }

        }],
        null,
        ["Imprimir Reporte de Operacion", function ($itemScope) {
            let data_report = {}

            if ($itemScope.gridApi.selection.getSelectedRows().length === 1) {
                if ($itemScope.selectedRow.entity.requerimiento.numero !== null){
                    //dataSet.requerimiento.remito.numero
                    data_report  = {
                        report_name : CONST.REPORT_OPERACION,
                        params      : {
                            param1: $itemScope.selectedRow.entity.requerimiento.numero
                        }
                    };

                    reportFactory1.print(data_report)
                        .then(() => {
                            console.log('OK');
                        }, () => {
                            console.log('FAIL');
                        });
                    //printRemito(dataSet, 20004, dataSet.requerimiento.remito.numero);
                }else{
                    warnAlert('Requerimiento No Existente.',false);
                }
            }else if ($itemScope.gridApi.selection.getSelectedRows().length === 0){
                if ($itemScope.selectedRow.entity.requerimiento.numero !== null){
                    //dataSet.requerimiento.remito.numero
                    data_report = {
                        report_name : CONST.REPORT_OPERACION,
                        params      : {
                            param1: $itemScope.selectedRow.entity.requerimiento.numero
                        }
                    };

                    reportFactory1.print(data_report)
                        .then(() => {
                            console.log('OK');
                        }, () => {
                            console.log('FAIL');
                        });
                    //printRemito(dataSet, 20004, dataSet.requerimiento.remito.numero);
                }else{
                    warnAlert('Operacion No Existe.',false);
                }
            }

        }],
        null,
        ["Reporte de Conceptos", function ($itemScope) {
            const row = $itemScope.selectedRow.entity;
            const reqId = row.requerimiento_id;

            $http.get('/operaciones/' + reqId + '/conceptos').success(function (data) {
                printConceptos(data, row);
            }).error(function () {
                $scope.showAlert = true;
                $scope.ctrl.alertType = "danger";
                $scope.ctrl.alertMessage = "Acceso denegado.";
            });

        }],
        null,
        ["Digitalizar Remitos Seleccionadas", function ($itemScope) {
            let dataSet = [];
            if($itemScope.gridApi.selection.getSelectedRows().length>0)
                dataSet = $itemScope.gridApi.selection.getSelectedRows();
            else
                dataSet.push($itemScope.selectedRow.entity);

            $itemScope.digitalizarRemitos(dataSet);

        }],
        null,
        ["Finalizar", function ($itemScope) {
            const row = $itemScope.selectedRow.entity;
            row.fechaEntrega = new Date(row.fechaEntrega + "T" + row.horaEntrega + "-03:00");
            if(row.requerimiento.hojaRuta_id===null || row.requerimiento.hojaRuta.estado==="Anulada"){
                $http({
                    method: "POST",
                    url: "/siguiente_operacion/finalizar/save",
                    data: JSON.stringify(row),
                    headers: {"Content-Type": "application/json"}
                }).success(function () {
                    $scope.aplicarFiltro();
                }).error(function (data) {
                    if (data.status === 405) {
                        $scope.showAlert = true;
                        $scope.ctrl.alertType = "danger";
                        $scope.ctrl.alertMessage = "Acceso denegado.";
                    }
                });
            } else {

                $scope.showAlert = true;
                $scope.ctrl.alertType = "danger";
                $scope.ctrl.alertMessage = "Requerimiento en Hoja de Ruta, debe ser removido antes de finalizarlo.";

            }

        }],
        null,
        ["Cancelar", function ($itemScope) {
            const row = $itemScope.selectedRow.entity;
            row.fechaEntrega = new Date(row.fechaEntrega + "T" + row.horaEntrega + "-03:00");
            if(row.requerimiento.hojaRuta_id===null || row.requerimiento.hojaRuta.estado==="Anulada"){
                $http({
                    method: "POST",
                    url: "/siguiente_operacion/cancelar/save",
                    data: JSON.stringify(row),
                    headers: {"Content-Type": "application/json"}
                }).success(function () {
                    $scope.aplicarFiltro();
                }).error(function (data) {
                    if (data.status === 405) {
                        $scope.showAlert = true;
                        $scope.ctrl.alertType = "danger";
                        $scope.ctrl.alertMessage = "Acceso denegado.";
                    }
                });
            } else {
                        $scope.showAlert = true;
                        $scope.ctrl.alertType = "danger";
                        $scope.ctrl.alertMessage = "Requerimiento en Hoja de Ruta, debe ser removido antes de cancelarlo.";
                    }

        }],
    ];

    const tipoTemplate = '<div ng-if=\"!row.groupHeader\" class="pointer"><span>' +
        '<img height="29px" ng-src="img/{{row.entity.tipoOperaciones.descripcion.substring(0,2)==\'Z-\'?'+
        '\'Requerimiento antiguo\':row.entity.tipoOperaciones.descripcion}}.png\"' +
        ' ng-click="grid.appScope.showOperacion($event,row)"/>' +
        '<md-tooltip md-direction="\'right\'">{{row.entity.tipoOperaciones.descripcion}}</md-tooltip>' +
        '</div>';

    const sortFn = function (aDate,bDate) {
        if(aDate!==null && bDate!==null) {
            const a = Number(aDate.split("/")[2] + aDate.split("/")[1] + aDate.split("/")[0]);
            const b = Number(bDate.split("/")[2] + bDate.split("/")[1] + bDate.split("/")[0]);

            if (a < b) {
                return -1;
            }
            else if (a > b) {
                return 1;
            }
            else {
                return 0;
            }
        }
    };

    $scope.setColors = function(rowValue) {
        const today = new Date();
        if (rowValue<today)
            return "color: red";
        else
            return "";
    };

    const reqTemplate = '<div ng-if="!row.groupHeader"><a class="ui-grid-cell-contents" ng-click="grid.appScope.editRow(grid, row)">' +
        '{{row.entity.requerimiento.numero}}</a>' +
        '<md-tooltip md-direction="\'right\'">{{row.entity.estado}}</md-tooltip>' +
        '</div>';

    const messageTemplate = '<div ng-if="!row.groupHeader" class="pointer"><span>' +
        '<img height="29px" ng-src="img/{{grid.appScope.mensaje[row.entity.requerimiento_id]?grid.appScope.mensaje[row.entity.requerimiento_id]:\'leido\'}}.png\"' +
        ' ng-click="grid.appScope.showMensaje($event,row)"/>' +
        '<md-tooltip md-direction="\'right\'">{{grid.appScope.mensaje[row.entity.requerimiento_id]?\'Nuevos Mensajes\':\'\'}}</md-tooltip>' +
        '</div>';

    $scope.getHojaRuta =  function (row) {
        let hayPendiente = false;
        let nroHdR = "";
        if(row.relacionOpEl.length>0) {
            row.relacionOpEl.forEach(function (itemRel) {
                itemRel.hdRxOperacion.forEach(function (item) {
                    if(!hayPendiente) {
                        nroHdR = item.hojaRuta.numero;
                        if (item.hojaRuta.estado === "Pendiente") {
                            nroHdR = item.hojaRuta.numero;
                            hayPendiente = true;
                        }
                    }
                });
            });
        }
        return nroHdR;
    };

    $scope.requerimientoVencido = function (row) {
        let vencido = false;
        if (row.entity.estado ) {
            if (!row.entity.estado.includes("Finalizado")) {
                vencido = new Date(row.entity.fechaEntrega + "T" + row.entity.horaEntrega + "-03:00") < new Date();
            }
        }
        return vencido;
    };

    let rowTemplate= '<div ng-class="{ \'grid-resaltada-row\':grid.appScope.requerimientoVencido(row) }">' +
        '  <div ng-if="row.entity.merge">{{row.entity.title}}</div>' +
        '  <div ng-if="!row.entity.merge" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }"  ui-grid-cell></div>' +
        '</div>';

    $scope.gridOptions = {
        rowHeight: 30,
        enableFiltering: true,
        enableGroupHeaderSelection: true,
        treeRowHeaderAlwaysVisible: true,
        showColumnFooter: true,
        enableGridMenu: true,
        saveFocus: false,
        saveScroll: true,
        saveGroupingExpandedStates: true,
        paginationPageSizes: [25, 50, 100, 250, 500, 1000],
        paginationPageSize: 25,
        enableAutoFitColumns: true,
        useExternalPagination: false,
        rowTemplate: rowTemplate,
        columnDefs: [
            {
                field: 'fechaEntregaString',
                name: 'Fecha Entrega',
                grouping: {groupPriority: 0},
                sort: {priority: 1, direction: 'desc'},
                enableCellEdit: false ,
                groupingShowAggregationMenu: false,
                //cellTemplate: fechaTemplate,
                sortingAlgorithm: sortFn
            },
            {
                field: 'depositos.descripcion',
                name: 'Sucursal',
                sort: {priority: 0, direction: 'desc'},
                enableCellEdit: false ,
                groupingShowAggregationMenu: false
            },
            {
                field: 'turno',
                name: 'Turno',
                sort: {priority: 2, direction: 'asc'},
                enableCellEdit: false ,
                groupingShowAggregationMenu: false
            },
            {
                field: 'tipoOperacion_id',
                name: 'Op',
                cellClass: "grid-align",
                cellTemplate: tipoTemplate,
                enableCellEdit: false,
                groupingShowAggregationMenu: false
            },
            {
                field: 'clienteEmp.personasJuridicas.razonSocial',
                name: 'Cliente',
                enableCellEdit: false,
                groupingShowAggregationMenu: false
            },
            {
                field: 'requerimiento.tipoRequerimiento.descripcion',
                name: 'Tipo Requerimiento',
                sort: {priority: 3, direction: 'asc'},
                enableCellEdit: false,
                groupingShowAggregationMenu: false
            },
            {
                field: 'requerimiento_id',
                name: 'Msj',
                enableCellEdit: false,
                groupingShowAggregationMenu: false,
                cellClass: "grid-align",
                cellTemplate: messageTemplate,
            },
            {
                field: 'requerimiento.numero',
                name: 'N°Req',
                enableCellEdit: false,
                groupingShowAggregationMenu: false,
                cellTemplate: reqTemplate
            },
            {
                field: 'requerimiento.cantidad',
                name: 'Elem',
                enableCellEdit: false,
                groupingShowAggregationMenu: false
            },
            {
                field: 'requerimiento.empAutorizante.personasFisicas.nombreCompleto',
                name: 'Responsable',
                enableCellEdit: false,
                groupingShowAggregationMenu: false
            },
            {
                field: 'requerimiento.remito.numero',
                name: 'N°Remito',
                enableCellEdit: false,
                groupingShowAggregationMenu: false
            },
            {
                field: 'requerimiento.hojaRuta.numero',
                name: 'N°HdRuta',
                enableCellEdit: false,
                groupingShowAggregationMenu: false,
                //cellTemplate: hDrTemplate
            },
            {
                field: 'usrAsignado.personasFisicas.nombreCompleto',
                name: 'Asignado',
                enableCellEdit: false,
                groupingShowAggregationMenu: false
            },
            {
                field: 'observaciones',
                name: 'Observaciones',
                enableCellEdit: false,
                groupingShowAggregationMenu: false
            },
        ],
        exporterLinkLabel: 'get your csv here',
        exporterIsExcelCompatible: true,
        exporterOlderExcelCompatibility: true,
        exporterPdfDefaultStyle: {fontSize: 9},
        exporterPdfTableStyle: {margin: [30, 30, 30, 30]},
        exporterPdfTableHeaderStyle: {fontSize: 10, bold: true, italics: true, color: 'blue'},
        exporterPdfOrientation: 'landscape',
        exporterPdfPageSize: 'A4',
        exporterPdfMaxGridWidth: 500,
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            // interval of zero just to allow the directive to have initialized
            $interval( function() {
                $scope.gridApi.core.addToGridMenu( $scope.gridApi.grid, [{
                title: 'Guardar grilla',
                order: 600,
                action: function () {$scope.saveState()}
            }]);
            $scope.gridApi.core.addToGridMenu( $scope.gridApi.grid, [{
                title: 'Restaurar grilla',
                order: 700,
                action: function () {$scope.restoreState()}
            }]);
            }, 0, 1);

            $scope.gridApi.grid.registerDataChangeCallback(function () {
                $scope.gridApi.treeBase.expandAllRows();
            });

            // Setup events so we're notified when grid state changes.
            $scope.gridApi.colMovable.on.columnPositionChanged($scope, $scope.saveState);
            $scope.gridApi.colResizable.on.columnSizeChanged($scope, $scope.saveState);
            $scope.gridApi.grouping.on.aggregationChanged($scope, $scope.saveState);
            $scope.gridApi.grouping.on.groupingChanged($scope, $scope.saveState);
            $scope.gridApi.core.on.columnVisibilityChanged($scope, $scope.saveState);
            $scope.gridApi.core.on.filterChanged($scope, $scope.saveState);
            $scope.gridApi.core.on.sortChanged($scope, $scope.saveState);
        }
    };

    $scope.getCurrentUser = function () {
        $http.get('/usuarios/current').success(function (states) {
            const userData = states;
            $scope.user = userData;
            $scope.usuarioActual = userData.username;

            /* $timeout(function(){
                $scope.aplicarFiltro();
            }); */
        }).error(function () {
        });
    };

    $scope.saveState = function () {
        const state = JSON.stringify($scope.gridApi.saveState.save());
        localStorage.setItem("requerimientos", state);
    };
    $scope.getCurrentUser();

    /*$scope.restoreState = function () {
        $scope.$on('$viewContentLoaded', function(){
            if (localStorage.getItem("requerimientos") === null)
                $scope.saveState();

            if($scope.gridOptions.data.length!==0)
                $scope.gridApi.saveState.restore($scope, JSON.parse(localStorage.getItem("requerimientos")));
            $scope.gridApi.core.refresh();
        });
    };*/

    $scope.hideButton = true;
    $scope.hideHdRSelector = true;

    // grid export definitions
    $scope.export = function () {
        if ($scope.export_format === 'csv') {
            const myElement = angular.element(document.querySelectorAll(".custom-csv-link-location"));
            $scope.gridApi.exporter.csvExport($scope.export_row_type, $scope.export_column_type, myElement);
        } else if ($scope.export_format === 'pdf') {
            $scope.gridApi.exporter.pdfExport($scope.export_row_type, $scope.export_column_type);
        }
    };

    /**
     *  Row edition dialog dispatcher
     * @param ev
     * @param row
     */
    $scope.editRow = function (ev, row) {
        const elementsIds = [];
        row.entity.relacionOpEl.forEach(function(entry) {
            if(entry.elemento_id===null )
                return;
            elementsIds.push(entry.elemento_id);
        });
        row.entity.elementos = {};
        $http({
            method: 'POST',
            url: '/elementos/por_lista',
            data: elementsIds,
            headers: {'Content-Type': 'application/json'}
        }).success(function (data) {
            row.entity.elementos.elemento = [];
            row.entity.elementos.tipoElemento = [];
            row.entity.elementos.tipoTrabajo = [];
            row.entity.elementos.clienteId = [];
            row.entity.elementos.estado = [];

            if(data != "") {
                data.forEach(function (elemento) {
                    row.entity.elementos.elemento += elemento.codigo + "\n";
                    row.entity.elementos.tipoElemento += elemento.tipoElemento.descripcion + "\n";
                    row.entity.elementos.tipoTrabajo = elemento.tipoTrabajo  !== null ? elemento.tipoTrabajo : '' + "\n";
                    if (elemento.clienteEmp!==null)
                        row.entity.elementos.clienteId += elemento.clienteEmp.codigo + "\n";
                        row.entity.elementos.estado += elemento.estado + "\n";
                });
            }
            //const fechaAlta = new Date(row.entity.fechaAlta + "T" + row.entity.horaAlta + "-03:00");
            const fechaAlta = new Date(row.entity.fechaAlta+ "T" + row.entity.horaAlta + "-03:00");
            row.entity.fechaAltaString = fechaAlta.toLocaleDateString();
            $scope.renderDynamicForm(ev, row, formRequerimiento, requerimientoSchema);
        }).error(function () {
        });
    };

    /**
     * Dynamic Form dispatcher
     * @param ev
     * @param row
     * @param formToLoad
     * @param schemaToLoad
     */
    $scope.renderDynamicForm = function (ev, row, formToLoad, schemaToLoad) {
        $scope.selectedRow  = row;
        $scope.formToLoad   = formToLoad;
        $scope.schemaToLoad = schemaToLoad;

        $mdDialog.show({
            controller:  $scope.dynamicFormCtrl,
            controllerAs: 'ctrl',
            templateUrl: 'edit-modal',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            onFocus: autosize(document.querySelectorAll('textarea')),
            locals: {
                operacion: $scope.items
            },
        }).then(function (answer) {
            row.entity = angular.extend($scope.modalData.row.entity, $scope.modalData.ctrl.entity);
            if (answer === "generaRemito") {
                const fila = angular.copy(row.entity);
                const facturable = row.entity.factura;
                fila.fechaEntrega = new Date(fila.fechaEntrega + "T" + fila.horaEntrega + "-03:00");

                const jsonToSend = {
                    "operacion": fila,
                    "facturable": facturable,
                    "tipoEnvio": row.entity.tipoEnvio,
                    "observaciones": row.entity.observaciones
                };
                $http({
                    method: 'POST',
                    url: '/siguiente_operacion/remitos/save',
                    data: JSON.stringify(jsonToSend),
                    headers: {'Content-Type': 'application/json'}
                }).success(function (data) {
                   // printRemito(row.entity, 20004, data);
                    $scope.printRemitoReport(data);
                    $scope.aplicarFiltro();

                }).error(function (data) {
                    // handle error things
                    if (data.status === 405) {
                        $scope.showAlert = true;
                        $scope.ctrl.alertType = "danger";
                        $scope.ctrl.alertMessage = "Acceso denegado.";
                    }
                });
            }
        }, function () {
            $scope.status = 'You cancelled the dialog.';
        });
    };

    /**
     * Row edition controller, uses bootstrap schema, definitions in forms.js & schemas.js
     */
    $scope.dynamicFormCtrl = function () {
        const row = $scope.selectedRow;
        const today = new Date();

        const ctrl = this;
        ctrl.schema = $scope.schemaToLoad;
        ctrl.entity = angular.copy(row.entity);
        ctrl.entity.today = today.getDate() + '/' + (today.getMonth()+1) + '/' + today.getFullYear();
        ctrl.entity.tipoEnvio = "";
        ctrl.entity.tipoRemito="";
        ctrl.entity.cantidad = row.entity.requerimiento.cantidad!==null?row.entity.requerimiento.cantidad:0;
        /* agregar manejo de mensajeria */
        //ctrl.entity.observaciones="";
        ctrl.form = $scope.formToLoad;

        ctrl.cancel = function () {
            $mdDialog.cancel();
            formDigitalizar["0"].items["0"].items["0"].readonly=false;
            formDigitalizar["2"].items["0"].items["0"].readonly=false;
            digitalizarSchema.required = ["horasArchivistas", "cantidadHojas", "cantidadElementos"];
        };
        ctrl.answer = function (answer,dynamicForm,modelData) {
            $scope.$broadcast('schemaFormValidate');
            $scope.modalData = {};
            $scope.modalData = {'row': row, 'ctrl': ctrl};

            // fix timezone to avoid date change on save data
            row.entity.fechaEntrega = new Date(row.entity.fechaEntrega + "T" + row.entity.horaEntrega + "-03:00");

            // switches between different actions depending of the form loaded
            switch (answer) {
                case "generaRemito":
                    if (ctrl.entity.tipoEnvio == "") {
                        ctrl.showAlert = true;
                        ctrl.alertType = "danger";
                        ctrl.alertMessage = "Debe completar todos los campos";
                    }
                    else
                        $mdDialog.hide(answer);
                    break;
                case "guardarDigital":
                    let soloCantidad = [ '59','99'].includes(row.entity.tipoOperaciones.codigo);
                    if (soloCantidad)
                        cantidadHojas.value = cantidadElementos.value = 0;

                    if ((cantidadHojas.value != "" && horasArchivistas.value != "" && cantidadElementos.value != "") || soloCantidad) {
                        $(".btn-group").hide();
                        var respuesta = {
                            operacion: row.entity,
                            hojas: cantidadHojas.value,
                            horas: horasArchivistas.value,
                            elementos: cantidadElementos.value,
                        };
                        $http({
                            method: 'POST',
                            url: '/siguiente_operacion/digitalizar/save',
                            data: JSON.stringify(respuesta),
                            headers: {'Content-Type': 'application/json'}
                        }).success(function () {
                            ctrl.cancel();
                            $scope.aplicarFiltro();
                        }).error(function (data) {
                            $(".btn-group").show();
                            // handle error things
                            if (data.status === 405) {
                                $scope.showAlert = true;
                                $scope.ctrl.alertType = "danger";
                                $scope.ctrl.alertMessage = "Acceso denegado.";
                            }
                        });
                        if (soloCantidad){
                            formDigitalizar["0"].items["0"].items["0"].readonly=false;
                            formDigitalizar["2"].items["0"].items["0"].readonly=false;
                            digitalizarSchema.required = ["horasArchivistas", "cantidadHojas", "cantidadElementos"];
                        }
                    } else {
                        ctrl.showAlert = true;
                        ctrl.alertType = "danger";
                        ctrl.alertMessage = "Debe completar todos los campos";
                    }

                    break;
                  //Prueba

                case "guardarModificarSiempre":
                  /*  let soloCantidad = [ '59','99'].includes(row.entity.tipoOperaciones.codigo);
                    if (soloCantidad)
                        cantidadHojas.value = cantidadElementos.value = 0;

                    if ((cantidadHojas.value != "" && horasArchivistas.value != "" && cantidadElementos.value != "") || soloCantidad) {
                        $(".btn-group").hide();*/
                        var respuesta = {
                            operacion: row.entity,
                            cantidadImagenes: cantidadImagenes.value,
                            cantidadImagenesPlanos: cantidadImagenesPlanos.value,
                            fletes: fletes.value,
                            horasArchivistas: horasArchivistas.value,
                        };
                        $http({
                            method: 'POST',
                            url: '/siguiente_operacion/modificarSiempre/save',
                            data: JSON.stringify(respuesta),
                            headers: {'Content-Type': 'application/json'}
                        }).success(function () {
                            ctrl.cancel();
                            $scope.aplicarFiltro();
                        }).error(function (data) {
                            $(".btn-group").show();
                            // handle error things
                            if (data.status === 405) {
                                $scope.showAlert = true;
                                $scope.ctrl.alertType = "danger";
                                $scope.ctrl.alertMessage = "Acceso denegado.";
                            }
                        });
                     /*   if (soloCantidad){
                            formDigitalizar["0"].items["0"].items["0"].readonly=false;
                            formDigitalizar["2"].items["0"].items["0"].readonly=false;
                            digitalizarSchema.required = ["horasArchivistas", "cantidadHojas", "cantidadElementos"];
                        }
                    } else {
                        ctrl.showAlert = true;
                        ctrl.alertType = "danger";
                        ctrl.alertMessage = "Debe completar todos los campos";
                    }*/

                    break;




                case "guardarCantidad":
                    if (cantidad.value != "") {

                        const serviceData = {
                            operacion: row.entity,
                            cantidad: cantidad.value,
                        };

                        ctrl.cancel();
                        //$scope.showProgress = true;
                        $('#requerimientosGrid').hide();

                        $http({
                            method: 'POST',
                            url: '/siguiente_operacion/cantidad/save',
                            data: JSON.stringify(serviceData),
                            headers: {'Content-Type': 'application/json'}
                        }).success(function () {
                            $scope.aplicarFiltro();
                        }).error(function (data) {
                            // handle error things
                            $scope.showAlert = true;
                            $scope.ctrl.alertType = "danger";
                            $scope.ctrl.alertMessage = data.status===405?"Acceso denegado.":data.message;
                            //$scope.showProgress = false;
                            $('#requerimientosGrid').show();
                        });
                    } else {
                        ctrl.showAlert = true;
                        ctrl.alertType = "danger";
                        ctrl.alertMessage = "Debe completar todos los campos";
                    }

                    break;
                case "guardarHdR":
                    if (nroHdR.value != "") {
                        const respuesta = {
                            operacion: row.entity,
                            numeroHdR: nroHdR.value,
                        };
                        const fila = [];
                        fila.push(respuesta.operacion);
                        $http({
                            method: 'POST',
                            url: '/siguiente_operacion/adjuntar_hdr/save',
                            data: JSON.stringify(respuesta),
                            headers: {'Content-Type': 'application/json'}
                        }).success(function (data) {
                            ctrl.cancel();
                            $scope.aplicarFiltro();
                            printHojaRuta(fila, data, "");
                        }).error(function (data) {
                            // handle error things
                            if (data.status === 405) {
                                $scope.showAlert = true;
                                $scope.ctrl.alertType = "danger";
                                $scope.ctrl.alertMessage = "Acceso denegado.";
                            } else {
                                ctrl.showAlert = true;
                                ctrl.alertType = "danger";
                                ctrl.alertMessage = data.message;
                            }
                        });
                    } else {
                        ctrl.showAlert = true;
                        ctrl.alertType = "danger";
                        ctrl.alertMessage = "Debe completar todos los campos";
                    }

                    break;

                default:
                    if (dynamicForm.$valid) {
                        modelData.fechaEntrega = new Date(modelData.fechaEntrega + "T" + row.entity.horaEntrega + "-03:00");
                        const jsonToSend = JSON.stringify(modelData);
                        $http({
                            method: 'POST',
                            url: '/operaciones/save_fecha',
                            data: jsonToSend,
                            headers: {'Content-Type': 'application/json'}
                        }).success(function () {
                            $scope.aplicarFiltro();
                        }).error(function (data) {
                            // handle error things
                            if (data.status === 405) {
                                $scope.showAlert = true;
                                $scope.ctrl.alertType = "danger";
                                $scope.ctrl.alertMessage = "Acceso denegado.";
                            }
                        });

                        $mdDialog.hide(answer);
                    }
                    break;
            }
        };

        let executed = false;
            ctrl.autoResize = function () {
                $("select[name='tipoEnvio']")[0][0].hidden=true;
                if(!executed) {
                    if ($("select[name='tipoEnvio']")[0]) {
                        $("select[name='tipoEnvio']")[0][0].hidden = true;
                        $("option[label='propio']")[0].selected = true;
                        ctrl.entity.tipoEnvio = "propio";
                    }
                    autosize(document.querySelectorAll('textarea'));
                    executed = true;
                }
        }
    };

    /**
     * Adds digitalizar remitos logic
     * @param dataSet
     */
    $scope.digitalizarRemitos = function (dataSet) {
        const rowToSend = [];

        // verifies if one or more rows where selected
        if(dataSet instanceof Array) {
            dataSet.forEach(function (row) {
                if (row.tipoOperacion_id === "18" && row.requerimiento.remito_id) {
                    rowToSend.push(row.id);
                }
            });
        } else {
            if (dataSet.tipoOperacion_id === "18" && dataSet.requerimiento.remito_id) {
                rowToSend.push(dataSet.id);

            }
        }

        if(rowToSend.length > 0) {
            $http({
                method: 'POST',
                url: "/siguiente_operacion/siguiente",
                data: JSON.stringify(rowToSend),
                headers: {'Content-Type': 'application/json'}
            }).success(function () {
                // handle success things
                $scope.aplicarFiltro();
            }).error(function (data) {
                // handle error things
                if (data.status === 405) {
                    $scope.showAlert = true;
                    $scope.ctrl.alertType = "danger";
                    $scope.ctrl.alertMessage = "Acceso denegado.";
                }
            });
        } else {
            $scope.showAlert = true;
            this.ctrl.alertType = "danger";
            this.ctrl.alertMessage = "No se encontraron remitos en estado correcto.";
        }
    };

    /**
     *  Adds "Imprimir Etiquetas" functionality
     * @param rows
     */
    $scope.imprimirEtiquetas = function (rows) {
        const promise = [];
        //$scope.showProgress = true;
        $('#requerimientosGrid').hide();

        /* retrieves all elements of the selected row and make a promise to wait getElements */
        rows.forEach(function (row) {
            const elementsIds = [];
            row.relacionOpEl.forEach(function (entry) {
                if(entry.elemento_id===null || (entry.estado!=="Pendiente" && entry.estado!=="Digitalizando"))
                    return;
                elementsIds.push(entry.elemento_id);
            });
            promise[row.id] = $scope.getElements($http, $q, elementsIds)
        });

        $q.all(promise).then(function (elementos) {
            if (Array.isArray(elementos) && elementos[Object.keys(elementos)[0]].length) {
                printEtiqueta(rows, elementos);
                //$scope.showProgress = false;
                $('#requerimientosGrid').show();
            } else {
               $scope.showAlert         = true;
               $scope.ctrl.alertType    = "danger";
               $scope.ctrl.alertMessage = "No se encontraron etiquetas.";
               //$scope.showProgress      = false;
                $('#requerimientosGrid').show();
           }
        });
    }
});

app.factory('ambienteFactory',['$http','$q', function ($http, $q) {

    var self = {
        ambiente: '',

        getAmbiente: function () {
            var defer = $q.defer();

            $http.get('js/config.json')
            .success(function (data) {
                self.ambiente = data.environment;
                defer.resolve();
            })
            .error(function () {
                self.ambiente = "PRD";
                defer.reject();
            });

            return defer.promise;
        },
    };
    return self;
}]);

/**
 *  replaces the custom action for left-click with a dropdown menu
 */
app.directive('ngContextMenu', function () {
    const renderContextMenu = function ($scope, event, options) {
        $scope.selectedRow = angular.element(event.toElement).scope().row;
        $(event.currentTarget).addClass('context');
        const $contextMenu = $('<div>');
        $contextMenu.addClass('dropdown clearfix');
        const $ul = $('<ul>');
        $ul.addClass('dropdown-menu');
        $ul.attr({'role': 'menu'});
        $ul.css({
            display: 'block',
            position: 'absolute',
            left: event.pageX + 'px',
            top: event.pageY + 'px'
        });
        angular.forEach(options, function (item) {
            const $li = $('<li>');
            if (item === null) {
                $li.addClass('divider');
            } else {
                const $a = $('<a>');
                $a.attr({tabindex: '-1', href: '#'});
                $a.text(item[0]);
                $li.append($a);
                $li.on('click', function () {
                    $scope.$apply(function () {
                        item[1].call($scope, $scope);
                    });
                });
            }
            $ul.append($li);
        });
        $contextMenu.append($ul);
        $contextMenu.css({
            width: '100%',
            height: '100%',
            position: 'absolute',
            top: 0,
            left: 0,
            zIndex: 9999
        });
        $(document).find('body').append($contextMenu);
        $contextMenu.on("click", function () {
            $(event.currentTarget).removeClass('context');
            $contextMenu.remove();
        }).on('contextmenu', function (event) {
            $(event.currentTarget).removeClass('context');
            event.preventDefault();
            $contextMenu.remove();
        });
    };
    return function ($scope, element, attrs) {
        element.on('contextmenu', function (event) {
            $scope.$apply(function () {
                event.preventDefault();
                const options = $scope.$eval(attrs.ngContextMenu);
                if (options instanceof Array) {
                    renderContextMenu($scope, event, options);
                } else {
                    throw '"' + attrs.ngContextMenu + '" not an array';
                }
            });
        });
    };

});

//------ Directiva para utilizar alertas flotantes
app.directive('alerta', function() {
    return {
        restrict: 'E',
        templateUrl: 'alerta'
    };
});