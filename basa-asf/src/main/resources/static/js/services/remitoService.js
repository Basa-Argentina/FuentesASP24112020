app.factory('remitoFactory', ['$http', '$q', 'CONST', function ($http, $q, CONST) {

    let self = {
        respuesta: {},
        error: '',
        getRemitoProceso: function(codigo){
            self.respuesta =  {};
            self.error = '';
            let defer = $q.defer();
            let request = {
                method: 'GET',
                url: `/remito/${codigo}`,
            };

            $http(request)
                .then(result => {
                    if (result.data.codigo == CONST.OK){
                        self.respuesta = result.data.respuesta;
                        defer.resolve();
                    }else{
                        self.error = result.data;
                        defer.reject();
                    }
                }, error => {
                    self.error = error.data;
                    defer.reject();
                });

            return defer.promise;
        },
        setRemitoProceso: function (remito) {
            self.respuesta =  {};
            self.error = '';

            let defer = $q.defer();
            let request = {
                method: 'POST',
                url: 'remito/save',
                data: remito,
                headers: {'Content-Type': 'application/json'}
            };

            $http(request)
                .then(result => {
                    if (result.data.codigo == CONST.OK){
                        self.respuesta = result.data.respuesta;
                        defer.resolve();
                    }else if(result.data.codigo == CONST.ERR){
                        self.respuesta = result.data.respuesta;
                        defer.resolve();
                    }
                    else {
                        self.error = result.data;
                        defer.reject();
                    }
                },error => {
                    console.log(error);
                    defer.reject();
            });
            return defer.promise;
        }
    }

    return self;
}]);