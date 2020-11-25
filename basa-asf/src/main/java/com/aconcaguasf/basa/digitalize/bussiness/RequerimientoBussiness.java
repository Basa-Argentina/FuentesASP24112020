package com.aconcaguasf.basa.digitalize.bussiness;

import com.aconcaguasf.basa.digitalize.config.Const;
import com.aconcaguasf.basa.digitalize.dto.ctrl.RequerimientoHdrDTO;
import com.aconcaguasf.basa.digitalize.dto.ctrl.ResponseDTO;
import com.aconcaguasf.basa.digitalize.model.Operaciones;
import com.aconcaguasf.basa.digitalize.model.Requerimiento;
import com.aconcaguasf.basa.digitalize.model.RequerimientoHojaRuta;
import com.aconcaguasf.basa.digitalize.repository.*;
import com.aconcaguasf.basa.digitalize.util.StringHelper;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.aconcaguasf.basa.digitalize.config.Const.*;

@Service("requerimientoBussiness")
public class RequerimientoBussiness {

    @Autowired
    private ReqHojaRutaRepository reqHojaRutaRepository;

    @Autowired
    private RequerimientoRepository requerimientoRepository;

    @Autowired
    private OperacionesRepository operacionesRepository;

    @Autowired
    private EstadoReqHojaRutaRepository estadoReqHojaRutaRepository;

    @Autowired
    private HojaRutaRepository hojaRutaRepository;

    @Autowired
    private EstadoHojaRutaRepository estadoHojaRutaRepository;

    @Autowired
    @Qualifier("operacionBussiness")
    private OperacionBussiness operacionBussiness;

    @Autowired
    @Qualifier("hojaRutaBussiness")
    private HojaRutaBussiness hojaRutaBussiness;

    public List<ResponseDTO> checkStatusOpReq(String listNumReq) throws Exception {
        List<ResponseDTO> responseDTOList = new ArrayList<>();
        checkStatusReqHdr(listNumReq).forEach(par -> {
            Operaciones operaciones = operacionesRepository.findFirstByRequerimiento_id(requerimientoRepository.findIdReqNroReq(par.getKey()));
            if (operaciones.getEstado().equalsIgnoreCase(PENDIENTE)
                    && operaciones.getTipoOperaciones().getDescripcion().replace(" ", "_").equalsIgnoreCase(ASIGNAR_HDR)) {
                if (par.getValue()) {
                    responseDTOList.add(new ResponseDTO(OK, findReqToHdr(par.getKey())));
                }else{
                    RequerimientoHojaRuta reqHdr = reqHojaRutaRepository.findFirstByIdRequerimientoOrderByFechaCreacionDesc(requerimientoRepository.findIdReqNroReq(par.getKey()));
                    if (reqHdr != null) {
                        responseDTOList.add(new ResponseDTO(ERR_101, par.getKey().toString(), ERR_101_MENSAJE + reqHdr.getEstadoReqHojaRuta().getNombre()));
                    } else {
                        responseDTOList.add(new ResponseDTO(OK, findReqToHdr(par.getKey())));
                    }
                }
            }else{
                responseDTOList.add(new ResponseDTO(ERR_100, par.getKey().toString(), ERR_100_MENSAJE));
            }
        });
        return responseDTOList;
    }

    /**
     * @param listNumReq    Numero requerimiento
     * @return List<Pair<Long, Boolean>> <numeroRequerimiento, estadoEnHojaDeRuta>
     * @throws Exception
     */
    private List<Pair<Long, Boolean>> checkStatusReqHdr(String listNumReq) throws Exception {
        List<Pair<Long, Boolean>> pairList = new ArrayList<>();
        StringHelper.getInstance().stringToListLong(listNumReq).forEach(numReq -> {
            RequerimientoHojaRuta rhdr = reqHojaRutaRepository
                    .findFirstByIdRequerimientoOrderByFechaCreacionDesc(requerimientoRepository.findIdReqNroReq(numReq));
            pairList.add(new Pair<Long, Boolean>(numReq, ((rhdr != null) && rhdr.getEstadoReqHojaRuta().getId() == 2)));

        });
        return pairList;
    }

    public RequerimientoHdrDTO findReqToHdr(Long idReq) {
        RequerimientoHdrDTO requerimientoHdrDTO = new RequerimientoHdrDTO();
        Requerimiento requerimiento = requerimientoRepository.findRequerimientoByNumero(idReq.toString());
        Operaciones operaciones = operacionesRepository.findFirstByRequerimiento_id(requerimiento.getId());
        requerimientoHdrDTO.setIdReq(requerimiento.getId());
        requerimientoHdrDTO.setNumReq(Long.parseLong(requerimiento.getNumero()));
        requerimientoHdrDTO.setIdTipoReq(requerimiento.getTipoRequerimiento().getId());
        requerimientoHdrDTO.setDescTipoReq(requerimiento.getTipoRequerimiento().getDescripcion());
        if (operaciones.getCantidadPendientes() != null) {
            requerimientoHdrDTO.setCantElementos((requerimiento.getCantidad() != null) ? requerimiento.getCantidad().longValue() : operaciones.getCantidadPendientes());
        }
        requerimientoHdrDTO.setDireccionCliente(requerimiento.getClientesDirecciones().getDirecciones().getDireccionCompleta());
        requerimientoHdrDTO.setLocalidadCliente(requerimiento.getClientesDirecciones().getLocalidades().getNombre());
        requerimientoHdrDTO.setProvinciaCliente(requerimiento.getClientesDirecciones().getProvincias().getNombre());
        requerimientoHdrDTO.setCodigoCliente(operaciones.getClienteEmp().getCodigo());
        requerimientoHdrDTO.setRazonSocialCliente(operaciones.getClienteEmp().getPersonasJuridicas().getRazonSocial());
        requerimientoHdrDTO.setTipoMovimiento(requerimiento.getTipoRequerimiento().getTipoMovimiento());
        return requerimientoHdrDTO;
    }

    public ResponseDTO matchElementsRequerimientos(List<String> elementos, String nroHdR) {
        List<ResponseDTO> responseList = new ArrayList<>();
        List<RequerimientoHojaRuta> requerimientoHojaRutas =
                reqHojaRutaRepository.findAllByIdHojaRuta(hojaRutaRepository.findHdrByNumero(nroHdR).getId());
        if (requerimientoHojaRutas != null && !requerimientoHojaRutas.isEmpty()) {
            requerimientoHojaRutas.forEach(rhr -> {
                Requerimiento requerimiento = requerimientoRepository.findRequerimientosById(rhr.getIdRequerimiento());
                if (requerimiento.getTipoRequerimiento().getTipoMovimiento().equalsIgnoreCase(Const.REQ_TIPO_MOV_EGRESO)) {
                    List<String> elementsOpe = operacionesRepository.findFirstByRequerimiento_id(rhr.getIdRequerimiento())
                            .getRelacionOpEl().stream().filter(e->!e.getEstado().equalsIgnoreCase(OMITIDO))
                            .map(s -> s.getCodigoElemento().getCodigo()).collect(Collectors.toList());
                    elementsOpe.removeAll(elementos);
                    if (!elementsOpe.isEmpty()) {
                        responseList.add(new ResponseDTO(requerimientoRepository.findRequerimientosById(rhr.getIdRequerimiento()).getNumero(), elementsOpe));
                    }
                }
            });
            if (responseList.isEmpty()) {
                requerimientoHojaRutas.forEach(rhr -> {
                    operacionBussiness.nextOperation(rhr.getIdRequerimiento());
                });
            }
        } else {
            List<String> numReqList = hojaRutaRepository.findIdRequerimientoByHdr(nroHdR);
            numReqList.forEach((String numReq) -> {
                Requerimiento requerimiento = requerimientoRepository.findRequerimientoByNumero(numReq);
                if (requerimiento.getTipoRequerimiento().getTipoMovimiento().equalsIgnoreCase(Const.REQ_TIPO_MOV_EGRESO)){
                    List<String> elementsOpe = operacionesRepository.findFirstByRequerimiento_id(requerimiento.getId())
                            .getRelacionOpEl().stream().map(s -> s.getCodigoElemento().getCodigo()).collect(Collectors.toList());
                    elementsOpe.removeAll(elementos);
                    if (!elementsOpe.isEmpty()) {
                        responseList.add(new ResponseDTO(requerimientoRepository.findRequerimientoByNumero(numReq).getNumero(), elementsOpe));
                    }
                }
            });
            if (responseList.isEmpty()) {
                numReqList.forEach(numReq -> operacionBussiness.nextOperation(requerimientoRepository.findIdReqNroReq(Long.parseLong(numReq))));
            }
        }
        if (responseList.isEmpty()){
            hojaRutaBussiness.setEstadoHojaRuta(nroHdR, Const.ESTADO_HDR_DESPACHADA);
        }
        return (responseList.isEmpty()) ? new ResponseDTO(OK) : new ResponseDTO(ERR, responseList);
    }

    public ResponseDTO matchRemitosRequerimientos(List<String> remitosList, String nroHdR) {
        List<ResponseDTO> responseList = new ArrayList<>();
        List<RequerimientoHojaRuta> requerimientoHojaRutas = reqHojaRutaRepository.findAllByIdHojaRuta(hojaRutaRepository.findHdrByNumero(nroHdR).getId());

        List<Requerimiento> reqBackOperacion = new ArrayList<>(), reqNextOperacion = new ArrayList<>();

        boolean flagError = false;

        if (requerimientoHojaRutas != null && !requerimientoHojaRutas.isEmpty()) {
            requerimientoHojaRutas.forEach(rhr -> {
                Requerimiento requerimiento = requerimientoRepository.findRequerimientosById(rhr.getIdRequerimiento());
                if (remitosList.contains(requerimiento.getRemito().getNumero().toString())) {
                    reqNextOperacion.add(requerimiento);
                    rhr.setEstadoReqHojaRuta(estadoReqHojaRutaRepository.findById(Const.ESTADO_REQ_HDR_CONTROLADO));
                }else{
                    reqBackOperacion.add(requerimiento);
                    rhr.setEstadoReqHojaRuta(estadoReqHojaRutaRepository.findById(Const.ESTADO_REQ_HDR_ANULADO));
                }
            });

            if (requerimientoHojaRutas.size() == (reqNextOperacion.size() + reqBackOperacion.size())){
                reqNextOperacion.forEach(requerimiento -> operacionBussiness.nextOperation(requerimiento.getId()));
                reqBackOperacion.forEach(requerimiento -> operacionBussiness.backOperationToAsignarHDR(requerimiento.getId()));
            }else{
                flagError = true;
            }
        }else {
            List<String> numReqList = hojaRutaRepository.findIdRequerimientoByHdr(nroHdR);
            numReqList.forEach((String numReq) -> {
                Requerimiento requerimiento = requerimientoRepository.findRequerimientoByNumero(numReq);
                if (remitosList.contains(requerimiento.getRemito().getNumero().toString())) {
                    reqNextOperacion.add(requerimiento);
                }else{
                    reqBackOperacion.add(requerimiento);
                }
            });
            if (numReqList.size() == (reqNextOperacion.size() + reqBackOperacion.size())){
                reqNextOperacion.forEach(requerimiento -> operacionBussiness.nextOperation(requerimiento.getId()));
                reqBackOperacion.forEach(requerimiento -> operacionBussiness.backOperationToAsignarHDR(requerimiento.getId()));
            }else{
                flagError = true;
            }
        }
        if (!flagError){
            hojaRutaBussiness.setEstadoHojaRuta(nroHdR, Const.ESTADO_HDR_FINALIZADA);
        }
        return (!flagError) ? new ResponseDTO(OK) : new ResponseDTO(ERR, responseList);
    }

}
