package com.codebydaud.training.lecture_02.book_api;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class BookApiTests {

    @Autowired
    private MockMvc mockMvc;

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

    @Test
    public void testGetBookListSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    @WithMockUser(username = "author", password = "admin123", roles = {"author"})
    public void testCreateBookByAuthorSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Sample Book\", \"description\": \"Sample Description\" }"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Sample Book"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Sample Description"));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin123", roles = {"admin"})
    public void testCreateBookByAdminSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Sample Book\", \"description\": \"Sample Description\" }"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Sample Book"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Sample Description"));
    }

    @Test
    @WithMockUser(username = "editor", password = "admin123", roles = {"editor"})
    public void testCreateBookUnAuthorizedAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Sample Book\", \"description\": \"Sample Description\" }"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin123", roles = {"admin"})
    public void testUpdateBookByAdminSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Update Title\", \"description\": \"Update Description\" }"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Update Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Update Description"));
    }

    @Test
    @WithMockUser(username = "editor", password = "admin123", roles = {"editor"})
    public void testUpdateBookByEditorSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Update Title\", \"description\": \"Update Description\" }"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Update Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Update Description"));
    }

    @Test
    @WithMockUser(username = "author", password = "admin123", roles = {"author"})
    public void testUpdateBookUnAuthorizedAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Update Title\", \"description\": \"Update Description\" }"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin123", roles = {"admin"})
    public void testUpdateNonExistingBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Update Title\", \"description\": \"Update Description\" }"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin123", roles = {"admin"})
    public void testDeleteBookByAdminSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(username = "editor", password = "admin123", roles = {"editor"})
    public void testDeleteBookUnAuthorizedAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin123", roles = {"admin"})
    public void testDeleteNonExistingBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books/9"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}