/**
 * Created by ldiaz on 12/09/2017.
 */
app.config(['notificationsConfigProvider', function(notificationsConfigProvider){
    notificationsConfigProvider.setAutoHide(false);
}]);
app.controller('mensajesCtrl', function ($scope, $http, notifications) {
    $scope.selectedUsuario = "";
    $scope.messageText = "";
    $scope.mensajesList = [];

    function warnAlertSound(text){
        notifications.showWarning({message: text});
        soundError.play();
    }

    const requerimientoId = $scope.$parent.row.entity.requerimiento_id;

    $http.get('siguiente_operacion/asignar_tarea/read').success(function (data) {
        $scope.usuarios = data;
    }).error(function () {
    });

    $http.get('/mensajes/' + requerimientoId + '/read').success(function (data) {
        $scope.mensajesList = data;
    }).error(function () {
    });

    $scope.agregarMensaje = function () {
        if (!$scope.selectedUsuario!="" || !$scope.messageText!="") {
            warnAlertSound("Debe completar todos los campos.");
        }
        else {
            const jsonToSend = {
                requerimiento_id: requerimientoId,
                usrDestino_id: $scope.selectedUsuario,
                texto: $scope.messageText
            };

            $http({
                method: 'POST',
                url: '/mensajes/save',
                data: JSON.stringify(jsonToSend),
                headers: {'Content-Type': 'application/json'}
            }).success(function () {
                $http.get('/mensajes/' + requerimientoId + '/read').success(function (data) {
                    $scope.mensajesList = data;
                    $scope.selectedUsuario = "";
                    $scope.messageText = "";
                }).error(function () {
                });
            }).error(function () {
                // handle error things
            });
        }

    };

    $scope.delMensaje = function(item) {
        item.fechaCreacion = new Date(item.fechaCreacion + "T" + item.fechaCreacion+"-03:00");
        $http({
            method: 'POST',
            url: '/mensajes/delete',
            data: JSON.stringify(item),
            headers: {'Content-Type': 'application/json'}
        }).success(function () {
            $("#" + item.id).css({"text-decoration": "line-through"});
            $("button[id='" + item.id + "']").hide();
        }).error(function () {
        });

    }
});