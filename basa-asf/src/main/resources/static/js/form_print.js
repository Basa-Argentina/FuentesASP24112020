/**
 * Created by ldiaz on 08/08/2017.
 */

// Data initialization
let remito = "";
let hojaRuta = "";
let legajo = "";
let hojaTarea = "";
const margen = 2;
const estados = {
    FINALIZADO_OK : "Finalizado OK"
};

/**
 * Generates the PDF file's body
 * @param row
 * @param tipo
 * @param empresa
 * @param nroRemito
 */
const genRemito = function (row, elementos, tipo, empresa, nroRemito, nroHoja) {
    const margen = 2;

    JsBarcode("#barcodeTarget", nroRemito, {format: "EAN13",displayValue: false});
    const img = document.getElementById("barcodeTarget").toDataURL("image/jpeg");
    const observaciones = row.observaciones.replace(new RegExp('\r?\n','g'), ' ');
    //const fechaSolicitud = new Date(row.fechaAlta + "T" + row.horaAlta + "-03:00");
    const fechaSolicitud = new Date(row.requerimiento.fechaAlta+ "T" + row.horaAlta + "-03:00").toLocaleDateString()

    const detalle = {
        fecha: new Date().getDate() + "/" + (new Date().getMonth() + 1) + "/" + new Date().getFullYear(),
        tipo: row.requerimiento.tipoRequerimiento.descripcion,
        operacion: row.tipoRemito===undefined?row.requerimiento.remito.tipoRemito:row.tipoRemito,
        estado: row.estado,
        cliente: row.clienteEmp.codigo  + " - " + row.clienteEmp.personasJuridicas.razonSocial,
        domicilio: row.requerimiento.clientesDirecciones.direcciones.direccionCompleta,
        provincia: row.requerimiento.clientesDirecciones.provincias.nombre,
        requerimiento: row.requerimiento.numero,
        solicita: row.requerimiento.empSolicitante.personasFisicas.nombreCompleto,
        fechaSolicitud: fechaSolicitud,
        elementos: "",
        totalCajas: 0,
        observaciones: observaciones.substring(0, 90),
        observaciones2: observaciones.substring(90, 200),
        observacionesTotal:row.observaciones,
        barcode: img,
        remito: nroRemito,
        pieFirma: empresa.pieFirma,
        tipoRequerimiento: row.requerimiento.tipoRequerimiento.codigo,
        cantidad: row.requerimiento.cantidad
    };

    let linea = 1;
    detalle.elementos = "\n";

    if(row.requerimiento.tipoRequerimiento.id===1){
        detalle.elementos = "Cajas vacias desde: " + row.relacionOpEl[0].codigoElemento.codigo + "\n\n" +
            "hasta: " + row.relacionOpEl[row.relacionOpEl.length-1].codigoElemento.codigo;
        detalle.totalCajas = row.relacionOpEl.length;
    } else {
        elementos.forEach(function (item) {
            if (item.codigoElemento) {
                detalle.elementos += "     [_] " + item.codigoElemento.codigo + " ";
                detalle.totalCajas += 1;
                if (detalle.elementos.length >= (66 * linea)) {
                    detalle.elementos += "\n\n";
                    linea += 1;
                }
            }
        });
    }

    remito.setFontSize(10);
    remito.setTextColor(0);
    remito.addImage(detalle.barcode, 'JPEG', 13, 2, 100, 30);
    remito.text('REMITO Nº: 011: ' + detalle.remito.substr(0,4) + "-" + detalle.remito.substr(4,detalle.remito.length), 250, 19 + margen);
    remito.setFillColor(255, 255, 255);
    remito.setDrawColor(0);
    remito.rect(13 , 23+ margen, 392, 55, "FD");
    remito.setFontSize(8);
    remito.text(tipo, 22, 75, "", 90);
    remito.addImage(empresa.logo, 'JPEG', 25, 25+margen, 70, 50);
    remito.setFontSize(6);
    remito.text('Dr. V. Alsina 2338 - Godoy Cruz\nMendoza - CP: (M5503HJX)\nTel: 4322047 - 4321296\n4321968', 105, 35);
    remito.setFontStyle('bold');
    remito.setFontSize(18);
    remito.rect(210, 35, 27, 27);
    remito.text("R", 217, 55);
    remito.setFontStyle('normal');
    remito.setFontSize(6);
    remito.text('IVA RESPONSABE INSCRIPTO\nCUIT: ' + empresa.cuit + '\nINGRESOS BRUTOS: ' + empresa.ingresosBrutos + '\nSEDE TIMBRADO: ' + empresa.timbrado + '\nFECHA INICIO ACTIVIDAD: ' + empresa.fechaInicio, 255, 35);
    remito.rect(13, 80, 392, 18);
    remito.setFontSize(8);
    remito.setFontStyle('bold');
    remito.text("Tipo: " + detalle.tipo, 15, 90);
    remito.setFontStyle('normal');
    remito.text("Fecha: " + detalle.fecha, 190, 90);
    remito.setFontStyle('bold');
    remito.rect(13, 100, 392, 36);
    remito.text("CLIENTE: " + detalle.cliente, 15, 113);
    remito.setFontStyle('normal');
    remito.text(`DOMICILIO: ${detalle.domicilio} - ${detalle.provincia}`, 15, 130);
    remito.rect(13, 138, 392, 18);
    remito.text("DESCRIPCION: ", 15, 150);
    remito.text("Hoja Nro: " + nroHoja, 180, 150);
    remito.text("REQUERIMIENTO: " + detalle.requerimiento, 230, 150);
    remito.rect(13, 156, 392, 308);
    remito.setFontSize(10);
    let totalItems = "";

    if (detalle.totalCajas!=0) {
        totalItems = "Total de items: " + row.relacionOpEl.length;
    } else if (detalle.tipoRequerimiento==="17") {
        totalItems = "Se envían " + row.relacionOpEl.length + " precintos.";
    }

    remito.text(detalle.elementos + "\n\n" + totalItems, 25, 166);
    remito.setFontSize(8);
    remito.rect(13, 466, 392, 52);
    remito.setFontStyle('bold');
    remito.text("Datos de la Entrega: ", 160, 476);
    remito.setFontStyle('normal');
    remito.text("Fecha de Solicitud: " + detalle.fechaSolicitud, 15, 486);
    remito.text("Sector: ", 215, 486);
    remito.text("Solicita: " + detalle.solicita, 15, 497);
    remito.text("Observaciones: " + detalle.observaciones, 15, 508);
    remito.text(detalle.observaciones2, 15, 515);
    remito.rect(13, 520, 392, 49);
    remito.text("___________________________", 15, 552);
    remito.text("____/____/____", 145, 552);
    remito.text("____:____", 215, 552);
    remito.text("___________________________", 275, 552);
    remito.text("Firma y Aclaracion: ", 15, 562);
    remito.text("Fecha: ", 165, 562);
    remito.text("Hora: ", 225, 562);
    remito.text(detalle.pieFirma, 285, 562);

};

/**
 * Main print remito function
 * @param row
 * @param codigoEmpresa
 * @param nroRemito
 */
const printRemito = function (row, codigoEmpresa, nroRemito) {
    const codEmpresa = row.clienteEmp.empresa_id;
    remito = new jsPDF('p', 'pt', 'A5');
    nroRemito = String("000000000000".substr(String(nroRemito).length) + String(nroRemito));

    /* Data initialization*/
    const empresa = [];
    empresa[20004] = {
        cuit: "30-69467417-4",
        ingresosBrutos: "913-6359179",
        establecimiento: "04 - 0427935 - 00",
        timbrado: "01 - Sede Central",
        fechaInicio: "01/11/1997",
        logo: logoBasa,
        pieFirma: "POR BASA: "
    };
    empresa[2] = {
        cuit: "30-69032695-3",
        ingresosBrutos: "0426297",
        establecimiento: "04 - 0426297 - 00",
        timbrado: "01 - Sede Central",
        fechaInicio: "01/10/1997",
        logo: logoCustodia,
        pieFirma: "POR CUSTODIA: "
    };

    const cantidadElementos = row.relacionOpEl.length>0?row.relacionOpEl.length:1;

    let inicio = 0;
    let fin = 33;
    let nroHoja = 1;
    for (let i = 1; i <= ((row.requerimiento.tipoRequerimiento.id === 1) ? 1 : Math.ceil(cantidadElementos / 33)); i++) {

        let elementos = row.relacionOpEl.slice(inicio, fin);
        genRemito(row, elementos, "ORIGINAL", empresa[codEmpresa], nroRemito, nroHoja);
        remito.addPage();
        genRemito(row, elementos, "DUPLICADO", empresa[codEmpresa], nroRemito, nroHoja);

        inicio = inicio + 33;
        if (fin <= cantidadElementos && row.requerimiento.tipoRequerimiento.id !== 1)
            remito.addPage();

        fin = fin + 33;
        nroHoja++;
    }

    remito.autoPrint();

    const url = remito.output('bloburl');

    const w = window.open(url);
    if (w == null || typeof(w) == 'undefined') return;
    else w.focus();
};

/**
 * Generates the PDF file's header
 * @param header
 */
const genHojaRutaHeader = function (header) {
    hojaRuta.setFontSize(14);
    hojaRuta.setTextColor(0);
    hojaRuta.setFontType('bold');
    hojaRuta.text("HOJA DE RUTA", 50, 25);
    hojaRuta.addImage(header.barcode, 'JPEG', 705, 15 , 100, 20);
    hojaRuta.setFontSize(10);
    //hojaRuta.text("Estado: " + header.estado, 455, 30);
    hojaRuta.text("Fecha: " + header.fecha, 12 + margen, 40);
    hojaRuta.text("Número: " + header.nroHdR, 200, 40);

    hojaRuta.setFillColor(220, 220, 220);
    hojaRuta.setDrawColor(0);
    hojaRuta.rect(13+ margen, 45, 800, 20, "FD");
    hojaRuta.text("REQ.", 15+ margen, 59);
    hojaRuta.text("Fecha C.", 65, 59);
    hojaRuta.text("CLIENTE", 165, 59);
    hojaRuta.text("SOLICITANTE", 505, 59);
};

/**
 * Generates the PDF file's body
 * @param posicion
 * @param detalle
 */
const genHojaRuta = function(posicion,detalle){
    posicion = posicion * 45;
    hojaRuta.setFillColor(255, 255, 255);
    hojaRuta.setDrawColor(0);
    hojaRuta.setFontSize(8);
    hojaRuta.setFontType('normal');
    hojaRuta.rect(13+ margen, 70 + posicion, 800, 40, "FD");
    hojaRuta.text(String(detalle.requerimiento), 15+ margen, 78 + posicion);
    hojaRuta.text(detalle.fechaEntrega, 65, 78 + posicion);
    hojaRuta.text(detalle.cliente, 165, 78 + posicion);
    hojaRuta.text(detalle.solicita, 505, 78 + posicion);
    hojaRuta.text(detalle.telefono, 505, 88 + posicion);
    hojaRuta.addImage(detalle.barcode, 'JPEG', 705, 78 + posicion, 100, 30);

    hojaRuta.text(detalle.direccion, 165, 88 + posicion);
    hojaRuta.text("Cant: " + detalle.elementos, 450, 88 + posicion);

    hojaRuta.text("Detalle: " + detalle.detalle.substr(0,117), 15+ margen, 98 + posicion);
    hojaRuta.text(detalle.detalle.substr(117,160), 15+ margen, 108+ posicion);
    hojaRuta.text("Hora: ____/____", 505, 98 + posicion);

    hojaRuta.setFillColor(255, 255, 255);
    hojaRuta.setDrawColor(0);
    hojaRuta.rect(820, 80 + posicion, 10, 10, "FD");
    hojaRuta.text("________________________________", 350, 560);
    hojaRuta.text(String(detalle.pagina), 13+ margen, 570);
    hojaRuta.text("Firma del Personal", 387, 570);
};

/**
 * Main function to print "HdR"
 * @param row
 * @param data
 * @param copia
 */
const printHojaRuta = function (row, data, copia) {
    hojaRuta = new jsPDF('l', 'pt', 'A4', 'landscape');
    if (copia !== "") {
        copia = "    (" + copia + ")";
    }
    JsBarcode("#barcodeTarget", data.Hdr, {format: "CODE39"});
    const img = document.getElementById("barcodeTarget").toDataURL("image/jpeg");
    const header = {
        nroHdR: data.Hdr + copia,
        fecha: new Date().getDate() + "/" + (new Date().getMonth() + 1) + "/" + new Date().getFullYear(),
        responsable: "responsable",
        estado: '',//row[0].requerimiento.hojaRuta===null?"Pendiente":row[0].requerimiento.hojaRuta.estado,
        barcode: img

    };

    genHojaRutaHeader(header);

    let posicion = -1;
    let pagina = 1;
    let totalElementos = 0;
    row.forEach(function (itemFila) {
        posicion++;
        JsBarcode("#barcodeTarget", itemFila.id_req, {format: "CODE39"});
        const img = document.getElementById("barcodeTarget").toDataURL("image/jpeg");
        //const filaDetalle = (itemFila.requerimiento.tipoRequerimiento.descripcion + " | " + itemFila.observaciones).substr(0,117);
        const detalle = {
            detalle      : itemFila.desc_tipo_req + (itemFila.observaciones ? " | " + itemFila.observaciones : ""),
            solicita     : itemFila.solicitante,
            fechaEntrega : itemFila.fecha_entrega,
            cliente      : itemFila.codigo_cliente + " - " + itemFila.razon_social_cliente,
            requerimiento: itemFila.num_req,
            barcode      : img,
            elementos    : itemFila.cant_elementos,
            sector       : data.sectores[posicion] != "" ? data.sectores[posicion] : "No registra sector",
            direccion    : itemFila.direccion_cliente,
            telefono     : (itemFila.telefono_cliente ? itemFila.telefono_cliente : ''),
            pagina       : pagina,
        };
        genHojaRuta(posicion, detalle);
        if (posicion === 9) {
            hojaRuta.addPage();
            pagina++;
            posicion = -1;
            genHojaRutaHeader(header);
        }
        totalElementos += detalle.elementos;
    });

    hojaRuta.setFontType('bold');
    hojaRuta.text("Cantidad de Requerimientos: " + String(row.length), 15, 78 + (posicion * 45) + 45);
    hojaRuta.text("Cantidad de Elementos: " + String(totalElementos), 650, 78 + (posicion * 45) + 45);

    hojaRuta.autoPrint();

    const url = hojaRuta.output('bloburl');

    const w = window.open(url);
    if (w == null || typeof(w) == 'undefined') return;
    else w.focus();
};

/**
 * Generates the PDF file's header
 * @param detalle
 */
const genTareaHeader = function (detalle, item) {
    hojaTarea.setFontSize(10);
    hojaTarea.setTextColor(0);
    hojaTarea.setFontStyle('bold');
    hojaTarea.setFontSize(14);
    hojaTarea.setTextColor(0);
    hojaTarea.text(detalle.tipo + " - " + detalle.asignado.toUpperCase(), 16, 25);
    hojaTarea.addImage(detalle.barcode, 'JPEG', 716, 10, 96, 20);
    hojaTarea.line(16, 32, 820, 32);
    hojaTarea.setFontSize(10);

    //Horizontal_1
    //Titulo Requerimiento
    hojaTarea.rect(16, 40, 100, 15);
    hojaTarea.text("Requerimiento", 18, 50);
    hojaTarea.setFontStyle('normal');
    hojaTarea.rect(116, 40, 140, 15);
    hojaTarea.text(String(detalle.requerimiento), 120, 50);
    //Titulo Cantidad
    hojaTarea.rect(178, 40, 50, 15);
    hojaTarea.text("Cantidad", 182, 50);
    hojaTarea.text((detalle.total === null ? '0' : String(detalle.total)), 232, 50);
    //Titulo  Razon Social
    hojaTarea.rect(228, 40, 100, 15);
    hojaTarea.text("Razon Social", 260, 50);
    hojaTarea.setFontStyle('normal');
    hojaTarea.rect(328, 40, 292, 15);
    hojaTarea.text(detalle.clienteNombre.substring(0, 50), 335, 50);
    //Fecha Alta
    hojaTarea.rect(620, 40, 80, 15);
    hojaTarea.text("Fecha Alta", 623, 50);
    hojaTarea.rect(700, 40, 120, 15);
    hojaTarea.text(String(detalle.fechaSolicitud), 703, 50);

    //Horizontal_2
    //Tipo Operacion
    hojaTarea.rect(16, 55, 100, 15);
    hojaTarea.text("Tipo Operacion", 18, 65);
    hojaTarea.rect(116, 55, 140, 15);
    hojaTarea.setFontSize(7);
    hojaTarea.text(detalle.tipo, 118, 65);
    //Solicitante
    hojaTarea.setFontSize(10);
    hojaTarea.rect(256, 55, 71, 15);
    hojaTarea.text("Solicitante", 260, 65);
    hojaTarea.rect(328, 55, 292, 15);
    hojaTarea.text(detalle.solicita.substring(0, 100), 338, 65);
    //Fecha Entrega
    hojaTarea.rect(620, 55, 80, 15);
    hojaTarea.text("Fecha Entrega", 623, 65);
    hojaTarea.rect(700, 55, 120, 15);
    hojaTarea.setFontSize(8);
    hojaTarea.text(String(detalle.fechaEntrega), 703, 65);


    //Horizontal_3
    //Nº Cliente
    hojaTarea.rect(16, 70, 100, 15);
    hojaTarea.text("Nº Cliente", 18, 80);
    hojaTarea.rect(116, 70, 140, 15);
    hojaTarea.text(String(detalle.cliente), 118, 80);
    //Autorizante
    hojaTarea.rect(256, 70, 71, 15);
    hojaTarea.text("Autorizante", 260, 80);
    hojaTarea.rect(328, 70, 292, 15);
    hojaTarea.text(detalle.autorizante.substring(0, 50), 338, 80);
    // Estado
    hojaTarea.rect(620, 70, 80, 15);
    hojaTarea.text("Estado", 623, 80);
    hojaTarea.rect(700, 70, 120, 15);
    hojaTarea.text(detalle.estado, 703, 80);

    //Horizontal_4
    //Observaciones
    hojaTarea.rect(16, 85, 100, 45);
    hojaTarea.text("Observaciones", 18, 110);
    hojaTarea.rect(116, 85, 704, 45)
    hojaTarea.setFontSize(7);
    hojaTarea.multiline = true;

    var incicio = 0;
    var fin = 130;
    var renglon = 95;
    if (parseInt(detalle.observacionesTotal.length) > 130){
        while (incicio <= parseInt(detalle.observacionesTotal.length)) {
            hojaTarea.text(detalle.observacionesTotal.substring(incicio, fin), 126, renglon);
            renglon = renglon + 10;
            incicio = fin;
            fin = fin + 140
        }
            hojaTarea.text(detalle.observacionesTotal.substring(incicio, detalle.observacionesTotal.length), 126, renglon);
        }
         else
        {
            hojaTarea.text(detalle.observacionesTotal, 126, 95);
    }
    hojaTarea.setFontSize(10);

    if (item)
    {
        // Titulos
        hojaTarea.setFontSize(8);
        hojaTarea.rect(16, 130, 33, 15);
        hojaTarea.text("Lote", 18, 140);
        hojaTarea.rect(49, 130, 50, 15);
        hojaTarea.text("T.Elemento", 52, 140);
        hojaTarea.rect(99, 130, 67, 15);
        hojaTarea.text("Etiqueta Legajo", 102, 140);
        hojaTarea.rect(166, 130, 114, 15);
        hojaTarea.text("N. Expediente", 168, 140);
        hojaTarea.rect(280, 130, 68, 15);
        hojaTarea.text("Caja",  282, 140);
        hojaTarea.rect(348, 130, 130, 15);
        hojaTarea.text("Posición",360, 140);
        hojaTarea.rect(478, 130, 80, 15);
        hojaTarea.text("Estado",500, 140);
        hojaTarea.rect(558, 130, 262, 15);
        hojaTarea.text("Ubicacion Provisoria", 620, 140);
    }

};

/**
 * Generates the PDF file's body
 * @param posicion
 * @param detalle
 * @param item
 */
const genTarea = function (posicion, detalle, item) {
    posicion = posicion * 15;
    //Valor Lote
    hojaTarea.setFontSize(8);
    hojaTarea.rect(16,  130 + posicion, 33, 15);
    hojaTarea.text("Lote",17, 140 + posicion);
    //Valor T.Elemento
    hojaTarea.rect(49,  130 + posicion, 50, 15);
    hojaTarea.setFontSize(6);
    hojaTarea.text(" " + String(item.tipoElemento.descripcion),50, 140 + posicion);
    var pos = " [Sin Posición]";
    var con = " [Sin Contenedor]";
    var UbProvisoriaCon = "" ;
    var UbProvisoriaLeg = "" ;

    //rect Etiqueta Legajo
    hojaTarea.setFontSize(8);
    hojaTarea.rect(99,  130 + posicion, 67, 15);
    //rect  N. Expediente
    hojaTarea.rect(166,  130 + posicion, 114, 15);

    //Control Tipo elemento
    if(parseInt(item.tipoElemento.contenido) === 1)
        // Legajos o Contenido

    {

        //Valor Etiqueta Legajo
        hojaTarea.text(" " + String(item.codigo),100, 140 + posicion);


        if (item.referencias != null) {
            hojaTarea.text(
                ((item.referencias[0].numero1 == null) ? "" : item.referencias[0].numero1) +
                ((item.referencias[0].texto1 == null) ? "" : "-" + (item.referencias[0].texto1).substring(0, 7)) +
                ((item.referencias[0].fecha1 == null) ? "" : "-" + parseInt(item.referencias[0].fecha1)), 168, 140 + posicion);
        } else {
            hojaTarea.text("Exp Sin Ref.", 168, 140 + posicion);
        }
        if (item.codigoContenedor.codigo!=null) {
            con = " " + item.codigoContenedor.codigo;
        }else{
            con = "Sin COdigo "

        }
        if (item.codigoContenedor.ubicacionProvisoria != null)  {
           UbProvisoriaCon =String(item.codigoContenedor.ubicacionProvisoria) ;
        }
        if(item.codigoContenedor.posicion != null) {
            pos = " E: " + item.codigoContenedor.posicion.estanteria.codigo + " (" + item.codigoContenedor.posicion.posVertical + ";" + item.codigoContenedor.posicion.posHorizontal + ")";
        }
        if (item.ubicacionProvisoria != null)  {
            UbProvisoriaLeg =String(item.ubicacionProvisoria) ;
        }

    } else {
        //Cajas

        hojaTarea.rect(99,  130 + posicion, 67, 15);
        hojaTarea.text(" " ,100, 140 + posicion);
        //Valor  N. Expediente
        hojaTarea.rect(166,  130 + posicion, 114, 15);

        if (item.codigo != null) {
            con = " " + item.codigo;
        }
        if (item.ubicacionProvisoria != null) {
            UbProvisoriaCon = String(item.ubicacionProvisoria);
        }
        if (item.posicion != null) {
            pos = " E: " + item.posicion.estanteria.codigo + " (" + item.posicion.posVertical + ";" + item.posicion.posHorizontal + ")";
        }
    }
    //Valor Caja
    hojaTarea.rect(280, 130 + posicion, 68, 15);
    hojaTarea.text(con, 280, 140 + posicion);
    //Valor Posición
    hojaTarea.rect(348, 130 + posicion, 130, 15);
    hojaTarea.text(pos,348, 140 + posicion);
    //Valor  Estado
    hojaTarea.rect(478, 130 + posicion, 80, 15);
    hojaTarea.text(String(item.estado),488, 140 + posicion);
    //Valor Ubicacion Provisoria
    hojaTarea.rect(558, 130 + posicion, 262, 15);
    if   ((item.referencias) == null){
        hojaTarea.text(" " + UbProvisoriaCon + " " + UbProvisoriaLeg, 558, 140 + posicion);
    }
    else {
        hojaTarea.text(" " + UbProvisoriaCon + " " + UbProvisoriaLeg + " Cant. Imagenes: " + item.referencias[0].cImagenes , 558, 140 + posicion);
    }
};


/**
 * Main funtion to print "Tareas"
 * @param rows
 * @param elementos
 */
const printTareas = function (rows, elementos) {
    hojaTarea = new jsPDF('l', 'pt', 'A4');
    let cantRows = rows.length;
    const clienteIdNumero = rows.clienteIdNumero;

    rows.forEach(function (row) {
        if (row.usrAsignado) {
            const nroRequerimiento = "D10" + String(row.requerimiento.numero);
            JsBarcode("#barcodeTarget", nroRequerimiento, {format: "CODE39", displayValue: false});
            const img = document.getElementById("barcodeTarget").toDataURL("image/jpeg");
            const observaciones = row.observaciones.replace(new RegExp('\r?\n','g'), ' ');

            const detalle = {
                fecha: new Date().getDate() + "/" + (new Date().getMonth() + 1) + "/" + new Date().getFullYear(),
                tipo: row.requerimiento.tipoRequerimiento.descripcion,
                estado: row.estado,
                cliente: clienteIdNumero[row.clienteEmp.personasJuridicas.id],
                clienteNombre: row.clienteEmp.personasJuridicas.razonSocial,
                domicilio: row.requerimiento.clientesDirecciones.direcciones.direccionCompleta,
                requerimiento: row.requerimiento.numero,
                solicita: row.requerimiento.empSolicitante.personasFisicas.nombreCompleto,
                fechaSolicitud: new Date(row.requerimiento.fechaAlta+ "T" + row.horaAlta + "-03:00"),
                fechaEntrega: new Date(row.fechaEntrega + "T" + row.horaEntrega + "-03:00"),
                elementos: "",
                total: row.cantidadPendientes,
                observaciones: observaciones.substring(0, 125),
                observaciones2: observaciones.substring(125, 250),
                observacionesTotal: observaciones.toString(),
                barcode: img,
                asignado: row.usrAsignado.personasFisicas.nombreCompleto,
                nroOperacion: row.id,
                deposito: row.depositos.descripcion,
                autorizante: row.requerimiento.empAutorizante.personasFisicas.nombreCompleto
            };
            detalle.fechaSolicitud = detalle.fechaSolicitud.getDate() + "/" + (detalle.fechaSolicitud.getMonth() + 1) + "/" + detalle.fechaSolicitud.getFullYear();
            detalle.fechaEntrega   = detalle.fechaEntrega.getDate()   + "/" + (detalle.fechaEntrega.getMonth()   + 1) + "/" + detalle.fechaEntrega.getFullYear();

            if (Array.isArray(elementos) && elementos.length) {
                if(Array.isArray(elementos[row.id])) {
                    elementos[row.id].forEach(function (item) {
                        genTareaHeader(detalle, item);
                    });
                } else {
                    genTareaHeader(detalle, '');
                }
            }

            let posicion = 0;
            if (Array.isArray(elementos) && elementos.length) {
                if(Array.isArray(elementos[row.id])) {
                    elementos[row.id].forEach(function (item) {
                        posicion++;
                        genTarea(posicion, detalle, item);
                        if (posicion >= 29){
                            hojaTarea.addPage();
                            genTareaHeader(detalle, item);
                            posicion = 0;
                        }
                    });
                }
            }

            cantRows -= 1;
            if (cantRows > 0) {
                hojaTarea.addPage();
            }
        }
        else
            cantRows -= 1;

    });
    hojaTarea.autoPrint();
    const url = hojaTarea.output('bloburl');

    const w = window.open(url);
    if (w == null || typeof(w) == 'undefined') return;
    else w.focus();
};

/**
 * Generates "etiquetas" for "legajo" type of element
 * @param item
 * @param posicion
 */
const genLegajo = function (item, posicion) {
    const margen = posicion === 0 ? 54 : 1;
    const fecha = new Date().getDate() + "/" + (new Date().getMonth() + 1) + "/" + new Date().getFullYear();
    const cliente = "Cl:" + item.clienteEmp.codigo;
    JsBarcode("#barcodeTarget", item.codigo, {format: "ean13", displayValue: true});
    const img = document.getElementById("barcodeTarget").toDataURL("image/jpeg");

    legajo.rect(margen, 0, 50, 23);
    legajo.setFont("arial");
    legajo.setFontSize(8);
    legajo.text("NO REMOVER " + cliente,10 + margen, 4);
    legajo.setFontSize(12);
    legajo.text(String(Number(item.codigo.substr(2,item.codigo.length))),16 + margen, 8);
    legajo.addImage(img, 'JPEG', 1 + margen, 9, 45, 12);
    legajo.setFontSize(8);
    legajo.text("ASP 2.0", 3 + margen, 16, "", 90);
    legajo.text(fecha, 48 + margen, 18, "", 90);
};

/**
 * Generates "etiquetas" for "caja" type of element
 * @param item
 */
const genCaja = function (item) {

    const cliente = "Cl:" + (item.clienteEmp!=null?item.clienteEmp.codigo:"");
    var canvas = document.createElement("canvas");
    JsBarcode(canvas, item.codigo, {format: "ean13", displayValue: true })
   const img = canvas.toDataURL("image/png;base64");


    legajo.rect(0, 0, 100, 35);
    legajo.setFont("arial");
    legajo.addImage(img,1, 10, 90, 25);
    legajo.setFontSize(35);
    legajo.text(String(Number(item.codigo.substr(2,item.codigo.length))),2, 10);
    legajo.setFontSize(24);
    legajo.text(cliente, 60, 8);
    legajo.setFontSize(14);
    legajo.text("ASP 2.0", 92, 30, "", 90);

}

/**
 * Main function to generate PDF file with printable "etiquetas"
 * @param rows
 * @param elementos
 */
const printEtiqueta = function (rows, elementos) {
    legajo = "";
    let page = false;
    let cant = 1;
    let posicion = 0;
    var isArray = false;

    rows.forEach(function (row) {
        let cantRows = row.cantidadPendientes;
        const requerimiento = row.requerimiento.numero;

        if (Array.isArray(elementos) && elementos.length) {
            if (Array.isArray(elementos[row.id])) {
                isArray = true;
                elementos[row.id].forEach(function (item) {
                    switch (item.tipoElemento.tipoEtiqueta) {
                        //case 1:
                        case "ETIQUETA MEDIA":
                            if (legajo === "") {
                                legajo = new jsPDF({orientation: "l", unit: "mm", format: [100, 35]});
                            }
                            genCaja(item, requerimiento);
                            page = true;
                            break;
                        //case 2:
                        case "ETIQUETA CHICA":
                            if (legajo === "") {
                                legajo = new jsPDF({orientation: "l", unit: "mm", format: [110, 23]});
                            }
                            posicion = cant % 2;
                            if (posicion === 0) {
                                page = true;
                            }
                            genLegajo(item, posicion);
                            break;
                        default:
                            genCaja(item);
                            break;
                    }

                    cantRows -= 1;
                    if (cantRows > 0 && page) {
                        legajo.addPage();
                        page = false;
                    }
                    cant++;
                });
            }
        }
    });

    if(isArray) {
        legajo.autoPrint();

        const url = legajo.output('bloburl');

        const w = window.open(url);
        if (w == null || typeof(w) == 'undefined') return;
        else w.focus();
    }

};

const printConceptos = function (data, row) {
    const reporte = new jsPDF('p', 'pt', 'A4');
    let posicion;
    let count = 0;
    reporte.setFontSize(14);
    reporte.setTextColor(0,0,0);
    reporte.setFontType('bold');
    reporte.text("REPORTE CONTROL AUTOMATICO DE CONCEPTOS", 100, 25);
    reporte.line(16, 32, 570, 32);
    reporte.setFontSize(10);
    reporte.setFontType('normal');
    reporte.text("Cliente: " + row.clienteEmp.codigo + " - " + row.clienteEmp.personasJuridicas.razonSocial, 20, 50);
    reporte.text("Requerimiento: (" + row.requerimiento.numero + ") " + row.requerimiento.tipoRequerimiento.descripcion + " Estado: " + row.requerimiento.estado, 20, 65);

    //generates PDF report
    reporte.setFontType('bold');
    reporte.setFontSize(8);
    reporte.text("Fecha", 30, 90);
    reporte.text(String("Concepto"), 80, 90);
    reporte.text(String("Cantidad"), 150, 90);
    reporte.text(String("Descripción"), 200, 90);


    data.forEach(detalle => {
        let fecha = new Date(detalle.fecha);
        fecha = fecha.getDate() + "/" + fecha.getMonth() + "/" + fecha.getFullYear();

        posicion = count * 15;
        reporte.setFontType('bold');
        reporte.text(String(count + 1) + ")", 20, 105 + posicion);
        reporte.setFontType('normal');
        reporte.text(String(fecha), 30, 105 + posicion);
        reporte.text(String(detalle.conceptoFacturable.codigo), 80, 105 + posicion);
        reporte.text(String(detalle.cantidad), 150, 105 + posicion);
        reporte.text(String(detalle.conceptoFacturable.descripcion), 200, 105 + posicion);
        count ++;
    });
    reporte.autoPrint();

    const url = reporte.output('bloburl');

    const w = window.open(url);
    if (w == null || typeof(w) == 'undefined') return;
    else w.focus();
};