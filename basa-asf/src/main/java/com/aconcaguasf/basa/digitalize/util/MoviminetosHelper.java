package com.aconcaguasf.basa.digitalize.util;
/*
 *
 *  Copyright (c) 2017./ Aconcagua SF S.A.
 *  *
 *  Licensed under the Goycoolea inc License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://crossover.com/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  @author 		: Hector Goycoolea
 *  @developer		: Hector Goycoolea
 *
 *  Date Changes
 *  02/09/17 03:53 Argentina Timezone
 *
 *  Notes
 *
 *  The Email helper uses basic java mail protocols and we are not using
 *  bean for configuration so we avoid the @Service("name") on the servlet-context
 *  this is a totally diff structure and this way we can manage a bit more the memmory
 *  of the iterations loop for sending massive mails.
 */

import com.aconcaguasf.basa.digitalize.model.Lecturas;
import com.aconcaguasf.basa.digitalize.model.Movimientos;
import com.aconcaguasf.basa.digitalize.model.Operaciones;

import java.util.*;

public class MoviminetosHelper {
    /**
     *
     *
    public static void generarMovimiento(Operaciones operacion, Lecturas lecturas) {

        Movimientos movimiento = new Movimientos();
        Date date = new Date();
        java.sql.Date data = new java.sql.Date(date.getTime());
        movimiento.setFecha(data);
        movimiento.setTipoMovimiento(operacion.getRequerimiento().getTipoRequerimiento().getTipoMovimiento());
        movimiento.setClienteAsp_id(operacion.getClienteAsp_id());
        movimiento.setDeposito_id(operacion.getDeposito_id());

        if(lecturas != null)
            movimiento.setLectura_id(lecturas.getId());

        movimiento.setRemito_id(operacion.getRequerimiento().getRemito_id());
        movimiento.setUsuario_id(operacion.getUsuario_id());
        movimiento.setDescripcionRemito(operacion.getRequerimiento().getRemito().getObservacion());
        movimiento.setDescripcion("Movimiento registrado el " + data.toString());
        movimiento.setResponsable_id(operacion.getRequerimiento().getEmpleadoAutorizante_id());

        //movimiento.setClienteEmpOrigenDestino_id(operacion.getClienteEmp_id());
        //depositoOrigenDestin
        //elemento_id
        //posicionOrigenDestino
        //estado
        //clasemovimiento
        //estadoElemento
        //codigocarga
    }

    public static void generarMovimiento(Operaciones operacion) {
        MoviminetosHelper.generarMovimiento(operacion, null);
    }
     */
}
