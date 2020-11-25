package com.aconcaguasf.basa.digitalize.controller;

import com.aconcaguasf.basa.digitalize.model.Mensajes;
import com.aconcaguasf.basa.digitalize.model.Users;
import com.aconcaguasf.basa.digitalize.repository.MensajesRepository;
import com.aconcaguasf.basa.digitalize.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Controller
public class MensajesController {

    @Autowired
    private MensajesRepository mensajesRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Elementos Testing Services to show every elemento
     *
     * @param principal
     * @return
     * @throws IOException
     */

    @ResponseBody
    @RequestMapping(value = "/mensajes/{idReq}/read", method = RequestMethod.GET)
    public List<Mensajes> getMessages(@AuthenticationPrincipal Principal principal, @PathVariable("idReq") Long idReq) {
        List<Mensajes> mensajes = mensajesRepository.findByRequerimientoId(idReq);
        Users user = userRepository.findByUsername(principal.getName());

        for (Mensajes men : mensajes) {
            if (men.getUsrDestino_id().equals(user.getId()) ) {
                men.setLeido(true);
                LocalDate date = LocalDate.now();
                java.sql.Date sqlDate = java.sql.Date.valueOf(date);
                men.setFechaLeido(sqlDate);
            }
        }
        mensajesRepository.save(mensajes);
        return mensajes;

    }

    @ResponseBody
    @RequestMapping(value = "/mensajes/descripcion_remito/{idReq}", method = RequestMethod.GET)
    public String remitosDescripcion(@AuthenticationPrincipal Principal principal, @PathVariable("idReq") Long idReq) {

        List<Mensajes> mensajes = mensajesRepository.findByRequerimientoId(idReq);
        List<String> mensajesString = new ArrayList<>();
        for (Mensajes men : mensajes){
            if (!men.getEliminado()) mensajesString.add(men.getTexto());
        }
        JSONObject obj = new JSONObject();
        return obj.put("mensajes",mensajesString.toString()).toString();
    }


    @ResponseBody
    @RequestMapping(value = "/mensajes/usuario", method = RequestMethod.GET)
    public List<Long> getUsrMessages(@AuthenticationPrincipal Principal principal){
        Users user = userRepository.findByUsername(principal.getName());
        return mensajesRepository.findByCurrentUsr(user.getId());
    }

    @ResponseBody
    @RequestMapping(value = "/mensajes/delete", method = RequestMethod.POST)
    public  void deleteState(@RequestBody final Mensajes mensajes,@AuthenticationPrincipal Principal principal) {

        mensajes.setEliminado(true);
        mensajesRepository.save(mensajes);
    }

    @ResponseBody
    @RequestMapping(value = "/mensajes/save", method = RequestMethod.POST)
    public  void saveState(@RequestBody final Mensajes mensaje,@AuthenticationPrincipal Principal principal) {

        LocalDate date = LocalDate.now();
        java.sql.Date sqlDate = java.sql.Date.valueOf(date);
        mensaje.setFechaCreacion(sqlDate);
        Users user = userRepository.findByUsername(principal.getName());
        mensaje.setUsrOrigen_id(user.getId());
        mensaje.setLeido(false);
        mensaje.setEliminado(false);
        mensajesRepository.save(mensaje);
    }

}
