package com.aconcaguasf.basa.digitalize.controller;

import com.aconcaguasf.basa.digitalize.bussiness.HojaRutaBussiness;
import com.aconcaguasf.basa.digitalize.bussiness.RequerimientoBussiness;
import com.aconcaguasf.basa.digitalize.config.Const;
import com.aconcaguasf.basa.digitalize.dto.ctrl.NewHdrDTO;
import com.aconcaguasf.basa.digitalize.dto.ctrl.ResponseDTO;
import com.aconcaguasf.basa.digitalize.exceptions.CustomException;
import com.aconcaguasf.basa.digitalize.model.HdRxOperacion;
import com.aconcaguasf.basa.digitalize.model.HojaRuta;
import com.aconcaguasf.basa.digitalize.model.Operaciones;
import com.aconcaguasf.basa.digitalize.model.RelacionOpEl;
import com.aconcaguasf.basa.digitalize.repository.*;
import com.aconcaguasf.basa.digitalize.util.Commons;
import com.aconcaguasf.basa.digitalize.util.MapHelper;
import com.aconcaguasf.basa.digitalize.util.StringHelper;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import org.json.JSONObject;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class HojaRutaController {

    @Autowired
    private HojaRutaRepository hojaRutaRepository;

    @Autowired
    private HdRxOperacionRepository hdRxOperacionRepository;

    @Autowired
    private RelacionOpElRepository relacionOpElRepository;

    @Autowired
    private OperacionesRepository operacionesRepository;

    @Autowired
    private RequerimientoRepository requerimientoRepository;

    @Autowired
    @Qualifier("hojaRutaBussiness")
    private HojaRutaBussiness hojaRutaBussiness;

    @Autowired
    @Qualifier("requerimientoBussiness")
    private RequerimientoBussiness requerimientoBussiness;

    private static final String PENDIENTE = "Pendiente";

    /**
     * @param principal
     * @param numero    #of hoja de ruta
     * @return list of Operaciones
     * @throws IOException Shows every found Operaciones in #numero Hoja de Ruta
     */
    @ResponseBody
    @RequestMapping(value = "/hoja_de_ruta/{numero}/", method = RequestMethod.GET)
    public List<Operaciones> ultimas2(@AuthenticationPrincipal Principal principal, @PathVariable("numero") String numero) {

        List<String> idRelOpEl = hdRxOperacionRepository.findByNumero(numero);
        List<Long> rel = idRelOpEl.stream().map(Long::parseLong).collect(Collectors.toList());
        return operacionesRepository.findByRelOpId(rel);
    }

    /**
     * @param operaciones Selected Operaciones
     * @param principal   Security
     * @param hdr         # of Hoja de Ruta
     * @throws Exception If hdr = 0 it creates a new one
     *                   else the Operations are added to the selected one
     */
    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/hoja_de_ruta/{hdr}/save", method = RequestMethod.POST)
    public String ultimaHoja(@RequestBody List<Operaciones> operaciones, @AuthenticationPrincipal Principal principal, @PathVariable("hdr") String hdr) throws Exception {

        // Gets Hoja de Ruta current number
        double value = Double.parseDouble(hdr);
        if (hdr.equals("0")) {
            Page<HojaRuta> hojaRutas = hojaRutaRepository.findAll(new PageRequest(0, 1,
                    new Sort(new Sort.Order(Sort.Direction.DESC, "numero"))));

            if (!hojaRutas.getContent().isEmpty()) {
                value = Double.parseDouble(hojaRutas.getContent().get(0).getNumero().split("\\.")[0]);
                value++;
            } else value = (double) 1;
        } else {
            List<Long> idList = operaciones.stream().map(Operaciones::getId).collect(Collectors.toList());
            List<String> idListString = Lists.transform(idList, Functions.toStringFunction());
            List<String> operacionesAntiguas = Lists.transform(operacionesRepository.findByHdR(hdr), Functions.toStringFunction());

            for (String opAntigua : operacionesAntiguas) {
                if (!idListString.contains(opAntigua)) {
                    Operaciones opAnt = operacionesRepository.findOne(Long.parseLong(opAntigua));
                    Integer opAnterior = null;
                    if (opAnt.getRequerimiento().getTipoRequerimiento_id().equals("11")) opAnterior = 32;
                    else if (opAnt.getTipoOperacion_id().equals("57") || opAnt.getTipoOperacion_id().equals("61") || opAnt.getTipoOperacion_id().equals("16"))
                        opAnterior = Integer.parseInt(opAnt.getTipoOperacion_id()) - 1;
                    else if (opAnt.getTipoOperacion_id().equals("55")) opAnterior = 15;
                    opAnt.setTipoOperacion_id(opAnterior.toString());
                    operacionesRepository.save(opAnt);
                }
            }
            hojaRutaRepository.updateHdR(value, "Anulada");
            value = value + 0.01;
        }
        HojaRuta hojaRuta = new HojaRuta();
        Date date = new Date();
        java.sql.Date data = new java.sql.Date(date.getTime());
        hojaRuta.setFecha_salida(data);
        hojaRuta.setEstado(PENDIENTE);
        hojaRuta.setNumero(Double.toString(value));
        hojaRuta = hojaRutaRepository.save(hojaRuta);
        List<String> estados = new ArrayList<>();
        List<String> idListaTipos = Arrays.asList("15", "32", "56", "60");

        for (Operaciones operacion : operaciones) {
            estados.add(hojaRutaRepository.getStates(operacion.getRequerimiento().getEmpleadoSolicitante_id()).toString());
            Set<RelacionOpEl> elementos = operacion.getRelacionOpEl();
            // Creates new Row with null element
            if (operacion.getRelacionOpEl().isEmpty()) {
                RelacionOpEl relacionOpEl = new RelacionOpEl();
                relacionOpEl.setEstado(PENDIENTE);
                relacionOpEl.setElemento_id(null);
                relacionOpEl.setOperacion_id(operacion.getId().toString());
                elementos.add(relacionOpEl);

                relacionOpElRepository.save(relacionOpEl);
            }
            // Updates Hoja de Ruta´s tables
            for (RelacionOpEl elemento : elementos) {
                if (elemento.getEstado() == null || elemento.getEstado().equals(PENDIENTE)) {
                    HdRxOperacion hdRxOperacion = new HdRxOperacion();
                    hdRxOperacion.setOperacionElemento_id(elemento.getId().toString());
                    hdRxOperacion.setHojaRuta_id(hojaRuta.getId());
                    hdRxOperacion.setEstado("Creado");
                    hdRxOperacionRepository.save(hdRxOperacion);
                }
            }

            if (idListaTipos.contains(operacion.getTipoOperacion_id())) {
                operacion.setTipoOperacion_id(operacion.getTipoOperaciones().getTipoOperacionSiguiente_id().toString());
                operacionesRepository.save(operacion);
            }

            requerimientoRepository.updateHdR_id(hojaRuta.getId(), operacion.getRequerimiento_id());
        }

        JSONObject obj = new JSONObject();
        obj.put("Hdr", value);
        obj.put("sectores", estados.toString());
        obj.put("http", MapHelper.setHttp(operaciones));

        // Returns hdr´s number
        return obj.toString();
    }


    /**
     * @param operaciones to get directions
     * @param principal   Security
     * @return url from google maps to get the maps
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/hoja_de_ruta/generate_map", method = RequestMethod.POST)
    public String generateMap(@RequestBody List<Operaciones> operaciones, @AuthenticationPrincipal Principal principal) throws Exception {
        return new JSONObject().put("http", MapHelper.setHttp(operaciones)).toString();
    }

    /**
     * @param principal        Security
     * @param pIdRequerimiento Id requerimiento
     * @return boolean
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/hoja_de_ruta/requerimiento/{pIdRequerimiento}", method = RequestMethod.GET)
    public ResponseEntity verificarAsignacionHDR(@AuthenticationPrincipal Principal principal,
                                                 @PathVariable("pIdRequerimiento") Long pIdRequerimiento) {
        try {
            String hojaDeRuta = hojaRutaBussiness.checkRequerimientoHDR(pIdRequerimiento);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(new ResponseDTO((hojaDeRuta != null) ? Const.OK : Const.ERR, hojaDeRuta)));
        } catch (Exception e) {
            CustomException.getInstance().setCustomException(e, "Hoja Ruta");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(new ResponseDTO(CustomException.getInstance().getCode(), CustomException.getInstance().getMessage())));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/hoja_de_ruta/check_estado_hdr/{numHdr}", method = RequestMethod.GET)
    public ResponseEntity verificarEstadoHdr(@AuthenticationPrincipal Principal principal,
                                             @PathVariable("numHdr") String numHdr) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(hojaRutaBussiness.checkStatusHdr(StringHelper.getInstance().convertScoresToComma(numHdr))));
        } catch (Exception e) {
            CustomException.getInstance().setCustomException(e, "Hoja Ruta");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(new ResponseDTO(CustomException.getInstance().getCode(), CustomException.getInstance().getMessage())));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/hoja_de_ruta/findReq/{numHdr}", method = RequestMethod.GET)
    public ResponseEntity buscarReqPorNumHdr(@AuthenticationPrincipal Principal principal,
                                             @PathVariable("numHdr") String numHdr) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(hojaRutaBussiness.findListReqByHdr(StringHelper.getInstance().convertScoresToComma(numHdr))));
        } catch (Exception e) {
            CustomException.getInstance().setCustomException(e, "Hoja Ruta");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(new ResponseDTO(CustomException.getInstance().getCode(), CustomException.getInstance().getMessage())));
        }
    }

    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/hoja_de_ruta/save", method = RequestMethod.POST)
    public ResponseEntity guardarHdr(@RequestBody String json, @AuthenticationPrincipal Principal principal) throws Exception {
        try {
            NewHdrDTO newHdrDTO = Commons.INSTANCE.getCommonGson().fromJson(json, NewHdrDTO.class);
            ResponseDTO responseDTO = (!hojaRutaBussiness.ckeckReqAllIncl(StringHelper.getInstance().convertScoresToComma(newHdrDTO.getNumHdr()), newHdrDTO.getNumReqList()))
                                        ? hojaRutaBussiness.generateHdr(StringHelper.getInstance().convertScoresToComma(newHdrDTO.getNumHdr()), newHdrDTO.getNumReqList())
                                        : new ResponseDTO(Const.OK);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(responseDTO));
        } catch (Exception e) {
            CustomException.getInstance().setCustomException(e, "Hoja Ruta");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(new ResponseDTO(CustomException.getInstance().getCode(), CustomException.getInstance().getMessage())));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/hoja_de_ruta/find_hdr_by_req/{numReq}")
    public ResponseEntity buscarHdrPorNumReq(@AuthenticationPrincipal Principal principal, @PathVariable("numReq") String numReq){
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Commons.INSTANCE.getCommonGson()
                        .toJson(hojaRutaBussiness.findListHdrByReq(numReq)));
        }catch (Exception e){
            CustomException.getInstance().setCustomException(e, "Hoja Ruta");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Commons.INSTANCE.getCommonGson()
                        .toJson(new ResponseDTO(CustomException.getInstance().getCode(), CustomException.getInstance().getMessage())));
        }
    }
}