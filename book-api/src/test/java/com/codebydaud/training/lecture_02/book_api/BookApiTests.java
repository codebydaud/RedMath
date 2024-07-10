package com.codebydaud.training.lecture_02.book_api;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Order(1)
    @Test
    public void testGetBookByIdSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("The 48 Laws of Power")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Best Seller")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author", Matchers.is("Robert Greene")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.publishedAt", Matchers.notNullValue()));
    }


    @Test
    public void testGetBookByIdNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/3"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Order(2)
    @Test
    public void testGetBookListSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    public void testCreateBookByAuthorSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{ \"title\": \"Sample Book\", \"description\": \"Sample Description\" }")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("test")
                                .authorities(new SimpleGrantedAuthority("author"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookId", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Sample Book")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Sample Description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author", Matchers.is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.publishedAt", Matchers.notNullValue()));
    }

    @Test
    public void testCreateBookByAdminSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{ \"title\": \"Sample Book\", \"description\": \"Sample Description\" }")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("test")
                                .authorities(new SimpleGrantedAuthority("admin"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookId", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Sample Book")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Sample Description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author", Matchers.is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.publishedAt", Matchers.notNullValue()));
    }

    @Test
    public void testCreateBookUnAuthorizedAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{ \"title\": \"Sample Book\", \"description\": \"Sample Description\" }")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("test")
                                .authorities(new SimpleGrantedAuthority("editor"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Order(3)
    @Test
    public void testUpdateBookByAdminSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Update Title\", \"description\": \"Update Description\" }")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("test")
                                .authorities(new SimpleGrantedAuthority("admin"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookId", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Update Title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Update Description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.publishedAt", Matchers.notNullValue()));
    }

    @Order(4)
    @Test
    public void testUpdateBookByEditorSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Update Title\", \"description\": \"Update Description\" }")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("test")
                                .authorities(new SimpleGrantedAuthority("editor"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookId", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Update Title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Update Description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.publishedAt", Matchers.notNullValue()));
    }

    @Test
    public void testUpdateBookUnAuthorizedAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Update Title\", \"description\": \"Update Description\" }")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("test")
                                .authorities(new SimpleGrantedAuthority("author"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testUpdateNonExistingBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Update Title\", \"description\": \"Update Description\" }")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("test")
                                .authorities(new SimpleGrantedAuthority("admin"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDeleteBookByAdminSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("test")
                                .authorities(new SimpleGrantedAuthority("admin"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDeleteBookUnAuthorizedAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("test")
                                .authorities(new SimpleGrantedAuthority("author"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testDeleteNonExistingBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books/5")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("test")
                                .authorities(new SimpleGrantedAuthority("admin"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}