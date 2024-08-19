package com.bside.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.bside.demo.entity.Student;

@Repository
public interface IStudentRepository extends PagingAndSortingRepository<Student, UUID>, JpaRepository<Student, UUID> {
	Optional<Student> findByCurpIgnoreCase(String curp);
	Optional<Student> findByCurpIgnoreCaseAndActive(String curp, Boolean active);
	Page<Student> findByNameContainingIgnoreCaseAndActive(String name, Boolean active, Pageable pageable);
	Page<Student> findByNameIgnoreCaseAndLastnameIgnoreCaseAndActive(String name, String lastname, Boolean active, Pageable pageable);
}
