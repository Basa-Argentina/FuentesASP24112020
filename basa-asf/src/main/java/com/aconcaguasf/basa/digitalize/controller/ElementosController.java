package com.aconcaguasf.basa.digitalize.controller;

import com.aconcaguasf.basa.digitalize.bussiness.ElementosBussiness;
import com.aconcaguasf.basa.digitalize.dto.ctrl.ResponseDTO;
import com.aconcaguasf.basa.digitalize.exceptions.CustomException;
import com.aconcaguasf.basa.digitalize.model.Elementos;
import com.aconcaguasf.basa.digitalize.repository.ElementosRepository;
import com.aconcaguasf.basa.digitalize.util.Commons;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Iterator;
import java.util.List;


@Controller
public class ElementosController {

	@Autowired
	private ElementosRepository elementosRepository;

	@Autowired
    @Qualifier("elementosBussiness")
    private ElementosBussiness elementosBussiness;

    /**
     * Elementos Testing Services to show every elemento
     *
     * @param principal
     * @return
     * @throws IOException
     */

    //TODO Rehacer el servicio para evitar la busqueda de todos los elementos

    @ResponseBody
    @RequestMapping(value = "/elementos/read", method = RequestMethod.GET)
    public List<Elementos> renderDocument(@AuthenticationPrincipal Principal principal) {

        Page<Elementos> elementos = elementosRepository.findAll( new PageRequest(0, 100,
                new Sort(new Sort.Order(Sort.Direction.DESC, "posicion_id"))));

        return elementos.getContent();
    }

    /**
     * Shows Elementos details of every Requerimiento
     *
     * @param elementoId Wrapper List of Elementos
     * @param principal
     * @return
     * @throws Exception
     */

        @ResponseBody
        @RequestMapping(value = "/elementos/por_lista", method = RequestMethod.POST)
        public  List<Elementos> listaElementos(@RequestBody final List<Long> elementoId, @AuthenticationPrincipal Principal principal){
            List<Elementos> elementos = null;
            if(!elementoId.isEmpty()) {
                elementos = elementosRepository.findByElementosIds(elementoId);
            }
            return elementos;
    }

    @ResponseBody
    @RequestMapping(value = "/elementos/{idEle}/get", method = RequestMethod.GET)
    public Elementos getElemento(@AuthenticationPrincipal Principal principal, @PathVariable("idEle") Long idEle) {
        return elementosRepository.findOne(idEle);
    }

    /**
     * @ResponseBody
     * @RequestMapping(value = "/elementos/{clienteAsp}/{codigoElemento}/")
    public ResponseEntity verificarElementoContenedor(@AuthenticationPrincipal Principal principal, @PathVariable("clienteAsp") Long clienteAsp, @PathVariable("codigoElemento") String codigoElemento){
        try{
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(new ResponseDTO(elementosBussiness.esContenedor(codigoElemento, clienteAsp) ? "contenedor" : "elemento")));
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Commons.INSTANCE.getCommonGson()
                            .toJson(new ResponseDTO(CustomException.getInstance().getCode(), CustomException.getInstance().getMessage())));
        }
    }
     * @return*/
    // Nuevos Servicios 2020 // Adrian

    @ResponseBody
    @RequestMapping(value = "/elementos/get/{cod}", method = RequestMethod.GET)
    public String elementoCodigo(@AuthenticationPrincipal Principal principal, @PathVariable("cod") String cod) {
         List<Elementos> list = elementosRepository.findByCodService(cod);
        JSONObject obj = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (Iterator<Elementos> it = list.iterator(); it.hasNext(); ) {
            Elementos elemento = it.next();

            if (elemento.getPosicion()==null) {
                obj.put("posicion", "No tiene Posicion");
            }else {
                obj.put("posicion", elemento.getPosicion());
            }
            obj.put("id", elemento.getId());
            obj.put("estado", elemento.getEstado());
            obj.put("codigo",elemento.getCodigo());

            jsonArray.add(obj);

          return jsonArray.toJSONString();

        }
        return jsonArray.toJSONString();

}

    @ResponseBody
    @RequestMapping(value = "/elementos/{clienteAsp}/{codigoElemento}/")
    public ResponseEntity verificarElementosContenedor(@AuthenticationPrincipal Principal principal, @PathVariable("clienteAsp") Long clienteAsp, @PathVariable("codigoElemento") String codigoElemento){
    try{

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Commons.INSTANCE.getCommonGson()
                        .toJson(elementosBussiness.sonContenedor(codigoElemento, clienteAsp)));
    }catch (Exception e){

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Commons.INSTANCE.getCommonGson()
                        .toJson(new ResponseDTO(CustomException.getInstance().getCode(), CustomException.getInstance().getMessage())));
    }
}

}
