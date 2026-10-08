package com.collegemanagementsystem.college.repository;

import com.collegemanagementsystem.college.entities.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectEntityRepository extends JpaRepository<SubjectEntity, Long> {}
