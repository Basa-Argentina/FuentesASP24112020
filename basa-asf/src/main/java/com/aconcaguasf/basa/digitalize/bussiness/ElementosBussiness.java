package com.aconcaguasf.basa.digitalize.bussiness;

import com.aconcaguasf.basa.digitalize.config.Const;
import com.aconcaguasf.basa.digitalize.dto.ctrl.ResponseDTO;
import com.aconcaguasf.basa.digitalize.model.Accion;
import com.aconcaguasf.basa.digitalize.model.ElementoHistorico;
import com.aconcaguasf.basa.digitalize.model.Elementos;
import com.aconcaguasf.basa.digitalize.repository.AccionRepository;
import com.aconcaguasf.basa.digitalize.repository.ElementoHistoricoRepository;
import com.aconcaguasf.basa.digitalize.repository.ElementosRepository;
import com.aconcaguasf.basa.digitalize.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("elementosBussiness")
public class ElementosBussiness {
    @Autowired
    private ElementosRepository elementosRepository;

    @Autowired
    private ElementoHistoricoRepository elementoHistoricoRepository;

    @Autowired
    private AccionRepository accionRepository;

    public void crearElementoHistorico(String pCodigoElemento, Long pClienteAspId, Long pClienteEmpId, Long pUserId, String pAccion){
        Elementos elemento = elementosRepository.findFirstByCodigoAndClienteAsp_idAndClienteEmp_id(pCodigoElemento, pClienteAspId, pClienteEmpId);

        if (elemento != null){
            Accion accion = accionRepository.findFirstByCodigo(pAccion);
            ElementoHistorico elementoHistorico = new ElementoHistorico();
            elementoHistorico.setFechaHora(new Date());
            elementoHistorico.setAccion_id(accion.getId());
            elementoHistorico.setAccion(accion.getCodigo());
            elementoHistorico.setClienteAsp_id(pClienteAspId);
            elementoHistorico.setCodigoElemento(elemento.getCodigo());
            elementoHistorico.setCodigoTipoElemento(elemento.getTipoElemento().getCodigo());
            elementoHistorico.setNombreTipoElemento(elemento.getTipoElemento().getDescripcion());
            elementoHistorico.setCodigoCliente(elemento.getClienteEmp().getCodigo().toString());
            elementoHistorico.setNombreCliente(elemento.getClienteEmp().getPersonasJuridicas().getRazonSocial());
            elementoHistorico.setUsuario_id(pUserId);
            elementoHistoricoRepository.save(elementoHistorico);
        }
    }

    public boolean esContenedor(Long codigoElemento){
        return elementosRepository.findContenidos(codigoElemento).size() > 0;
    }

    public ResponseDTO sonContenedor(String codigosElemento, Long idClienteAsp){
        List<String> elementos = StringHelper.getInstance().stringToListString(codigosElemento);
        List<String> contenedores = new ArrayList<>();
        elementos.forEach(elemento -> {
            if (elementosRepository.findContenidos1(elementosRepository.findIdByCodigoAndClienteAsp(elemento, idClienteAsp)).size() > 0)
                contenedores.add(elemento);
        });

        return contenedores.size() > 0 ? new ResponseDTO("contenedor", contenedores) : new ResponseDTO("elemento", contenedores);
    }

}
