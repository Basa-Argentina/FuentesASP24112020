package com.aconcaguasf.basa.digitalize.bussiness;

import com.aconcaguasf.basa.digitalize.config.Const;
import com.aconcaguasf.basa.digitalize.dto.ctrl.RemitoProcesoDTO;
import com.aconcaguasf.basa.digitalize.dto.ctrl.ResponseDTO;
import com.aconcaguasf.basa.digitalize.model.*;
import com.aconcaguasf.basa.digitalize.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.awt.Color;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.security.Principal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service("remitoBussiness")
public class RemitoBussiness {

    @Autowired
    private LecturasRepository lecturasRepository;
    @Autowired
    private LecturasDetallesRepository lecturasDetallesRepository;
    @Autowired
    private RemitosRepository remitosRepository;
    @Autowired
    private RemitosDetalleRepository remitosDetalleRepository;
    @Autowired
    private RequerimientoRepository requerimientoRepository;
    @Autowired
    private ElementosRepository elementosRepository;
    @Autowired
    private OperacionesRepository operacionesRepository;
    @Autowired
    private MovimientosRepository movimientosRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RelacionOpElRepository relacionOpElRepository;
    @Autowired
    private MovimientoRemitoRepository movimientoRemitoRepository;

    @Autowired
    private MovimientosBussiness movimientosBussiness;
    @Autowired
    private ElementosBussiness elementosBussiness;

           public ResponseDTO getRemitoProceso(String codigoRemito){
            String numeroRemito = this.decodificarCodigo(codigoRemito);
            RemitoProcesoDTO remitoProcesoDTO;

            List<Elementos> elementosDetalleRemito = new ArrayList<>(),
                    elementosLecturaCamion = new ArrayList<>();
            List<String> codElementosSobrantesCamion = new ArrayList<>(),
                    codElementosFaltantesCamion = new ArrayList<>(),
                    codElemFaltantesPlanta      = new ArrayList<>();
            List<Lecturas> lecturasPlanta;
            Lecturas lecturaCamion;
            Requerimiento requerimiento;
            Operaciones operacion = operacionesRepository.findByNumRemito(numeroRemito);
            Long ultimoCodigoCarga;
            AtomicReference<Long> codCargaDC = new AtomicReference<>(); //Devol. Cliente
            AtomicReference<Long> codCargaGC = new AtomicReference<>(); //Guard. Custodia
            List<LecturasDetalles> lecturaPendPlantaGlobal = new ArrayList<>();

        if (operacion == null)
            return new ResponseDTO(Const.ERR, "No se ha encontrado información correspondiente al remito ingresado");
        if (operacion.getRequerimiento() == null)
            return new ResponseDTO(Const.ERR, "No existe un requerimiento asociado al remito ingresado");
        if (operacion.getRequerimiento().getRemito() == null)
            return new ResponseDTO(Const.ERR, "El Remito ingresado no existe");
        if (operacion.getRequerimiento().getRemito().getIngresoEgreso().equalsIgnoreCase(Const.REQ_TIPO_MOV_EGRESO))
            return new ResponseDTO(Const.ERR, "El remito ingresado corresponde a uno de salida");
       // if (!operacion.getTipoOperaciones().getId().toString().equals("17"))
        //    return new ResponseDTO(Const.ERR, "No se puede Procesar este Remito Ya que su Requerimiento se Encuentra en Estado :\""+ operacion.getTipoOperaciones().getDescripcion()+" \"");

        remitoProcesoDTO = new RemitoProcesoDTO();
        remitoProcesoDTO.setCantidadElementos((long) operacion.getRequerimiento().getRemito().getCantidadElementos());
        remitoProcesoDTO.setNumeroRemito(Long.parseLong(numeroRemito));
//        remitoProcesoDTO.setFechaEntregaRemito(operacion.getRequerimiento().getRemito().getFechaEntrega() != null
//                ? operacion.getRequerimiento().getRemito().getFechaEntrega().getTime()
//                : '0'
//        );
        remitoProcesoDTO.setTurno(this.retornarTurno(operacion.getRequerimiento().getRemito().getFechaEntrega()));
        remitoProcesoDTO.setNumeroRequerimiento(Long.parseLong(operacion.getRequerimiento().getNumero()));
        remitoProcesoDTO.setTipoRequerimiento(operacion.getRequerimiento().getTipoRequerimiento().getId());
        remitoProcesoDTO.setDescTipoRequerimiento(operacion.getRequerimiento().getTipoRequerimiento().getDescripcion());
        remitoProcesoDTO.setDescCliente(operacion.getClienteEmp().getPersonasJuridicas().getRazonSocial());
        remitoProcesoDTO.setProcesado(Const.NN);

        List<Movimientos> movimientos = movimientosRepository.findByRemito_id(remitosRepository.findByNumeroSinPrefijo(numeroRemito).getId());
        //---- Remito previamente procesado

        if (movimientos.stream().anyMatch(movimiento -> movimiento.getCodigoCarga() != null)){
            movimientos.forEach(movimiento -> {
                Elementos elemento = elementosRepository.findOne(movimiento.getElemento_id());
                remitoProcesoDTO.addElementoAgrupado(
                        new RemitoProcesoDTO.ElementoRemito(
                            elemento.getId(),
                            elemento.getCodigo(),
                            movimiento.getEstadoElemento().equalsIgnoreCase(Const.ELEM_ESTADO_EN_CONSULTA)
                                    ? Const.ELEM_COD_GRUPO_DEVOLUCION
                                    : Const.ELEM_COD_GRUPO_GUARDA,
                            movimiento.getEstadoElemento().equalsIgnoreCase(Const.ELEM_ESTADO_EN_CLIENTE)
                                    ? Const.ELEM_GRUPO_GUARDA
                                    : Const.ELEM_GRUPO_DEVOLUCION,
                            movimiento.getCodigoCarga()
                ));
            });
            remitoProcesoDTO.setProcesado(Const.SS);
            return new ResponseDTO(Const.OK, remitoProcesoDTO);
        }

        ultimoCodigoCarga = movimientosRepository.findMaxCodigoCarga() != null ? movimientosRepository.findMaxCodigoCarga() : 0L;
        lecturasPlanta    = lecturasRepository.findLectura(Const.LECTURA_PLANTA);
        lecturaCamion     = lecturasRepository.findLecturaByRemito(numeroRemito, Const.LECTURA_CAMION);

        if (lecturasPlanta.size() == 0)
            return new ResponseDTO(Const.ERR, "No se encontraron lecturas de planta");
        if (lecturaCamion == null)
            return new ResponseDTO(Const.ERR, "No se encontraron lecturas de camión");

        lecturasPlanta.forEach(lectura ->
                lecturaPendPlantaGlobal.addAll(
                        lectura.getLecturasDetallesList()
                                .stream()
                                .filter(elemLectura -> elemLectura.getEstado_id().equals(Const.LECTURA_ESTADO_PENDIENTE))
                                .collect(Collectors.toList())
                )
        );

        if (operacion.getRequerimiento().getTipoRequerimiento().getId().equals(Const.REQ_TIPO_RETIRO_CAJA_CANT)){

            lecturaCamion.getLecturasDetallesList().forEach(elemLectura -> {
                remitoProcesoDTO.addIdLecturasCamion(elemLectura.getId());
                Elementos elemento = elementosRepository.findOne(elemLectura.getElemento_id());

                if (elemento.getEstado().equalsIgnoreCase(Const.ELEM_ESTADO_EN_CLIENTE)){
                    codCargaGC.set(this.getCodCargaMov(ultimoCodigoCarga, codCargaGC.get(), codCargaDC.get()));
                    remitoProcesoDTO.addElementoAgrupado(
                            new RemitoProcesoDTO.ElementoRemito(elemLectura.getElemento_id(),
                                    elemLectura.getCodigoBarras(),
                                    Const.ELEM_COD_GRUPO_GUARDA,
                                    Const.ELEM_GRUPO_GUARDA,
                                    codCargaGC.get()));
                }else{
                    codCargaDC.set(this.getCodCargaMov(ultimoCodigoCarga, codCargaDC.get(), codCargaGC.get()));
                    remitoProcesoDTO.addElementoAgrupado(
                            new RemitoProcesoDTO.ElementoRemito(elemLectura.getElemento_id(),
                                    elemLectura.getCodigoBarras(),
                                    Const.ELEM_COD_GRUPO_DEVOLUCION,
                                    Const.ELEM_GRUPO_DEVOLUCION,
                                    codCargaDC.get()));

                }
            });

            codElemFaltantesPlanta = lecturaCamion.getLecturasDetallesList()
                    .stream()
                    .filter(elemCamion -> {
                        if (lecturaPendPlantaGlobal.stream().anyMatch(elemPlanta -> elemPlanta.getElemento_id().equals(elemCamion.getElemento_id()))){
                            remitoProcesoDTO.addIdLecturasPlanta(lecturaPendPlantaGlobal
                                    .stream()
                                    .filter(elemPlanta -> elemPlanta.getElemento_id().equals(elemCamion.getElemento_id()))
                                    .findFirst()
                                    .get()
                                    .getId());
                            return false;
                        }
                        return true;
                    })
                    .map(elemCamion -> elemCamion.getCodigoBarras())
                    .collect(Collectors.toList());

        }else{
            List<RemitosDetalle> remitosDetalles = remitosDetalleRepository.findByRemitos(operacion.getRequerimiento().getRemito().getId()) ;
            remitosDetalles.forEach(detalle ->{
                Elementos elemento = elementosRepository.findOne(detalle.getElemento_id());
                elementosDetalleRemito.add(elemento);
            });

            lecturaCamion.getLecturasDetallesList().forEach(elemLectura -> {
                remitoProcesoDTO.addIdLecturasCamion(elemLectura.getId());
                elementosLecturaCamion.add(elementosRepository.findOne(elemLectura.getElemento_id()));

                Elementos elemento = elementosRepository.findOne(elemLectura.getElemento_id());

                if (elemento.getEstado().equalsIgnoreCase(Const.ELEM_ESTADO_EN_CLIENTE)){
                    codCargaGC.set(this.getCodCargaMov(ultimoCodigoCarga, codCargaGC.get(), codCargaDC.get()));
                    remitoProcesoDTO.addElementoAgrupado(
                            new RemitoProcesoDTO.ElementoRemito(
                                elemLectura.getElemento_id(),
                                elemLectura.getCodigoBarras(),
                                Const.ELEM_COD_GRUPO_GUARDA, Const.ELEM_GRUPO_GUARDA,
                                codCargaGC.get()));
                }else{
                    codCargaDC.set(this.getCodCargaMov(ultimoCodigoCarga, codCargaDC.get(), codCargaGC.get()));
                    remitoProcesoDTO.addElementoAgrupado(
                            new RemitoProcesoDTO.ElementoRemito(
                                elemLectura.getElemento_id(),
                                elemLectura.getCodigoBarras(),
                                Const.ELEM_COD_GRUPO_DEVOLUCION, Const.ELEM_GRUPO_DEVOLUCION,
                                codCargaDC.get()));
                }
            });

            codElemFaltantesPlanta = lecturaCamion.getLecturasDetallesList()
                                        .stream()
                                        .filter(elemCamion -> {
                                            if (lecturaPendPlantaGlobal.stream().anyMatch(elemPlanta -> elemPlanta.getElemento_id().equals(elemCamion.getElemento_id()))){
                                                remitoProcesoDTO.addIdLecturasPlanta(lecturaPendPlantaGlobal
                                                        .stream()
                                                        .filter(elemPlanta -> elemPlanta.getElemento_id().equals(elemCamion.getElemento_id()))
                                                        .findFirst()
                                                        .get()
                                                        .getId());
                                                return false;
                                            }
                                            return true;
                                        })
                                        .map(elemCamion -> elemCamion.getCodigoBarras())
                                        .collect(Collectors.toList());

            codElementosSobrantesCamion = elementosLecturaCamion
                    .stream()
                    .filter(elemCamion -> !elementosDetalleRemito.contains(elemCamion))
                    .map(elemCamion -> elemCamion.getCodigo())
                    .collect(Collectors.toList());

            codElementosFaltantesCamion = elementosDetalleRemito
                    .stream()
                    .filter(elemRemito -> !elementosLecturaCamion.contains(elemRemito))
                    .map(elemRemito -> elemRemito.getCodigo())
                    .collect(Collectors.toList());
        }

        remitoProcesoDTO.setElementosSobrantesCamion(codElementosSobrantesCamion);
        remitoProcesoDTO.setElementosFaltantesCamion(codElementosFaltantesCamion);
        remitoProcesoDTO.setElementosFaltantesPlanta(codElemFaltantesPlanta);

        return new ResponseDTO(Const.OK, remitoProcesoDTO);
    }

    public ResponseDTO procesarRemito(Principal principal, RemitoProcesoDTO remitoProceso) throws Exception {

        Users currentUser = userRepository.findByUsername(principal.getName());
        Long userJose = currentUser.getId();
        if (userJose == 60264 || userJose == 60277 ) {

            Operaciones operaciones = operacionesRepository.findFirstByRequerimiento_id(requerimientoRepository.findIdReqNroReq(remitoProceso.getNumeroRequerimiento()));

            operaciones.setFechaEntrega(this.getFechaEntrega(remitoProceso.getFechaEntregaRemito(), remitoProceso.getTurno()));
            operaciones.getRequerimiento().setFechaEntrega(this.getFechaEntrega(remitoProceso.getFechaEntregaRemito(), remitoProceso.getTurno()));
            operaciones.getRequerimiento().getRemito().setFechaEntrega(this.getFechaEntrega(remitoProceso.getFechaEntregaRemito(), remitoProceso.getTurno()));

            boolean flagModifyElements = false;

          //  if (operaciones.getRequerimiento().getTipoRequerimiento().getId().equals(Const.REQ_TIPO_RETIRO_CAJA_CANT) && remitoProceso.getElementosAgrupados().size() != operaciones.getRequerimiento().getRemito().getCantidadElementos()) {
               if (operaciones.getRequerimiento().getTipoRequerimiento().getId().equals(Const.REQ_TIPO_RETIRO_CAJA_CANT) ) {

                    flagModifyElements = true;
            } else {
                if (remitoProceso.getElementosAgrupados().size() > operaciones.getRequerimiento().getRemito().getCantidadElementos()) {
                    return new ResponseDTO(Const.ERR, "Error. La cantidad de lecturas no puede ser superior a la cantidad de elementos declarados en el remito");
                } else if (remitoProceso.getElementosAgrupados().size() < operaciones.getRequerimiento().getRemito().getCantidadElementos()) {
                    flagModifyElements = true;
                }
            }

            boolean finalBanderaAgregaElemento = flagModifyElements;
            remitoProceso.getElementosAgrupados().forEach(elementoRemitoDTO -> {

                if (finalBanderaAgregaElemento && operaciones.getRelacionOpEl().stream().noneMatch(elemReq -> elemReq.getElemento_id().equals(elementoRemitoDTO.getIdElemento().toString()))) {
                    //----Nuevo elemento en operación
                    RelacionOpEl relacionOpEl = new RelacionOpEl();
                    relacionOpEl.setEstado(Const.PENDIENTE);
                    relacionOpEl.setOperacion_id(operaciones.getId().toString());
                    relacionOpEl.setElemento_id(elementoRemitoDTO.getIdElemento().toString());
                    relacionOpElRepository.save(relacionOpEl);
                    operaciones.addRelacionOpEl(relacionOpEl);

                    //----Nuevo elemento en remito
                    RemitosDetalle remitoDetalle = new RemitosDetalle();
                    remitoDetalle.setElemento_id(elementoRemitoDTO.getIdElemento());
                    remitoDetalle.setRemito_id(operaciones.getRequerimiento().getRemito_id());
                    remitosDetalleRepository.save(remitoDetalle);
                }

                movimientosBussiness.crearMovimiento(operaciones.getRequerimiento().getTipoRequerimiento().getTipoMovimiento().toUpperCase(),
                        operaciones.getClienteAsp_id(),
                        operaciones.getClienteEmp_id(),
                        operaciones.getDeposito_id(),
                        elementoRemitoDTO.getIdElemento(),
                        operaciones.getRequerimiento().getRemito_id(),
                        operaciones.getUsuarioAsignado_id(),
                        operaciones.getRequerimiento().getRemito().getNumRequerimiento(),
                        elementoRemitoDTO.getCodigoCarga()
                );

                elementosBussiness.crearElementoHistorico(elementoRemitoDTO.getCodigoElemento(),
                        operaciones.getClienteAsp_id(),
                        operaciones.getClienteEmp_id(),
                        currentUser.getId(),
                        Const.ACCION_MS018ELE
                );

            });

            operaciones.getRelacionOpEl().forEach(elemOp -> {

                Elementos elemento = elementosRepository.findOne(Long.parseLong(elemOp.getElemento_id()));

                if (remitoProceso.getElementosFaltantesCamion().contains(elemento.getCodigo())) {
                    Long idRemitoDetalle = remitosDetalleRepository.findRemitoDetalleIdByRemito_idAndElemento_id(operaciones.getRequerimiento().getRemito().getId(), elemento.getId());
                    MovimientoRemito movimientoRemito = new MovimientoRemito();
                    movimientoRemito.setIdRemito(operaciones.getRequerimiento().getRemito().getId());
                    movimientoRemito.setFecha(Calendar.getInstance());
                    movimientoRemito.setIdElemento(elemento.getId());
                    movimientoRemito.setMovimiento(Const.MOVIMIENTO_REMOVIDO);

                    operaciones.getRelacionOpEl().remove(elemOp);

                    remitosDetalleRepository.delete(idRemitoDetalle);
                    relacionOpElRepository.delete(elemOp.getId());
                    movimientoRemitoRepository.save(movimientoRemito);
                } else if (elemOp.getEstado().equalsIgnoreCase(Const.PENDIENTE)) {
                    elemOp.setEstado(Const.PROCESADO);
                    elemento.setEstado(Const.ELEM_ESTADO_GUARDA);
                    List<Elementos> elementosContendor = elementosRepository.findContenidos(Long.parseLong(elemOp.getElemento_id()));
                    elementosContendor.forEach(elemContenido -> {
                        elemContenido.setEstado(Const.ELEM_ESTADO_GUARDA);
                        elementosBussiness.crearElementoHistorico(elemContenido.getCodigo(),
                                operaciones.getClienteAsp_id(),
                                operaciones.getClienteEmp_id(),
                                currentUser.getId(),
                                Const.ACCION_MS018ELE
                        );
                    });
                    elementosRepository.save(elemento);
                    elementosRepository.save(elementosContendor);
                    relacionOpElRepository.save(elemOp);
                }
            });

            operaciones.setCantidadProcesados((int) operaciones.getRelacionOpEl().stream().filter(elemento -> elemento.getEstado().equalsIgnoreCase(Const.PROCESADO)).count());
            operaciones.setCantidadPendientes((int) operaciones.getRelacionOpEl().stream().filter(elemento -> elemento.getEstado().equalsIgnoreCase(Const.PENDIENTE)).count());

            if (operaciones.getRequerimiento().getRemito().getCantidadElementos() != operaciones.getCantidadProcesados()) {
                Integer diferencia = operaciones.getCantidadProcesados() - operaciones.getRequerimiento().getRemito().getCantidadElementos();
                MovimientoRemito movimientoRemito = new MovimientoRemito();

                movimientoRemito.setIdRemito(operaciones.getRequerimiento().getRemito().getId());
                movimientoRemito.setFecha(Calendar.getInstance());
                movimientoRemito.setCantidadAnterior((long) operaciones.getRequerimiento().getRemito().getCantidadElementos());
                movimientoRemito.setMovimiento("Cantidad de elementos " + (diferencia < 0 ? "reducida " : "aumentada ") + "en " + Math.abs(diferencia));
                movimientoRemitoRepository.save(movimientoRemito);

                operaciones.getRequerimiento().getRemito().setCantidadElementos(operaciones.getCantidadProcesados());
            }

         //Envio de Correos de Remito
            Remitos remito = remitosRepository.findByNumeroSinPrefijo(remitoProceso.getNumeroRemito().toString());
            String descCliente = operaciones.getClienteEmp().getPersonasJuridicas().getRazonSocial();
            String mail = userRepository.findemailUser(operaciones.getRequerimiento().getEmpleadoSolicitante_id());
            this.envioCorreoRemito(remito,remitoProceso.getNumeroRemito(),descCliente,mail,operaciones);

            operaciones.getRequerimiento().getRemito().setEstado(Const.PROCESADO);

            operaciones.setTipoOperacion_id("48");
            operaciones.setEstado(Const.FINALIZADO_OK);
            operaciones.setOrigen_finalizado("Pantalla Remitos");
            operaciones.getRequerimiento().setEstado(Const.FINALIZADO_OK);

            operacionesRepository.save(operaciones);
            remitosRepository.save(operaciones.getRequerimiento().getRemito());
            requerimientoRepository.save(operaciones.getRequerimiento());

            this.cambiarEstadoLecturas(remitoProceso.getIdLecturasPlanta(), remitoProceso.getIdLecturasCamion(), remitoProceso.getElementosSobrantesCamion(), flagModifyElements);

            return new ResponseDTO(Const.OK, "El remito ha sido procesado");

        } else {

            return new ResponseDTO(Const.ERR, "Error. No tiene permisos Para Realizar esta Accion");

        }

    }

    private void cambiarEstadoLecturas(List<Long> detLecturasPlanta, List<Long> detLecturasCamion, List<String> elementosSobrantes, boolean agregaElemento){
            List<LecturasDetalles> lecturasPlantaDetallesList,
                    lecturasCamionDetallesList;
            Lecturas lecturaPlanta,
                    lecturaCamion;
            lecturasPlantaDetallesList = lecturasDetallesRepository.findByIdIn(detLecturasPlanta);
            lecturasCamionDetallesList = lecturasDetallesRepository.findByIdIn(detLecturasCamion);

        lecturasPlantaDetallesList.stream().forEach(detLecturaPlanta -> {
            if (!elementosSobrantes.contains(detLecturaPlanta.getCodigoBarras()) || agregaElemento)
                detLecturaPlanta.setEstado_id(Const.LECTURA_ESTADO_PROCESADA);
        });
        lecturasCamionDetallesList.stream().forEach(detLecturaCamion -> {
            if (!elementosSobrantes.contains(detLecturaCamion.getCodigoBarras()) || agregaElemento)
                detLecturaCamion.setEstado_id(Const.LECTURA_ESTADO_PROCESADA);
        });

        lecturasDetallesRepository.save(lecturasPlantaDetallesList);
        lecturasDetallesRepository.save(lecturasCamionDetallesList);

        lecturaPlanta = lecturasRepository.findOne(lecturasPlantaDetallesList.stream().findFirst().get().getLectura_id());
        lecturaCamion = lecturasRepository.findOne(lecturasCamionDetallesList.stream().findFirst().get().getLectura_id());

        if (lecturasCamionDetallesList.stream().allMatch(elemLecturaCamion -> elemLecturaCamion.getEstado_id().equalsIgnoreCase(Const.LECTURA_ESTADO_PROCESADA)))
            lecturaCamion.setEstado_id(Const.LECTURA_ESTADO_PROCESADA);
        if (lecturaPlanta.getLecturasDetallesList().stream().allMatch(detLecturaPlanta -> detLecturaPlanta.getEstado_id().equalsIgnoreCase(Const.LECTURA_ESTADO_PROCESADA)))
            lecturaPlanta.setEstado_id(Const.LECTURA_ESTADO_PROCESADA);

        lecturasRepository.save(lecturaPlanta);
        lecturasRepository.save(lecturaCamion);

    }

    private Date getFechaEntrega(Long fechaInMilliSeconds, String turno){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fechaInMilliSeconds);
        calendar.set(Calendar.HOUR_OF_DAY, turno.equalsIgnoreCase("M")
                                                ? 9
                                                : 14);
        return new Date(calendar.getTime().getTime());
    }

 /*   private TipoRequerimiento recuperarUltimoTipoReq(Long idElemento){
        Operaciones o = operacionesRepository.findFirstByRelacionOpElAndRequerimientoTipoRequerimientoTipoMovimiento(idElemento.toString(), Const.REQ_TIPO_MOV_EGRESO);
        return o.getRequerimiento().getTipoRequerimiento();
    }*/

    private String decodificarCodigo(String pCodigo){
        //Quita codigo verificador
        if (pCodigo.length() == 12){
            String codigo = pCodigo.substring(0, pCodigo.length() - 1);
            Long numero = Long.parseLong(codigo) - 100000000;
            return numero.toString();
        }else{
            return pCodigo;
        }
    }

    private Long getCodCargaMov(Long ultimoCodigo, Long codUsar, Long codUsado){
        if (codUsar != null)
            return codUsar;
        if (codUsado != null)
            return codUsado + 1L;
        return  ultimoCodigo + 1L;
    }

    private String retornarTurno(Object o){
        Calendar c = Calendar.getInstance();
        if (o != null){
            if (o instanceof Date)
                c.setTime((Date) o);
            if(o instanceof Calendar)
                c = ((Calendar) o);
        }
        return c.get(Calendar.HOUR_OF_DAY) < 14 ? "M" : "T";
    }

    private void envioCorreoRemito(Remitos remito,Long numRemito, String descCliente,String mail,Operaciones operaciones) throws Exception {

        List<Movimientos> listaMovimientos = new ArrayList<>();
        listaMovimientos = movimientosRepository.findByRemito_id(remito.getId());
        List<String>listSum = new ArrayList();
        List<String>listSum1 = new ArrayList();
        StringBuilder listAsunto = new StringBuilder();
        StringBuilder listAsunto1 = new StringBuilder();
        StringBuilder listAsunto2 = new StringBuilder();
        String nuevalinea = "<br>";
        String tipoELem = "cajas" ;
        String espacio = "&nbsp;&nbsp;";
        System.out.println(mail);

        String destinatario1 = "pedidos@basaargentina.com.ar" ;
        String destinatario = mail ;
        //Test  String destinatario = "sistemas@basaargentina.com.ar" ;
        String asunto = "Ingreso Remito  0001-0000"+remito.getNumeroSinPrefijo() ;
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

            listAsunto.append("Cliente : " + descCliente.toUpperCase() + nuevalinea) ;
            listAsunto.append("Estimado/a  " + remito.getEmpleadoSolicitante().toUpperCase() + nuevalinea);
         if(operaciones.getRequerimiento().getTipoRequerimiento().equals("22")) {
             listAsunto.append("Por intermedio del presente le informamos los movimientos de ingreso de legajos de la fecha " + formateador.format(remito.getFechaEntrega()) + nuevalinea);
         } else {
             listAsunto.append("Por intermedio del presente le informamos los movimientos de ingreso de la fecha " + formateador.format(remito.getFechaEntrega()) + nuevalinea);
         }
             listAsunto.append(nuevalinea);

            listaMovimientos.stream().sorted(Comparator.comparing(Movimientos::getEstadoElemento)).forEach(movimiento -> {
            String elem = elementosRepository.findByIdString(movimiento.getElemento_id().longValue());

            if (movimiento.getEstadoElemento().equals("En el Cliente") || movimiento.getEstadoElemento().equals("En el Cliente S/C")) {
                    listAsunto1.append("- " + elem + espacio + "<b>Guarda y Custodia</b> " + nuevalinea);
                    listSum.add(elem);
                }
            if (movimiento.getEstadoElemento().equals("En Consulta") || movimiento.getEstadoElemento().equals("En Consulta S/C")) {
                listAsunto2.append("- " + elem + espacio + "<b>Devoluci&#243;n</b> " + nuevalinea);
                listSum1.add(elem);
            }

            });


        if (!listSum.isEmpty()) {
            listAsunto.append(listAsunto1 + nuevalinea);
            listAsunto.append("En Total : " + listSum.size() + nuevalinea);
            listAsunto.append(nuevalinea);
        }

        if (!listSum1.isEmpty()) {
            listAsunto.append(listAsunto2 + nuevalinea);
            listAsunto.append("En Total : " + listSum1.size() + nuevalinea);
            listAsunto.append(nuevalinea);
        }

            String fin2 = addColor("Muchas gracias por su colaboraci&#243;n " + nuevalinea + " Saludos Cordiales", Color.black);

            listAsunto.append(nuevalinea);
         //   listAsunto.append(fin + nuevalinea);
            listAsunto.append(fin2 + nuevalinea);

                enviarConGMail(destinatario,asunto,listAsunto,destinatario1);

    }
    public static String addColor(String msg, Color color) {
        String hexColor = String.format("#%06X",  (0xFFFFFF & color.getRGB()));
        String colorMsg = "<FONT COLOR=\"#" + hexColor + "\">" + msg + "</FONT>";
        return colorMsg;
    }

    private static void enviarConGMail(String destinatario, String asunto, StringBuilder cuerpo ,String destinatario1) throws Exception {

        // Esto es lo que va delante de @gmail.com en tu cuenta de correo. Es el
        // remitente tambiÃ©n.
        String remitente = "pedidos@basaargentina.com.ar"; // Para la direcciÃ³n nomcuenta@gmail.com
        String clave = "Basa2014";
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "mail.basaargentina.com.ar"); // El servidor SMTP de Google
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", clave); // La clave de la cuenta
        props.put("mail.smtp.auth", "true"); // Usar autenticaciÃ³n mediante usuario y clave


        props.put("mail.smtp.starttls.enable", "true"); // Para conectar de manera segura al servidor SMTP

        //	props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // Fixed

        props.put("mail.smtp.port", "366"); //
        // El puerto SMTP seguro de Google

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {

            message.setFrom(new InternetAddress(remitente));

            message.addRecipients(Message.RecipientType.TO, destinatario);
            message.addRecipients(Message.RecipientType.TO, destinatario1);
            // varios de la misma manera
            message.setSubject(asunto);
            MimeMultipart multipart = new MimeMultipart("related");

            // first part (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = String.valueOf(cuerpo) +"<img src=\"cid:image\">";
            messageBodyPart.setContent(htmlText, "text/html");
            // add it
            multipart.addBodyPart(messageBodyPart);

            // second part (the image)
            messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource("C:/reportes/firmaCorreo.png");

            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");

            // add image to the multipart
            multipart.addBodyPart(messageBodyPart);

            // put everything together
            message.setContent(multipart ,"text/html");

            Transport transport = session.getTransport("smtp");
            transport.connect("mail.basaargentina.com.ar", remitente, clave);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();


            System.out.println("Se envio el correo");

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();// Si se produce un error
            System.out.println("Error D");
        }
    }

}


