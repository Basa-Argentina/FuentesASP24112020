app.factory('clienteFactory', function ($http, $q) {
    let self = {
        respuesta: '',
        getClientes: function () {
            let defer = $q.defer();
            let request = {
                method: 'GET',
                url: '/clientes_emp/get_all',
                headers: {'Content-Type': 'application/json'}
            };
            $http(request).then(data => {
                self.respuesta = data;
                defer.resolve();
            },error => {
                defer.reject();
            });
            return defer.promise;
        }
    };
    return self;
});