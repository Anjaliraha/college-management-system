package com.collegemanagementsystem.college.services;

import com.collegemanagementsystem.college.dto.StudentDto;
import com.collegemanagementsystem.college.dto.SubjectDto;
import com.collegemanagementsystem.college.entities.AdmissionRecordEntity;
import com.collegemanagementsystem.college.entities.ProfessorEntity;
import com.collegemanagementsystem.college.entities.StudentEntity;
import com.collegemanagementsystem.college.entities.SubjectEntity;
import com.collegemanagementsystem.college.exception.ResourceNotFoundException;
import com.collegemanagementsystem.college.repository.AdmissionRecordEntityRepository;
import com.collegemanagementsystem.college.repository.ProfessorEntityRepository;
import com.collegemanagementsystem.college.repository.StudentEntityRepository;
import com.collegemanagementsystem.college.repository.SubjectEntityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

  private final ModelMapper modelMapper;
  private final AdmissionRecordEntityRepository admissionRecordEntityRepository;
  private final ProfessorEntityRepository professorEntityRepository;
  private final StudentEntityRepository studentEntityRepository;
  private final SubjectEntityRepository subjectEntityRepository;
  private final ObjectMapper objectMapper;
  private final Long FEES = 1110000L;

  public StudentService(
      ModelMapper modelMapper,
      AdmissionRecordEntityRepository admissionRecordEntityRepository,
      ProfessorEntityRepository professorEntityRepository,
      StudentEntityRepository studentEntityRepository,
      SubjectEntityRepository subjectEntityRepository,
      ObjectMapper objectMapper) {

    this.modelMapper = modelMapper;
    this.admissionRecordEntityRepository = admissionRecordEntityRepository;
    this.professorEntityRepository = professorEntityRepository;
    this.studentEntityRepository = studentEntityRepository;
    this.subjectEntityRepository = subjectEntityRepository;
    this.objectMapper = objectMapper;
  }

  public StudentDto getStudentById(Long id) {
    StudentEntity studentEntity =
        studentEntityRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "student with this id " + id + " does not exist"));

    return modelMapper.map(studentEntity, StudentDto.class);
  }

  public List<StudentDto> getAllStudent() {
    List<StudentEntity> studentEntities = studentEntityRepository.findAll();
    List<StudentDto> studentDtos =
        studentEntities.stream().map(s -> modelMapper.map(s, StudentDto.class)).toList();
    return studentDtos;
  }

  @Transactional
  public StudentDto createStudent(StudentDto studentDto) {
    // Convert the incoming DTO into an Entity because
    // the repository works with StudentEntity.
    StudentEntity studentEntity = verifySubjectAndProfessorList(studentDto);
    AdmissionRecordEntity admissionRecordEntity = studentEntity.getAdmissionRecord();
    if (admissionRecordEntity == null || StringUtils.isEmpty(admissionRecordEntity.toString())) {
      admissionRecordEntity = new AdmissionRecordEntity();
      admissionRecordEntity.setFees(FEES);
    }
    admissionRecordEntity.setStudent(studentEntity);
    studentEntity.setAdmissionRecord(admissionRecordEntity);
    // Save the entity into the database.
    StudentEntity savedEntity = studentEntityRepository.save(studentEntity);
    // Convert the saved Entity back into a DTO before
    // returning the response to the client.

    return modelMapper.map(savedEntity, StudentDto.class);
  }

  @Transactional
  public StudentDto updateStudent(StudentDto studentDto, Long id) {

    // First, find the existing student in the database.
    // If the student does not exist, throw an exception.
    StudentEntity studentEntity =
        studentEntityRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "student with this id " + id + " does not exist"));

    StudentEntity verifiedEntity = verifySubjectAndProfessorList(studentDto);
    // Copy the values from the DTO into the existing Entity.
    // Since this is a full update, the DTO represents the
    // complete set of values that should be updated.
    modelMapper.map(studentDto, studentEntity);
    studentEntity.setStudentId(id);

    // Save the updated Entity back to the database.
    StudentEntity savedStudent = studentEntityRepository.save(studentEntity);

    // Convert the saved Entity into a DTO and return it.
    return modelMapper.map(savedStudent, StudentDto.class);
  }

  /**
   * Performs a PARTIAL update of a student.
   *
   * <p>This method is useful for PATCH-style requests where the client sends only the fields that
   * need to be changed instead of sending the complete Student object.
   *
   * <p>Example:
   *
   * <p>Existing student: { "id": 1, "name": "John", "age": 20, "email": "john@gmail.com" }
   *
   * <p>PATCH request: { "age": 21 }
   *
   * <p>We should update only the age while keeping the other fields unchanged.
   *
   * <p>The process is:
   *
   * <p>1. Fetch the existing StudentEntity from the database.
   *
   * <p>2. Convert the existing StudentEntity into a Map. The Map represents the current state of
   * the student as key-value pairs.
   *
   * <p>Example: { "id": 1, "name": "John", "age": 20, "email": "john@gmail.com" }
   *
   * <p>3. Add the fields received in studentPatches to the existing Map. putAll() replaces the
   * existing value when the same key exists.
   *
   * <p>Example: Existing Map: age = 20
   *
   * <p>Patch: age = 21
   *
   * <p>After putAll(): age = 21
   *
   * <p>Fields that were not included in the PATCH remain unchanged.
   *
   * <p>4. Convert the updated Map into StudentDto.
   *
   * <p>5. Use ModelMapper to copy the updated DTO values back into the existing StudentEntity.
   *
   * <p>6. Save the updated Entity using the repository.
   *
   * <p>7. Convert the saved Entity back into StudentDto and return it as the response.
   */
  @Transactional
  public StudentDto updateStudentDetails(Map<String, Object> studentPatches, Long id) {

    // Step 1:
    // Fetch the existing student from the database.
    StudentEntity studentEntity =
        studentEntityRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "student with this id " + id + " does not exist"));

    // Step 2:
    // Convert the existing StudentEntity into a Map.
    //
    // This allows us to treat the student's fields as
    // key-value pairs so that we can easily apply only
    // the fields received in the PATCH request.
    Map<String, Object> existingStudent = objectMapper.convertValue(studentEntity, Map.class);

    // Step 3:
    // Apply the PATCH values to the existing student data.
    //
    // putAll() adds the new fields to the Map.
    // If a field already exists, its old value is replaced
    // with the new value from studentPatches.
    //
    // Fields that are NOT present in studentPatches remain
    // unchanged.
    existingStudent.putAll(studentPatches);

    // Step 4:
    // Convert the updated Map into StudentDto.
    //
    // At this point, the Map contains the original student
    // data plus the newly patched values.
    StudentDto studentDto = objectMapper.convertValue(existingStudent, StudentDto.class);

    // Step 5:
    // Copy the updated DTO values into the existing Entity.
    // We are modifying the entity that we originally fetched
    // from the database.
    modelMapper.map(studentDto, studentEntity);

    // Step 6:
    // Save the modified Entity back to the database.
    StudentEntity savedStudent = studentEntityRepository.save(studentEntity);

    // Step 7:
    // Convert the saved Entity back into StudentDto.
    // This DTO is returned to the client as the PATCH response.
    return modelMapper.map(savedStudent, StudentDto.class);
  }

  @Transactional
  public Boolean deleteStudentById(Long id) {

    // Step 1:
    // Check whether the student exists before attempting to delete.
    // If the student does not exist, throw an exception.
    StudentEntity studentEntity =
        studentEntityRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Student with this id " + id + " does not exist"));

    try {
      // Step 2:
      // Delete the student using the ID.
      studentEntityRepository.deleteById(id);

      // Step 3:
      // If no exception occurred, the delete operation was successful.
      return true;

    } catch (Exception e) {

      // Step 4:
      // If something goes wrong while deleting,
      // convert the exception into a RuntimeException.
      throw new RuntimeException("Failed to delete student with id " + id, e);
    }
  }

  @Transactional
  public StudentEntity verifySubjectAndProfessorList(StudentDto studentDto) {
    List<Long> requestSubjectIds =
        studentDto.getSubjectList().stream().map(s -> s.getSubjectId()).toList();

    List<Long> requestProfessorIds =
        studentDto.getProfessorList().stream().map(s -> s.getProfessorId()).toList();

    StudentEntity studentEntity = modelMapper.map(studentDto, StudentEntity.class);

    List<SubjectEntity> subjectEntityList =
        fetchAllorThrowError(
            requestSubjectIds,
            subjectEntityRepository,
            SubjectEntity::getSubjectId,
            "SubjectEntity");
    List<ProfessorEntity> professorEntityList =
        fetchAllorThrowError(
            requestProfessorIds,
            professorEntityRepository,
            ProfessorEntity::getProfessorId,
            "ProfessorEntity");

    studentEntity.setSubjectList(subjectEntityList);
    studentEntity.setProfessorList(professorEntityList);

    return studentEntity;
  }

  public List<SubjectDto> getAllSubject() {

    List<SubjectEntity> subjectEntityList = subjectEntityRepository.findAll();
    List<SubjectDto> subjectEntities =
        subjectEntityList.stream().map(s -> modelMapper.map(s, SubjectDto.class)).toList();
    return subjectEntities;
  }

  //  @Transactional
  //  public ProfessorDto createProfessor(ProfessorDto professorDto) {
  //    ProfessorEntity professor = modelMapper.map(professorDto, ProfessorEntity.class);
  //    List<Long> requestedSubjectIds =
  //        professor.getSubjectList().stream().map(s -> s.getSubjectId()).toList();
  //    List<SubjectEntity> subjectEntity =
  //        fetchAllorThrowError(
  //            requestedSubjectIds,
  //            subjectEntityRepository,
  //            SubjectEntity::getSubjectId,
  //            "SubjectEntity");
  //    professor.setSubjectList(subjectEntity);
  //
  //    List<Long> requestedStudentIds =
  //        professor.getStudentList().stream().map(s -> s.getStudentId()).toList();
  //    List<StudentEntity> studentEntity =
  //        fetchAllorThrowError(
  //            requestedStudentIds,
  //            studentEntityRepository,
  //            StudentEntity::getStudentId,
  //            "StudentEntity");
  //    studentEntity = professor.getStudentList();
  //    professor.setStudentList(studentEntity);
  //    professorEntityRepository.save(professor);
  //    return modelMapper.map(professor, ProfessorDto.class);
  //  }

  //  @Transactional
  //  public SubjectDto createSubject(@Valid SubjectDto subjectDto) {
  //    SubjectEntity subjectEntity = modelMapper.map(subjectDto, SubjectEntity.class);
  //    List<Long> requestedProfessorIds =
  //        subjectDto.getProfessorList().stream().map(s -> s.getProfessorId()).toList();
  //    List<ProfessorEntity> professor =
  //        fetchAllorThrowError(
  //            requestedProfessorIds,
  //            professorEntityRepository,
  //            ProfessorEntity::getProfessorId,
  //            "ProfessorEntity");
  //    subjectEntity.setProfessorList(professor);
  //
  //    List<Long> requestedStudentId =
  //        subjectDto.getStudent().stream().map(s -> s.getStudentId()).toList();
  //    List<StudentEntity> studentEntity =
  //        fetchAllorThrowError(
  //            requestedStudentId,
  //            studentEntityRepository,
  //            StudentEntity::getStudentId,
  //            "StudentEntity");
  //    subjectEntity.setStudent(studentEntity);
  //    subjectEntityRepository.save(subjectEntity);
  //    return modelMapper.map(subjectEntity, SubjectDto.class);
  //  }

  public <E> List<E> fetchAllorThrowError(
      Collection<Long> requestIds,
      JpaRepository<E, Long> jpaRepository,
      Function<E, Long> getterMethod,
      String entityName) {
    if (requestIds == null || requestIds.isEmpty()) {
      return new ArrayList<>();
    }

    List<E> found = jpaRepository.findAllById(requestIds);
    List<Long> existing = found.stream().map(getterMethod).toList();
    List<Long> missing = requestIds.stream().filter(s -> !existing.contains(s)).toList();
    if (!missing.isEmpty()) {
      throw new ResourceNotFoundException(entityName + " does not exist with ids " + missing);
    }
    return found;
  }
}
