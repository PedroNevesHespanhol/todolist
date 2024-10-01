package com.pedroneves.todolist.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import com.pedroneves.todolist.service.TarefaService;

@WebMvcTest(TarefaController.class)
public class TarefaControllerTest {
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private TarefaService tarefaService;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  // Teste para criar tarefa
  @Test
  public void criarTarefa_DeveRetornar201() throws Exception {
    TarefaDTO tarefaDTO = TarefaDTO.builder()
    .id(1L)
    .name("Estudar")
    .description("Estudar Spring Boot")
    .observations("Nenhuma")
    .status("PENDENTE")
    .build();

    Tarefa tarefa = Tarefa.builder().id(1L).name("Estudar").description("Estudar Spring Boot").observations("Nenhuma").status(StatusTarefa.PENDENTE).build();
    
    when(tarefaService.criarTarefa(any(Tarefa.class))).thenReturn(tarefa);
    
    mockMvc.perform(post("/api/tarefas")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(tarefaDTO)))
    .andExpect(status().isCreated())
    .andExpect(jsonPath("$.id").value(1L))
    .andExpect(jsonPath("$.name").value("Estudar"));
  }
  
  // Teste para atualizar tarefa
  @Test
  public void atualizarTarefa_DeveRetornar200() throws Exception {
    TarefaDTO tarefaDTO = TarefaDTO.builder()
    .id(1L)
    .name("Estudar")
    .description("Estudar Spring Boot")
    .observations("Teste 1")
    .status("PENDENTE")
    .build();

    Tarefa tarefa = Tarefa.builder().id(1L).name("Estudar").description("Estudar Spring Boot").observations("Teste 1").status(StatusTarefa.PENDENTE).build();
    
    when(tarefaService.buscar(any(Tarefa.class))).thenReturn(Arrays.asList(tarefa));
    when(tarefaService.atualizarTarefa(any(Tarefa.class))).thenReturn(tarefa);
    
    mockMvc.perform(put("/api/tarefas/1")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(tarefaDTO)))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.id").value(1L))
    .andExpect(jsonPath("$.name").value("Estudar"));
  }
  
  // Teste para deletar tarefa
  @Test
  public void deletarTarefa_DeveRetornar204() throws Exception {
    Tarefa tarefa = Tarefa.builder().id(1L).name("Estudar").description("Estudar Spring Boot").observations("Nenhuma").status(StatusTarefa.PENDENTE).build();
    
    when(tarefaService.buscar(any(Tarefa.class))).thenReturn(Arrays.asList(tarefa));
    Mockito.doNothing().when(tarefaService).deletarTarefa(any(Tarefa.class));
    
    mockMvc.perform(delete("/api/tarefas/1"))
    .andExpect(status().isNoContent());
  }
  
  // Teste para buscar tarefas
  @Test
  public void buscarTarefas_DeveRetornar200() throws Exception {
    TarefaDTO filtroDTO = TarefaDTO.builder()
    .id(null)
    .name("Estudar")
    .description(null)
    .observations(null)
    .status("PENDENTE")
    .build();

    Tarefa tarefa = Tarefa.builder().id(1L).name("Estudar").description("Estudar Spring Boot").observations("Nenhuma").status(StatusTarefa.PENDENTE).build();
    List<Tarefa> tarefas = Arrays.asList(tarefa);
    
    when(tarefaService.buscar(any(Tarefa.class))).thenReturn(tarefas);
    
    mockMvc.perform(get("/api/tarefas")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(filtroDTO)))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$[0].id").value(1L))
    .andExpect(jsonPath("$[0].name").value("Estudar"));
  }
  
  // Teste para atualizar status da tarefa
  @Test
  public void atualizarStatusTarefa_DeveRetornar200() throws Exception {
    Tarefa tarefa = Tarefa.builder().id(1L).name("Estudar").description("Estudar Spring Boot").observations("Nenhuma").status(StatusTarefa.FINALIZADO).build();
    
    when(tarefaService.buscar(any(Tarefa.class))).thenReturn(Arrays.asList(tarefa));
    Mockito.doNothing().when(tarefaService).atualizarStatus(any(Tarefa.class), eq(StatusTarefa.FINALIZADO));
    
    mockMvc.perform(put("/api/tarefas/1/status")
    .contentType(MediaType.APPLICATION_JSON)
    .content("FINALIZADO"))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.status").value("FINALIZADO"));
  }
}
