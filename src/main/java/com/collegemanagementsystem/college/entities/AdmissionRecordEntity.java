package com.collegemanagementsystem.college.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AdmissionRecordEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long admissionId;

  private Long fees;

  @OneToOne
  @JoinColumn(name = "student_id", unique = true, nullable = false)
  @JsonIgnore
  private StudentEntity student;
}
