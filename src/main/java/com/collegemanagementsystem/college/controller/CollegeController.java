package com.collegemanagementsystem.college.controller;

import com.collegemanagementsystem.college.dto.StudentDto;
import com.collegemanagementsystem.college.dto.SubjectDto;
import com.collegemanagementsystem.college.services.StudentService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/college")
public class CollegeController {

  private final StudentService collegeService;

  public CollegeController(StudentService collegeService) {
    this.collegeService = collegeService;
  }

  @GetMapping("/student")
  public ResponseEntity<List<StudentDto>> getStudent() {
    List<StudentDto> studentDtoList = collegeService.getAllStudent();
    if (studentDtoList.isEmpty()) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(studentDtoList);
  }

  @GetMapping("/subject")
  public ResponseEntity<List<SubjectDto>> getAllSubject() {
    List<SubjectDto> subjectDtos = collegeService.getAllSubject();
    if (subjectDtos.isEmpty()) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(subjectDtos);
  }

  @GetMapping("/student/{id}")
  public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
    StudentDto studentDto = collegeService.getStudentById(id);
    return ResponseEntity.ok(studentDto);
  }

  @PostMapping("/student")
  public ResponseEntity<StudentDto> createStudent(@RequestBody @Valid StudentDto studentDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(collegeService.createStudent(studentDto));
  }

  @PutMapping("/student/{id}")
  public ResponseEntity<StudentDto> updateStudent(
      @RequestBody @Valid StudentDto studentDto, @PathVariable Long id) {
    return ResponseEntity.ok(collegeService.updateStudent(studentDto, id));
  }

  @PatchMapping("/student/{id}")
  public ResponseEntity<StudentDto> updateStudentDetails(
      @RequestBody @Valid Map<String, Object> studentPatches, @PathVariable Long id) {
    return ResponseEntity.ok(collegeService.updateStudentDetails(studentPatches, id));
  }

  //  @PostMapping("/professor")
  //  public ResponseEntity<ProfessorDto> createProfessor(
  //      @RequestBody @Valid ProfessorDto professorDto) {
  //    return ResponseEntity.status(HttpStatus.CREATED)
  //        .body(collegeService.createProfessor(professorDto));
  //  }
  //
  //  @PostMapping("/subject")
  //  public ResponseEntity<SubjectDto> createSubject(@RequestBody @Valid SubjectDto subjectDto) {
  //    return
  // ResponseEntity.status(HttpStatus.CREATED).body(collegeService.createSubject(subjectDto));
  //  }

  @DeleteMapping("/student/{id}")
  public ResponseEntity<Boolean> deleteStudentById(@PathVariable Long id) {
    return ResponseEntity.ok(collegeService.deleteStudentById(id));
  }

  @GetMapping("/test-500")
  public String test500() {

    String name = null;

    return name.toUpperCase();
  }
}

/*
 * ResponseEntity Quick Reference
 * ==============================
 *
 * 1. I need a standard HTTP response
 *    --------------------------------
 *    Use the predefined ResponseEntity methods:
 *
 *    ResponseEntity.ok()                  -> 200 OK
 *    ResponseEntity.notFound()            -> 404 NOT_FOUND
 *    ResponseEntity.badRequest()          -> 400 BAD_REQUEST
 *    ResponseEntity.noContent()           -> 204 NO_CONTENT
 *
 *
 * 2. I need a custom HTTP status
 *    ----------------------------
 *    Use:
 *
 *    ResponseEntity.status(HttpStatus.CREATED)
 *                   .body(response);
 *
 *
 * 3. I need custom HTTP headers
 *    ---------------------------
 *    Use:
 *
 *    ResponseEntity.status(HttpStatus.CREATED)
 *                   .header("Header-Name", "value")
 *                   .body(response);
 *
 *
 * 4. I need complete/manual control
 *    --------------------------------
 *    Use the ResponseEntity constructor:
 *
 *    new ResponseEntity<>(
 *            body,
 *            headers,
 *            status
 *    );
 *
 *
 * Easy rule to remember:
 *
 * Standard response  -> ResponseEntity.ok(), notFound(), etc.
 * Custom status      -> ResponseEntity.status(...)
 * Custom headers     -> ResponseEntity.status(...).header(...)
 * Full control       -> new*
 */
