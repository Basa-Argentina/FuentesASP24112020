package com.aconcaguasf.basa.digitalize.config;

public class Const {

    //Tipos Lectura
    public static final String LECTURA_PLANTA           = "1";
    public static final String LECTURA_CAMION           = "2";
    public static final String LECTURA_ESTADO_PENDIENTE = "0";
    public static final String LECTURA_ESTADO_PROCESADA = "1";

    // ESTADOS
    public static final String FINALIZADO_OK         = "Finalizado OK";
    public static final String FINALIZADO_ASP        = "Finalizado";
    public static final String PENDIENTE             = "Pendiente";
    public static final String PROCESADO             = "Procesado";
    public static final String EN_TRANSITO           = "En Transito";
    public static final String OMITIDO               = "Omitido";
    public static final String MENSAJE               = "mensaje";
    public static final String ANULADA               = "Anulada";
    public static final String ASIGNAR_HDR           = "asignar_hdr";

    public static final String ESTADO_REMOVIDO       = "Removido";

    public static final String MOVIMIENTO_AGREGADO   = "Agregado";
    public static final String MOVIMIENTO_REMOVIDO   = "Removido";

    public static final String ESTADO_HDR_PENDIENTE  = "Pendiente";
    public static final String ESTADO_HDR_ANULADA    = "Anulada";
    public static final String ESTADO_HDR_DESPACHADA = "Despachada";
    public static final String ESTADO_HDR_FINALIZADA = "Finalizada";

    public static final Long ESTADO_REQ_HDR_PENDIENTE  = 1L;
    public static final Long ESTADO_REQ_HDR_ANULADO    = 2L;
    public static final Long ESTADO_REQ_HDR_CONTROLADO = 3L;

    public static final String REQ_TIPO_MOV_INGRESO = "ingreso";
    public static final String REQ_TIPO_MOV_EGRESO  = "egreso";

    public static final Long REQ_TIPO_RETIRO_CAJA_CANT = 11L;
    public static final Long REQ_TIPO_CAJA_VACIA       = 1L;

    public static final String ELEM_COD_GRUPO_DEVOLUCION = "DC";
    public static final String ELEM_COD_GRUPO_GUARDA     = "GC";
    public static final String ELEM_GRUPO_DEVOLUCION     = "Devolución cliente";
    public static final String ELEM_GRUPO_GUARDA         = "En guarda y custodia";

    public static final String ELEM_ESTADO_GUARDA      = "En Guarda";
    public static final String ELEM_ESTADO_CREADO      = "Creado";
    public static final String ELEM_ESTADO_EN_CONSULTA = "En Consulta";
    public static final String ELEM_ESTADO_EN_CLIENTE  = "En el Cliente";
    public static final String ELEM_ESTADO_PLANTA      = "En Planta";
    public static final String ELEM_ESTADO_TRANSITO    = "En Transito";
    public static final String ELEM_ESTADO_SALIDA    = "En Salida";

    // Codigos de error
    public static final String ERR_100 = "100";
    public static final String ERR_101 = "101";
    public static final String ERR_102 = "102";
    public static final String ERR_103 = "103";

    public static final String OK  = "OK";
    public static final String ERR = "ERR";
    public static final String INF = "INF";

    public static final String SS = "S";
    public static final String NN = "N";

    public static final String DB_100         = "DB_100";
    public static final String DB_100_MENSAJE = "Clave duplicada";

    public static final String ERR_100_MENSAJE = "Requerimiento en estado incorrecto";
    public static final String ERR_101_MENSAJE = "Requerimiento asignado a Hoja de Ruta en estado ";
    public static final String ERR_102_MENSAJE = "Requerimiento no encontrado";

    public static final String ACCION_MS001ELE = "MS001ELE"; //Creación de elemento
    public static final String ACCION_MS002ELE = "MS002ELE"; //Modificación de elemento
    public static final String ACCION_MS003ELE = "MS003ELE"; //Eliminación de elemento
    public static final String ACCION_MS004ELE = "MS004ELE"; //Actualización del cliente del contenedor por actualización de referencias.
    public static final String ACCION_MS005ELE = "MS005ELE"; //Borrado del contenedor del elemento por eliminación de referencias.
    public static final String ACCION_MS006ELE = "MS006ELE"; //Actualización del elemento por actualización de referencias.
    public static final String ACCION_MS007ELE = "MS007ELE"; //Actualización del elemento por creación de referencias.
    public static final String ACCION_MS008ELE = "MS008ELE"; //Actualización del cliente del contenedor por actualización de referencias en alta de rearchivos
    public static final String ACCION_MS009ELE = "MS009ELE"; //Borrado del contenedor del elemento por eliminación de referencias en alta/mod de rearchivos
    public static final String ACCION_MS010ELE = "MS010ELE"; //Actualización del elemento por creación de referencias en alta/mod de rearchivos.
    public static final String ACCION_MS011ELE = "MS011ELE"; //Actualización del elemento por creación de movimiento.
    public static final String ACCION_MS012ELE = "MS012ELE"; //Actualización del elemento por actualización de movimiento.
    public static final String ACCION_MS013ELE = "MS013ELE"; //Actualización del elemento por reposicionamiento.
    public static final String ACCION_MS014ELE = "MS014ELE"; //Actualización del elemento por asignación de posición libre.
    public static final String ACCION_MS015ELE = "MS015ELE"; //Actualización del elemento por alta/modificación de operación.
    public static final String ACCION_MS016ELE = "MS016ELE"; //Actualización del elemento tipo legajo contatible al  estado de la caja
    public static final String ACCION_MS017ELE = "MS017ELE"; //Actualización del elemento por creacion de remito de salida
    public static final String ACCION_MS003REF = "MS003REF"; //Borrado de referencia por eliminación de lote de referencia.
    public static final String ACCION_MS004REF = "MS004REF"; //Creación de referencia por alta/modificación de lote de referencia.
    public static final String ACCION_MS005REF = "MS005REF"; //Borrado de referencia por modificación de lote de referencia.
    public static final String ACCION_MS006REF = "MS006REF"; //Actualización de referencia por modificación de lote de referencia.
    public static final String ACCION_MS018ELE = "MS018ELE"; //Actualización del elemento por control de remito de entrada
    public static final String ACCION_MS019ELE = "MS019ELE"; //Actualización del elemento por control de remito de salida

    public static final String REPORT_REMITO = "remito";
    // Mensajes de Error
    public static final String INCONSISTENCIA_DB     = "Se detectó inconsistencia en la base de datos";
    public static final String INCONSISTENCIA_DB_HDR = "Se encontraron varias hojas de rutas vinculadas al requerimiento";

}
