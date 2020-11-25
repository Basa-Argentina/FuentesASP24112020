
app.run(['i18nService', function (i18nService) {
    i18nService.setCurrentLang('es');
}]);


app.controller('AdminConceptosCtrl', function ($scope, $http, $mdDialog, $element, $timeout, $q, $interval, listenerHdR, listaHdR) {
    const paginationOptions = {
        pageNumber: 1,
        pageSize: 25,
    };

    $scope.$watch(function(){
        return listaHdR.numerosHdR;
    }, function(newValue){
        $scope.hojasDeRuta = newValue;
    });

    $scope.mensaje = {};

    const self = this;
    loadAll();
    self.searchText = null;
    self.querySearch = querySearch;

    // Search for states
    function querySearch(query) {
        const results = query ? self.states.filter(createFilterFor(query)) : self.states;
        const deferred = $q.defer();
        deferred.resolve(results);
        return deferred.promise;
    }

    /**
     * Build `states` list of key/value pairs
     */
    function loadAll() {

        $scope.showProgress = true;
        const getPersona = function () {
            // Angular $http() and then() both return promises themselves
            return $http.get('/personas_juridicas/read').success(function (data) {
                $scope.showProgress = false;
                return data;
            });
        };

        const myDataPromise = getPersona();
        myDataPromise.then(function (result) {

            const obj = [];
            for (let i = 0; i < result.data.length; i++) {

                obj.push(
                    {
                        value: result.data[i][0].id,
                        display: result.data[i][1] + ": " + result.data[i][0].razonSocial
                    }
                );
            }
            self.states = obj;
        });
    }

    /**
     * Create filter function for a query string
     */
    function createFilterFor(query) {
        const lowercaseQuery = angular.lowercase(query);

        return function (state) {
            return (state.display.toLowerCase().indexOf(lowercaseQuery) !== -1);
        };

    }

    /**
     * Verifies that the session stills valid
     */
    $scope.validateSession = function () {
        $http.get("/usuarios/check_session/").success(function () {
        }).error(function (data) {
            if (data.message==="Sesión expirada."){
                this.location.reload();
            }
        });
    };

    /**
     * Filters grid content with the dropdown values
     * @param filter
     * @param keepPage
     */
    $scope.aplicarFiltro = function (filter,keepPage) {
        $('body').css('background-color', 'blue !important');
        $('#conceptosGrid').hide();
        if(!keepPage) {
            $scope.gridOptions.paginationCurrentPage = 1;
        }
        if (filter=="sucursal"){
            listenerHdR.rows = "clean";
        }
        const clienteId = $scope.selectedCliente != null ? $scope.selectedCliente.value : null;
        $scope.showProgress = true;
        $http.defaults.headers.post['Content-Type'] = 'application/json';
        this.showAlert = false;
        if($scope.selectedOperacion!=0) {
            $scope.sinHDR = false;
            $scope.hideButton=true;
        }
        // send login data
        $http({
            method: 'POST',
            url: '/conceptos/filter',
            data: {
                "sucursal": $scope.selectedSucursal,
                "concepto_id": $scope.selectedTipoRequerimiento,
                "cliente": clienteId,
                "date": $scope.verUltimos,
                "page": paginationOptions.pageNumber - 1,
                "size": paginationOptions.pageSize,
                "fechaDesde": new Date($scope.fechaDesde),
                "fechaHasta": new Date($scope.fechaHasta)
            },
            headers: {'Content-Type': 'application/json'}
        }).success(function (data) {
            // handle success things
            $scope.gridOptions.totalItems = data.totalElements;
            if ($scope.mostrarFinalizados) {
                const filtrado = data.content.filter(function (item) {
                    return item.requerimientoLite.estado.indexOf("Finalizado") !== -1;
                });
                data.content = filtrado;
            }
            if ($scope.mostrarHabilitados) {
                const filtrado = data.content.filter(function (item) {
                    return item.estado === true;
                });
                data.content = filtrado;
            }
            $scope.gridOptions.data = data.content;
            if($scope.sinHDR) {
                $scope.hideButton=true;
                $scope.aplicarFiltroHdR();
            }
            $scope.showProgress = false;
            $('#conceptosGrid').show();
            $scope.restoreState();

        }).error(function (data) {
            // handle error things
            if (data === undefined){
                $scope.showAlert = true;
                $scope.ctrl.alertType = "danger"
                $scope.ctrl.alertMessage = "Error inesperado, actualice la sesión y vuelva a intentar.";
            } else if (data.status === 405) {
                $scope.showAlert = true;
                $scope.ctrl.alertType = "danger";
                $scope.ctrl.alertMessage = "Acceso denegado.";
            } else if (data.message==="Sesión expirada."){
                this.location.reload();
            }
            $scope.showProgress = false;
        });


        $http.get('/mensajes/usuario').success(function (data) {
            $scope.mensaje = [];
            data.forEach(nuevo=>{
                $scope.mensaje[nuevo] = "no_leido";
            })

        }).error(function () {

        });
    };

    /**
     * Applies filter by "Hoja de Ruta" slider
     */
    $scope.aplicarFiltroHdR = function(){
        if($scope.sinHDR) {
            $scope.selectedOperacion = 0;
            const filtrado = $scope.gridOptions.data.filter(function (item) {
                return (item.tipoOperaciones.descripcion.indexOf("Asignar HdR") !== -1 /*&& numeroHdR==0*/);
            });
            $scope.gridOptions.data = filtrado;
        } else {
            $scope.aplicarFiltro();
        }
        $scope.hideButton = !$scope.hideButton;
        $scope.hideHdRSelector = true;
    };



    $scope.showMensaje = function (ev, row) {
        $mdDialog.show({
            controller: DialogController,
            templateUrl: "mensajes_dialog",
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            locals: {
                tipoRequerimientos: $scope.tipoRequerimientos,
                operacion: $scope.items,
                row: row
            },
            fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
        })
            .then(function () {
                $scope.aplicarFiltro();
            }, function () {
                $scope.status = "You cancelled the dialog.";
            });
    };
    function DialogController($scope, $mdDialog, row, tipoRequerimientos) {
        $scope.row = row;
        $scope.tipoRequerimientos = tipoRequerimientos
        $scope.hide = function () {
            $mdDialog.hide();
        };

        $scope.cancel = function () {
            $mdDialog.cancel();
        };

        $scope.answer = function (answer) {
            $mdDialog.hide(answer);
        };
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


    getTipoRequerimiento();
    function getTipoRequerimiento() {
        $http.get("/conceptos/tipos").then(function (result) {
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
    };

    $http.get("/sucursales/depositos").success(function (data) {
        $scope.sucursales = data;
    })

    // filters init
    $scope.verUltimos = 100;
    $scope.fechaDesde = new Date();
    $scope.fechaDesde.setDate($scope.fechaDesde.getDate() - 10);
    $scope.fechaHasta = new Date();
    $scope.isOpen = false;
    $scope.selectedSucursal = 0;
    $scope.selectedOperacion = "0";
    $scope.mostrarFinalizados = true;
    $scope.mostrarHabilitados = true;
    $scope.sinHDR = false;
    $scope.selectedTipoRequerimiento = 0;

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

    $scope.menuOptions = [
        ["Habilitar/deshabilitar", function ($itemScope) {
            let dataSet = [];

            if ($itemScope.gridApi.selection.getSelectedRows().length > 0) {
                dataSet = $itemScope.gridApi.selection.getSelectedRows();
            }
            else {
                dataSet.push($scope.selectedRow.entity);
            }

            dataSet.forEach(item=>{
                item.estado = !item.estado;
                item.fecha = new Date(item.fecha + "T" + "11:23-03:00");
            })

            $http({
                method: "POST",
                url: "/conceptos/update_state",
                data: dataSet,
                headers: {"Content-Type": "application/json"}
            }).success(function() {
                $scope.aplicarFiltro();
            }).error(function(err) {
                defered.reject(err)
            });
        }],
    ];

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
    let rowTemplate= '<div ng-class="{ \'grid-resaltada-row\': row.entity.requerimientoLite.estado==\'Finalizado OK\' }">' +
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
        useExternalPagination: true,
        rowTemplate: rowTemplate,
        columnDefs: [
            {
                field: 'requerimientoLite.clienteEmp.personasJuridicas.razonSocial',
                name: 'Cliente',
                grouping: {groupPriority: 1},
                sort: {priority: 0, direction: 'desc'},
                enableCellEdit: false ,
                groupingShowAggregationMenu: false,
            },
            {
                field: 'fechaString',
                name: 'Fecha Creacion',
                cellClass: "grid-align",
                enableCellEdit: false ,
                groupingShowAggregationMenu: false,
                sort: {priority: 1, direction: 'desc'},
                sortingAlgorithm: sortFn
            },
            {
                field: 'conceptoFacturable.descripcion',
                name: 'Concepto',
                enableCellEdit: false ,
                groupingShowAggregationMenu: false,
            },
            {
                field: 'cantidad',
                name: 'Cant',
                cellClass: "grid-align",
                enableCellEdit: false ,
                groupingShowAggregationMenu: false
            },
            {
                field: 'estado',
                name: 'Habilitado',
                type: 'boolean',
                cellTemplate: '<div ng-if="!row.groupHeader"><input type="checkbox" ng-model="row.entity.estado" disabled="disabled"></div>',
                cellClass: "grid-align",
                enableCellEdit: false,
                groupingShowAggregationMenu: false
            },
            {
                field: 'requerimientoLite.fechaEntregaString',
                name: 'Fecha Compromiso',
                cellClass: "grid-align",
                enableCellEdit: false,
                groupingShowAggregationMenu: false
            },
            {
                field: 'requerimientoLite.estado',
                name: 'Estado Requerim',
                enableCellEdit: false,
                groupingShowAggregationMenu: false,
                cellClass: "grid-align",
            },
            {
                field: 'requerimientoLite.numero',
                name: 'N°Req',
                cellClass: "grid-align",
                enableCellEdit: false,
                groupingShowAggregationMenu: false,
            },
            {
                field: 'requerimientoLite.remito.empleadoSolicitante',
                name: 'Solicitante',
                cellClass: "grid-align",
                enableCellEdit: false,
                groupingShowAggregationMenu: false,
            },
            {
                field: 'requerimientoLite.remito.numero',
                name: 'Remito',
                enableCellEdit: false,
                groupingShowAggregationMenu: false
            },
            {
                field: 'requerimientoLite.hojaRuta.numero',
                name: 'HdR',
                cellClass: "grid-align",
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
        }).error(function () {
        });
    };

    $scope.saveState = function () {
        const state = JSON.stringify($scope.gridApi.saveState.save());

        localStorage.setItem("conceptos", state);

/*        $http.defaults.headers.post['Content-Type'] = 'application/json';
        // send login data
        $http({
            method: 'POST',
            url: '/operaciones/save_state',
            data: state,
            headers: {'Content-Type': 'text/plain'}
        }).success(function () {

        }).error(function () {
            // handle error things
        });*/


    };
    $scope.getCurrentUser();

    $scope.restoreState = function () {

        if (localStorage.getItem("conceptos") === null)
            $scope.saveState();

        if($scope.gridOptions.data.length!==0)
            $scope.gridApi.saveState.restore($scope, JSON.parse(localStorage.getItem("conceptos")));
        $scope.gridApi.core.refresh();

/*        $scope.getCurrentUser();

        if ($scope.user.personas.observaciones === null){
            $scope.saveState();
        }
        else {

            $scope.gridApi.saveState.restore($scope, JSON.parse($scope.user.personas.observaciones));
        };
        $scope.gridApi.core.refresh();*/

    };

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
     * Dynamic Form dispatcher
     * @param ev
     * @param row
     * @param formToLoad
     * @param schemaToLoad
     */


    // adds refresh on "Requerimiento tab click
    $("#requerimiento").click(function(){
        $scope.aplicarFiltro();
    });

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

})});
