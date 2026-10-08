package com.collegemanagementsystem.college.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProfessorEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "professor_seq_id")
  @SequenceGenerator(
      name = "professor_seq_id",
      sequenceName = "professor_seq_id",
      initialValue = 2001)
  private Long professorId;

  private String professorName;

  @ManyToMany(mappedBy = "professorList")
  @JsonIgnore
  private List<SubjectEntity> subjectList;

  @ManyToMany(mappedBy = "professorList")
  @JsonIgnore
  private List<StudentEntity> studentList;
}
