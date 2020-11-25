package com.aconcaguasf.basa.digitalize.controller;

import com.aconcaguasf.basa.digitalize.bussiness.HojaRutaBussiness;
import com.aconcaguasf.basa.digitalize.bussiness.OperacionBussiness;
import com.aconcaguasf.basa.digitalize.bussiness.RequerimientoBussiness;
import com.aconcaguasf.basa.digitalize.dto.ctrl.ResponseDTO;
import com.aconcaguasf.basa.digitalize.exceptions.CustomException;
import com.aconcaguasf.basa.digitalize.util.Commons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class RequerimientoController {


    @Autowired
    @Qualifier("operacionBussiness")
    OperacionBussiness operacionBussiness;

    @Autowired
    @Qualifier("hojaRutaBussiness")
    HojaRutaBussiness hojaRutaBussiness;

    @Autowired
    @Qualifier("requerimientoBussiness")
    RequerimientoBussiness requerimientoBussiness;


    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/requerimiento/{codigo}/{idClient}/verificar_elemento", method = RequestMethod.GET)
    public ResponseEntity checkElementClient(@AuthenticationPrincipal Principal principal,
                                             @PathVariable("codigo") String codigo,
                                             @PathVariable("idClient") Long idClient){
        try{
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(operacionBussiness.checkElementClient(codigo, idClient));
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(new ResponseDTO(CustomException.getInstance().getCode(), CustomException.getInstance().getMessage())));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/requerimiento/check_req_hdr/{listNumReq}", method = RequestMethod.GET)
    public ResponseEntity CheckReqHdr(@AuthenticationPrincipal Principal principal,
                                      @PathVariable("listNumReq") String listNumReq) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(requerimientoBussiness.checkStatusOpReq(listNumReq)));
        } catch (Exception e) {
            CustomException.getInstance().setCustomException(e, "siguienteOperacion");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(new ResponseDTO(CustomException.getInstance().getCode(), CustomException.getInstance().getMessage())));
        }
    }



    @ResponseBody
    @RequestMapping(value = "/requerimiento/findReq/{idReq}", method = RequestMethod.GET)
    public ResponseEntity findReqFromHdr(@PathVariable("idReq") String idReq) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(requerimientoBussiness.checkStatusOpReq(idReq)));
        } catch (Exception e) {
            CustomException.getInstance().setCustomException(e, "siguienteOperacion");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(new ResponseDTO(CustomException.getInstance().getCode(), CustomException.getInstance().getMessage())));
        }
    }



}
