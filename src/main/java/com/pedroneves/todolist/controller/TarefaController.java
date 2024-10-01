package com.pedroneves.todolist.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedroneves.todolist.dto.TarefaDTO;
import com.pedroneves.todolist.enums.StatusTarefa;
import com.pedroneves.todolist.model.Tarefa;
import com.pedroneves.todolist.service.TarefaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tarefas")
@RequiredArgsConstructor
public class TarefaController {
  
  private final TarefaService tarefaService;
  
  // Converte Tarefa para TarefaDTO
  private TarefaDTO toDTO(Tarefa tarefa) {
    return TarefaDTO.builder()
    .id(tarefa.getId())
    .name(tarefa.getName())
    .description(tarefa.getDescription())
    .observations(tarefa.getObservations())
    .status(tarefa.getStatus().toString())
    .build();
  }
  
  // Converte TarefaDTO para Tarefa
  private Tarefa toEntity(TarefaDTO tarefaDTO) {
    return Tarefa.builder()
    .id(tarefaDTO.getId())
    .name(tarefaDTO.getName())
    .description(tarefaDTO.getDescription())
    .observations(tarefaDTO.getObservations())
    .status(tarefaDTO.getStatus() != null ? StatusTarefa.valueOf(tarefaDTO.getStatus().toUpperCase()) : StatusTarefa.PENDENTE) // Define o status como "PENDENTE" se for nulo
    .build();
  }
  
  @PostMapping
  public ResponseEntity<TarefaDTO> criarTarefa(@RequestBody TarefaDTO tarefaDTO) {
    Tarefa tarefa = toEntity(tarefaDTO);
    tarefa.setId(null);
    Tarefa novaTarefa = tarefaService.criarTarefa(tarefa);
    return new ResponseEntity<>(toDTO(novaTarefa), HttpStatus.CREATED);
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<TarefaDTO> atualizarTarefa(@PathVariable("id") Long id, @RequestBody TarefaDTO tarefaDTO) {
    Tarefa tarefa = tarefaService.buscar(Tarefa.builder().id(id).build()).stream().findFirst()
    .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
    tarefa.setName(tarefaDTO.getName());
    tarefa.setDescription(tarefaDTO.getDescription());
    tarefa.setObservations(tarefaDTO.getObservations());
    tarefa.setStatus(StatusTarefa.valueOf(tarefaDTO.getStatus().toUpperCase()));
    
    Tarefa tarefaAtualizada = tarefaService.atualizarTarefa(tarefa);
    return new ResponseEntity<>(toDTO(tarefaAtualizada), HttpStatus.OK);
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletarTarefa(@PathVariable("id") Long id) {
    Tarefa tarefa = tarefaService.buscar(Tarefa.builder().id(id).build()).stream().findFirst()
    .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
    tarefaService.deletarTarefa(tarefa);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
  
  @GetMapping
  public ResponseEntity<List<TarefaDTO>> buscarTarefas(TarefaDTO filtroDTO) {
    Tarefa filtro = toEntity(filtroDTO);
    List<Tarefa> tarefas = tarefaService.buscar(filtro);
    List<TarefaDTO> tarefasDTO = tarefas.stream().map(this::toDTO).collect(Collectors.toList());
    return new ResponseEntity<>(tarefasDTO, HttpStatus.OK);
  }
  
  @PutMapping("/{id}/status")
  public ResponseEntity<TarefaDTO> atualizarStatusTarefa(@PathVariable("id") Long id, @RequestBody String status) {
    Tarefa tarefa = tarefaService.buscar(Tarefa.builder().id(id).build()).stream().findFirst()
    .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
    
    tarefaService.atualizarStatus(tarefa, StatusTarefa.valueOf(status.toUpperCase()));
    
    TarefaDTO tarefaAtualizadaDTO = toDTO(tarefa);
    return new ResponseEntity<>(tarefaAtualizadaDTO, HttpStatus.OK);
  }
}
