app.factory('hojaRutaFactory', ['$http', '$q', function ($http, $q) {
    var self = {
        respuesta        : '',
        codigo           : '',
        inputNroHdR      : [],
        lastHdR          : '',
        getEstadoHojaRuta: function (pNumeroHdR) {
            let defer = $q.defer();
            let url = `hoja_de_ruta/check_estado_hdr/${pNumeroHdR.replace('.','-')}`;

            $http.get(url)
                .success(function (data) {
                    if (data.respuesta) self.respuesta = data.respuesta;
                    if (data.codigo)    self.codigo    = data.codigo;
                    defer.resolve();
                })
                .error(function (error) {
                    self.codigo = 'ERR';
                    defer.reject();
                });

            return defer.promise;
        },
        hojaRutaData     : [],
        getHojaRuta      : function(pNumero){
            let defer          = $q.defer();
            let requerimientos = [];
            $http.get(`hoja_de_ruta/findReq/${pNumero.replace('.','-')}`)
                .success(data => {
                    if (data.codigo)
                        self.codigo = data.codigo;
                    else
                        self.hojaRutaData = data;
                    defer.resolve();
                })
                .error(error => {
                    defer.reject();
                });
            return defer.promise;
        },
        setHojaRuta      : function (data) {
            let defer = $q.defer();
            let http = {
                method: 'POST',
                url: `/hoja_de_ruta/save`,
                data: data,
                headers: {
                    'Content-Type': 'application/json'
                },
            };
            $http(http)
                .success(function (data) {
                    if (data.codigo === 'OK'){
                        self.lastHdR = data.respuesta.split(".").length === 1
                            ? data.respuesta.concat('.00')
                            : data.respuesta;
                        defer.resolve();
                    }
                    else{
                        self.codigo    = data.codigo;
                        self.respuesta = data.respuesta;
                        defer.reject();
                    }
                })
                .error(function () {
                    defer.reject();
                });

            return defer.promise;
        },
        getNumHojaRuta   : function () {
            let defer = $q.defer();
            $http.get('/hoja_de_ruta/distinct')
                .success(function (data) {
                    self.inputNroHdR = [];
                    try {
                        angular.forEach(data, (numero) => {
                            self.inputNroHdR.push(numero);
                        });
                        defer.resolve();
                    }catch (e) {
                        defer.reject('Respuesta inesperada');
                    }
                })
                .error(function () {
                    defer.reject();
                });
            return defer.promise;
        },
        listNumHdr: [],
        getNumHojaRutaByNumReq : function (numReq) {
            let defer = $q.defer();
            $http({
                method: 'GET',
                url: `/hoja_de_ruta/find_hdr_by_req/${numReq}`
            }).then(data => {
                self.listNumHdr = data.data;
                defer.resolve();
            },error => {
                defer.reject();
            });
            return defer.promise;
        }
    };

    return self;
}]);