
app.factory('requerimientoFactory', ['$http', '$q', 'CONST', function ($http, $q, CONST) {
    //Verificará si el requerimiento está disponible para continuar con la operación de forma aislada
    var self = {
        codigo    : '',
        respuesta : '',
        adicional : '',
        getExisteHDR: function (pIdRequerimiento) {
            let defer = $q.defer();
            let url = `/hoja_de_ruta/requerimiento/${pIdRequerimiento}`;

            $http.get(url)
                .success((data) => {
                    self.codigo = data.codigo;
                    if (data.codigo === CONST.OK)
                        self.respuesta = data.respuesta;
                    defer.resolve();
                })
                .error((error, status) => {
                    if (status === 500) {
                        self.codigo    = error.codigo;
                        self.respuesta = error.respuesta;
                    }
                    defer.reject();
                });
            return defer.promise;
        },
        getEstadoRequerimientos: function (pRequerimientos) {

            let defer = $q.defer();
            let url = `/requerimiento/check_req_hdr/${pRequerimientos}`;
            let http = {
                method: 'GET',
                url: url,
                data: pRequerimientos,
                headers: {
                    'Content-Type': 'application/json'
                }
            };

            $http(http)
                .success((data) => {
                    self.respuesta = data;  //Array
                    defer.resolve();
                })
                .error((error) => {
                    console.log(error);
                    defer.reject();
                });
            return defer.promise;
        },
        getTiposRequerimiento: function () {
            let defer = $q.defer();
            let request = {
                method: 'GET',
                url: '/requerimiento/tipo_view',
                headers: {'Content-Type': 'application/json'}
            };
            $http(request)
                .then((data) => {
                    self.respuesta = data;
                    defer.resolve();
                },
                (error) => {
                    defer.reject();
                });
            return defer.promise;
        }
    };
    return self;
}]);