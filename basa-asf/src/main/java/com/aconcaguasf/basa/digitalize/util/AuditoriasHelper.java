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

import com.aconcaguasf.basa.digitalize.model.Auditoria;
import com.aconcaguasf.basa.digitalize.model.Lecturas;
import com.aconcaguasf.basa.digitalize.model.Movimientos;
import com.aconcaguasf.basa.digitalize.model.Operaciones;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AuditoriasHelper {
    /**
     *
     */
    public static Auditoria generarAuditoria(Operaciones operacion, Long user_id) {
        Auditoria auditoria = new Auditoria();
        auditoria.setRequerimiento_id(operacion.getRequerimiento_id());
        auditoria.setTipoOperacion_id(Long.parseLong(operacion.getTipoOperacion_id()));
        LocalDate date = LocalDate.now();
        java.sql.Date sqlDate = java.sql.Date.valueOf(date);
        auditoria.setFechaCreacion(sqlDate);
        auditoria.setUser_id(user_id);
        //auditoria.setClienteEmp_id(operacion.getClienteEmp_id());
        if(operacion.getFechaEntregaString().equals(operacion.getFechaStringEntrega()))
        auditoria.setDescripcion("Se salto el control");
        else auditoria.setDescripcion("fechaEntrega: "+operacion.getFechaEntregaString() + " a fechaEntrega: "+ operacion.getFechaStringEntrega());
        return auditoria;
    }

}
