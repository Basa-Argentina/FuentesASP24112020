package com.aconcaguasf.basa.digitalize.controller;
/*
 *
 *  Copyright (c) 2017./ Aconcagua SF.
 *  *
 *  Licensed under the Aconcagua SF License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://aconcaguasf.com/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  @author   : Alejandro Correa
 *  @developer  : Alejandro Correa
 *
 *  Date Changes
 *  07/21/17 11:21:40 Argentina Timezone
 *
 *  Changes :
 *
 *  email column
 */

import com.aconcaguasf.basa.digitalize.Application;
import com.aconcaguasf.basa.digitalize.model.Depositos;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.List;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class FiltersControllerTest {

    @Autowired
    protected WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {

        // Setup Spring test in standalone mode
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    }

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));


    /**
     * Testing "/clientes_asp/read" service
     *
     * Connection test:
     * status expected = 200
     * [0].id expected = 1
     * [2].nombreAbreviado" expected = "bank"
     */
    @Test
    public void readArticulosTest() throws Exception {
        mockMvc.perform(get("/clientes_asp/read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[2].nombreAbreviado", is("bank")));

    }

    /**
     * Testing "/personas_juridicas/read" service
     *
     * Connection test:
     * status expected = 200
     * [11].id expected = 10023
     * [11  ].razonSocial" expected = "ICBC Argentina"
     */
    @Test
    public void personasJuridicasTest() throws Exception {
        mockMvc.perform(get("/personas_juridicas/read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[11].[0].id", is("20083")))
                .andExpect(jsonPath("$[11].[0].razonSocial", is("QUILMES")));

    }

    /**
     * Testing "/hoja_de_ruta/distinct" service
     *
     * Connection test:
     * status expected = 200
     * Expected response : Json type
     *
     */
    @Test
    public void hojaRutasTest() throws Exception {
        mockMvc.perform(get("/hoja_de_ruta/distinct"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON)) ;

    }

    /**
     * Testing "/operaciones/tipos" service
     *
     * Connection test:
     * status expected = 200
     * [11].id expected = 10023
     * [11].razonSocial" expected = "ICBC Argentina"
     */
    @Test
    public void tipoOpsTest() throws Exception {
        mockMvc.perform(get("/operaciones/tipos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[11]", is("Finalizar")));

    }

    /**
     * Testing "/requerimiento/tipo_view" service
     *
     * Connection test:
     * status expected = 200
     * [0].descripcion expected = "PEDIDO DE CAJAS VACIAS"
     * [9].tipoMovimiento" expected = "egreso"
     */
    @Test
    public void tipoRequerimientoTest() throws Exception {
        mockMvc.perform(get("/requerimiento/tipo_view"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descripcion", is("PEDIDO DE CAJAS VACIAS")))
                .andExpect(jsonPath("$[9].tipoMovimiento", is("egreso")));

    }

    /**
     * Testing "/sucursales/depositos" service
     *
     * Connection test:
     * status expected = 200
     * [0].descripcion expected = "Basa mendoza"
     * [0].codigo" expected = "01"
     */

    @Test
    public void despositosTest() throws Exception {
        mockMvc.perform(get("/sucursales/depositos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[2].descripcion", is("Basa mendoza")))
                .andExpect(jsonPath("$[0].codigo", is("01")));

    }

}
