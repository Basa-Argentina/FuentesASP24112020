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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ElementosControllerTest {

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
     * Testing "/elementos/read" service
     *
     * Connection test:
     * status expected = 200
     * [0].codigo type expected = String
     * [3].estado expected = "Creado"
     */

    @Test
    public void readArticulosFailTest() throws Exception {
        mockMvc.perform(get("/elementos/read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value(any(String.class)))
                .andExpect(jsonPath("$[3].estado",is ("En Guarda")));

    }

    /**
     * Testing "/elementos/por_lista" service
     *
     * Connection test:
     * status expected = 200
     * Json "expected List<#Elementos>
     *     [9489569, 9489568,9489567,9489566]
     *
     */

    @Test
    public void test() throws Exception {
        mockMvc.perform(post("/elementos/por_lista")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[9489569, 9489568,9489567,9489566]"))
                .andExpect(status().isOk());

    }

    /**
     * Testing "/elementos/por_lista" service
     *
     * Fail test:
     * status expected = 415
     * Json "expected List<#Elementos>
     *     [9489569, 9489568,9489567,9489566]
     *
     * Json sent not a list {"elemento_id":9489569}
     *
     */

    @Test
    public void fail_test() throws Exception {
        mockMvc.perform(post("/elementos/por_lista")
                .content("{\"elemento_id\" : 9489569}"))
                .andExpect(status().is4xxClientError());
    }

}
