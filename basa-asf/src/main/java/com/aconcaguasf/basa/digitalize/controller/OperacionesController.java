package com.aconcaguasf.basa.digitalize.controller;

import com.aconcaguasf.basa.digitalize.bussiness.ElementosBussiness;
import com.aconcaguasf.basa.digitalize.bussiness.MovimientosBussiness;
import com.aconcaguasf.basa.digitalize.config.Const;
import com.aconcaguasf.basa.digitalize.dto.OperacionesFilter;
import com.aconcaguasf.basa.digitalize.model.*;
import com.aconcaguasf.basa.digitalize.repository.*;
import com.aconcaguasf.basa.digitalize.util.AuditoriasHelper;
import com.aconcaguasf.basa.digitalize.util.ConceptosHelper;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class OperacionesController {

    @Autowired
    private OperacionesRepository operacionesRepository;
    @Autowired
    private TipoOperacionesRepository tipoOperacionesRepository;
    @Autowired
    private HojaRutaRepository hojaRutaRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UsuarioPlantaRepository usuarioPlantaRepository;
    @Autowired
    private RequerimientoRepository requerimientoRepository;
    @Autowired
    private TipoRequerimientoRepository tipoRequerimientoRepository;
    @Autowired
    private PersonasRepository personasRepository;
    @Autowired
    private RemitosRepository remitosRepository;
    @Autowired
    private MensajesRepository mensajesRepository;
    @Autowired
    private ElementosLiteRepository elementosLiteRepository;
    @Autowired
    private HdRxOperacionRepository hdRxOperacionRepository;
    @Autowired
    private ElementosRepository elementosRepository;
    @Autowired
    private RelacionReqConRepository relacionReqConRepository;
    @Autowired
    private LecturasRepository lecturasRepository;
    @Autowired
    private AuditoriasRepository auditoriasRepository;
    @Autowired
    private EstadoElementoHojaRutaRepository estadoElementoHojaRutaRepository;
    @Autowired
    private RelacionOpElRepository relacionOpElRepository;

    @Autowired
    @Qualifier("movimientosBussiness")
    private MovimientosBussiness movimientosBussiness;
    @Autowired
    @Qualifier("elementosBussiness")
    private ElementosBussiness elementosBussiness;

    private static final String REQUERIMIENTO = "requerimiento";
    private static final String MENSAJE = "mensaje";
    private static final String SALIDA = "salida";
    private static final String ENTRADA = "1";
    private static final String ELEMENTOS = "Elementos ";
    private static final String CAMION = "2";

    /**
     * Gets the whole list of Operaciones for the main view
     * searching by all filters.
     *
     * @param filtro    Json with filter params
     *                  {
     *                  "sucursal": $scope.selectedSucursal,
     *                  "tipoRequerimiento": $scope.selectedTipoRequerimiento,
     *                  "estado": $scope.selectedOperacion,
     *                  "cliente": clienteId,
     *                  "date": $scope.verUltimos
     *                  }
     * @param principal Security
     * @return
     * @throws Exception Tested!
     */

    @ResponseBody
    @RequestMapping(value = "/operaciones/filter", method = RequestMethod.POST)
    public Page<Operaciones> operacionesFilters(@RequestBody final OperacionesFilter filtro, @AuthenticationPrincipal Principal principal) throws Exception {
        Page<Operaciones> operaciones = null;

        if (principal == null) throw new Exception("Sesión expirada.");

        //Replaces null params with wildcard
        if (filtro.getCliente() == null) filtro.setCliente("%%");
        filtro.setEstado((filtro.getEstado() == null || filtro.getEstado().equals("0")) ? ("%%") : ("%" + filtro.getEstado() + "%"));
        filtro.setTipoRequerimiento((filtro.getTipoRequerimiento() == null || filtro.getTipoRequerimiento().equals("0")) ? ("%%") : (filtro.getTipoRequerimiento()));

        //sets filter�s date by substracting {dia} from {today}
        Integer dia = filtro.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -dia);
        java.util.Date date = calendar.getTime();

        // Sort descending and Search the first #### elements
        Pageable pageRequest = new PageRequest(filtro.getPage(), Integer.MAX_VALUE, new Sort(Sort.Direction.DESC, "id"));
       // Pageable pageRequest = new PageRequest(0,500);

        if (filtro.getNum_requerimiento() != null){
            operaciones = operacionesRepository.findOperacionesByNumReq(filtro.getNum_requerimiento(), pageRequest);
        }else if(filtro.getCodigo_elemento() != null) {
            operaciones = operacionesRepository.findOperacionesByCodigoElemento(filtro.getCodigo_elemento(), pageRequest);
        }else if(filtro.getSucursal() == 0){
            // Buscar todos los requerimientos sin distincion de Sucursal
            operaciones = operacionesRepository.findByAllBranchOfficeFilters(filtro.getTipoRequerimiento(), filtro.getEstado(), filtro.getCliente(), date, pageRequest);
        }else{
            // Buscar todos los requerimientos segun Sucursal
            operaciones = operacionesRepository.findByFilters(filtro.getSucursal(), filtro.getTipoRequerimiento(), filtro.getEstado(), filtro.getCliente(), date, pageRequest);

        }

        operaciones.forEach(operacion -> {

            if(operacion.getRequerimiento().getHojaRuta() == null){
                operacion.getRequerimiento().setHojaRuta(hojaRutaRepository.findHdrByNumero(hojaRutaRepository.findNroHdrByIdReq(operacion.getRequerimiento().getNumero())));
            }

            Set<RelacionOpEl> elementos = operacion
                    .getRelacionOpEl()
                    .stream()
                    .filter(elemento -> !elemento.getEstado().equalsIgnoreCase(Const.OMITIDO))
                    .sorted(Comparator.comparing(RelacionOpEl::getElemento_id))
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            operacion.setRelacionOpEl(elementos);
        });

        return operaciones;
    }

    /**
     * Gets current session User�s details
     *
     * @param principal
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/usuarios/current", method = RequestMethod.GET)
    public User getCurrentUser(@AuthenticationPrincipal Principal principal) throws Exception {

        if (principal == null)
        throw new Exception("Sesión expirada.");
        User user = new User();
        Users users = userRepository.findByUsername(principal.getName());
        user.setGrupos(users.getGrupos());
        user.setGruposRoles(users.getGruposRoles());
        user.setUsername(users.getUsername());
        user.setId(users.getId());
        return user;

    }

    @ResponseBody
    @RequestMapping(value = "/usuarios/check_session", method = RequestMethod.GET)
    public void checkSession(@AuthenticationPrincipal Principal principal) throws Exception {

    if (principal == null)
            throw new Exception("Sesión expirada.");
    }

    /**
     * Sets current session User�s grid configuration
     *
     * @param state
     * @param principal
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/operaciones/save_state", method = RequestMethod.POST)
    public void saveState(@RequestBody final String state, @AuthenticationPrincipal Principal principal) {

        Users users = userRepository.findByUsername(principal.getName());
        users.getPersonas().setObservaciones(state);
        userRepository.save(users);
    }

    /**
     * Sets new date for fechaEntrefa
     *
     * @param operacion
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/operaciones/save_fecha", method = RequestMethod.POST)
    public void saveFecha(@RequestBody final Operaciones operacion, @AuthenticationPrincipal Principal principal) throws ParseException {

        Users users = userRepository.findByUsername(principal.getName());
        auditoriasRepository.save(AuditoriasHelper.generarAuditoria(operacion,users.getId()));
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date date = format.parse(operacion.getFechaStringEntrega());
        java.sql.Date sqlDate = new Date(date.getTime());

        operacion.setFechaEntrega(sqlDate);
        operacion.getRequerimiento().setFechaEntrega(sqlDate);

        requerimientoRepository.save(operacion.getRequerimiento());
        operacionesRepository.save(operacion);
    }

    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/operaciones/controlar_remitos", method = RequestMethod.GET)
    public String getControlarRemitos(@AuthenticationPrincipal Principal principal) throws Exception {


        // Lists every operation with id #17
        List<Operaciones> listaOperaciones = operacionesRepository.findControlarRemitos();
        List<String> lecturasB = generarListas(lecturasRepository.findLectura(CAMION));
        List<String> lecturasC = new ArrayList<>();// = generarListas(lecturasRepository.findLectura(ENTRADA));

        // transform List<Long> into List<String> so it can by matched
        // where remitos lectures = A, truck lectures = B and Entry lectures = C

        List<String> lecturaAB = new ArrayList<>();
        List<String> lecturaAC = new ArrayList<>();
        List<String> lecturaCB = new ArrayList<>();
        List<String> lecturaABC = new ArrayList<>();
        String tipo;
        List<String> stackErrores = new ArrayList<>();

        JSONObject obj = new JSONObject();

        for (Iterator<Operaciones> it = listaOperaciones.iterator(); it.hasNext(); ) {
            Operaciones operacion = it.next();

            // gets every element from remitos found by idReq
            Long numRequerimiento = Long.parseLong(operacion.getRequerimiento().getNumero());
            List<Long> lecturasAl = remitosRepository.findElementosByReq("%" + numRequerimiento.toString());

            List<String> lecturasA = Lists.transform(lecturasAl, Functions.toStringFunction());

            if (operacion.getRequerimiento().getTipoRequerimiento().getTipoMovimiento().equals("ingreso")) {
                tipo  = "1" ;

            } else tipo = SALIDA;

            // Match lists of elements, from remitos, truck lectures and Entry Lectures
            lecturaAB.addAll(lecturasA);
            lecturaAC.addAll(lecturasC);
            lecturaCB.addAll(lecturasC);
            lecturaABC.addAll(lecturasC);

            //union between A and B
            lecturaAB.retainAll(lecturasB);
            //union between left in A and C
            lecturaAC.retainAll(lecturasA);
            //union between C and left in B
            lecturaCB.retainAll(lecturasB);
            //union between C and AB
            lecturaABC.retainAll(lecturaAB);
            // left in AB
            lecturaAB.removeAll(lecturaABC);
            // left in AC
            lecturaAC.removeAll(lecturaABC);
            // left in BC
            lecturaCB.removeAll(lecturaABC);

            //left in A
            lecturasA.removeAll(lecturaAB);
            //left in A
            lecturasA.removeAll(lecturaAC);
            //left in A
            lecturasA.removeAll(lecturaABC);

            //left in B
            lecturasB.removeAll(lecturaAB);
            //left in B
            lecturasB.removeAll(lecturaCB);
            //left in B
            lecturasB.removeAll(lecturaABC);

            //left in C
            lecturasC.removeAll(lecturaAC);
            //left in C
            lecturasC.removeAll(lecturaCB);
            //left in C
            lecturasC.removeAll(lecturaABC);
            if (tipo.equals("1")) {
                obj.put(REQUERIMIENTO, operacion.getRequerimiento().getNumero().toString());
                obj.put(MENSAJE ,"Procese este Remito por la pestaña de REMITOS");
                stackErrores.add(obj.toString());
            }
            if (!lecturaAC.isEmpty()) {
                obj.put(REQUERIMIENTO, operacion.getRequerimiento().getNumero());
                obj.put(MENSAJE, ELEMENTOS + elementosRepository.findByElemId(lecturaAC.stream().map(Long::parseLong).collect(Collectors.toList())).toString() + " sin salida");
                stackErrores.add(obj.toString());
            } else if (!lecturaABC.isEmpty())
                if (tipo.equals(ENTRADA)) {
                    obj.put(REQUERIMIENTO, operacion.getRequerimiento().getNumero());
                    obj.put(MENSAJE, ELEMENTOS + elementosRepository.findByElemId(lecturaABC.stream().map(Long::parseLong).collect(Collectors.toList())).toString() + " recibidos");
                    stackErrores.add(obj.toString());
                } else {
                    obj.put(REQUERIMIENTO, operacion.getRequerimiento().getNumero());
                    obj.put(MENSAJE, ELEMENTOS + elementosRepository.findByElemId(lecturaABC.stream().map(Long::parseLong).collect(Collectors.toList())).toString() + " en planta");
                    stackErrores.add(obj.toString());
                }
            else if (!lecturaAB.isEmpty()) {
                if (tipo.equals(SALIDA)) {
                    obj.put(REQUERIMIENTO, operacion.getRequerimiento().getNumero());
                    obj.put(MENSAJE, ELEMENTOS + elementosRepository.findByElemId(lecturaAB.stream().map(Long::parseLong).collect(Collectors.toList())).toString() + " entregados");
                    stackErrores.add(obj.toString());
                    operacion.setTipoOperacion_id(operacion.getTipoOperaciones().getTipoOperacionSiguiente_id() == null ? operacion.getTipoOperacion_id() : operacion.getTipoOperaciones().getTipoOperacionSiguiente_id().toString());

                    if (!lecturasA.isEmpty()) {
                        obj.put(REQUERIMIENTO, operacion.getRequerimiento().getNumero());
                        obj.put(MENSAJE, ELEMENTOS + elementosRepository.findByElemId(lecturasA.stream().map(Long::parseLong).collect(Collectors.toList())).toString() + " no entregados");
                        stackErrores.add(obj.toString());
                    }
                } else {
                    obj.put(REQUERIMIENTO, operacion.getRequerimiento().getNumero());
                    obj.put(MENSAJE, ELEMENTOS + elementosRepository.findByElemId(lecturaAB.stream().map(Long::parseLong).collect(Collectors.toList())).toString() + " sin entrada");
                    stackErrores.add(obj.toString());
                }
            } else if (!lecturasA.isEmpty()) {
                obj.put(REQUERIMIENTO, operacion.getRequerimiento().getNumero());
                obj.put(MENSAJE, ELEMENTOS + elementosRepository.findByElemId(lecturasA.stream().map(Long::parseLong).collect(Collectors.toList())).toString() + " Sin movimientos");
                stackErrores.add(obj.toString());
            }
            if (lecturasA.isEmpty() && lecturaAC.isEmpty() &&
                    (!tipo.equals("1") && tipo.equals(SALIDA) && lecturaABC.isEmpty()) || (!tipo.equals("1") && tipo.equals(ENTRADA) && lecturaAB.isEmpty())) {
                operacion.setTipoOperacion_id(operacion.getTipoOperaciones().getTipoOperacionSiguiente_id() == null ? operacion.getTipoOperacion_id() : operacion.getTipoOperaciones().getTipoOperacionSiguiente_id().toString());
                operacionesRepository.save(operacion);
                Users currentUser = userRepository.findByUsername(principal.getName());
                List<String> listaUrgentes = Arrays.asList("3", "5", "7");

                //setear id de flete urgente o normal. Listo
                Integer conceptoTipo = listaUrgentes.contains(operacion.getRequerimiento().getTipoRequerimiento_id()) ? 7 : 6;
                //1 flete cada 20;
                Integer cantidad = (int) Math.ceil(operacion.getCantidadPendientes() / 20f);
                relacionReqConRepository.save(ConceptosHelper.setConcepto(operacion.getRequerimiento_id(), currentUser.getId(), cantidad, conceptoTipo));

                if (operacion.getRequerimiento().getRemito() != null) {
                    Remitos remito = operacion.getRequerimiento().getRemito();
                    remito.setEstado("Entregado");
                    remitosRepository.save(remito);
                }
                ElementosLite elementos = new ElementosLite();
                lecturaAB.addAll(lecturaABC);
                for (String ele : lecturaAB) {
                    elementos.setId(Long.parseLong(ele));
                    List<Elementos> elementosContenidos = elementosRepository.findContenidos(Long.parseLong(ele));

                    movimientosBussiness.crearMovimiento(
                            operacion.getRequerimiento().getRemito().getIngresoEgreso(),
                            operacion.getClienteAsp_id(),
                            operacion.getClienteEmp_id(),
                            operacion.getRequerimiento().getRemito().getDepositoOrigen_id(),
                            elementos.getId(),
                            operacion.getRequerimiento().getRemito_id(),
                            currentUser.getId(),
                            operacion.getRequerimiento().getRemito().getNumRequerimiento(),
                            null
                    );

                    if (tipo.equals(SALIDA)) {
                        elementosBussiness.crearElementoHistorico(
                                ele,
                                operacion.getClienteAsp_id(),
                                operacion.getClienteEmp_id(),
                                currentUser.getId(),
                                Const.ACCION_MS019ELE
                        );
                        elementos.setEstado("En Consulta");
                        if (!elementosContenidos.isEmpty()) {
                            elementosContenidos.forEach(c -> {
                                movimientosBussiness.crearMovimiento(
                                        operacion.getRequerimiento().getRemito().getIngresoEgreso(),
                                        operacion.getClienteAsp_id(),
                                        operacion.getClienteEmp_id(),
                                        operacion.getRequerimiento().getRemito().getDepositoOrigen_id(),
                                        c.getId(),
                                        operacion.getRequerimiento().getRemito_id(),
                                        currentUser.getId(),
                                        operacion.getRequerimiento().getRemito().getNumRequerimiento(),
                                        null
                                );
                                c.setEstado("En Consulta S/C");
                            });
                        }
                        if (operacion.getRequerimiento().getTipoRequerimiento_id().equals("1")){
                            elementos.setEstado("En el Cliente");
                        }

                    } else if (tipo.equals(ENTRADA)) {
                        elementosBussiness.crearElementoHistorico(
                                ele,
                                operacion.getClienteAsp_id(),
                                operacion.getClienteEmp_id(),
                                currentUser.getId(),
                                Const.ACCION_MS019ELE
                        );
                        elementos.setEstado("En Guarda");
                        if (!elementosContenidos.isEmpty()) {
                            elementosContenidos.forEach(c -> {
                                movimientosBussiness.crearMovimiento(
                                        operacion.getRequerimiento().getRemito().getIngresoEgreso(),
                                        operacion.getClienteAsp_id(),
                                        operacion.getClienteEmp_id(),
                                        operacion.getRequerimiento().getRemito().getDepositoOrigen_id(),
                                        c.getId(),
                                        operacion.getRequerimiento().getRemito_id(),
                                        currentUser.getId(),
                                        operacion.getRequerimiento().getRemito().getNumRequerimiento(),
                                        null
                                );
                                c.setEstado("En Guarda S/C");
                            });
                        }
                    }
                    elementosRepository.save(elementosContenidos);
                    elementosLiteRepository.save(elementos);
                }

                //setear concepto guarda y custodia, alta y ref y trasv. Listo

                Integer[] idListaConceptos = {5, 16, 17};
                if (tipo.equals(ENTRADA) && operacion.getRequerimiento().getTipoRequerimiento_id().equals("1"))
                    relacionReqConRepository.save(ConceptosHelper.setConcepto(operacion.getRequerimiento_id(), currentUser.getId(), lecturaAB.size(), idListaConceptos));

                Set<RelacionOpEl> relacionOpElSet = operacion.getRelacionOpEl();
                for (RelacionOpEl rel : relacionOpElSet) {
                    try {
                        rel.setEstadoElementoHojaRuta(estadoElementoHojaRutaRepository.findEstadoElementoHojaRutaById(2L));
                        relacionOpElRepository.save(rel);
                    } catch (Exception name) {
                        throw new Exception("Sin hoja de ruta asignada");
                    }
                }
            }

            if (it.hasNext()) {
                lecturasB.addAll(lecturaCB);
                lecturasC.addAll(lecturaCB);
                lecturaCB.clear();
            }
        }
        if (!lecturasC.isEmpty()) {
            obj.put(REQUERIMIENTO, "");
            obj.put(MENSAJE, ELEMENTOS + elementosRepository.findByElemId(lecturasC.stream().map(Long::parseLong).collect(Collectors.toList())).toString() + " Sin Lectura Camion");
            stackErrores.add(obj.toString());
        }
        if (!lecturasB.isEmpty()) {
            obj.put(REQUERIMIENTO, "");
            obj.put(MENSAJE, ELEMENTOS + elementosRepository.findByElemId(lecturasB.stream().map(Long::parseLong).collect(Collectors.toList())).toString() + " Sin Lectura Entrada");
            stackErrores.add(obj.toString());
        }
        if (!lecturaCB.isEmpty()) {
            obj.put(REQUERIMIENTO, "");
            obj.put(MENSAJE, ELEMENTOS + elementosRepository.findByElemId(lecturaCB.stream().map(Long::parseLong).collect(Collectors.toList())).toString() + " Sin Requerimiento");
            stackErrores.add(obj.toString());
        }

        return stackErrores.toString();
    }

    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/operaciones/{idReq}/conceptos", method = RequestMethod.GET)
    public List<RelacionReqConF> getConceptos(@AuthenticationPrincipal Principal principal, @PathVariable("idReq") Long idReq) {
        return relacionReqConRepository.findByReqId(idReq);
    }

    private List<String> generarListas(List<Lecturas> listEntrada) {
        List<String> listaSalida = new ArrayList<>();

        for (Lecturas listEntra : listEntrada) {
            if (listEntra.getRemito_id() != null){
                try{
                    if (remitosRepository.findOne(listEntra.getRemito_id()).getIngresoEgreso().equalsIgnoreCase(Const.REQ_TIPO_MOV_EGRESO)){
                        listEntra.setEstado_id("1");
                        for (LecturasDetalles detallesEntr : listEntra.getLecturasDetallesList()) {
                            detallesEntr.setEstado_id("1");
                            listaSalida.add(detallesEntr.getElemento_id().toString());
                        }
                        lecturasRepository.save(listEntra);
                    }
                }catch (Exception e){

                }
            }else{

                //Para registros anteriores al cambio que aún no poseen el id del remito
                listEntra.setEstado_id("1");
                for (LecturasDetalles detallesEntr : listEntra.getLecturasDetallesList()) {

                    detallesEntr.setEstado_id("1");
                    listaSalida.add(detallesEntr.getElemento_id().toString());

                }

                lecturasRepository.save(listEntra);
            }
        }
        return listaSalida;
    }

}