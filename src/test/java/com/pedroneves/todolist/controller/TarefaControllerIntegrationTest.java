package com.pedroneves.todolist.controller;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedroneves.todolist.dto.TarefaDTO;
import com.pedroneves.todolist.enums.StatusTarefa;
import com.pedroneves.todolist.model.Tarefa;
import com.pedroneves.todolist.repository.TarefaRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class TarefaControllerIntegrationTest {
  
  @Autowired
  private MockMvc mockMvc;
  
  @Autowired
  private TarefaRepository tarefaRepository;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @BeforeEach
  public void setUp() {
    tarefaRepository.deleteAll();
  }
  
  // Teste de criação de tarefa
  @Test
  public void criarTarefa_DeveRetornar201() throws Exception {
    TarefaDTO tarefaDTO = TarefaDTO.builder()
    .name("Estudar")
    .description("Estudar Spring Boot")
    .observations("Nenhuma")
    .status("PENDENTE")
    .build();
    
    mockMvc.perform(post("/api/tarefas")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(tarefaDTO)))
    .andExpect(status().isCreated())
    .andExpect(jsonPath("$.name").value("Estudar"));
    
    List<Tarefa> tarefas = tarefaRepository.findAll();
    assertThat(tarefas).hasSize(1);
  }
  
  // Teste de atualização de tarefa
  @Test
  public void atualizarTarefa_DeveRetornar200() throws Exception {
    Tarefa tarefa = Tarefa.builder()
    .id(1L) // Adicionando o ID
    .name("Estudar")
    .description("Estudar Spring Boot")
    .observations("Nenhuma")
    .status(StatusTarefa.PENDENTE)
    .build();
    tarefa = tarefaRepository.save(tarefa);
    
    TarefaDTO tarefaDTO = TarefaDTO.builder()
    .id(tarefa.getId()) // Adicionando o ID
    .name("Estudar Atualizado")
    .description("Atualização sobre Spring Boot")
    .observations("Nenhuma")
    .status("PENDENTE")
    .build();
    
    mockMvc.perform(put("/api/tarefas/" + tarefa.getId())
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(tarefaDTO)))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.name").value("Estudar Atualizado"));
  }
  
  // Teste de exclusão de tarefa
  @Test
  public void deletarTarefa_DeveRetornar204() throws Exception {
    Tarefa tarefa = Tarefa.builder()
    .id(1L) // Adicionando o ID
    .name("Estudar")
    .description("Estudar Spring Boot")
    .observations("Nenhuma")
    .status(StatusTarefa.PENDENTE)
    .build();
    tarefa = tarefaRepository.save(tarefa);
    
    mockMvc.perform(delete("/api/tarefas/" + tarefa.getId()))
    .andExpect(status().isNoContent());
    
    List<Tarefa> tarefas = tarefaRepository.findAll();
    assertThat(tarefas).isEmpty();
  }
  
  // Teste de busca de tarefas
  @Test
  public void buscarTarefas_DeveRetornar200() throws Exception {
    Tarefa tarefa = Tarefa.builder()
    .id(1L) // Adicionando o ID
    .name("Estudar")
    .description("Estudar Spring Boot")
    .observations("Nenhuma")
    .status(StatusTarefa.PENDENTE)
    .build();
    tarefaRepository.save(tarefa);
    
    mockMvc.perform(get("/api/tarefas")
    .contentType(MediaType.APPLICATION_JSON))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$[0].name").value("Estudar"));
  }
  
  // Teste de atualização de status da tarefa
  @Test
  public void atualizarStatusTarefa_DeveRetornar200() throws Exception {
    Tarefa tarefa = Tarefa.builder()
    .id(1L) // Adicionando o ID
    .name("Estudar")
    .description("Estudar Spring Boot")
    .observations("Nenhuma")
    .status(StatusTarefa.PENDENTE)
    .build();
    tarefa = tarefaRepository.save(tarefa);
    
    mockMvc.perform(put("/api/tarefas/" + tarefa.getId() + "/status")
    .contentType(MediaType.APPLICATION_JSON)
    .content("FINALIZADO")) // Mudança para "FINALIZADO"
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.status").value("FINALIZADO"));
  }
  
}
