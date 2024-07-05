package com.codebydaud.training.lecture_01.hello_world;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class HelloWorldControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHelloWorldController() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hello-world"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello World!"));
    }

    @Test
    public void testHelloWorldControllerWithNameParam() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hello-world").param("name", "Daud"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello Daud!"));
    }

    /* @Test
    public void testHelloWorldControllerJSON() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hello-world"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Hello World!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt", Matchers.notNullValue()));

    } */

    /* @Test
    public void testHelloWorldSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hello-world"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Hello World!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.at", Matchers.notNullValue()));
    } */
}
