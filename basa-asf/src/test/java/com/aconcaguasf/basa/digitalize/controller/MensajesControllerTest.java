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
 *  07/20/17 11:21:40 Argentina Timezone
 *
 *  Changes :
 *
 *  email column
 */

import com.aconcaguasf.basa.digitalize.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class MensajesControllerTest {

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
     * Testing "/hoja_de_ruta/{numero}" service
     *
     * Connection test:
     * status expected = 200
     * [0].id expected = 153945
     * [0].observaciones" expected = "operaciones"
     * [0].turno expected = "Tarde"
     */
    @Test
    public void ultimasTest() throws Exception {
        mockMvc.perform(get("/mensajes/1/read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(184151)))
                .andExpect(jsonPath("$[0].estado", is("Finalizado ERROR")))
                .andExpect(jsonPath("$[1].requerimiento_id", is(142283)));
    }

    /**
     * Testing "/hoja_de_ruta/{numero}" service
     *
     * Connection test:
     * status expected = 40x
     * hdRnumer type expected Double
     * sent integer
     */
    @Test
    public void ultimasErrorTest() throws Exception {
        mockMvc.perform(get("/hoja_de_ruta//"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void ultimaHojaTest() throws Exception {
        mockMvc.perform(post("/hoja_de_ruta/0/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"operaciones\":[]}"))
                        .andExpect(status().isOk());

    }

    @Test
    public void ultimaHojaFailTest() throws Exception {
        mockMvc.perform(post("/hoja_de_ruta/187.00/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{[]}"))
                .andExpect(status().is4xxClientError());

    }


    @Test
    public void generateMapTest() throws Exception {
        mockMvc.perform(post("/hoja_de_ruta/generate_map")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[]"))
                .andExpect(status().isOk());
    }

    @Test
    public void generateMapTestFailTest() throws Exception {
        mockMvc.perform(post("/hoja_de_ruta/generate_map")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}


