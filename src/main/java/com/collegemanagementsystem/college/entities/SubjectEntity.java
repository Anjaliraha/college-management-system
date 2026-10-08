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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"subjectTitle"})})
public class SubjectEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subject_id_seq")
  @SequenceGenerator(
      name = "subject_id_seq",
      sequenceName = "subject_id_seq",
      initialValue = 101,
      allocationSize = 1)
  private Long subjectId;

  private String subjectTitle;

  @ManyToMany(mappedBy = "subjectList")
  @JsonIgnore
  private List<StudentEntity> student;

  @ManyToMany
  @JsonIgnore
  @JoinTable(
      name = "subject_professor",
      joinColumns = @JoinColumn(name = "subject_id"),
      inverseJoinColumns = @JoinColumn(name = "professor_id"),
      uniqueConstraints =
          @UniqueConstraint(
              name = "uk_subject_professor",
              columnNames = {"subject_id", "professor_id"}))
  private List<ProfessorEntity> professorList;
}
