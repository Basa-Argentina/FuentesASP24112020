app.factory('grillaHojaRutaFactory', function () {
    var self = {
        cargada                 : false,
        vaciar                  : false,
        data                    : [],
        numero_hdr_seleccionada : '0',  //appCtrl checkea si existe HdR en pantalla
        numero_hdr_a_cargar     : '',   //HdRCtrl aplica filtro sobre esta HdR seteada. Se setea desde app.js en tipo operacion (Leer HdR y Remitos)
        event: '',
        dialog: {
            'title'    : '',
            'subtitle' : '',
            'columnOne': {
                'title'   : '',
                'subtitle': ''
            },
            'columnTwo': {
                'title'   : '',
                'subtitle': ''
            }
        },
    };

    return self;
});
