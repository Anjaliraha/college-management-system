package com.collegemanagementsystem.college.repository;

import com.collegemanagementsystem.college.entities.ProfessorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorEntityRepository extends JpaRepository<ProfessorEntity, Long> {}
