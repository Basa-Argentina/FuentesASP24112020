app.factory('elementoFactory', function ($http, $q) {

    let self = {
        respuesta: '',
        codigo: '',
        verificarElementoContenedor: function (codigoElemento, clienteAsp) {
            let defer = $q.defer();
            let request = {
                method: 'GET',
                url: `/elementos/${clienteAsp}/${codigoElemento}/`
            };

            $http(request).then(
                data => {
                    if (data.data.codigo){
                        self.codigo = data.data.codigo;
                        self.respuesta = data.data.respuesta;
                        defer.resolve();
                    }else {
                        defer.reject();
                    }
                },
                error => {
                    //self.respuesta = error;
                    defer.reject();
            });

            return defer.promise;
        }
    };

    return self;
});