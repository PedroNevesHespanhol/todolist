package com.pedroneves.todolist.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter 
@Builder @Data
public class TarefaDTO {
  public Long id;
  public String name;
  public String description;
  public String observations;
  public String status;
}
