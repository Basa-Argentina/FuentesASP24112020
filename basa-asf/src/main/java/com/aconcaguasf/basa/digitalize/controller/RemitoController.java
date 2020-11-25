package com.aconcaguasf.basa.digitalize.controller;

import com.aconcaguasf.basa.digitalize.bussiness.RemitoBussiness;
import com.aconcaguasf.basa.digitalize.dto.ctrl.RemitoProcesoDTO;
import com.aconcaguasf.basa.digitalize.dto.ctrl.ResponseDTO;
import com.aconcaguasf.basa.digitalize.exceptions.CustomException;
import com.aconcaguasf.basa.digitalize.repository.LecturasRepository;
import com.aconcaguasf.basa.digitalize.util.Commons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class RemitoController {

    @Autowired
    @Qualifier("remitoBussiness")
    private RemitoBussiness remitoBussiness;

    @ResponseBody
    @RequestMapping(value = "/remito/{codigo}", method = RequestMethod.GET)
    public ResponseEntity getRemito(@AuthenticationPrincipal Principal principal, @PathVariable("codigo") String codigo){
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(remitoBussiness.getRemitoProceso(codigo)));
        }catch (Exception e){
            CustomException.getInstance().setCustomException(e, "Remito proceso");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(new ResponseDTO(CustomException.getInstance().getCode(), CustomException.getInstance().getMessage())));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/remito/save", method = RequestMethod.POST)
    public ResponseEntity setRemito(@AuthenticationPrincipal Principal principal, @RequestBody String remitoProceso){
        try {
            RemitoProcesoDTO remitoProcesoDTO = Commons.INSTANCE.getCommonGson().fromJson(remitoProceso, RemitoProcesoDTO.class);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(remitoBussiness.procesarRemito(principal, remitoProcesoDTO)));
        }catch (Exception e){
            CustomException.getInstance().setCustomException(e, "Remito proceso");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(new ResponseDTO(CustomException.getInstance().getCode(), CustomException.getInstance().getMessage())));
        }
    }

}
