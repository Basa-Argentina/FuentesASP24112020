app.run(['i18nService', function (i18nService) {
    i18nService.setCurrentLang('es');
}]);

app.controller('HdRCtrl', function ($scope,
                                    $http,
                                    $mdDialog,
                                    $element,
                                    $timeout,
                                    $q,
                                    $modal,
                                    CONST,
                                    listenerHdR,
                                    listaHdR,
                                    hojaRuta,
                                    hojaRutaRemito,
                                    grillaHojaRutaFactory,
                                    hojaRutaFactory,
                                    notifications
) {

    function warnAlert(text, sound){ notifications.showWarning({message: text}); if(sound){ soundError.play(); } }

    function successAlert(text, sound){ notifications.showSuccess({message: text}); }

    function errorAlert(text){ notifications.showError({message: text}); }

    // Environment init
    $scope.hdrSeleccionada   = null;//{ display: null, value: null};
    $scope.selectedHdR       = '0';
    $scope.removedRows       = [];
    $scope.dataHdR           = [];

    //----------------------- Buttons
    //Show buttons
    $scope.enableControlar       = false;
    $scope.enableReimprimir      = false;
    $scope.enableGuardarImprimir = false;

    //Labels buttons
    $scope.labelButtonImprimir   = 'Guardar e Imprimir';    //Sólo se muestra cuando la HdR se está creando
    $scope.labelButtonReImprimir = 'Reimprimir';            //Se mostrará cuando se consulte una HdR. Si se le asignan nuevos requerimientos o se quitan, mostrará "Crear nueva versión"
    $scope.labelButtonControlar  = 'Controlar';             //"Leer elementos", "Leer remitos". Dependerá del tipo de operación en el que se encuentren los requerimientos
    $scope.labelButtonVaciar     = 'Vaciar';

    //----------------------- Watches
    $scope.$watch(function () {                             //Carga HdR desde Requerimiento
        return grillaHojaRutaFactory.numero_hdr_a_cargar;
    }, function (newValue) {
        if (newValue && newValue !== ''){
            $scope.hdrSeleccionada = {
                display: newValue,
                value  : newValue,
            };
            $scope.setearSeleccion($scope.hdrSeleccionada)
                .then(() => {
                    $scope.mostrarModal(grillaHojaRutaFactory.event);
                    grillaHojaRutaFactory.numero_hdr_a_cargar = ''; //Reseteo valor
            });
        }
    });

    //----- Determina si la grilla se encuentra cargada o no
    $scope.$watch(function () {
        return $scope.gridOptionsHdR.data;
    },function (nuewValue) {
        if ($scope.gridOptionsHdR.data.length !== 0)
            grillaHojaRutaFactory.cargada = true;
        else
            grillaHojaRutaFactory.cargada = false;
    });

    //----- Determina si la factoria se encuentra cargada para agregar los requerimientos a la grilla de HdR
    $scope.$watch(function () {
        return grillaHojaRutaFactory.data;
    },function (nuevosRequerimientos) {
        if (nuevosRequerimientos && nuevosRequerimientos.length > 0){
            if (grillaHojaRutaFactory.vaciar){
                $scope.vaciarGrillaHdR(true);
                grillaHojaRutaFactory.vaciar = false;
            }
            $scope.cargarGrillaHdR(grillaHojaRutaFactory.data);
            //No evalua botón controlar
            $scope.setShowButtons(false);
            grillaHojaRutaFactory.data = [];
        }
    });

    $scope.vaciarGrillaHdR = function(limpiaNumero = false){
        $scope.gridOptionsHdR.data = [];

        hojaRuta.event             = "";

        if (limpiaNumero){
            $scope.ctrl.searchText     = "";
            $scope.selectedHdR         = '0';
            //Vacío data de factory
            grillaHojaRutaFactory.numero_hdr_seleccionada = '0';
        }
    };

    $scope.cargarGrillaHdR = function(pRequerimientos){

        let reqToGrillaHdr = [];

        angular.forEach($scope.gridOptionsHdR.data, (reqEnHdR) => { reqToGrillaHdr.push(reqEnHdR); });  //Pierde referencia

        angular.forEach(pRequerimientos, (nuevoReq) => {
            let agregarReq = true;
            angular.forEach($scope.gridOptionsHdR.data, (reqEnHdR) => {
                if (reqEnHdR.id_req === nuevoReq.id_req){
                    agregarReq = false;
                    return;
                }
            });
            if (agregarReq)
                reqToGrillaHdr.push(nuevoReq);  //Con array.push no se dispara el $watch
        });

        $scope.gridOptionsHdR.data = reqToGrillaHdr;    //Dispara $watch
    };

    // Grid specs
    $scope.gridOptionsHdR = {
        rowHeight                      : 30,
        enableFiltering                : true,
        enableGroupHeaderSelection     : true,
        treeRowHeaderAlwaysVisible     : true,
        showColumnFooter               : true,
        enableGridMenu                 : true,
        saveFocus                      : false,
        saveScroll                     : true,
        saveGroupingExpandedStates     : true,
        paginationPageSizes            : [25, 50, 100, 250, 500, 1000],
        paginationPageSize             : 250,
        enableAutoFitColumns           : true,
        columnDefs                     : [
            {
                field: 'codigo_cliente',            //clienteEmp.codigo
                name: 'Cliente',
                enableCellEdit: false,
                groupingShowAggregationMenu: false,
            },
            {
                field: 'num_req',                   //'requerimiento.numero',
                name: 'Requerimiento',
                enableCellEdit: false,
                groupingShowAggregationMenu: false,
            },
            {
                field: 'desc_tipo_req',             //'requerimiento.tipoRequerimiento.descripcion',
                name: 'Tipo Requerimiento',
                enableCellEdit: false,
                groupingShowAggregationMenu: false,
            },
            {
                field: 'cant_elementos',            //'cantidadPendientes',
                name: 'Elementos',
                enableCellEdit: false,
                groupingShowAggregationMenu: false,
            },
            {
                field: 'localidad_cliente',         //'requerimiento.clientesDirecciones.localidades.nombre',
                name: 'Localidad',
                enableCellEdit: false,
                groupingShowAggregationMenu: false,
            },
            {
                field: 'provincia_cliente',         //'requerimiento.clientesDirecciones.provincias.nombre',
                name: 'Provincia',
                enableCellEdit: false,
                groupingShowAggregationMenu: false,
            },
            {
                field: 'direccion_cliente',         //'requerimiento.clientesDirecciones.direcciones.direccionCompleta',
                name: 'Direccion',
                enableCellEdit: false,
                groupingShowAggregationMenu: false,
            },
            {
                field: 'usuario_asignado',
                name: 'Usuario',
                enableCellEdit: false,
                groupingShowAggregationMenu: false,
            },{
                field: 'fecha_entrega',
                name: 'Fecha Entrega',
                enableCellEdit: false,
                groupingShowAggregationMenu: false,
            },
        ],
        exporterLinkLabel              : 'get your csv here',
        exporterIsExcelCompatible      : true,
        exporterOlderExcelCompatibility: true,
        exporterPdfDefaultStyle        : { fontSize: 9 },
        exporterPdfTableStyle          : { margin  : [30, 30, 30, 30] },
        exporterPdfTableHeaderStyle    : { fontSize: 10, bold: true, italics: true, color: 'blue' },
        exporterPdfOrientation         : 'landscape',
        exporterPdfPageSize            : 'A4',
        exporterPdfMaxGridWidth        : 500,
        onRegisterApi: function (gridApiHdR) {
            $scope.gridApiHdR = gridApiHdR;
            // Auto expand all rows
            $scope.gridApiHdR.grid.registerDataChangeCallback(function () {
                $scope.gridApiHdR.treeBase.expandAllRows();
            });
        }
    };

    /*$scope.inicializarDatos = function (all) {
        if (all) {
            $http.get('/personas_juridicas/read').success(function (data) {
                for (let i = 0; i < data.length; i++) {
                    //Building JS object to have "numero de cliente" available instead id
                    $scope.clienteIdNumero[data[i][0].id] = data[i][1];
                }
            });
        }
    };
    $scope.inicializarDatos(true);*/

    const self = this;
    setInputNumHdR();
    self.searchText = "";
    self.querySearch = querySearch;

    $scope.setearSeleccion = function (seleccionada) {
        let defer = $q.defer();
        if (seleccionada) {
            $scope.selectedHdR = seleccionada.value; //$scope.hdrSeleccionada;
            $scope.aplicarFiltro()
                .then( () => { defer.resolve(); })
                .catch(() => { defer.reject();  });
        }
        else{
            $scope.selectedHdR = '0';
            defer.resolve();
        }
        //Seteo factoría para poder ver desde Ctrl Requerimientos
        grillaHojaRutaFactory.numero_hdr_seleccionada = $scope.selectedHdR;
        return defer.promise;
    };

    // Search for states
    function querySearch(query) {
        const results = query ? self.numeros.filter(createFilterFor(query)) : self.numeros;
        const deferred = $q.defer();
        deferred.resolve(results);
        return deferred.promise;
    }

    /**
     * Create filter function for a query string
     */
    function createFilterFor(query) {
        return function (numero) {
            return (numero.display.toLowerCase().indexOf(query) !== -1);
        };
    }

    function setInputNumHdR() {
        hojaRutaFactory.getNumHojaRuta()
            .then(function () {
                let obj = [];
                angular.forEach(hojaRutaFactory.inputNroHdR, (numHdR) => {
                    obj.push({
                        value  : numHdR.numero,
                        display: numHdR.numero,
                    });
                });

                self.numeros = obj;
            })
            .catch(function () {

            });
    }

    $scope.aplicarFiltro = function () {
        let defer            = $q.defer();
        let hojaRutaResponse = [];
        $scope.showProgress  = true;
        $scope.vaciarGrillaHdR();
        $scope.setShowButtons(false);

        hojaRutaFactory.getHojaRuta($scope.selectedHdR)
            .then(function () {
                $scope.showProgress = false;
                if (hojaRutaFactory.codigo === CONST.ERR){
                    errorAlert("Ha ocurrido un error al buscar la HdR. Probablemente no exista");
                    defer.reject();
                }
                else{
                    angular.forEach(hojaRutaFactory.hojaRutaData, (hojaRuta) => {
                        hojaRutaResponse.push(hojaRuta.respuesta);
                    });
                    $scope.cargarGrillaHdR(hojaRutaResponse);
                    $scope.setShowButtons();
                    defer.resolve();
                }
            })
            .catch(function () {
                errorAlert("Ha ocurrido un error al recuperar la Hoja de Ruta");
                $scope.showProgress = false;
            });
        return defer.promise;
    };

    $scope.detectarCambiosEnGrilla = function(){
        let matchRequerimientos = hojaRutaFactory.hojaRutaData.filter(requerimientoRecuperado => {
            let flag = false;
            angular.forEach($scope.gridOptionsHdR.data, (requerimientoEnGrilla) => {
                 if (requerimientoRecuperado.respuesta.id_req === requerimientoEnGrilla.id_req)
                     flag = true;
            });
            return flag;
        });

        return matchRequerimientos.length !== $scope.gridOptionsHdR.data.length || hojaRutaFactory.hojaRutaData.length !== $scope.gridOptionsHdR.data.length;

    };

    //Determina botones a mostrar
    $scope.setShowButtons = function(evaluaButtonControlar = true){ //Posible param "numero hoja ruta". 0 ó <> 0

        if ($scope.selectedHdR === '0'){    //Nueva HdR
            $scope.enableReimprimir      = false;
            $scope.enableGuardarImprimir = ($scope.gridOptionsHdR.data.length > 0);
            $scope.enableControlar       = false;
        }else{  //HdR existente
            $scope.labelButtonReImprimir = ($scope.detectarCambiosEnGrilla() ? 'Nueva versión' : 'Reimprimir');
            $scope.enableReimprimir      = true;
            $scope.enableGuardarImprimir = false;

            $scope.enableControlar = evaluaButtonControlar;

            if (evaluaButtonControlar){
                $scope.setButtonControlar()
                    .then(function () {
                        $scope.enableControlar = true;
                    })
                    .catch(function () {
                        $scope.enableControlar = false;
                    });
            }

        }
    };

    //Determina el label de botón controolar. Opciones: "Leer elementos" | "Leer remitos"
    $scope.setButtonControlar = function(respuesta){
        let defer = $q.defer();

        if ($scope.gridOptionsHdR.data.length > 0){
            let reqTipoOp = [];
            hojaRutaFactory.getEstadoHojaRuta($scope.selectedHdR)
                .then(function () {

                    if (hojaRutaFactory.respuesta.toLowerCase() === CONST.HDR_ESTADO_PENDIENTE || hojaRutaFactory.respuesta.toLowerCase() === CONST.HDR_ESTADO_DESPACHADA){
                        switch ($scope.determinarTipoOperacion($scope.gridOptionsHdR.data)) {
                            case CONST.CONTROLAR_ELEMENTOS:
                                $scope.labelButtonControlar = 'Controlar elementos';
                                defer.resolve();
                                return defer.promise;
                            case CONST.LEER_HDR_Y_REMITOS:
                                $scope.labelButtonControlar = 'Leer remitos';
                                defer.resolve();
                                return defer.promise;
                            case CONST.ERR:
                                errorAlert('Los requerimientos de la hoja de ruta no se encuentran en el mismo Tipo de Operación. No se puede controlar');
                                break;
                        }
                    }
                    defer.reject(); //Hoja de ruta en estado Controlada o Anulada
                })
                .catch(function () {
                    errorAlert("Ha ocurrido un problema al determinar si la hoja de ruta se debe controlar")
                    defer.reject();
                });
        }else{
            defer.reject();
        }
        return defer.promise;
    };

    $scope.determinarTipoOperacion = function(pRequerimientos){

        let reqTipoOp = [];

        reqTipoOp = pRequerimientos.filter(requerimiento => ($scope.toSnakeCase(requerimiento.tipo_operacion) === CONST.LEER_HDR_Y_REMITOS));
        if (reqTipoOp.length === $scope.gridOptionsHdR.data.length)
            return CONST.LEER_HDR_Y_REMITOS;


        reqTipoOp = pRequerimientos.filter(requerimiento => (
            $scope.toSnakeCase(requerimiento.tipo_operacion) === CONST.CONTROLAR_ELEMENTOS || $scope.toSnakeCase(requerimiento.tipo_movimiento) === 'ingreso' && $scope.toSnakeCase(requerimiento.tipo_operacion) === CONST.LEER_HDR_Y_REMITOS)
        );
        if (reqTipoOp.length === $scope.gridOptionsHdR.data.length)
            return CONST.CONTROLAR_ELEMENTOS;

        if (reqTipoOp.length === 0)
            return 'no_controla';

        return CONST.ERR;
    };

    $scope.toSnakeCase = function(pString){
        return pString.toLowerCase().split(" ").join("_");
    };

    $scope.buttonClick = function (opcion, ev = null) {
        $scope.validateSession();

        switch (opcion) {
            case 'controlar':
                $scope.mostrarModal(ev);
                break;
            case 'save':
                if ($scope.gridOptionsHdR.data.length > 0) {
                    let nroHdR = $scope.selectedHdR != null ? $scope.selectedHdR : '0';
                    let reqs   = [];

                    angular.forEach($scope.gridOptionsHdR.data, (requerimiento) => {
                        reqs.push(requerimiento.num_req);
                    });

                    $scope.guardarHdR(nroHdR, reqs)
                        .then(() => {
                            $scope.imprimirPDF(hojaRutaFactory.lastHdR);
                        });
                }
                else {
                    warnAlert('No hay datos para crear una Hoja de Ruta', false);
                }

                break;
            case 'clean':
                $scope.vaciarGrillaHdR(true);
                $scope.setShowButtons();
                break;
            case 'print':
                if ($scope.detectarCambiosEnGrilla()){
                    if ($scope.gridOptionsHdR.data.length > 0) {
                        let nroHdR = $scope.selectedHdR != null ? $scope.selectedHdR : '0';
                        let reqs   = [];

                        angular.forEach($scope.gridOptionsHdR.data, (requerimiento) => {
                            reqs.push(requerimiento.num_req);
                        });

                        $scope.guardarHdR(nroHdR, reqs)
                            .then(() => {
                                $scope.imprimirPDF(hojaRutaFactory.lastHdR);
                            });
                    }
                    else {
                        warnAlert('No hay datos para crear nueva versión de Hoja de Ruta', false);
                    }
                }else{
                    $scope.imprimirPDF($scope.selectedHdR, 'DUPLICADO');
                }
                break;
        }
    };

    $scope.imprimirPDF = function(pNumeroHdR, pCopia = 'ORIGINAL'){
        let data = {
            Hdr            : pNumeroHdR,
            sectores       : [],
        };
        printHojaRuta($scope.gridOptionsHdR.data, data, pCopia);
    };

    $scope.guardarHdR = function(pNumeroHdR, pRequerimientos){

        let defer = $q.defer();

        let hdr = {
            num_hdr     : pNumeroHdR.replace('.','-'),
            num_req_list: pRequerimientos,
        };

        hojaRutaFactory.setHojaRuta(hdr)
            .then(function () {
                successAlert(`Hoja de ruta ${hojaRutaFactory.lastHdR} generada correctamente.`);
                setInputNumHdR();   //recargo input de números
                //Seteo la nueva HdR generada y recupero sus elementos
                $scope.hdrSeleccionada = {
                    display: hojaRutaFactory.lastHdR,
                    value  : hojaRutaFactory.lastHdR,
                };
                $scope.setearSeleccion($scope.hdrSeleccionada)
                    .then(() => {
                        defer.resolve();
                    })
                    .catch(() => {
                        defer.reject();
                    });
            })
            .catch(function () {
                errorAlert('Ha ocurrido un problema al generar la Hoja de Ruta');
                errorAlert(hojaRutaFactory.respuesta);
                defer.reject();
            });
        return defer.promise;
    };

    // Left click modal initialization
    $scope.menuOptionsHdR = [
        ['Quitar requerimiento', function ($itemScope) {
            if ($scope.selectedHdR !== '0'){
                hojaRutaFactory.getEstadoHojaRuta($scope.selectedHdR)
                    .then(() => {
                        if(hojaRutaFactory.respuesta.toLowerCase() === CONST.HDR_ESTADO_PENDIENTE || hojaRutaFactory.respuesta.toLowerCase() === CONST.HDR_ESTADO_DESPACHADA){
                            $scope.quitarRequerimiento($itemScope.selectedRow, false);
                            $scope.setShowButtons(false);
                        }else{
                            warnAlert(`No se puede quitar Requerimiento debido a que la Hoja de Ruta N° ${$scope.selectedHdR} se encuentra ${hojaRutaFactory.respuesta}`);
                        }
                    })
                    .catch(() => {
                        errorAlert(`No se ha podico comprobar el estado de la Hoja de Ruta N°${$scope.selectedHdR}`);
                    });
            }else{
                $scope.quitarRequerimiento($itemScope.selectedRow);
                $scope.setShowButtons(false);
            }
        }],
    ];

    $scope.quitarRequerimiento = function(pRequerimientoClick){
        if ($scope.gridApiHdR.selection.getSelectedRows().length > 0){
            $scope.gridOptionsHdR.data = $scope.gridOptionsHdR.data.filter((reqEnHoja) => {
                let flag = true;
                angular.forEach($scope.gridApiHdR.selection.getSelectedRows(), (reqQuitar) => {
                    if (reqEnHoja.id_req === reqQuitar.id_req){
                        flag = false;
                    }
                });
                return flag;
            });
        }else if(pRequerimientoClick){
            $scope.gridOptionsHdR.data = $scope.gridOptionsHdR.data.filter((reqEnHoja) => (reqEnHoja.id_req !== pRequerimientoClick.entity.id_req));
        }
    };

    $scope.mostrarModal = function (ev) {
        if ($scope.gridOptionsHdR.data.length > 0) {
            let rows          = [];
            let tipoOperacion = $scope.determinarTipoOperacion($scope.gridOptionsHdR.data);
            let option        = {};

            let levantar_modal = false;

            switch (tipoOperacion) {
                case CONST.CONTROLAR_ELEMENTOS:
                    levantar_modal = true;
                    rows           = $scope.gridOptionsHdR.data;
                    option         = {
                        dialog: CONST.CONTROLAR_ELEMENTO_DIALOG
                    };
                    break;
                case CONST.LEER_HDR_Y_REMITOS:
                    levantar_modal = true;
                    rows           = $scope.gridOptionsHdR.data;
                    option         = {
                        dialog: CONST.LEER_HDR_Y_REMITOS_DIALOG
                    };
                    break;
                default:
                    errorAlert("Todos los requerimientos no se encuentran en el mismo tipo de operación");
                    break;
            }

            if (levantar_modal){

                $mdDialog.show({
                    controller         : DialogController,
                    templateUrl        : tipoOperacion,
                    parent             : angular.element(document.body),
                    targetEvent        : ev,
                    clickOutsideToClose: false,
                    locals             : {
                        operacion  : $scope.items,  //quitar
                        row        : rows,
                        selectedHdR: $scope.selectedHdR,
                        option     : option
                    },
                    fullscreen         : $scope.customFullscreen // Only for -xs, -sm breakpoints.
                }).then((answer) => {
                    switch (answer) {
                        case 'cancelar':
                            break;
                        case 'aceptar':
                            $scope.aplicarFiltro().then(()=>{});
                            break;
                        default:
                            //successAlert("Hoja de Ruta creada: " + answer);
                            //$scope.selectedHdR = answer;
                            //$scope.inicializarDatos(false);
                            //$scope.aplicarFiltro();
                            break;
                    }

                }, function () {
                    $scope.status = 'You cancelled the dialog.';
                });
            }

        } else {
            warnAlert("Debe cargar una Hoja de Ruta",false);
        }
    };

    function modalBusquedaController($scope, $mdDialog, hojaRutaFactory){
        $scope.disableButtonSerachHdr = false;
        $scope.showProgressSearch = false;
        $scope.mostrarListaHdR = false;

        $scope.getListHdr = function () {
            if ($scope.num_requerimiento){
                $scope.disableButtonSerachHdr = true;
                $scope.showProgressSearch = true;
                $scope.mostrarListaHdR = false;
                hojaRutaFactory.getNumHojaRutaByNumReq($scope.num_requerimiento)
                    .then(() => {
                        $scope.mostrarListaHdR = true;
                        $scope.disableButtonSerachHdr = false;
                        $scope.showProgressSearch = false;
                        $scope.listNumHdr = hojaRutaFactory.listNumHdr;
                    },() => {
                        $scope.disableButtonSerachHdr = false;
                        $scope.showProgressSearch = false;
                    });
            }
        };

        $scope.closeModal = function (numHdrSelected) {
            if (numHdrSelected)
                $mdDialog.hide(numHdrSelected);
            else
                $mdDialog.cancel();
        }
    }

    $scope.mostrarModalBusqueda = function (ev) {
        $mdDialog.show({
            controller         : modalBusquedaController,
            templateUrl        : 'dialogs/dialog_find_hdr_by_req',
            parent             : angular.element(document.body),
            targetEvent        : ev,
            clickOutsideToClose: false,
            fullscreen         : true,
            openFrom : {
                top: -50,
                width: 30,
                height: 80
            },
            closeTo: {
                left: 1500
            }
        }).then((numHdrSelected) => {
            console.log(numHdrSelected);
            $scope.hdrSeleccionada = {
                display: numHdrSelected,
                value  : numHdrSelected,
            };
            $scope.setearSeleccion($scope.hdrSeleccionada)
                .then(() => { })
                .catch(() => { });
        }, () => { });
    };

    function DialogController($scope, $mdDialog, row, selectedHdR, option) {
        $scope.row         = row;
        $scope.selectedHdR = selectedHdR;

        $scope.dialog = {
            title   : option.dialog.title,
            subtitle: option.dialog.subtitle
        };

        if ('columnOne' in option.dialog)
            $scope.dialog.columnOne = {
                title   : option.dialog.columnOne.title,
                subtitle: option.dialog.columnOne.subtitle
            };

        if ('columnTwo' in option.dialog)
            $scope.dialog.columnTwo = {
                title   : option.dialog.columnTwo.title,
                subtitle: option.dialog.columnTwo.subtitle
            };

        $scope.hide = function () {
            $mdDialog.hide();
        };
        $scope.cancel = function () {
            $mdDialog.cancel();
        };
        $scope.answer = function (answer, hojaRuta) {
            $mdDialog.hide(answer, hojaRuta);
        };
    }
});

app.factory('listenerHdR', function () {
    return {
        rows: []
    }
});

app.factory('listaHdR', function () {
    return {
        numerosHdR: []
    }
});

app.factory('hojaRuta', function () {
    return {
        numero: 0,
        event: '',
        dialog: {
            'title': '',
            'subtitle': '',
            'columnOne': {'title': '', 'subtitle': ''},
            'columnTwo': {'title': '', 'subtitle': ''}
        }
    }
});
