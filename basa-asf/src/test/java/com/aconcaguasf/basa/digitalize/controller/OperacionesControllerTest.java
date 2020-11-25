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
 *  07/21/17 15:33:40 Argentina Timezone
 *
 *  Changes :
 *
 *  email column
 */

import com.aconcaguasf.basa.digitalize.Application;
import com.aconcaguasf.basa.digitalize.dto.OperacionesFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.security.Principal;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class OperacionesControllerTest {

    @Autowired
    protected WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    public String filter;



    @Before
    public void setup() {

        // Setup Spring test in standalone mode
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        filter = "{\"sucursal\": 4, " +
                "\"tipoRequerimiento\": 0, " +
                "\"estado\": 0, " +
                "\"cliente\": null, " +
                "\"date\": 10, " +
                "\"page\": 0, " +
                "\"size\": 10}";


        //private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    }

    /**
     * Testing "/operaciones/filter" service
     *
     * Connection test:
     * status expected = 200
     * content send type Json Filter type
     * content received typo Json
     */
    @Test
    public void operacionesFiltersTest() throws Exception  {
        mockMvc.perform(post("/operaciones/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .principal(new Principal() {
                    @Override
                    public String getName() {
                        return "admin";
                    }
                })
                .content(filter)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(10))
        ;
    }

    /**
     * Testing "/usuarios/current" service
     *
     * Connection test:
     * status expected = 200
     * User logged id  darias@basa
     *        id expected {"id": 60320}
     */

    @Test
    public void readArticulosTest() throws Exception {
        mockMvc.perform(get("/usuarios/current")
                .principal(new Principal() {
                    @Override
                    public String getName() {
                        return "darias@basa";
                    }
                }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(60320));

    }

    /**
     * Testing "/usuarios/current" service
     *
     * Connection test:
     * status expected = 200
     * User logged id  darias@basa
     *        id expected {"id": 60320}
     */

    @Test
    public void readArticulosFailTest() throws Exception {
        mockMvc.perform(get("/usuarios/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(60320));

    }

    /**
     * Testing "/operaciones/siguiente" service
     *
     * Connection test:
     * status expected = 200
     * Json "expected #Operacion
     *     {"id": 133469}
     */
    @Test
    public void operacionesSiguienteTest() throws Exception {
        mockMvc.perform(post("/operaciones/siguiente")
                .contentType(MediaType.APPLICATION_JSON)
                .content("133469"))
                .andExpect(status().isOk());

    }

    /**
     * Testing "/operaciones/siguiente" service
     *
     * Fail test:
     * status expected = 415
     * max id expected 153000
     * id sent 1000000
     *
     * Json sent {"id":1000000}
     *
     */

    @Test
    public void fail_test() throws Exception {
        mockMvc.perform(post("/operaciones/siguiente")
                .content("1000000"))
                .andExpect(status().is4xxClientError());
    }



}
