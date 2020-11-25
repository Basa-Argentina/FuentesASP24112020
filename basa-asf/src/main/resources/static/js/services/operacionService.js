
app.factory('operacionFactory', ['$http', '$q', 'CONST', function ($http, $q, CONST) {

    let paginationOptions = {
        pageNumber: 1,
        pageSize: 25
    };

    var self = {
        codigo    : '',
        respuesta : '',
        adicional : '',
        error     : null,
        siguienteOperacion: function (request, operacion = CONST.CONTROLAR_ELEMENTOS) {
            let defer = $q.defer();
            let url;
            if (operacion === CONST.CONTROLAR_ELEMENTOS)
                url = `/siguiente_operacion/controlar_elementos`;
            else
                url = `/siguiente_operacion/controlar_remitos`;

            let http = {
                method: 'POST',
                url: url,
                data: request,
                headers: {
                    'Content-Type': 'application/json'
                }
            };

            $http(http)
                .success((data) => {
                    self.codigo = data.codigo;
                    if (data.codigo === CONST.ERR) self.respuesta = data.respuesta;
                    defer.resolve();
                })
                .error((error, status) => {
                    if (status === 500) {
                        self.codigo    = error.codigo;
                        self.respuesta = error.respuesta;
                    }else{
                        self.codigo = status;
                    }
                    defer.reject();
                });
            return defer.promise;
        },
        siguienteOperacionMarcarElementos: function(data){
            //Adaptar método siguienteOperación para que funciona con ésto también
            let defer = $q.defer();

            let request = {
                method         : 'POST',
                url            : data.url,
                headers        : {
                    'Content-Type': 'application/json'
                },
                data           : {
                    elementosLeidos: data.elementosLeidos,
                    operacion      : data.operacion,
                    sinControl     : data.sinControl
                }
            };

            $http(request)
                .then(result => {
                    self.respuesta = result.data;
                    defer.resolve();
                }, error => {
                    self.respuesta = error;
                    defer.reject();
                });

            return defer.promise;
        },
        getOperaciones: function (filtro) {
            //$http.defaults.headers.post['Content-Type'] = 'application/json';
            let defer = $q.defer();
            let data = {
                'sucursal'         : filtro.selectedSucursal,
                'tipoRequerimiento': filtro.selectedTipoRequerimiento,
                'estado'           : filtro.selectedTipoOperacion,
                'cliente'          : filtro.selectedCliente ? filtro.selectedCliente.value : null,
                'date'             : filtro.verUltimos,
                'num_requerimiento': filtro.num_requerimiento,
                'codigo_elemento'  : filtro.codigo_elemento,
                'page'             : paginationOptions.pageNumber - 1,
                'size'             : paginationOptions.pageSize
            };
            $http({
                method : 'POST',
                url    : '/operaciones/filter',
                data   : data,
                headers: {'Content-Type': 'application/json'}
            }).then((data) => {
                self.respuesta = data.data;
                defer.resolve();
            },(error) =>{
                defer.reject();
            });
            return defer.promise;
        },
        getTiposOperacion: function () {
            let defer = $q.defer();
            let request = {
                method: 'GET',
                url: '/operaciones/tipos',
                headers: {'Content-Type': 'application/json'}
            };
            $http(request)
                .then(data => {
                    self.respuesta = data;
                    defer.resolve();
                },error => {
                    defer.reject();
                });
            return defer.promise;
        }
    };
    return self;
}]);