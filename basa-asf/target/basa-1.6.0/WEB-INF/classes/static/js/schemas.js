/**
 * Created by ldiaz on 01/08/2017.
 */
var requerimientoSchema = {
    type: 'object',
    title: "Requerimiento",
    style: "width: 85vw; overflow-x: hidden;",
    properties: {
        "depositos.descripcion": { type: "string", title: "Sucursal" },
        "fechaEntregaString": { type: "string", title: "F.Entrega","pattern": /^(0[1-9]|1\d|2\d|3[01])\/(0[1-9]|1[0-2])\/(19|20)\d{2}$/,
            "validationMessage": "Fecha no válida", "required": true},
        "turno": { type: "string", title: "Turno" },
        "requerimiento.tipoRequerimiento.descripcion": { type: "string", title: "Tipo Requerimiento" },
        "tipoOperacion_id": { type: "string", title: "Tipo Operacion" },
        "requerimiento.numero": { type: "string", title: "Req." },
        "clienteEmp.personasJuridicas.razonSocial": { type: "string", title: "Cliente" },
        "requerimiento.cantidad": { type: "string", title: "Elementos" },
        "requerimiento.empCarga.personasFisicas.nombreCompleto": { type: "string", title: "P.Carga" },
        "requerimiento.empSolicitante.personasFisicas.nombreCompleto": { type: "string", title: "P.Solicitante" },
        "estado": { type: "string", title: "Estado" },
        "fechaAltaString": {type: "string", title: "F.Carga"},
        "users.personasFisicas.nombreCompleto": {type: "string", title: "P.Carga"},
        "usrAsignado.personasFisicas.nombreCompleto": {type: "string", title: "P.Asignada"},
        "requerimiento.cantidad": { type: "string", title: "Cantidad"},
        "observaciones": { type: "string", title: "Observaciones" },
    }
};

var remitoSchema = {
    type: 'object',
    title: "Remito",
    style: "width: 80vw;",
    properties: {
        "numeroCliente": {type: "string", title: "Nro"},
        "today": {type: "string", title: "Fecha"},
        "remito": {type: "string", title: "Remito"},
        "clienteEmp.personasJuridicas.razonSocial": {type: "string", title: "Cliente"},
        "requerimiento.tipoRequerimiento.descripcion": {type: "string", title: "Tipo Requerimiento"},
        "requerimiento.empSolicitante.personasFisicas.nombreCompleto": {type: "string", title: "P.Solicitante"},
        "requerimiento.cantidad": {title: "Cantidad", type: "string"},
        //"tipoRemito": {title: "Tipo", type: "string"},
        "requerimiento.tipoRequerimiento.tipoMovimiento": {title: "Tipo", type: "string"},
        "correo": {title: "Correo",type: "boolean"},
        "tipoEnvio": {title: "Envio", type: "enum", default: "string:propio", value:"propio", validationMessage: "Campo Requerido"},
        "observaciones": { type: "string", title: "Observaciones", maxLength: 161, validationMessage: "Longitud excede area de impresión" },
        "mensajes": { type: "string", title: "Mensajes" },
    },
    required: ["tipoRemito"]
}

var digitalizarSchema = {
    type: 'object',
    title: "Consulta Digital",
    subtitle: "",
    style: "width: 35vw; ",
    properties: {
        "cantidadElementos": {type: "string", title: "Cantidad de Elementos", "pattern":/^\+?(0|[1-9]\d*)$/, "validationMessage": "Cantidad no válida"},
        "horasArchivistas": {type: "number", title: "Horas de Archivista"},
        "cantidadHojas": {type: "string", title: "Cantidad de hojas", "pattern":/^\+?(0|[1-9]\d*)$/, "validationMessage": "Cantidad no válida"},
    },
    required: ["cantidadElementos","horasArchivistas","cantidadHojas"]
};
var modificarSchema = {
    type: 'object',
    title: "Carga de Datos",
    subtitle: "",
    style: "width: 35vw; ",
    properties: {
        "cantidadImagenes": {type: "number", title: "Cantidad de Imagenes", "pattern":/^\+?(0|[1-9]\d*)$/, "validationMessage": "Cantidad no válida"},
        "cantidadImagenesPlanos": {type: "number", title: "Cantidad de Planos" , "pattern":/^\+?(0|[1-9]\d*)$/, "validationMessage": "Cantidad no válida"},
        "fletes": {type: "number", title: "Fletes", "pattern":/^\+?(0|[1-9]\d*)$/, "validationMessage": "Cantidad no válida"},
        "horasArchivistas": {type: "number", title: "Horas de Archivista", "pattern":/^\+?(0|[1-9]\d*)$/, "validationMessage": "Cantidad no válida"},

    },
  //  required: ["cantidadElementos","horasArchivistas","cantidadHojas"]
};



var enPlantaSchema = {
    type: 'object',
    title: "Consulta en Planta",
    style: "width: 30vw; ",
    properties: {
        "entity.requerimiento.empSolicitante.personasFisicas.nombreCompleto": {type: "number", title: "Solicitante"},
        "entity.requerimiento.empCarga.personasFisicas.nombreCompleto": {type: "number", title: "Cargó"},
        "entity.requerimiento.empAutorizante.personasFisicas.nombreCompleto": {type: "number", title: "Autorizante"},
    },
    //required: ["cantidadElementos","horasArchivistas","cantidadHojas"]
};

var cantidadSchema = {
    type: 'object',
    title: "Cantidad",
    style: "width: 35vw; ",
    properties: {
        "cantidad": {type: "number", title: "Cantidad"},
    },
    required: ["cantidad"]
};

var adjuntarHDRSchema = {
    type: 'object',
    title: "Número Hoja de Ruta",
    style: "width: 30vw; ",
    properties: {
        "nroHdR": {type: "string", title: "Número Hoja de Ruta", "pattern":/^(?=.*[1-9])\d+\.\d{2}$/, "validationMessage": "Hoja de Ruta no válida"},
    },
    required: ["nroHdR"]
};