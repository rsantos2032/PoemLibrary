package io.rsantos.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rsantos.api.entity.Poem;
import io.rsantos.api.service.PoemService;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.Instant;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Transactional
public class PoemControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private PoemService poemService;

  private String id;

  @BeforeEach
  void setup() {
    Poem poem = new Poem(
        null,
        "TheOffice",
        "Michael Scott",
        "This is an office poem",
        null,
        null
    );
    poem = poemService.createPoem(poem);
    id = poem.getId();
  }

  @Test
  void testCreatePoem() throws Exception {
    Poem poem = new Poem(
        null,
        "Test Poem",
        "John Smith",
        "This is a poem",
        null,
        null
    );

    mockMvc.perform(post("/poems")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(poem)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("Test Poem"))
        .andExpect(jsonPath("$.author").value("John Smith"));
  }

  @Test
  void testGetPoemByTitle() throws Exception {
    mockMvc.perform(get("/poems/title/{title}", "TheOffice"))
        .andExpect(status().isOk());
  }

  @Test
  void testGetPoemById() throws Exception {
    mockMvc.perform(get("/poems/id/{id}", id))
        .andExpect(status().isOk());
  }

  @Test
  void testGetPoemById_NotFound() throws Exception {
    mockMvc.perform(get("/poems/id/{id}", "Bad ID"))
        .andExpect(status().isNotFound());
  }

  @Test
  void testGetPoems() throws Exception {
    mockMvc.perform(get("/poems"))
        .andExpect(status().isOk());
  }

  @Test
  void testUpdatePoem() throws Exception {
    Poem poem = new Poem(
        id,
        "Parks and Rec",
        "Ron Swanson",
        "This is a parks and rec poem",
        null,
        null
    );

    mockMvc.perform(put("/poems/id/{id}", id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(poem)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Parks and Rec"))
        .andExpect(jsonPath("$.author").value("Ron Swanson"));
  }

  @Test
  void testDeletePoem() throws Exception {
    mockMvc.perform(delete("/poems/id/{id}", id))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/poems/id/{id}", id))
        .andExpect(status().isNotFound());
  }
}
