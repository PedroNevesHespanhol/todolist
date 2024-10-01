package com.pedroneves.todolist.model;

import java.time.LocalDate;

import com.pedroneves.todolist.enums.StatusTarefa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Entity @Builder @Data
@Table(name="tarefa", schema="to_do_list")
public class Tarefa {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "status")
  @Enumerated(value=EnumType.STRING)
  private StatusTarefa status;

  @Column(name = "observations")
  private String observations;

  @Column(name = "createDate")
  private LocalDate createDate;

  @Column(name = "updateDate")
  private LocalDate updateDate;
}
