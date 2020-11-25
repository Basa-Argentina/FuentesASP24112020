/**
 * Created by ldiaz on 20/07/2017.
 */
//Requerimiento form

var formRequerimiento = [
    {
        "type": "section",
        "htmlClass": "row",
        "items": [
            {
                "type": "section",
                "htmlClass": "col-xs-5",
                "items": [{
                    "key": "clienteEmp.personasJuridicas.razonSocial",
                    "title": "Cliente",
                    feedback: false,
                    "readonly": true
                }]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-1",
                "items": [{
                    "key": "requerimiento.numero",
                    title: "Req.",
                    feedback: false,
                    "readonly": true
                }]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-4",
                "items": [{
                    "key": "requerimiento.tipoRequerimiento.descripcion",
                    "title": "Tipo Requerimiento",
                    feedback: false,
                    "readonly": true
                }]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-2",
                "items": [{"key": "estado", feedback: false, "readonly": true}]
            },

        ]
    },
    {
        "type": "section",
        "htmlClass": "row",
        "items": [
            {
                "type": "section",
                "htmlClass": "col-xs-2",
                "items": [{"key": "fechaEntregaString", feedback: false, "readonly": false, "format": "dd-mm-yyyy",}]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-2",
                "items": [{"key": "turno", feedback: false, "readonly": true}]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-2",
                "items": [{"key": "fechaAltaString", feedback: false, "readonly": true, "format": "dd-mm-yyyy",}]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-5",
                "items": [{
                    "key": "usrAsignado.personasFisicas.nombreCompleto",
                    "title": "P.Asignada",
                    feedback: false,
                    "readonly": true
                }]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-1",
                "items": [{
                    "key": "requerimiento.cantidad",
                    "title": "Cantidad",
                    feedback: false,
                    "readonly": true
                }]
            },

        ]
    },
    {
        "type": "section",
        "htmlClass": "row",
        "items": [
            {
                "type": "section",
                "htmlClass": "col-xs-4",
                "items": [
                    {
                        "key": "requerimiento.empCarga.personasFisicas.nombreCompleto",
                        "title": "P.Carga",
                        feedback: false,
                        "readonly": true
                    }
                ]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-5",
                "items": [
                    {
                        "key": "requerimiento.empSolicitante.personasFisicas.nombreCompleto",
                        "title": "P.Solicitante",
                        feedback: false,
                        "readonly": true
                    }
                ]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-2",
                "items": [{"key": "depositos.descripcion", "title": "Sucursal", feedback: false, "readonly": true}]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-1",
                "items": [{"key": "cantidadPendientes", feedback: false, "readonly": true}]
            }
        ]
    },
    {
        "type": "section",
        "htmlClass": "row",
        "items": [

            {
                "type": "section",
                "htmlClass": "col-xs-2",
                "items": [{

                    "key": "elementos.elemento",
                    "type": "textarea",
                    feedback: false,
                    "readonly": true,
                    "title": "Elemento"
                }]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-3",
                "items": [{
                    "key": "elementos.tipoTrabajo",
                    "type": "textarea",
                    feedback: false,
                    "readonly": true,
                    "title": "Tipo de Trabajo"
                }]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-2",
                "items": [{

                    "key": "elementos.tipoElemento",
                    "type": "textarea",
                    feedback: false,
                    "readonly": true,
                    "title": "Tipo Elemento"
                }]
            },

            {
                "type": "section",
                "htmlClass": "col-xs-2",
                "items": [{
                    "key": "elementos.clienteId",
                    "type": "textarea",
                    feedback: false,
                    "readonly": true,
                    "title": "Cliente"
                }]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-3",
                "items": [{
                    "key": "elementos.estado",
                    "type": "textarea",
                    feedback: false,
                    "readonly": true,
                    "title": "Estado"
                }]
            },

        ]
    },
    {
        "type": "section",
        "htmlClass": "row",
        "items": [

            {
                "type": "section",
                "htmlClass": "col-xs-12",
                "items": [{
                    "key": "observaciones",
                    "type": "textarea",
                    "readonly": true,
                    feedback: false,
                    "title": "Observaciones"
                }]
            },
        ]
    },
    {
        "type": "actions",
        "items": [
            {
                "type": "submit",
                "style": "btn-danger",
                "title": "Aceptar",
            },
            {
                "type": "button",
                "style": "btn-info",
                "title": "Cancelar",
                "onClick": "ctrl.cancel()"
            }
        ]
    }
];

//Requerimiento form
var formRemito = [
    {
        "type": "section",
        "htmlClass": "row",
        "items": [
            {
                "type": "section",
                "htmlClass": "col-xs-2",
                "items": [{"key": "numeroCliente", "title": "Nro.Cliente", feedback: false, "readonly": true}]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-8",
                "items": [{
                    "key": "clienteEmp.personasJuridicas.razonSocial",
                    "title": "Cliente",
                    feedback: false,
                    "readonly": true
                }]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-2",
                "items": [
                    {"key": "today", "title": "Fecha", feedback: false, "readonly": true}
                ]
            },
        ]
    },
    {
        "type": "section",
        "htmlClass": "row",
        "items": [
            {
                "type": "section",
                "htmlClass": "col-xs-2",
                "items": [
                    {"key": "elementos.tipoElemento", "title": "Tipo Elemento", feedback: false, "readonly": true}
                ]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-2",
                "items": [
                    {
                        "key": "tipoEnvio",
                        "type": "select",
                        "title": "Tipo de Envío",
                        "titleMap": [
                            {"value": "propio", "name": "propio" ,selected: true},
                            {"value": "porCorreo", "name": "Por Correo"}],

                    },
                ]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-2",
                "items": [
                    {
                        "key": "requerimiento.tipoRequerimiento.tipoMovimiento",
                        "title": "Tipo",
                        "required": true,
                        "readonly": true, feedback: false,
                    },
                ]
            },

            {
                "type": "section",
                "htmlClass": "col-xs-1",
                "items": [
                    {"key": "cantidadPendientes", "title": "Cantidad", feedback: false, "readonly": true}
                ]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-5",
                "items": [{
                    "key": "requerimiento.tipoRequerimiento.descripcion",
                    "title": "Tipo Requerimiento",
                    feedback: false,
                    "readonly": true
                }]
            },

        ]
    },
    {
        "type": "section",
        "htmlClass": "row",
        "items": [

            {
                "type": "section",
                "htmlClass": "col-xs-12",
                "items": [{
                    "key": "elementos.elemento",
                    "type": "textarea",
                    feedback: false,
                    "readonly": true,
                    "title": "Elemento"
                }]
            },

        ]
    },
    {
        "type": "section",
        "htmlClass": "row",
        "items": [
            {
                "type": "section",
                "htmlClass": "col-xs-2",
                "items": [
                    {
                        "key": "requerimiento.empSolicitante.personasFisicas.id",
                        "title": "Nro",
                        feedback: false,
                        "readonly": true
                    }
                ]
            },
            {
                "type": "section",
                "htmlClass": "col-xs-10",
                "items": [
                    {
                        "key": "requerimiento.empSolicitante.personasFisicas.nombreCompleto",
                        "title": "Cliente",
                        feedback: false,
                        "readonly": true
                    }
                ]
            },

        ]
    },
    {
        "type": "section",
        "htmlClass": "row",
        "items": [

            {
                "type": "section",
                "htmlClass": "col-xs-12",
                "items": [{"key": "observaciones", "type": "textarea", feedback: true}]
            },
        ]
    },
    {
        "type": "section",
        "htmlClass": "row",
        "items": [

            {
                "type": "section",
                "htmlClass": "col-xs-12",
                "items": [{"key": "mensajes", "type": "textarea", feedback: false,"readonly": true}]
            },
        ]
    },
    {
        "type": "actions",
        "items": [
            {
                "type": "button",
                "style": "btn-danger",
                "title": "Aceptar",
                "onClick": "ctrl.answer('generaRemito')"
            },
            {
                "type": "button",
                "style": "btn-info",
                "title": "Cancelar",
                "onClick": "ctrl.cancel()"
            }
        ]
    }
];

//digitalizar form
var formDigitalizar = [
    {
        "type": "section",
        "htmlClass": "row",
        "items": [
            {
                "type": "section",
                "htmlClass": "col-xs-12",
                "items": [{
                    "key": "cantidadElementos",
                    "title": "Cantidad de Elementos",
                    feedback: false,
                    "readonly": false
                }]
            },
        ]
    },
    {
        "type": "section",
        "htmlClass": "row",
        "items": [
            {
                "type": "section",
                "htmlClass": "col-xs-12",
                "items": [{
                    "key": "horasArchivistas",
                    "title": "Horas de Archivista",
                    feedback: false,
                    "readonly": false
                }]
            },
        ]
    },
    {
        "type": "section",
        "htmlClass": "row",
        "items": [
            {
                "type": "section",
                "htmlClass": "col-xs-12",
                "items": [{
                    "key": "cantidadHojas",
                    "title": "Cantidad de hojas",
                    feedback: false,
                    "readonly": false
                }]
            },

        ]
    },
    {
        "type": "actions",
        "items": [
            {
                "type": "button",
                "style": "btn-danger",
                "title": "Aceptar",
                "onClick": "ctrl.answer('guardarDigital')"
            },
            {
                "type": "button",
                "style": "btn-info",
                "title": "Cancelar",
                "onClick": "ctrl.cancel()"
            }
        ]
    }
]

//Cliente en planta form
var formEnPlanta = [
    {
        "type": "section",
        "htmlClass": "row",
        "items": [
            {
                "type": "section",
                "htmlClass": "col-xs-12",
                "items": [{
                    "key": "requerimiento.empSolicitante.personasFisicas.nombreCompleto",
                    "title": "Solicitante",
                    feedback: false,
                    "readonly": true
                }]
            },
        ]
    },
    {
        "type": "section",
        "htmlClass": "row",
        "items": [
            {
                "type": "section",
                "htmlClass": "col-xs-12",
                "items": [{
                    "key": "requerimiento.empCarga.personasFisicas.nombreCompleto",
                    "title": "Cargó",
                    feedback: false,
                    "readonly": true
                }]
            },
        ]
    },
    {
        "type": "section",
        "htmlClass": "row",
        "items": [
            {
                "type": "section",
                "htmlClass": "col-xs-12",
                "items": [{
                    "key": "requerimiento.empAutorizante.personasFisicas.nombreCompleto",
                    "title": "Autorizante",
                    feedback: false,
                    "readonly": true
                }]
            },

        ]
    },
    {
        "type": "actions",
        "items": [
            {
                "type": "button",
                "style": "btn-info",
                "title": "Cerrar",
                "onClick": "ctrl.cancel()"
            }
        ]
    }
]

//precintos form
var formCantidad = [
    {
        "type": "section",
        "htmlClass": "row",
        "items": [
            {
                "type": "section",
                "htmlClass": "col-xs-12",
                "items": [{
                    "key": "cantidad",
                    "title": "Cantidad",
                    feedback: false,
                    "readonly": false
                }]
            },
        ]
    },
    {
        "type": "actions",
        "items": [
            {
                "type": "button",
                "style": "btn-danger",
                "title": "Aceptar",
                "onClick": "ctrl.answer('guardarCantidad')"
            },
            {
                "type": "button",
                "style": "btn-info",
                "title": "Cancelar",
                "onClick": "ctrl.cancel()"
            }
        ]
    }
];

//precintos form
var formAdjuntarHDR = [
    {
        "type": "section",
        "htmlClass": "row",
        "items": [
            {
                "type": "section",
                "htmlClass": "col-xs-12",
                "items": [{
                    "key": "nroHdR",
                    "title": "Número Hoja de Ruta",
                    feedback: false,
                    "readonly": false
                }]
            },
        ]
    },
    {
        "type": "actions",
        "items": [
            {
                "type": "button",
                "style": "btn-danger",
                "title": "Aceptar",
                "onClick": "ctrl.answer('guardarHdR')"
            },
            {
                "type": "button",
                "style": "btn-info",
                "title": "Cancelar",
                "onClick": "ctrl.cancel()"
            }
        ]
    }
];

//digitalizar form
var formModificarSiempre = [
    {
        "type": "section",
        "htmlClass": "row",
        "items": [
            {
                "type": "section",
                "htmlClass": "col-xs-12",
                "items": [{
                    "key": "cantidadImagenes",
                    "title": "Cantidad de Imagenes",
                    feedback: false,
                    "readonly": false
                }]
            },
        ]
    },
    {
        "type": "section",
        "htmlClass": "row",
        "items": [
            {
                "type": "section",
                "htmlClass": "col-xs-12",
                "items": [{
                    "key": "cantidadImagenesPlanos",
                    "title": "Cantidad de Planos",
                    feedback: false,
                    "readonly": false
                }]
            },
        ]
    },
    {
        "type": "section",
        "htmlClass": "row",
        "items": [
            {
                "type": "section",
                "htmlClass": "col-xs-12",
                "items": [{
                    "key": "horasArchivistas",
                    "title": "Horas de Archivista",
                    feedback: false,
                    "readonly": false
                }]
            },
        ]
    },
    {
        "type": "section",
        "htmlClass": "row",
        "items": [
            {
                "type": "section",
                "htmlClass": "col-xs-12",
                "items": [{
                    "key": "fletes",
                    "title": "Fletes",
                    feedback: false,
                    "readonly": false
                }]
            },

        ]
    },
    {
        "type": "actions",
        "items": [
            {
                "type": "button",
                "style": "btn-danger",
                "title": "Aceptar",
                "onClick": "ctrl.answer('guardarModificarSiempre')"
            },
            {
                "type": "button",
                "style": "btn-info",
                "title": "Cancelar",
                "onClick": "ctrl.cancel()"
            }
        ]
    }
];


