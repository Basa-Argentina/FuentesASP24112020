var app = angular.module('BasaApp.filtroReqCtrl', [])
app.controller('filtroReqCtrl', function ($scope,
                                          $q,
                                          operacionFactory,
                                          requerimientoFactory,
                                          clienteFactory)
{

    $scope.filtro = {
        verUltimos                : 10,
        selectedSucursal          : 0,
        selectedTipoOperacion     : '0',
        selectedCliente           : null,
        mostrarFinalizados        : true,
        sinHDR                    : false,
        selectedTipoRequerimiento : 0,
        num_requerimiento         : null,
        codigo_elemento           : null,
    };

    $scope.filtroTiposOperacion = [];
    $scope.filtroTiposRequerimiento = [];
    $scope.filtroClientes = [];

    $scope.buttonSearchDisabled = false;
    $scope.buttonSearchLabel    = 'Buscar'

    const self = this;
    prepareFilters();
    self.searchClientText = "";
    self.querySearchClient = querySearchClient;

    function querySearchClient(query) {
    let results = query ? self.clientes.filter(createFilterForClient(query)) : self.clientes;
    let defer = $q.defer();
    defer.resolve(results);
    return defer.promise;
}

    function createFilterForClient(query) {
        let lowercaseQuery = angular.lowercase(query);
        return function (cliente) {
            return (cliente.display.toLowerCase().indexOf(lowercaseQuery) !== -1);
        };
    }

    function prepareFilters() {

        operacionFactory.getTiposOperacion()
            .then(() => {
                let tiposOperacion = [];
                angular.forEach(operacionFactory.respuesta.data, (tipoOperacion) => {
                    tiposOperacion.push({descripcion: tipoOperacion});
                });
                $scope.filtroTiposOperacion = tiposOperacion;
            })
            .catch(() => { });

        requerimientoFactory.getTiposRequerimiento()
            .then(() => {
                $scope.filtroTiposRequerimiento = requerimientoFactory.respuesta.data;
            })
            .catch(() => { });

        clienteFactory.getClientes()
            .then(() => {
                let obj = [];
                angular.forEach(clienteFactory.respuesta.data, (cliente) => {
                    obj.push({
                        value: cliente.persona_juridica_id,
                        display: `${cliente.codigo} - ${cliente.persona_juridica_razon_social}`
                    });
                });
                self.clientes = obj;
            })
            .catch(() => { })
    }

    $scope.resetFilter = function(input){
        switch (input) {
            case 'codigo_elemento':
                $scope.filtro.verUltimos                = 10;
                $scope.filtro.selectedSucursal          = 0;
                $scope.filtro.selectedTipoOperacion     = '0';
                $scope.filtro.selectedCliente           = null;
                $scope.filtro.mostrarFinalizados        = true;
                $scope.filtro.selectedTipoRequerimiento = 0;
                $scope.filtro.num_requerimiento         = null;
                $scope.filtro.codigo_elemento           = parseInt($scope.filtro.codigo_elemento ? $scope.filtro.codigo_elemento.toString().substring(0,12) : $scope.filtro.codigo_elemento);
                break;

            case 'num_requerimiento':
                $scope.filtro.verUltimos                = 1;
                $scope.filtro.selectedSucursal          = 0;
                $scope.filtro.selectedTipoOperacion     = '0';
                $scope.filtro.selectedCliente           = null;
                $scope.filtro.mostrarFinalizados        = true;
                $scope.filtro.selectedTipoRequerimiento = 0;
                $scope.filtro.codigo_elemento           = null;
                break;
        }
    };

    $scope.filtrar = function () {
        if($scope.filtro.verUltimos){
            $scope.$emit('ocultarGrillaReq', function () {});
            $scope.buttonSearchDisabled = true;
            $scope.showProgress = true;
            $scope.buttonSearchLabel = 'Buscando...';
            let operaciones;
            operacionFactory.getOperaciones($scope.filtro)
                .then(() => {

                    operaciones = operacionFactory.respuesta;
                    $scope.showProgress = false;
                    $scope.buttonSearchLabel = 'Buscar';
                    $scope.buttonSearchDisabled = false;
                    $scope.$emit('filtroAplicado', {
                        operaciones: operaciones,
                        showButtonHdR: $scope.showButtonHdR($scope.filtro.selectedTipoOperacion),
                        tipoRequerimientos: $scope.filtroTiposRequerimiento
                    });
                },() => {

                    $scope.showProgress = false;

                });
        }
    };

    $scope.showButtonHdR = function(filtro){
        return filtro === 'Asignar HdR';
    };

    $scope.$on('aplicarFiltro', function () {
        $scope.filtrar();
    });

    $scope.filtrar();

});