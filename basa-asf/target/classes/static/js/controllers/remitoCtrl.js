var app = angular.module('BasaApp.remitoCtrl', []);
app.controller('remitoCtrl', function ($scope,
                                       $window,
                                       $filter,
                                       $mdDialog,
                                       CONST,
                                       remitoFactory,
                                       helpersFactory,
                                       messageFactory) {

    function warnAlert(text){
        notifications.showWarning({message: text});
    }
    function successAlert(text, sound){
        notifications.showSuccess({message: text});
    }
    function errorAlert(text){
        notifications.showError({message: text});
    }

    $scope.filtro.remito             = '';
    $scope.showProgress              = false;
    $scope.disableButtonSearch       = false;
    $scope.showRemito                = false;
    $scope.hideButtonProcesar        = false;
    $scope.disableFechaRemito        = false;
    $scope.disableTurnoRemitoProceso = false;

    $scope.remitoProceso;
    $scope.fechaRemito;

    $scope.turnos = [{ codigo: 'M', descripcion: 'Mañana'}
                    ,{ codigo: 'T', descripcion: 'Tarde' }];

    $scope.buttonClick = function (action, ev) {
        switch (action) {
            case 'findRemito':
                $scope.clear();
                $scope.showProgress = true;
                $scope.buscarRemito($scope.filtro.remito);
                break;
            case 'process':
                if($scope.validar())
                    if ($scope.remitoProceso.elementos_sobrantes_camion && $scope.remitoProceso.elementos_sobrantes_camion.length > 0)
                        $scope.showAlertDialog();
                    else if($scope.remitoProceso.elementos_agrupados.length !== $scope.remitoProceso.cantidad_elementos)
                        $scope.showConfirmDialog(ev);
                    else
                        $scope.procesaRemito();
                break;
            case 'clear':
                $scope.clear();
                break;
        }
    };

    $scope.showAlertDialog = function(ev){
        $mdDialog.show(
            $mdDialog.alert()
                .parent(angular.element(document.querySelector('#popupContainer')))
                .clickOutsideToClose(true)
                .title('Ups! Se ha detectado elementos sobrantes.')
                .textContent('Verifique las lecturas y los elementos del remito')
                .ariaLabel('Alert Dialog Demo')
                .ok('Aceptar')
                .targetEvent(ev)
        );
    };

    $scope.showConfirmDialog = function(ev){
        let messagge;

        if ($scope.remitoProceso.tipo_requerimiento === CONST.RQ_TIPO_RET_CANT)
            if ($scope.remitoProceso.elementos_agrupados.length < $scope.remitoProceso.cantidad_elementos)
                messagge = `La cantidad de elementos del remito será reducida en ${$scope.remitoProceso.cantidad_elementos - $scope.remitoProceso.elementos_agrupados.length}`;
            else
                messagge = `La cantidad de elementos del remito será aumentada en ${$scope.remitoProceso.elementos_agrupados.length - $scope.remitoProceso.cantidad_elementos}`;
        else
            if ($scope.remitoProceso.elementos_agrupados.length < $scope.remitoProceso.cantidad_elementos)
                messagge = `Cantidad de elementos a ser quitados: ${$scope.remitoProceso.cantidad_elementos - $scope.remitoProceso.elementos_agrupados.length}`;

        let confirm = $mdDialog.confirm()
            .title('¿Las cantidades de elementos son diferentes. Desea continuar?')
            .textContent(messagge)
            .ariaLabel('Lucky day')
            .targetEvent(ev)
            .ok('Aceptar')
            .cancel('Cancelar');
        $mdDialog.show(confirm).then(function() {
            $scope.status = 'Procesará';
            $scope.procesaRemito();
        }, function() {
            $scope.status = 'NO procesará';
        });
    };

    $scope.clear = function(){
        $scope.remitoProceso = null;
        $scope.fechaRemito   = null;
        $scope.showRemito    = false;
    };

    $scope.clearClass = function () {
        $scope.fE = false;
    };

    $scope.buscarRemito = function (codigo) {
        remitoFactory.getRemitoProceso(codigo)
            .then(() => {
                $scope.hideButtonProcesar = false;
                $scope.disableFechaRemito = false;
                $scope.disableTurnoRemitoProceso = false;


                $scope.remitoProceso = remitoFactory.respuesta;
                $scope.fechaRemito = "";

                if ($scope.remitoProceso.cantidad_elementos < $scope.remitoProceso.elementos_agrupados.length ){
                    messageFactory.warnAlert('La cantidad de lecturas es superior a la cantidad de elementos declaradas en el remito');

                    if ($scope.remitoProceso.elementos_sobrantes_camion && $scope.remitoProceso.elementos_sobrantes_camion.length > 0){
                        messageFactory.warnAlert('Se detectaron lecturas sobrantes de camión');
                        $scope.hideButtonProcesar = true;
                        $scope.disableFechaRemito = true;
                        $scope.disableTurnoRemitoProceso = true;
                    }
                }

                if ($scope.remitoProceso.cantidad_elementos > $scope.remitoProceso.elementos_agrupados.length && $scope.remitoProceso.tipo_requerimiento === CONST.RQ_TIPO_RET_CANT){
                    messageFactory.warnAlert('La cantidad de lecturas es menor a la cantidad de elementos declarados en el remito');
                }

                if ($scope.remitoProceso.elementos_faltantes_camion && $scope.remitoProceso.elementos_faltantes_camion.length > 0){
                    messageFactory.warnAlert('Se detectaron elementos faltantes de CAMIÓN. FALTA LECTURA DE REMITO');
                }
                if ($scope.remitoProceso.elementos_faltantes_planta && $scope.remitoProceso.elementos_faltantes_planta.length > 0) {
                    $scope.hideButtonProcesar = true;
                    $scope.disableFechaRemito = true;
                    $scope.disableTurnoRemitoProceso = true;
                    messageFactory.warnAlert('Se detectaron elementos faltantes en PLANTA. FALTA LECTURA DE PLANTA');
                }

                if ($scope.remitoProceso.procesado === 'S'){
                    $scope.hideButtonProcesar = true;
                    $scope.disableFechaRemito = true;
                    $scope.disableTurnoRemitoProceso = true;
                    messageFactory.warnAlert('El remito ya ha sido procesado');
                }

                $scope.showRemito = true;
                $scope.showProgress = false;
            }, () => {
                messageFactory.errorAlert(remitoFactory.error.respuesta);
                $scope.showProgress = false;
            });
    };

    $scope.procesaRemito = function(){

        $scope.remitoProceso.fecha_entrega_remito = helpersFactory.dateToLong($scope.fechaRemito);

        $scope.mostrarProgreso();

        remitoFactory.setRemitoProceso($scope.remitoProceso)
            .then(() => {
                $scope.$emit('ocultarProgreso', function () {});
                if (remitoFactory.respuesta.startsWith('Error')) messageFactory.errorAlert(remitoFactory.respuesta);
                if (!remitoFactory.respuesta.startsWith('Error')){
                $scope.hideButtonProcesar = true;
                $scope.disableFechaRemito = true;
                $scope.disableTurnoRemitoProceso = true;
                messageFactory.successAlert('El remito  ha sido procesado' );
                }
            }, () => {
                $scope.$emit('ocultarProgreso', function () {});
                messageFactory.errorAlert(messageFactory.error.respuesta);
            });
    };

    $scope.validar = function(){
        if (!$scope.remitoProceso.turno || $scope.remitoProceso.turno === null || !$scope.fechaRemito || $scope.fechaRemito === null)
            return false;
        return true;
    };

    $scope.deshabilitarButtonProcesar = function(){
        if ($scope.remitoProceso.elementos_faltantes_camion && $scope.remitoProceso.elementos_faltantes_camion.length > 0
            || $scope.remitoProceso.elementos_faltantes_planta && $scope.remitoProceso.elementos_faltantes_planta.length > 0){
            $scope.hideButtonProcesar = true;
        }
    };

    $scope.retornarCodigoCarga = function (grupo) {
        if (!$scope.remitoProceso)
            return 0;
        let elementos = $filter(grupo)($scope.remitoProceso.elementos_agrupados);
        if (elementos.length > 0){
            return elementos[0].codigo_carga;
        }
        return 0;
    }

    function WaitController($rootScope, $mdDialog){
        $rootScope.$on('ocultarProgreso', function () {
            $mdDialog.hide();
        });
    }

    $scope.mostrarProgreso = function(){
        $mdDialog.show({
            controller: WaitController,
            template: `<md-dialog id="plz_wait" style="background-color:transparent;box-shadow:none">
                            <div layout="column" layout-sm="column" layout-align="center center" aria-label="wait">
                                <md-progress-circular md-mode="indeterminate" ></md-progress-circular>
                                <br>
                                <label>Procesando remito</label>
                            </div>
                        </md-dialog>`,
            parent: angular.element(document.body),
            clickOutsideToClose:false,
            escapeToClose: false,
            fullscreen: true
        }).then(function(answer) { });
    };

    function SeleccionMetodoController($rootScope, $scope, $mdDialog, tipoRequerimiento) {
        $scope.tipoRequerimiento = tipoRequerimiento;
        $scope.actionButton = function (action) {
            if (action){
                $mdDialog.hide($scope.metodo);
            }else{
                $mdDialog.hide();
            }
        }
    }

    $scope.mostrarSeleccionProceso = function () {
        $mdDialog.show({
            controller: SeleccionMetodoController,
            template: `<div flex="" style="min-height: 50px">
                            <md-toolbar>
                                <div class="md-toolbar-tools">
                                  <h3>
                                    <span>Seleccione el método de proceso</span>
                                  </h3>
                                </div>
                            </md-toolbar>
                            <md-content class="md-padding">
                                <md-radio-group ng-model="metodo">
                                  <md-radio-button value="A" class="md-primary">Agregar nuevos elementos al remito y procesar</md-radio-button>
                                  <md-radio-button value="P" class="md-primary" ng-hide="tipoRequerimiento === 11">Únicamente procesar elementos pertenecientes al remito</md-radio-button>
                                </md-radio-group>
                                <md-button class="btn btn-success btn-block" ng-disabled="!metodo" ng-click="actionButton('procesar')">Procesar</md-button>
                                <md-button class="btn btn-default btn-block" ng-click="actionButton()">Cancelar</md-button>
                            </md-content>
                        </div>`,
            parent: angular.element(document.body),
            locals: {
                tipoRequerimiento: $scope.remitoProceso.tipo_requerimiento
            },
            clickOutsideToClose:true,
            escapeToClose: true,
            fullscreen: true
        }).then(function(metodo) {
            if (metodo){
                $scope.remitoProceso.metodo = metodo;
                $scope.procesaRemito();
            }
        });
    };

});

// Filtro Devolución Cliente
app.filter('dc', function () {
    return function (elementos, ) {
        //return elementos;
        if (elementos)
            return elementos.filter(e => e.codigo_grupo === 'DC');
        return [];
    }
})

// Filtro Guarda y Custodia
app.filter('gc', function () {
    return function (elementos) {
        //return elementos;
        if (elementos)
            return elementos.filter(e => e.codigo_grupo === 'GC');
        return [];
    }
});
