package com.bside.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bside.demo.entity.Student;
import com.bside.demo.repository.IStudentRepository;

import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;

@Log
@Service("studentService")
public class StudentService implements IStudentService {
	
	@Autowired
	IStudentRepository studentRepository;

	@Override
	public Optional<Student> findByCurpAndActive(String curp, Boolean active) {
		Optional<Student> optStudent =  studentRepository.findByCurpAndActive(curp, active);
		return optStudent;
	}

	@Override
	public Page<Student> findByNameAndActive(String name, Boolean active, Pageable pageable) {
		return studentRepository.findByNameContainingIgnoreCaseAndActive(name, active, pageable);
	}

	@Override
	public Page<Student> findByNameAndLastnameAndActive(String name, String lastname, Boolean active, Pageable pageable) {
		return studentRepository.findByNameAndLastnameAndActive(name, lastname, active, pageable);
	}

	@Transactional
	@Override
	public Student save(Student entity) {
		return studentRepository.save(entity);
	}

}
