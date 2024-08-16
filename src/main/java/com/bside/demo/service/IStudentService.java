package com.bside.demo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bside.demo.entity.Student;

public interface IStudentService{
	Optional<Student> findByCurpAndActive(String curp, Boolean active);
	Page<Student> findByNameAndActive(String name, Boolean active, Pageable pageable);
	Page<Student> findByNameAndLastnameAndActive(String name, String lastname, Boolean active, Pageable pageable);
	Student save(Student student);
}
