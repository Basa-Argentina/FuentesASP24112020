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

import com.aconcaguasf.basa.digitalize.model.Operaciones;

import java.util.ArrayList;
import java.util.List;

public class MapHelper {
    /**
     *
     */


    public static String setHttp(List<Operaciones> operaciones) {
        List<String> http = new ArrayList<>();
        http.add("https://maps.googleapis.com/maps/api/staticmap?size=512x512&maptype=roadmap");
        char[] letter = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int i = 0;
        for(Operaciones operacion : operaciones) {
            String direccionCompleta = operacion.getRequerimiento().getClientesDirecciones().getDirecciones().getDireccionCompleta();
            String localidad = operacion.getRequerimiento().getClientesDirecciones().getLocalidades().getNombre();
            String provincia = operacion.getRequerimiento().getClientesDirecciones().getProvincias().getNombre();
            direccionCompleta = direccionCompleta.replace(" ", "+").trim();
            localidad = localidad.replace(" ", "+").trim();
            provincia = provincia.replace(" ", "+").trim();
            http.add("&markers=size:mid%7Ccolor:red%7Clabel:" + letter[i] + "%7C" + direccionCompleta + "," + localidad + "," + provincia + ",AR");
            if (i<letter.length)i++;
            else i=0;
        }
        http.add("&key=AIzaSyBilymNrfGfT0fVc7tsJ_GBpSLBR9N_7IQ");
        return http.toString().replace(" ","");
    }
}
