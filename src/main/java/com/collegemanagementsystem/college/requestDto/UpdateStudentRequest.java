package com.collegemanagementsystem.college.requestDto;

import com.collegemanagementsystem.college.Annotation.SpecialCharacterNotAllowed;
import com.collegemanagementsystem.college.dto.ProfessorDto;
import com.collegemanagementsystem.college.dto.SubjectDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentRequest {
    @SpecialCharacterNotAllowed
    private String studentName;

    @NotEmpty(message = "At least one subject is required")
    private List<SubjectDto> subjectList;

    private List<ProfessorDto> professorList;
}
