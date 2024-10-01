package com.pedroneves.todolist.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pedroneves.todolist.enums.StatusTarefa;
import com.pedroneves.todolist.model.Tarefa;
import com.pedroneves.todolist.repository.TarefaRepository;
import com.pedroneves.todolist.service.TarefaService;

@Service
public class TarefaServiceImpl implements TarefaService {

  private final TarefaRepository tarefaRepository;

  public TarefaServiceImpl(TarefaRepository tarefaRepository){
    super();
    this.tarefaRepository = tarefaRepository;
  }

  @Override
	@Transactional
	public Tarefa criarTarefa(Tarefa tarefa) {
		tarefa.setStatus(StatusTarefa.PENDENTE);
		return tarefaRepository.save(tarefa);
	}

	@Override
	@Transactional
	public Tarefa atualizarTarefa(Tarefa tarefa) {
		Objects.requireNonNull(tarefa.getId());
		return tarefaRepository.save(tarefa);
	}

	@Override
	@Transactional
	public void deletarTarefa(Tarefa tarefa) {
		Objects.requireNonNull(tarefa.getId());
		tarefaRepository.delete(tarefa);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Tarefa> buscar(Tarefa tarefaFiltro) {
		@SuppressWarnings("rawtypes")
		Example example = Example.of(tarefaFiltro,
				ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING) 
		);
		return tarefaRepository.findAll(example);
	}

	@Override
	@Transactional
	public void atualizarStatus(Tarefa tarefa, StatusTarefa status) {
		tarefa.setStatus(status);
		atualizarTarefa(tarefa);
	}

}
