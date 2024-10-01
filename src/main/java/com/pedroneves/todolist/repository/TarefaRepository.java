package com.pedroneves.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pedroneves.todolist.model.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Long>{
  
}
