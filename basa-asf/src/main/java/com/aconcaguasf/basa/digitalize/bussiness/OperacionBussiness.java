package com.aconcaguasf.basa.digitalize.bussiness;


import com.aconcaguasf.basa.digitalize.model.Operaciones;
import com.aconcaguasf.basa.digitalize.model.TipoOperaciones;
import com.aconcaguasf.basa.digitalize.repository.ElementosRepository;
import com.aconcaguasf.basa.digitalize.repository.OperacionesRepository;
import com.aconcaguasf.basa.digitalize.repository.RequerimientoRepository;
import com.aconcaguasf.basa.digitalize.repository.TipoOperacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("operacionBussiness")
public class OperacionBussiness {

    @Autowired
    ElementosRepository elementosRepository;
    @Autowired
    OperacionesRepository operacionesRepository;
    @Autowired
    RequerimientoRepository requerimientoRepository;
    @Autowired
    TipoOperacionesRepository tipoOperacionesRepository;
    
    public boolean checkElementClient(String codigo, Long idCliEle){
        return (elementosRepository.findByCodigoAndClienteEmp_id(codigo, idCliEle)!=null);
    }

    void nextOperation(Long idReq) {
        Operaciones operaciones = operacionesRepository.findFirstByRequerimiento_id(idReq);
        operaciones.setTipoOperacion_id(operaciones.getTipoOperaciones().getTipoOperacionSiguiente_id().toString());
        operacionesRepository.save(operaciones);
    }

    void backOperationToAsignarHDR(Long pReqId){
        Operaciones operaciones = operacionesRepository.findFirstByRequerimiento_id(pReqId);
        TipoOperaciones tOpCtrElems, tOpAsgHDRs;
        tOpCtrElems = tipoOperacionesRepository.findByPreviousIdCtrlElem(operaciones.getTipoOperaciones().getId());
        tOpAsgHDRs  = (tOpCtrElems != null)
                        ? tipoOperacionesRepository.findByPreviousIdAsgHdR(tOpCtrElems.getId())
                        : tipoOperacionesRepository.findByPreviousIdAsgHdR(operaciones.getTipoOperaciones().getId());
        operaciones.setTipoOperacion_id(tOpAsgHDRs.getId().toString());
        operacionesRepository.save(operaciones);
    }
}
