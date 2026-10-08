package com.collegemanagementsystem.college.dto;

import com.collegemanagementsystem.college.Annotation.SpecialCharacterNotAllowed;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
  private Long studentId;

  @NotNull(message = "Please enter the studentName")
  @Size(min = 4, max = 20, message = "Please enter the name in size limit")
  @SpecialCharacterNotAllowed
  private String studentName;

  private List<SubjectDto> subjectList;
  private List<ProfessorDto> professorList;
  private AdmissionRecordDto admissionRecord;
}
