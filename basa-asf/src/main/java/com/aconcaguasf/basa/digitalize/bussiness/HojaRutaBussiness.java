package com.aconcaguasf.basa.digitalize.bussiness;

import com.aconcaguasf.basa.digitalize.config.Const;
import com.aconcaguasf.basa.digitalize.dto.ctrl.ElementosDTO;
import com.aconcaguasf.basa.digitalize.dto.ctrl.HojaRutaDTO;
import com.aconcaguasf.basa.digitalize.dto.ctrl.RequerimientoHdrDTO;
import com.aconcaguasf.basa.digitalize.dto.ctrl.ResponseDTO;
import com.aconcaguasf.basa.digitalize.model.*;
import com.aconcaguasf.basa.digitalize.repository.*;
import com.aconcaguasf.basa.digitalize.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.aconcaguasf.basa.digitalize.config.Const.OK;
import static com.aconcaguasf.basa.digitalize.config.Const.OMITIDO;

@Service("hojaRutaBussiness")
public class HojaRutaBussiness {

    @Autowired
    private HojaRutaRepository hojaRutaRepository;
    @Autowired
    private RequerimientoRepository requerimientoRepository;
    @Autowired
    private ReqHojaRutaRepository reqHojaRutaRepository;
    @Autowired
    private OperacionesRepository operacionesRepository;
    @Autowired
    private EstadoReqHojaRutaRepository estadoReqHojaRutaRepository;
    @Autowired
    private EstadoHojaRutaRepository estadoHojaRutaRepository;
    @Autowired
    @Qualifier("operacionBussiness")
    private OperacionBussiness operacionBussiness;

    public String checkRequerimientoHDR(Long idReq) throws Exception {
        RequerimientoHojaRuta requerimientoHojaRuta = reqHojaRutaRepository.findFirstByIdRequerimientoOrderByFechaCreacionDesc(idReq);
        return (requerimientoHojaRuta != null) ? hojaRutaRepository.findFirstById(requerimientoHojaRuta.getIdHojaRuta()).getNumero() : hojaRutaRepository.findHdrByIdRequerimiento(idReq);
    }

    public ResponseDTO checkStatusHdr(String numHdr) throws Exception {
        HojaRuta hdr = hojaRutaRepository.findHdrByNumero(numHdr);
        return (hdr != null
                ? (hdr.getEstadoHojaRuta() != null
                    ? new ResponseDTO(hdr.getEstadoHojaRuta().getDescripcion())
                    : new ResponseDTO(hdr.getEstado()))
                : new ResponseDTO(Const.ERR_103)
                );
    }

    public List<ResponseDTO> findListReqByHdr(String numHdr) {
        List<ResponseDTO> responseDTOList = new ArrayList<>();
        HojaRuta hojaRuta = hojaRutaRepository.findHdrByNumero(numHdr);
        List<RequerimientoHojaRuta> requerimientoHojaRuta = reqHojaRutaRepository.findAllByIdHojaRuta(hojaRuta.getId());
        if (!requerimientoHojaRuta.isEmpty()) {
            requerimientoHojaRuta.forEach(r -> {
                responseDTOList.add(new ResponseDTO(OK, findReqHdr(requerimientoRepository.findRequerimientosById(r.getIdRequerimiento()).getId())));
            });
        } else {
            hojaRutaRepository.findIdRequerimientoByHdr(numHdr).forEach(r -> {
                responseDTOList.add(new ResponseDTO(OK, findReqHdr(requerimientoRepository.findRequerimientoByNumero(r).getId())));
            });
        }
        return responseDTOList;
    }

    public RequerimientoHdrDTO findReqHdr(Long idReq) {
        RequerimientoHdrDTO requerimientoHdrDTO = new RequerimientoHdrDTO();
        Requerimiento requerimiento = requerimientoRepository.findRequerimientosById(idReq);
        Operaciones operacion = operacionesRepository.findFirstByRequerimiento_id(idReq);

        requerimientoHdrDTO.setIdReq(requerimiento.getId());
        requerimientoHdrDTO.setNumReq(Long.parseLong(requerimiento.getNumero()));
        requerimientoHdrDTO.setIdTipoReq(requerimiento.getTipoRequerimiento().getId());
        requerimientoHdrDTO.setDescTipoReq(requerimiento.getTipoRequerimiento().getDescripcion());

        if (operacion.getCantidadPendientes() != null)
            requerimientoHdrDTO.setCantElementos((requerimiento.getCantidad() != null) ? requerimiento.getCantidad().longValue() : operacion.getCantidadPendientes());

        requerimientoHdrDTO.setDireccionCliente(requerimiento.getClientesDirecciones().getDirecciones().getDireccionCompleta());
        requerimientoHdrDTO.setLocalidadCliente(requerimiento.getClientesDirecciones().getLocalidades().getNombre());
        requerimientoHdrDTO.setProvinciaCliente(requerimiento.getClientesDirecciones().getProvincias().getNombre());
        requerimientoHdrDTO.setCodigoCliente(operacion.getClienteEmp().getCodigo());
        requerimientoHdrDTO.setRazonSocialCliente(operacion.getClienteEmp().getPersonasJuridicas().getRazonSocial());
        requerimientoHdrDTO.setTipoOperacion(operacion.getTipoOperaciones().getDescripcion());
        requerimientoHdrDTO.setRemito(requerimiento.getRemito().getNumero().toString());
        requerimientoHdrDTO.setElementos(new ArrayList<>());
        requerimientoHdrDTO.setObservaciones(requerimiento.getObservaciones());
        requerimientoHdrDTO.setSolicitante(requerimiento.getEmpSolicitante().getPersonasFisicas().getNombreCompleto());
        requerimientoHdrDTO.setFechaEntrega(StringHelper.getInstance().dateToString(requerimiento.getFechaEntrega(), "dd/mm/yyyy"));
        requerimientoHdrDTO.setTipoMovimiento(requerimiento.getTipoRequerimiento().getTipoMovimiento());
        requerimientoHdrDTO.setUsuarioAsignado(StringHelper.getInstance().capitalize(operacion.getUsrAsignado() != null ? operacion.getUsrAsignado().getPersonasFisicas().getNombreCompleto() : ""));
        Optional<ClienteEmpleados> clientEmpleado = operacion.getClienteEmp().getClienteEmpleados().stream().filter(clienteEmpleado -> clienteEmpleado.getCelular().length() > 0).findFirst();
        clientEmpleado.ifPresent(clienteEmpleados -> requerimientoHdrDTO.setTelefonoCliente(clienteEmpleados.getCelular()));
        operacion.getRelacionOpEl().forEach(e -> {
            if (e.getId() != null && e.getCodigoElemento() != null && !e.getEstado().equalsIgnoreCase(OMITIDO)) {
                ElementosDTO elementosDTO = new ElementosDTO();
                elementosDTO.setId(e.getCodigoElemento().getId());
                elementosDTO.setCodigo(e.getCodigoElemento().getCodigo());
                requerimientoHdrDTO.getElementos().add(elementosDTO);
            }
        });
        return requerimientoHdrDTO;
    }

    public ResponseDTO generateHdr(String numHdr, List<String> numReqList) {


        String numNewHdr = (numHdr.equalsIgnoreCase("0")) ? checkNextHdr() : checkNextHdr(numHdr);
        createHdr(numNewHdr);

        if (this.verificarReqPendienteEnHdr(numHdr.equalsIgnoreCase("0") ? numNewHdr : numHdr, numReqList)){
            return new ResponseDTO(Const.ERR, "Algunos requerimientos ya se encuentran pendiente o controlado en otra Hoja de Ruta");
        }

        HojaRuta hojaRuta = hojaRutaRepository.findHdrByNumero(numNewHdr);
        List<String> oldNumReqs = new ArrayList<>();

        if (!numHdr.equalsIgnoreCase("0")) {
            HojaRuta oldHdr = hojaRutaRepository.findHdrByNumero(numHdr);
            List<RequerimientoHojaRuta> requerimientoHojaRutaList = reqHojaRutaRepository.findAllByIdHojaRuta(oldHdr.getId());
            requerimientoHojaRutaList.forEach(rhdr -> {
                rhdr.setEstadoReqHojaRuta(estadoReqHojaRutaRepository.findById(Const.ESTADO_REQ_HDR_ANULADO));
                reqHojaRutaRepository.save(rhdr);
                oldNumReqs.add(requerimientoRepository.findRequerimientosById(rhdr.getIdRequerimiento()).getNumero());
            });
            hojaRuta.setEstadoHojaRuta(oldHdr.getEstadoHojaRuta());
            oldHdr.setEstadoHojaRuta(estadoHojaRutaRepository.findById(4L));   //Anulada
            hojaRutaRepository.save(oldHdr);
        }

        numReqList.forEach(numReq -> {
            Requerimiento requerimiento = requerimientoRepository.findRequerimientoByNumero(numReq);
            RequerimientoHojaRuta requerimientoHojaRuta = new RequerimientoHojaRuta();
            requerimientoHojaRuta.setIdRequerimiento(requerimiento.getId());
            requerimientoHojaRuta.setIdHojaRuta(hojaRuta.getId());
            requerimientoHojaRuta.setFechaCreacion(new java.sql.Date(new Date().getTime()));
            requerimientoHojaRuta.setEstadoReqHojaRuta(estadoReqHojaRutaRepository.findById(Const.ESTADO_REQ_HDR_PENDIENTE));
            reqHojaRutaRepository.save(requerimientoHojaRuta);
            //Paso a siguiente tipo operación del requerimiento
            if (!oldNumReqs.contains(numReq)) {
                operacionBussiness.nextOperation(requerimiento.getId());
            }
        });

        oldNumReqs.forEach(oldNumReq -> {
            if (!numReqList.contains(oldNumReq)){
                operacionBussiness.backOperationToAsignarHDR(requerimientoRepository.findRequerimientoByNumero(oldNumReq).getId());
            }
        });

        return new ResponseDTO(Const.OK, hojaRutaRepository.findHdrByNumero(numNewHdr).getNumero());
    }

    private boolean verificarReqPendienteEnHdr(String numHdR, List<String> numerosReq){
        boolean flag = false;
        for (String numReq : numerosReq) {
            if (reqHojaRutaRepository.getRequerimientoHojaRutaPendienteControlado(hojaRutaRepository.findByNumero(numHdR), requerimientoRepository.findIdReqNroReq(Long.parseLong(numReq)), Const.ESTADO_REQ_HDR_ANULADO).size() > 0)
                flag = true;
        }
        return flag;
    }

    public List<HojaRutaDTO> findListHdrByReq(String numReq){
        List<HojaRutaDTO> hojaRutaDTOList = new ArrayList<>();
        //List<RequerimientoHojaRuta> requerimientoHojaRutaList = reqHojaRutaRepository.findByIdRequerimiento(requerimientoRepository.findRequerimientoByNumero(numReq).getId());
        Requerimiento requerimiento = requerimientoRepository.findRequerimientoByNumero(numReq);
        if (requerimiento != null){
            List<RequerimientoHojaRuta> requerimientoHojaRutaList = reqHojaRutaRepository.findByIdRequerimiento(requerimiento.getId());
            requerimientoHojaRutaList.forEach(rhdr -> {
                hojaRutaDTOList.add(new HojaRutaDTO(rhdr.getIdHojaRuta(), hojaRutaRepository.findFirstById(rhdr.getIdHojaRuta()).getNumero()));
            });
        }
        return hojaRutaDTOList;
    }

    public boolean ckeckReqAllIncl(String numHdr, List<String> numReqList) {
        boolean val = true;
        if (!numHdr.equalsIgnoreCase("0")) {

            List<RequerimientoHojaRuta> listRhdr = reqHojaRutaRepository.findAllByIdHojaRuta(hojaRutaRepository.findHdrByNumero(numHdr).getId());

            for (RequerimientoHojaRuta rhdr : listRhdr) {
                if (!numReqList.contains(requerimientoRepository.findRequerimientosById(rhdr.getIdRequerimiento()).getNumero())) {
                    val = false;
                    break;
                }
            }
            val = (numReqList.size() == listRhdr.size()) && val;
        } else {
            val = false;
        }
        return val;
    }

    private void createHdr(String numHdr) {
        HojaRuta hojaRuta = new HojaRuta();
        hojaRuta.setEstadoHojaRuta(estadoHojaRutaRepository.findById(1L)); //Pendiente
        hojaRuta.setNumero(numHdr);
        hojaRuta.setFecha_salida(new java.sql.Date(new Date().getTime()));
        hojaRutaRepository.save(hojaRuta);
    }

    private String checkNextHdr(String... numHdr) {
        return (numHdr.length == 0)
                ? StringHelper.getInstance().truncateDecimal(Double.parseDouble(String.valueOf(hojaRutaRepository.findLastHdr())) + 1, 0).toString()
                : StringHelper.getInstance().truncateDecimal(Double.parseDouble(String.valueOf(numHdr[0])) + 0.01, 2).toString();
    }

    public void setEstadoHojaRuta(String nroHdR, String nuevoEstado){
        int idEstado;

        switch (nuevoEstado){
            case Const.ESTADO_HDR_PENDIENTE:
                idEstado = 1;
                break;
            case Const.ESTADO_HDR_DESPACHADA:
                idEstado = 2;
                break;
            case Const.ESTADO_HDR_FINALIZADA:
                idEstado = 3;
                break;
            default:
                idEstado = 4;
                break;
        }

        HojaRuta hojaRuta = hojaRutaRepository.findHdrByNumero(nroHdR);
        hojaRuta.setEstadoHojaRuta(estadoHojaRutaRepository.findById((long) idEstado));
        hojaRutaRepository.save(hojaRuta);
    }
}
