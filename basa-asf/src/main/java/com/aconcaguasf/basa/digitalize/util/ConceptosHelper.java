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

import com.aconcaguasf.basa.digitalize.model.RelacionReqConF;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConceptosHelper {
    /**
     *
     */
    public static List<RelacionReqConF> setConcepto( Long idRequerimiento, Long idUser,Double cantidad, Integer[] idConcepto) {

        List<RelacionReqConF> relacionReqConFList = new ArrayList<>();
        for(Integer concepto : idConcepto) {
            RelacionReqConF relacionReqConF = new RelacionReqConF();
            relacionReqConF.setCantidad(cantidad);
            relacionReqConF.setEstado(true);
            LocalDate date = LocalDate.now();
            java.sql.Date sqlDate = java.sql.Date.valueOf(date);
            relacionReqConF.setFecha(sqlDate);
            relacionReqConF.setRequerimiento_id(idRequerimiento);
            relacionReqConF.setUsuario_id(idUser);
            relacionReqConF.setConceptoFacturable_id(concepto.toString());
            relacionReqConFList.add(relacionReqConF);
        }

        return relacionReqConFList;

    }
    public static List<RelacionReqConF> setConcepto( Long idRequerimiento, Long idUser,Double cantidad, Integer idConcepto) {
        Integer[] conceptoList = {idConcepto};

        return ConceptosHelper.setConcepto(idRequerimiento,idUser,cantidad,conceptoList);
    }
    public static List<RelacionReqConF> setConcepto( Long idRequerimiento, Long idUser,Integer cantidad, Integer idConcepto) {
        Integer[] conceptoList = {idConcepto};

        return ConceptosHelper.setConcepto(idRequerimiento,idUser,Double.parseDouble(cantidad.toString()),conceptoList);
    }
    public static List<RelacionReqConF> setConcepto( Long idRequerimiento, Long idUser,Integer cantidad, Integer[] idConcepto) {
        return ConceptosHelper.setConcepto(idRequerimiento,idUser,Double.parseDouble(cantidad.toString()),idConcepto);
    }
}
