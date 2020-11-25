package com.aconcaguasf.basa.digitalize.bussiness;

import com.aconcaguasf.basa.digitalize.model.Elementos;
import com.aconcaguasf.basa.digitalize.model.Movimientos;
import com.aconcaguasf.basa.digitalize.repository.ElementosRepository;
import com.aconcaguasf.basa.digitalize.repository.MovimientosRepository;
import com.aconcaguasf.basa.digitalize.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;


@Service("movimientosBussiness")
public class MovimientosBussiness {

    @Autowired
    private MovimientosRepository movimientosRepository;

    @Autowired
    private ElementosRepository elementosRepository;

    public void crearMovimiento(String tipoMovimiento,
                                Long clienteAsp_id,
                                Long clienteEmp_id,
                                Long deposito_id,
                                Long idElemento,
                                Long remito_id,
                                Long user_id,
                                String descRemito,
                                Long codCarga)
    {
        Movimientos movimiento = new Movimientos();
        Elementos elemento = elementosRepository.findOne(idElemento);
        //Date today = new Date();
        Calendar today = Calendar.getInstance();

        movimiento.setFecha(today);
        movimiento.setTipoMovimiento(tipoMovimiento.toUpperCase());
        movimiento.setClienteAsp_id(clienteAsp_id);
        movimiento.setClienteEmpOrigenDestino_id(clienteEmp_id);
        movimiento.setDeposito_id(deposito_id);
        movimiento.setElemento_id(elemento.getId());
        movimiento.setEstadoElemento(elemento.getEstado());
        movimiento.setRemito_id(remito_id);
        movimiento.setUsuario_id(user_id);
        movimiento.setResponsable_id(user_id);
        movimiento.setDescripcionRemito(descRemito);
        if (codCarga != null)
            movimiento.setCodigoCarga(codCarga);
        //movimiento.setClaseMovimiento(claseMovimiento);
        movimiento.setDescripcion("Movimiento registrado el " + StringHelper.getInstance().dateToString(today.getTime(), "dd/mm/yyyy"));

        movimientosRepository.save(movimiento);
    }

}
