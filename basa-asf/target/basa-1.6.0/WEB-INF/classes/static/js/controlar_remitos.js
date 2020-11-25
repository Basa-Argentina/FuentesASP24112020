app.controller('controlarRemitosCtrl', function ($scope, $http, $mdDialog, $element, $timeout) {
    const scope = $scope;
    // service call to trigger data validation
    $http.get('/operaciones/controlar_remitos').success(function (data) {
        // handle success things
        const json = data;
        const reporte = new jsPDF('l', 'pt', 'A4');
        let posicion;
        let count = 0;
        reporte.setFontSize(14);
        reporte.setTextColor(0,0,0);
        reporte.setFontType('bold');
        reporte.text("REPORTE CONTROL AUTOMATICO DE REMITOS", 250, 25);
        reporte.line(16, 32, 820, 32);

        //generates PDF report
        json.forEach(detalle => {
            posicion = count * 15;
            if(detalle.mensaje.match("entregados")===null) {
                reporte.setTextColor(255, 0, 0);
            } else {
                reporte.setTextColor(0, 0, 0);
            }
            reporte.setFontSize(8);
            reporte.setFontType('bold');
            reporte.text("Requerimiento: ", 15, 58 + posicion);
            reporte.setFontType('normal');
            reporte.text(String(detalle.requerimiento), 80, 58 + posicion);
            reporte.setFontType('bold');
            reporte.text("Detalle: ", 115, 58 + posicion);
            reporte.setFontType('normal');
            reporte.text(detalle.mensaje, 150, 58 + posicion);
            count ++;
        });

        $timeout(function() {
            $mdDialog.cancel();
            scope.$parent.answer('aceptar');

            const url = reporte.output('bloburl');
            window.open(url);

        });


     }).error(function () {
     // handle error things
        $mdDialog.cancel();
     });
});