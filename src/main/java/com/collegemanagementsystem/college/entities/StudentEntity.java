package com.collegemanagementsystem.college.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class StudentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long studentId;

  private String studentName;

  @ManyToMany
  @JoinTable(
      name = "Student_Subject",
      joinColumns = @JoinColumn(name = "student_id"),
      inverseJoinColumns = @JoinColumn(name = "subject_id"),
      uniqueConstraints =
          @UniqueConstraint(
              name = "uk_student_subject",
              columnNames = {"student_id", "subject_id"}))
  private List<SubjectEntity> subjectList;

  @ManyToMany
  @JoinTable(
      name = "Student_Professor",
      joinColumns = @JoinColumn(name = "student_id"),
      inverseJoinColumns = @JoinColumn(name = "professor_id"),
      uniqueConstraints =
          @UniqueConstraint(
              name = "uk_student_professor",
              columnNames = {"student_id", "professor_id"}))
  private List<ProfessorEntity> professorList;

  @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
  private AdmissionRecordEntity admissionRecord;

  public void addSubject(SubjectEntity subjectEntity) {
    if (subjectEntity == null) return;
    if (this.subjectList == null) subjectList = new ArrayList<>();
    if (!this.subjectList.contains(subjectEntity)) this.subjectList.add(subjectEntity);
    if (subjectEntity.getStudent() == null) subjectEntity.setStudent(new ArrayList<>());
    if (!subjectEntity.getStudent().contains(this)) subjectEntity.getStudent().add(this);
  }

  public void addProfessor(ProfessorEntity professor) {
    if (professor == null) return;
    if (this.professorList == null) professorList = new ArrayList<>();
    if (!this.professorList.contains(professor)) this.professorList.add(professor);
    if (professor.getStudentList() == null) professor.setStudentList(new ArrayList<>());
    if (!professor.getStudentList().contains(this)) professor.getStudentList().add(this);
  }
}
