app.factory('reportFactory', ['$http', '$q', 'CONST', function ($http, $q, CONST) {

    let self = {
        print: function (data) {
            let url = `/reporte/imprimir`;
            let http = {
                method: 'POST',
                data           : data,
                url: url,
                responseType: 'arraybuffer'
            };

            let defer = $q.defer();

            //$http.get(url, {responseType: 'arraybuffer'})
            $http(http)
                .then( response => {
                    let file = new Blob([response.data], {type: 'application/pdf'});
                    let fileURL = URL.createObjectURL(file);
                    window.open(fileURL);
                    defer.resolve();
                }, error => {
                    defer.reject();
                });

            return defer.promise;
        }
    };

    return self;
}]);

app.factory('reportFactory1', ['$http', '$q', 'CONST', function ($http, $q, CONST) {

    let self = {

        print: function (data) {

            let url = `/reporte/imprimirOpe`;

            let http = {
                method: 'POST',
                data           : data,
                url: url,
                responseType: 'arraybuffer'
            };


            let defer = $q.defer();

            //$http.get(url, {responseType: 'arraybuffer'})
            $http(http)
                .then( response => {
                    let file = new Blob([response.data], {type: 'application/pdf'});
                    let fileURL = URL.createObjectURL(file);
                    window.open(fileURL);
                    defer.resolve();
                }, error => {
                    defer.reject();
                });

            return defer.promise;
        }
    };

    return self;
}]);