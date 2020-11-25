package com.aconcaguasf.basa.digitalize.controller;

import com.aconcaguasf.basa.digitalize.bussiness.ElementosBussiness;
import com.aconcaguasf.basa.digitalize.bussiness.RequerimientoBussiness;
import com.aconcaguasf.basa.digitalize.config.Const;
import com.aconcaguasf.basa.digitalize.dto.*;
import com.aconcaguasf.basa.digitalize.dto.ctrl.ReqControlarElementoDTO;
import com.aconcaguasf.basa.digitalize.dto.ctrl.ReqControlarRemitosDTO;
import com.aconcaguasf.basa.digitalize.dto.ctrl.ResponseDTO;
import com.aconcaguasf.basa.digitalize.exceptions.CustomException;
import com.aconcaguasf.basa.digitalize.model.*;
import com.aconcaguasf.basa.digitalize.repository.*;
import com.aconcaguasf.basa.digitalize.util.AuditoriasHelper;
import com.aconcaguasf.basa.digitalize.util.Commons;
import com.aconcaguasf.basa.digitalize.util.ConceptosHelper;
import com.aconcaguasf.basa.digitalize.util.StringHelper;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.Size;
import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@Controller
public class SiguienteOperacionController {

    @Autowired
    UsersxGrupoRepository usuariosPlantaRepository;

    @Autowired
    OperacionesRepository operacionesRepository;

    @Autowired
    RemitosRepository remitosRepository;

    @Autowired
    TransporteRepository transporteRepository;

    @Autowired
    RemitosDetalleRepository remitosDetalleRepository;

    @Autowired
    ElementosRepository elementosRepository;

    @Autowired
    RelacionOpElRepository relacionOpElRepository;

    @Autowired
    RequerimientoRepository requerimientoRepository;

    @Autowired
    HojaRutaRepository hojaRutaRepository;

    @Autowired
    HdRxOperacionRepository hdRxOperacionRepository;

    @Autowired
    MensajesRepository mensajesRepository;

    @Autowired
    ElementosLiteRepository elementosLiteRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RelacionReqConRepository relacionReqConRepository;
    @Autowired
    SeriesRepository seriesRepository;

    @Autowired
    TipoOperacionesRepository tipoOperacionesRepository;

    @Autowired
    private AuditoriasRepository auditoriasRepository;

    @Autowired
    private ElementoHistoricoRepository elementoHistoricoRepository;

    @Autowired
    @Qualifier("requerimientoBussiness")
    private RequerimientoBussiness requerimientoBussiness;

    @Autowired
    @Qualifier("elementosBussiness")
    private ElementosBussiness elementosBussiness;

    private static final String FINALIZADO_OK  = "Finalizado OK";
    private static final String FINALIZADO_ASP = "Finalizado";
    private static final String PENDIENTE      = "Pendiente";
    private static final String EN_TRANSITO    = "En Transito";
    private static final String OMITIDO        = "Omitido";
    private static final String MENSAJE        = "mensaje";

    /**
     * Asignar Tarea View
     * Shows Basa´s employees to select one of them
     *
     * @param principal Security
     * @return
     * @throws IOException
     */


    @ResponseBody
    @RequestMapping(value = "/siguiente_operacion/asignar_tarea/read", method = GET)
    public List<UsersxGrupo> usuariosPlanta(@AuthenticationPrincipal Principal principal) {
        return usuariosPlantaRepository.findByGroup(23);
    }

    /**
     * Seves usuario_asignado to current Operacion
     *
     * @param brokerUsuario Current User an Operacion Broker
     * @param principal
     * @throws Exception
     */

    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/siguiente_operacion/asignar_tarea/save", method = POST)
    public void usuariosPlantaSave(@RequestBody BrokerUsuario brokerUsuario, @AuthenticationPrincipal Principal principal) throws Exception {

        UsersxGrupo user = brokerUsuario.getUser();
        Operaciones operacion = brokerUsuario.getOperacion();
        if (operacion.getRequerimiento().getTipoRequerimiento_id().equals("1")) {
            Users currentUser = userRepository.findByUsername(principal.getName());

            // Primero verifico que el usuario posea la cantidad necesaria de etiquetas a reservar para el requerimiento
            Integer cantidadElementosDisponibles = elementosRepository.getCantidadDisponible(1, operacion.getClienteAsp_id(),currentUser.getId());

            if (operacion.getCantidadPendientes() > cantidadElementosDisponibles)
                throw new Exception("No posees la cantidad necesaria de etiquetas disponible.");

            elementosRepository.updateReserva(operacion.getCantidadPendientes(), operacion.getRequerimiento_id().toString(), 1, operacion.getClienteAsp_id(), currentUser.getId());
            List<Elementos> elementosList = elementosRepository.findByReqId(operacion.getRequerimiento_id().toString());
            if (elementosList.size() != operacion.getCantidadPendientes())
                throw new Exception("No Hay etiquetas disponibles");
            List<RelacionOpEl> relacionOpElList = new ArrayList<>();
            for (Elementos ele : elementosList) {
                RelacionOpEl relacionOpEl = new RelacionOpEl();
                relacionOpEl.setEstado(PENDIENTE);
                relacionOpEl.setOperacion_id(operacion.getId().toString());
                ele.setEstado(EN_TRANSITO);
                ele.setClienteEmp_id(operacion.getClienteEmp_id());
                relacionOpEl.setElemento_id(ele.getId().toString());
                relacionOpElList.add(relacionOpEl);
                relacionOpElRepository.save(relacionOpElList);
            }
            //Actualizar contenidos
            /*List<Elementos> elementosContenidos =  elementosRepository.findContenidos(elementosList.stream().map(Elementos::getId).collect(Collectors.toList()));
            elementosContenidos.forEach(c -> c.setEstado("En Planta S/C"));
            elementosList.addAll(elementosContenidos);*/
            elementosRepository.save(elementosList);
        }

        operacion.setUsuarioAsignado_id(user.getUser_id());
        operacion.setTipoOperacion_id(operacion.getTipoOperaciones().getTipoOperacionSiguiente_id() == null ? operacion.getTipoOperacion_id() : operacion.getTipoOperaciones().getTipoOperacionSiguiente_id().toString());
        operacionesRepository.save(operacion);


    }

    /**
     * Service to set next tipoOperacion_id in DB
     *
     * @param idOperacionActual #id of current Operacion
     * @param principal
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/siguiente_operacion/siguiente", method = POST)
    public void operacionesSiguiente(@RequestBody final List<Long> idOperacionActual, @AuthenticationPrincipal Principal principal) {

        Users currentUser = userRepository.findByUsername(principal.getName());

        for (Long id : idOperacionActual) {
            Operaciones operaciones = operacionesRepository.findOne(id);

            operaciones.setTipoOperacion_id(operaciones.getTipoOperaciones().getTipoOperacionSiguiente_id() == null ? operaciones.getTipoOperacion_id() : operaciones.getTipoOperaciones().getTipoOperacionSiguiente_id().toString());
            if (operaciones.getTipoOperaciones().getTipoOperacionSiguiente_id() == 19 || operaciones.getTipoOperaciones().getTipoOperacionSiguiente_id() == 48) {
                operaciones.setUsuarioFinalizo_id(currentUser.getId());
                operaciones.setOrigen_finalizado("Finalizacion automatica ASF. Tipo Operacion anterior: " + operaciones.getTipoOperaciones().getTipoOperacionSiguiente_id());
                operaciones.setTipoOperacion_id("48");
                operaciones.setEstado(FINALIZADO_OK);
                Requerimiento requerimiento = operaciones.getRequerimiento();
                requerimiento.setEstado(FINALIZADO_OK);
                requerimientoRepository.save(requerimiento);
                for (RelacionOpEl rel : operaciones.getRelacionOpEl()) {
                    if (rel.getEstado() != null && rel.getEstado().equals(PENDIENTE)) rel.setEstado("Procesado");
                    relacionOpElRepository.save(rel);
                }
            }
            //CF impresion de etiquetas para caja vacia, etiquetas y consulta digital
            List<String> tipos = asList("1", "8", "9");
            if (tipos.contains(operaciones.getRequerimiento().getTipoRequerimiento_id()))
                relacionReqConRepository.save(ConceptosHelper.setConcepto(operaciones.getRequerimiento_id(), currentUser.getId(), operaciones.getCantidadPendientes(), 15));
            operacionesRepository.save(operaciones);
        }
    }

    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/siguiente_operacion/legajo/siguiente", method = POST)
    public HashMap<String, String> legajosSiguiente(@RequestBody BrokerElementos brokerElementos, @AuthenticationPrincipal Principal principal) {

        Operaciones operacion                   = brokerElementos.getOperacion();
        List<String> elementosleidos            = brokerElementos.getElementosLeidos();
        List<RelacionOpEl> relacionOpElList     = new ArrayList<>();
        List<String> elementos                  = new ArrayList<>();
        HashMap<String, String> elementos_error = new HashMap<String, String>();
        Boolean operacion_error                 = false;

        relacionOpElList.addAll(operacion.getRelacionOpEl());
        for (RelacionOpEl rel : relacionOpElList) {
            elementos.add(rel.getCodigoElemento().getCodigo());
        }
        Users user = userRepository.findByUsername(principal.getName());
        //CF carga de Legajos
        Integer[] idListaConceptos = {14};
        //Cf para Consulta Digital

        Requerimiento requerimiento = null;
        if (operacion.getRequerimiento().getTipoRequerimiento_id().equals("8")){
            idListaConceptos = new Integer[]{14, 16, 17};
        } else if (operacion.getRequerimiento().getTipoRequerimiento_id().equals("16")) {
            //Búsqueda documentación
            requerimiento = operacion.getRequerimiento();
            requerimiento.setTipoRequerimiento_id(brokerElementos.getTipoRequerimiento().toString());
        }

        Integer getCantidadPendientes = operacion.getCantidadPendientes();
        if(operacion.getCantidadPendientes() == null)
            getCantidadPendientes = 0;

        relacionReqConRepository.save(ConceptosHelper.setConcepto(operacion.getRequerimiento_id(), user.getId(), getCantidadPendientes, idListaConceptos));

        List<String> elementosNuevos = new ArrayList<>();
        elementosNuevos.addAll(elementosleidos);

        elementosleidos.retainAll(elementos);
        elementosNuevos.removeAll(elementosleidos);
        elementos.removeAll(elementosleidos);

        Integer cantidadPendientes = elementosleidos.size() + elementosNuevos.size();

        operacion.setCantidadOmitidos(elementos.size());

        for (RelacionOpEl rel : relacionOpElList) {
            if(elementos.contains(rel.getCodigoElemento().getCodigo())){
                rel.setEstado(OMITIDO);
                relacionOpElRepository.save(rel);
            }else{

                elementosBussiness.crearElementoHistorico(rel.getCodigoElemento().getCodigo(),operacion.getClienteAsp_id(),operacion.getClienteEmp_id(),user.getId(),Const.ACCION_MS015ELE);
            }
        }


        for (String eleNuevo : elementosNuevos) {
            /* Search New Element Added */
            String elementExist = elementosRepository.findByCodigo(eleNuevo, operacion.getClienteAsp_id());
            if (elementExist != null) {
                /* If Element exist, then Search by Client */
                String elementNotClient = elementosRepository.findByCodigoAndCliente(eleNuevo, operacion.getClienteAsp_id(), operacion.getClienteEmp_id());
                if (elementNotClient != null) {
                    RelacionOpEl relacionOpEl = new RelacionOpEl();
                    relacionOpEl.setEstado(PENDIENTE);
                    relacionOpEl.setElemento_id(elementNotClient);
                    relacionOpEl.setOperacion_id(operacion.getId().toString());
                    relacionOpElRepository.save(relacionOpEl);

                    ElementosLite elementoLite = elementosLiteRepository.findOne(Long.parseLong(elementNotClient));
                    elementoLite.setEstado(EN_TRANSITO);
                    elementosLiteRepository.save(elementoLite);

                    elementosBussiness.crearElementoHistorico(eleNuevo, operacion.getClienteAsp_id(), operacion.getClienteEmp_id(),user.getId(), Const.ACCION_MS015ELE);
                } else {
                    elementos_error.put("notClient", (elementos_error.get("notClient") != null) ? elementos_error.get("notClient") + ", " + eleNuevo : eleNuevo);
                    operacion_error = true;
                    cantidadPendientes -= 1;
                }
            } else {
                elementos_error.put("notExist", (elementos_error.get("notExist") != null) ? elementos_error.get("notExist") + ", " + eleNuevo : eleNuevo);
                operacion_error = true;
                cantidadPendientes -= 1;
            }
            if (operacion.getRequerimiento().getCantidad() == null || operacion.getRequerimiento().getCantidad() == 0) {
                operacion.getRequerimiento().setCantidad(cantidadPendientes);
                requerimientoRepository.save(operacion.getRequerimiento());
            }
            operacion.setCantidadPendientes(cantidadPendientes);
            operacionesRepository.save(operacion);

        }

        operacion.setTipoOperacion_id(operacion.getTipoOperaciones().getTipoOperacionSiguiente_id() == null ? operacion.getTipoOperacion_id() : operacion.getTipoOperaciones().getTipoOperacionSiguiente_id().toString());

        if (brokerElementos.getSiguienteRequerimiento_id() != null) {
            String idSigOper = tipoOperacionesRepository.findIdByTipoReq(brokerElementos.getSiguienteRequerimiento_id());
            operacion.setTipoOperacion_id(idSigOper);
        }
        operacionesRepository.save(operacion);

        /* Al ser Búsqueda documentación, se setea el nuevo id del tipo Requerimiento seleccionado en el front
            2 CONSULTA NORMAL DE LEGAJOS
            4 CONSULTA NORMAL DE CAJA
            8 CONSULTA DIGITAL
        */
        if (requerimiento instanceof Requerimiento){
            requerimientoRepository.save(requerimiento);
        }

        return elementos_error;
    }

    //servicio de prueba para remito
    @ResponseBody
    @RequestMapping(value = "/siguiente_operacion/remitos/read", method = GET)
    public String remitosTodos(@AuthenticationPrincipal Principal principal) {

        Page<Remitos> remitos = remitosRepository.findAll(new PageRequest(0, 1,
                new Sort(new Sort.Order(Sort.Direction.DESC, "id"))));

        return remitos.getContent().get(0).getNumeroSinPrefijo();
    }

    @ResponseBody
    @RequestMapping(value = "/siguiente_operacion/remitos/transportes", method = GET)
    public List<Transporte> transportes(@AuthenticationPrincipal Principal principal) {

        return transporteRepository.findAll();
    }

    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/siguiente_operacion/remitos/save", method = POST)
    public Long saveRemito(@RequestBody final BrokerRemitos remito, @AuthenticationPrincipal Principal principal) {
        //Campo fechaSolicitud es de tipo string en DataBase. Formato: dd/mm/yyyy
        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        Operaciones operaciones = remito.getOperacion();
        String numeroSinPrefijo;

        Series serie = seriesRepository.findOne((long) 1);
        numeroSinPrefijo = serie.getUltNroImpreso().isEmpty() ? "1" : serie.getUltNroImpreso();

        Long numeroSinPrefijoLong = (Long.parseLong(numeroSinPrefijo) + (long) 1);
        serie.setUltNroImpreso(numeroSinPrefijoLong.toString());
        seriesRepository.save(serie);
        Remitos remitoNuevo = new Remitos();

        remitoNuevo.setCantidadElementos(operaciones.getCantidadPendientes());

        remitoNuevo.setEstado(PENDIENTE);
        LocalDate date = LocalDate.now();
        java.sql.Date sqlDate = java.sql.Date.valueOf(date);
        remitoNuevo.setFechaEmision(sqlDate);
        remitoNuevo.setFechaEntrega(operaciones.getFechaEntrega());
        remitoNuevo.setTipoComprobante_id((long) 13);
        remitoNuevo.setLetraComprobante("R");

       // remitoNuevo.setNumeroSinPrefijo("00000000".substring(numeroSinPrefijoLong.toString().length()) + numeroSinPrefijoLong.toString());
        remitoNuevo.setNumeroSinPrefijo(numeroSinPrefijoLong.toString());
        //prefijo String de formato x.xx pasado a double luego a long para eliminar decimales inservibles
        Long prefijoLong = (long) Double.parseDouble(operaciones.getRequerimiento().getPrefijo());
        remitoNuevo.setPrefijo("0000".substring(prefijoLong.toString().length()) + prefijoLong.toString());
        remitoNuevo.setNumero(Long.parseLong(remitoNuevo.getPrefijo() +"000"+ remitoNuevo.getNumeroSinPrefijo()));
        remitoNuevo.setObservacion(remito.getObservaciones());
        remitoNuevo.setTipoRemito("cliente");
        remitoNuevo.setClienteEmp_id(operaciones.getClienteEmp_id());
        remitoNuevo.setClienteAsp_id(operaciones.getClienteAsp_id());

        if (operaciones.getRequerimiento().getTipoRequerimiento().getTipoMovimiento().equals(Const.REQ_TIPO_MOV_INGRESO)) {
            remitoNuevo.setDepositoDestino_id(operaciones.getDeposito_id());
            remitoNuevo.setIngresoEgreso(Const.REQ_TIPO_MOV_INGRESO);
        } else if (operaciones.getRequerimiento().getTipoRequerimiento().getTipoMovimiento().equals(Const.REQ_TIPO_MOV_EGRESO)) {
            remitoNuevo.setDepositoOrigen_id(operaciones.getDeposito_id());
            remitoNuevo.setIngresoEgreso(Const.REQ_TIPO_MOV_EGRESO);
        }

        remitoNuevo.setDireccion_id(operaciones.getRequerimiento().getDireccionDefecto_id());
        //remitoNuevo.setEmpleado_id(operaciones.getUsuario_id());
        remitoNuevo.setSucursal_id(operaciones.getRequerimiento().getSucursal_id());
        remitoNuevo.setUsuario_id(operaciones.getUsuario_id());
        int maxLength = (operaciones.getRequerimiento().getEmpSolicitante().getPersonasFisicas().getNombreCompleto().length() < 49)
                            ? operaciones.getRequerimiento().getEmpSolicitante().getPersonasFisicas().getNombreCompleto().length()
                            : 49;

        remitoNuevo.setEmpleadoSolicitante(operaciones.getRequerimiento().getEmpSolicitante().getPersonasFisicas().getNombreCompleto().substring(0, maxLength));
        remitoNuevo.setFechaSolicitud(formatoFecha.format(operaciones.getFechaAlta())); // "dd/mm/yyyy"
        Long numReq = Long.parseLong(operaciones.getRequerimiento().getNumero());
        remitoNuevo.setNumRequerimiento("4: " + "0001" + " - " + ("00000000".substring(numReq.toString().length()) + numReq.toString()));

        String aviso = "";

        remitoNuevo = remitosRepository.save(remitoNuevo);

        for (RelacionOpEl el : operaciones.getRelacionOpEl()) {
            if (el.getElemento_id() != null && el.getEstado().equals(PENDIENTE)) {
                RemitosDetalle remitosDetalle = new RemitosDetalle();
                remitosDetalle.setElemento_id(Long.parseLong(el.getElemento_id()));
                remitosDetalle.setRemito_id(remitoNuevo.getId());
                remitosDetalleRepository.save(remitosDetalle);

                if (remitoNuevo.getIngresoEgreso().equals(Const.REQ_TIPO_MOV_EGRESO)){
                    Users user = userRepository.findByUsername(principal.getName());
                    elementosBussiness.crearElementoHistorico(el.getCodigoElemento().getCodigo(), operaciones.getClienteAsp_id(), operaciones.getClienteEmp_id(), user.getId(), Const.ACCION_MS017ELE);
                }
            }
            if (elementosBussiness.esContenedor(Long.valueOf(el.getElemento_id()))) aviso="aviso" ;

        }
         if (aviso.equals("aviso") && operaciones.getRequerimiento().getTipoRequerimiento().getId()==4) remitoNuevo.setAviso("ESTA CAJA CONTIENE CARGA INDIVIDUAL DE LEGAJOS AL SALIR EN CONSULTA, EL CLIENTE SE HACE RESPONSABLE DE LA MODIFICACION TOTAL O PARCIAL DEL CONTENIDO DE LA MISMA.");

        remitosRepository.save(remitoNuevo);
        Requerimiento req = operaciones.getRequerimiento();
        req.setRemito_id(remitoNuevo.getId());
        req.setCantidad(operaciones.getCantidadPendientes());
        requerimientoRepository.save(req);

        operaciones.setTipoOperacion_id(operaciones.getTipoOperaciones().getTipoOperacionSiguiente_id() == null ? operaciones.getTipoOperacion_id() : operaciones.getTipoOperaciones().getTipoOperacionSiguiente_id().toString());
        operacionesRepository.save(operaciones);

        return remitoNuevo.getId();
    }

    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/siguiente_operacion/operacionesSiguiente", method = POST)
    public void operacionSiguiente(@RequestBody BrokerOperaciones brokerOperaciones, @AuthenticationPrincipal Principal principal) {

        List<Long> remitos = brokerOperaciones.getRemitos();
        List<Operaciones> operaciones = brokerOperaciones.getOperaciones();

        hojaRutaRepository.updateHdR(brokerOperaciones.getHdr(), "Controlada");

        for (Operaciones operacion : operaciones) {
            if (remitos.contains(operacion.getRequerimiento().getRemito().getNumero()))
                operacion.setTipoOperacion_id(operacion.getTipoOperaciones().getTipoOperacionSiguiente_id() == null ? operacion.getTipoOperacion_id() : operacion.getTipoOperaciones().getTipoOperacionSiguiente_id().toString());
            else {
                Integer opAnterior = Integer.parseInt(operacion.getTipoOperacion_id()) - 1;
                if (operacion.getRequerimiento().getTipoRequerimiento_id().equals("11")) opAnterior = 32;
                operacion.setTipoOperacion_id(opAnterior.toString());
            }
            operacionesRepository.save(operacion);

        }

    }

    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/siguiente_operacion/digitalizar/save", method = POST)
    public void updateConusltaDigital(@RequestBody BrokerConsultaDigital consultaDigital, @AuthenticationPrincipal Principal principal) {
        Operaciones op = consultaDigital.getOperacion();
        Users user = userRepository.findByUsername(principal.getName());
        //setear id elementos.Listo
        relacionReqConRepository.save(ConceptosHelper.setConcepto(op.getRequerimiento_id(), user.getId(), consultaDigital.getElementos(), 4));
        //setear id hojas.Listo
        relacionReqConRepository.save(ConceptosHelper.setConcepto(op.getRequerimiento_id(), user.getId(), consultaDigital.getHojas(), 8));
        //setear id horas.Listo
        relacionReqConRepository.save(ConceptosHelper.setConcepto(op.getRequerimiento_id(), user.getId(), consultaDigital.getHoras(), 3));
        op.setTipoOperacion_id(op.getTipoOperaciones().getTipoOperacionSiguiente_id() == null ? op.getTipoOperacion_id() : op.getTipoOperaciones().getTipoOperacionSiguiente_id().toString());
        if (op.getTipoOperaciones().getTipoOperacionSiguiente_id() == 19 || op.getTipoOperaciones().getTipoOperacionSiguiente_id() == 48) {
            op.setUsuarioFinalizo_id(userRepository.findByUsername(principal.getName()).getId());
            op.setOrigen_finalizado("Finalizacion automatica ASF en /siguiente_operacion/digitalizar/save. Tipo Operacion anterior: " + op.getTipoOperaciones().getTipoOperacionSiguiente_id());
            op.setTipoOperacion_id("48");
            op.setEstado(FINALIZADO_OK);
            Requerimiento requerimiento = op.getRequerimiento();
            requerimiento.setEstado(FINALIZADO_ASP);
            requerimientoRepository.save(requerimiento);
        }
        operacionesRepository.save(op);
    }

    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/siguiente_operacion/modificarSiempre/save", method = POST)
    public void updateModificarReq(@RequestBody BrokerModificarReq consultaModificarReq, @AuthenticationPrincipal Principal principal) {
        Operaciones op = consultaModificarReq.getOperacion();
        Users user = userRepository.findByUsername(principal.getName());
        //Traigo Requerimiento
        Requerimiento requerimiento = op.getRequerimiento();
        //Seteo si viene algun valor
        if( consultaModificarReq.getCantidadImagenes()!=null)if( consultaModificarReq.getCantidadImagenes()!=0)requerimiento.setCantidadImagenes(consultaModificarReq.getCantidadImagenes());
        if(consultaModificarReq.getCantidadImagenesPlanos()!=null)if( consultaModificarReq.getCantidadImagenesPlanos()!=0)requerimiento.setCantidadImagenesPlanos(consultaModificarReq.getCantidadImagenesPlanos());
        if(consultaModificarReq.getFletes()!=null)if(consultaModificarReq.getFletes()!=0)requerimiento.setFletes(consultaModificarReq.getFletes());
        if(consultaModificarReq.getHorasArchivistas()!=null)if(consultaModificarReq.getHorasArchivistas()!=0)requerimiento.setHorasArchivistas(consultaModificarReq.getHorasArchivistas());
        requerimientoRepository.save(requerimiento);

    }

    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/siguiente_operacion/cantidad/save", method = POST)
    public void updatePrecintos(@RequestBody BrokerCantidad broker, @AuthenticationPrincipal Principal principal) throws Exception {
        Operaciones operacion = broker.getOperacion();
        Requerimiento requerimiento = operacion.getRequerimiento();
        Integer pendientes = operacion.getCantidadPendientes();
        Integer cantidad = broker.getCantidad();
        Users user = userRepository.findByUsername(principal.getName());
            relacionReqConRepository.save(ConceptosHelper.setConcepto(operacion.getRequerimiento_id(), user.getId(), cantidad, 9));

        operacion.setCantidadPendientes(pendientes);
        requerimiento.setCantidad(broker.getCantidad());
        requerimientoRepository.save(requerimiento);
        operacion.setTipoOperacion_id(operacion.getTipoOperaciones().getTipoOperacionSiguiente_id() == null ? operacion.getTipoOperacion_id() : operacion.getTipoOperaciones().getTipoOperacionSiguiente_id().toString());
        operacionesRepository.save(operacion);
    }

    @PreAuthorize("hasAuthority('2')")
    @Size(min=1)
    @ResponseBody
    @RequestMapping(value = "/siguiente_operacion/caja/siguiente", method = POST)
    public void cosultasSiguiente(@RequestBody BrokerElementos brokerElementos, @AuthenticationPrincipal Principal principal) {
        if (brokerElementos.isSinControl()) {
            saltarControl(brokerElementos.getOperacion(), principal);
        }
        Operaciones operacion           = brokerElementos.getOperacion();
        List<String> elementos          = brokerElementos.getElementosLeidos();
        Set<RelacionOpEl> setElementos  = operacion.getRelacionOpEl();

        // CF correspondiente a consulta normal
        Integer[] idListaConceptos  = {4};
        Users currentUser           = userRepository.findByUsername(principal.getName());

        // CF correspondiente a caja vacía
        if (operacion.getRequerimiento().getTipoRequerimiento_id().equals("1")) idListaConceptos = new Integer[]{12, 5};

        List<String> tipoLegajo = asList("2", "3");
        if (tipoLegajo.contains(operacion.getRequerimiento().getTipoRequerimiento_id())) idListaConceptos = new Integer[]{14};

        Operaciones clonOperacion           = new Operaciones();
        List<RelacionOpEl> elementosUpdate  = new ArrayList<>();
        Integer pendientesOriginal          = operacion.getCantidadPendientes();
        Integer omitidosOriginal            = operacion.getCantidadOmitidos();
        Integer pendientesClon              = operacion.getCantidadPendientes();
        Integer omitidosClon                = operacion.getCantidadOmitidos();
        Long numRequerimiento               = 0L;

        List<String> eRequerimiento     = new ArrayList<>();
        List<RelacionOpEl> rOpElList    = new ArrayList<>();
        List<String> eNuevo             = new ArrayList<>();
        List<String> eLeido             = new ArrayList<>();
        List<String> cPendiente         = new ArrayList<>();

        /* Obtener los elementos del requerimiento */
        rOpElList.addAll(operacion.getRelacionOpEl());
        for (RelacionOpEl rel : rOpElList) {
            eRequerimiento.add(rel.getCodigoElemento().getCodigo());
        }

        /* Obtener elementos leidos */
        eLeido = brokerElementos.getElementosLeidos();

        /* Obtener los elementos nuevos */
        eNuevo.addAll(eLeido);
        eNuevo.removeAll(eRequerimiento);

        /* Cantidad Pendiente de elementos del requerimiento Original */
        cPendiente.addAll(eRequerimiento);
        cPendiente.removeAll(eLeido);

        /* Si el la cantidad de elementos si marcar es mayor a acero, el requerimiento se divide. */
        if (cPendiente.size() > 0 && !brokerElementos.isSinControl()) {
            BeanUtils.copyProperties(operacion, clonOperacion);

            clonOperacion.setId(null);
            Requerimiento clonRequerimiento = new Requerimiento();
            BeanUtils.copyProperties(operacion.getRequerimiento(), clonRequerimiento);

            clonRequerimiento.setId(null);
            Series serie = seriesRepository.findOne((long) 2);

            numRequerimiento = Long.parseLong(serie.getUltNroImpreso()) + 1;
            clonRequerimiento.setNumero(numRequerimiento.toString());

            clonRequerimiento = requerimientoRepository.save(clonRequerimiento);
            clonOperacion.setRequerimiento_id(clonRequerimiento.getId());
            clonOperacion = operacionesRepository.save(clonOperacion);

            serie.setUltNroImpreso(numRequerimiento.toString());
            seriesRepository.save(serie);
        }

        for (Iterator<RelacionOpEl> it = setElementos.iterator(); it.hasNext(); ) {
            RelacionOpEl ele = it.next();

            if (ele.getEstado().equals(OMITIDO)) continue; // ele = it.next();

            if (cPendiente.size() > 0 && !brokerElementos.isSinControl()) {
                RelacionOpEl clonElemento = new RelacionOpEl();
                BeanUtils.copyProperties(ele, clonElemento);
                clonElemento.setId(null);
                clonElemento.setOperacion_id(clonOperacion.getId().toString());

                if (!elementos.contains(ele.getCodigoElemento().getCodigo())) {
                    ele.setEstado(OMITIDO);
                    omitidosOriginal++;
                    pendientesOriginal--;
                } else {
                    clonElemento.setEstado(OMITIDO);
                    omitidosClon++;
                    pendientesClon--;
                }
                elementosUpdate.add(ele);
                elementosUpdate.add(clonElemento);
            }
            if (!operacion.getRequerimiento().getTipoRequerimiento_id().equals("10")) {
                if (ele.getEstado() != OMITIDO){
                    ElementosLite elemento = new ElementosLite();
                    elemento.setId(Long.parseLong(ele.getElemento_id()));
                    elemento.setEstado(EN_TRANSITO);
                    elementosLiteRepository.save(elemento);

                    elementosBussiness.crearElementoHistorico(ele.getCodigoElemento().getCodigo(),operacion.getClienteAsp_id(),operacion.getClienteEmp_id(),currentUser.getId(),Const.ACCION_MS015ELE);

                    List<Elementos> elementosContenidos = elementosRepository.findContenidos(Long.parseLong(ele.getElemento_id()));
                    if (!elementosContenidos.isEmpty()) {
                        elementosContenidos.forEach(c -> {
                            c.setEstado("En Planta S/C");
                            elementosBussiness.crearElementoHistorico(c.getCodigo(),c.getClienteAsp_id(),c.getClienteEmp_id(),currentUser.getId(),Const.ACCION_MS015ELE);
                        });
                        elementosRepository.save(elementosContenidos);
                    }
                }
            }
        }

        /* Si el la cantidad de elementos sin marcar es mayor a cero, guarda el requerimiento actual con los elementos leidos. */
        if (cPendiente.size() > 0 && !brokerElementos.isSinControl()) {
            relacionOpElRepository.save(elementosUpdate);
            operacion.setCantidadPendientes(pendientesOriginal);
            operacion.setCantidadOmitidos(omitidosOriginal);
            clonOperacion.setCantidadPendientes(pendientesClon);
            clonOperacion.setCantidadOmitidos(omitidosClon);
            operacionesRepository.save(clonOperacion);
            operacion.setObservaciones("Los elementos restantes de este requerimiento salen en requerimiento número: " + numRequerimiento.toString());
        }
        relacionReqConRepository.save(ConceptosHelper.setConcepto(operacion.getRequerimiento_id(), currentUser.getId(), pendientesOriginal, idListaConceptos));
        operacion.setTipoOperacion_id(operacion.getTipoOperaciones().getTipoOperacionSiguiente_id() == null ? operacion.getTipoOperacion_id() : operacion.getTipoOperaciones().getTipoOperacionSiguiente_id().toString());
        operacionesRepository.save(operacion);

        List<String> tipos = asList("2", "3");
        if (tipos.contains(operacion.getRequerimiento().getTipoRequerimiento_id())) {
            List<String> elementos_error        = new ArrayList<>();
            List<String> elementosNuevos        = new ArrayList<>();
            List<String> elementosRequerimiento = new ArrayList<>();
            List<RelacionOpEl> relacionOpElList = new ArrayList<>();

            elementosNuevos.addAll(brokerElementos.getElementosLeidos());

            relacionOpElList.addAll(operacion.getRelacionOpEl());
            for (RelacionOpEl rel : relacionOpElList) {
                elementosRequerimiento.add(rel.getCodigoElemento().getCodigo());
            }

            elementosNuevos.removeAll(elementosRequerimiento);

            Integer cantidadPendientes = brokerElementos.getElementosLeidos().size();

            for (String eleNuevo : elementosNuevos) {
                String elemento_id = elementosRepository.findByCodigo(eleNuevo, operacion.getClienteAsp_id());
                if (elemento_id != null) {
                    RelacionOpEl relacionOpEl = new RelacionOpEl();
                    relacionOpEl.setEstado(PENDIENTE);
                    relacionOpEl.setElemento_id(elemento_id);
                    relacionOpEl.setOperacion_id(operacion.getId().toString());
                    relacionOpElRepository.save(relacionOpEl);

                    ElementosLite elementoLite = elementosLiteRepository.findOne(Long.parseLong(elemento_id));
                    elementoLite.setEstado(EN_TRANSITO);
                    elementosLiteRepository.save(elementoLite);

                    elementosBussiness.crearElementoHistorico(eleNuevo,operacion.getClienteAsp_id(),operacion.getClienteEmp_id(),currentUser.getId(),Const.ACCION_MS015ELE);
                } else {
                    elementos_error.add(eleNuevo);
                    cantidadPendientes -= 1;
                }
            }
            if (operacion.getRequerimiento().getCantidad() == null || operacion.getRequerimiento().getCantidad() == 0) {
                operacion.getRequerimiento().setCantidad(cantidadPendientes);
                requerimientoRepository.save(operacion.getRequerimiento());
            }
            operacion.setCantidadPendientes(cantidadPendientes);
            operacionesRepository.save(operacion);
        }
    }

    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/siguiente_operacion/adjuntar_hdr/save", method = POST)
    public String agregarHdr(@RequestBody BrokerHdR brokerHdR, @AuthenticationPrincipal Principal principal) throws Exception {

        List<String> idRelOpEl = hdRxOperacionRepository.findByNumero(brokerHdR.getNumeroHdR());
        List<Long> rel = idRelOpEl.stream().map(Long::parseLong).collect(Collectors.toList());
        List<String> listaLeerHdr = asList("16", "57", "61");
        HojaRuta hdr = hojaRutaRepository.findHdrByNumero(brokerHdR.getNumeroHdR());
        if (hdr == null) throw new Exception("Hoja de Ruta no existe");
        else if (hdr.getEstado().equals("Controlada")) throw new Exception("Hoja de Ruta Controlada");
        else if (hdr.getEstado().equals("Anulada")) throw new Exception("Hoja de Ruta Anulada");
        List<Operaciones> operaciones = operacionesRepository.findByRelOpId(rel);
        for (Operaciones ops : operaciones) {
            if (!listaLeerHdr.contains(ops.getTipoOperacion_id())) throw new Exception("Hoja de Ruta sin Salida ");
        }
        List<String> estados = new ArrayList<>();
        Operaciones operacion = brokerHdR.getOperacion();
        for (RelacionOpEl rela : operacion.getRelacionOpEl()) {
            HdRxOperacion hdRxOperacion = new HdRxOperacion();
            hdRxOperacion.setHojaRuta_id(hojaRutaRepository.findByNumero(brokerHdR.getNumeroHdR()));
            hdRxOperacion.setOperacionElemento_id(rela.getId().toString());
            hdRxOperacionRepository.save(hdRxOperacion);
            estados.add(hojaRutaRepository.getStates(operacion.getRequerimiento().getEmpleadoSolicitante_id()).toString());

        }
        operacion.setTipoOperacion_id(operacion.getTipoOperaciones().getTipoOperacionSiguiente_id() == null ? operacion.getTipoOperacion_id() : operacion.getTipoOperaciones().getTipoOperacionSiguiente_id().toString());
        operacionesRepository.save(operacion);
        return "{\"Hdr\":" + brokerHdR.getNumeroHdR() + ",\"sectores\" : \"" + estados.toString() + "\"}";

    }

    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/siguiente_operacion/finalizar/save", method = POST)
    public void finalizar(@RequestBody Operaciones operacion, @AuthenticationPrincipal Principal principal) {

        operacion.setUsuarioFinalizo_id(userRepository.findByUsername(principal.getName()).getId());
        operacion.setOrigen_finalizado("Finalizacion manual ASF.");
        operacion.setTipoOperacion_id("48");
        operacion.setEstado("Finalizado ERROR");
        Requerimiento requerimiento = operacion.getRequerimiento();
        requerimiento.setEstado("Finalizado ERROR");
        requerimientoRepository.save(requerimiento);

        for (RelacionOpEl rel : operacion.getRelacionOpEl()) {
            if (rel.getEstado() != null && rel.getEstado().equals(PENDIENTE)) rel.setEstado("Procesado");
            relacionOpElRepository.save(rel);
        }
        operacionesRepository.save(operacion);
    }

    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/siguiente_operacion/cancelar/save", method = POST)
    public void cancelar(@RequestBody Operaciones operacion, @AuthenticationPrincipal Principal principal) {

        operacion.setTipoOperacion_id("75");
        operacion.setEstado("Cancelado");
        Requerimiento requerimiento = operacion.getRequerimiento();
        requerimiento.setEstado("Cancelado");
        requerimientoRepository.save(requerimiento);
        for (RelacionOpEl rel : operacion.getRelacionOpEl()) {
            if (rel.getEstado() != null && rel.getEstado().equals(PENDIENTE)) rel.setEstado("Cancelado");
            relacionOpElRepository.save(rel);
        }
        operacionesRepository.save(operacion);
    }

    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/siguiente_operacion/verificar_digitales", method = POST)
    public String verificarDigitales(@RequestBody Operaciones operacion, @AuthenticationPrincipal Principal principal) {

       if (operacion.getTipoOperaciones().getId() == 78) {
           operacion.setUsuarioFinalizo_id(userRepository.findByUsername(principal.getName()).getId());
           operacion.setOrigen_finalizado("Finalización Correo enviado");
           operacion.setEstado(FINALIZADO_OK);
           operacion.setTipoOperacion_id(operacion.getTipoOperaciones().getTipoOperacionSiguiente_id() == null ? operacion.getTipoOperacion_id() : operacion.getTipoOperaciones().getTipoOperacionSiguiente_id().toString());
           Requerimiento requerimiento = operacion.getRequerimiento();
           requerimiento.setEstado(FINALIZADO_OK);
           requerimientoRepository.save(requerimiento);
           operacionesRepository.save(operacion);
           JSONObject obj = new JSONObject();
           return obj.put(MENSAJE, "Requerimiento Finalizado").toString();

       } else{

            JSONObject obj = new JSONObject();
            return obj.put(MENSAJE, "Aguarde, los Elementos se Encuentran en  Proceso de Digitalizacion").toString();
       }

       }

    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/siguiente_operacion/controlar_elementos", method = POST)
    public ResponseEntity controlarElementos(@RequestBody String json, @AuthenticationPrincipal Principal principal) throws Exception {
        try {
            ReqControlarElementoDTO reqControlarElementoDTO = Commons.INSTANCE.getCommonGson().fromJson(json, ReqControlarElementoDTO.class);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(requerimientoBussiness.matchElementsRequerimientos(reqControlarElementoDTO.getElementosLeidos(), reqControlarElementoDTO.getNroHdr())));
        } catch (Exception e) {
            CustomException.getInstance().setCustomException(e, "Siguiente Operacion");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(new ResponseDTO(CustomException.getInstance().getCode(), CustomException.getInstance().getMessage())));
        }
    }


    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/siguiente_operacion/controlar_remitos", method = POST)
    public ResponseEntity controlarRemitos(@RequestBody String json, @AuthenticationPrincipal Principal principal) {
        try {
            ReqControlarRemitosDTO reqControlarElementoDTO = Commons.INSTANCE.getCommonGson().fromJson(json, ReqControlarRemitosDTO.class);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(requerimientoBussiness.matchRemitosRequerimientos(reqControlarElementoDTO.getNroRemitosList(), StringHelper.getInstance().convertScoresToComma(reqControlarElementoDTO.getNroHdr()))));
        } catch (Exception e) {
            CustomException.getInstance().setCustomException(e, "Siguiente Operacion");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(new ResponseDTO(CustomException.getInstance().getCode(), CustomException.getInstance().getMessage())));
        }
    }

    public void saltarControl(@RequestBody Operaciones operacion, Principal principal) {
        Users users = userRepository.findByUsername(principal.getName());
        auditoriasRepository.save(AuditoriasHelper.generarAuditoria(operacion, users.getId()));
    }

}
