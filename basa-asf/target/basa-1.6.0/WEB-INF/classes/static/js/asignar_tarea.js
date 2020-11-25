app.config(['notificationsConfigProvider', function(notificationsConfigProvider){
    notificationsConfigProvider.setAutoHide(false);
}]);
app.controller('asingarTareaCtrl', function ($scope, $http, notifications) {
    $scope.showProgress = false;
    $scope.showUsers = true;

    function errorAlert(text){
        notifications.showError({
            message: text,
            hideDelay: 2000,
            hide: true
        });
    }
    // Retrieve all the available users
    $http.get('siguiente_operacion/asignar_tarea/read').success(function (data) {
        $scope.userList = data;
    }).error(function () {
    });

    /**
     * Service call to assign a user to a "requerimiento"
     */
    $scope.asignarUsuario = function(){
        $scope.showProgress = true;
        $scope.showUsers = false;
        const row = angular.copy($scope.$parent.row.entity);
        row.fechaEntrega = new Date(row.fechaEntrega + "T" + row.horaEntrega+"-03:00");
        const jsonToSend = JSON.stringify({"user": this.item, "operacion": row});
        $http({
            method: 'POST',
            url: '/siguiente_operacion/asignar_tarea/save',
            data: jsonToSend,
            headers: {'Content-Type': 'application/json'}
        }).success(function () {
            // handle success things
            $scope.$parent.answer('aceptar');
        }).error(function (data) {
            // handle error things
            errorAlert(data.status === 405 ? "Acceso denegado." : data.message);
                $scope.showProgress = false;
                $scope.showUsers = true;
        });
    }
});