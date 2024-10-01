package com.pedroneves.todolist.service;

import java.util.List;

import com.pedroneves.todolist.enums.StatusTarefa;
import com.pedroneves.todolist.model.Tarefa;

public interface TarefaService {
  Tarefa criarTarefa(Tarefa tarefa);
  Tarefa atualizarTarefa(Tarefa tarefa);
  void deletarTarefa(Tarefa tarefa);
  List<Tarefa> buscar(Tarefa tarefaFiltro);
  void atualizarStatus(Tarefa tarefa, StatusTarefa status);
}
