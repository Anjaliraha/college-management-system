package com.collegemanagementsystem.college.repository;

import com.collegemanagementsystem.college.entities.AdmissionRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionRecordEntityRepository
    extends JpaRepository<AdmissionRecordEntity, Long> {}
