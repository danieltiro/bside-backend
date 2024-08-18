package com.bside.demo.controller;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bside.demo.entity.Student;
import com.bside.demo.exception.StudentAlreadyExistsException;
import com.bside.demo.exception.StudentArgumentNotValidException;
import com.bside.demo.exception.StudentNotFoundException;
import com.bside.demo.service.IStudentService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@CrossOrigin(origins = {"*"})
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/student")
public class StudentController {
	
	@Autowired
	IStudentService studentService;
	
	@GetMapping("/curp")
	@ResponseStatus(value = HttpStatus.OK)
	public Student findByCurp(@RequestParam(name = "curp", required = true) String curp){
		return studentService.findByCurpAndActive(curp, true).orElseThrow(() -> new StudentNotFoundException("Student not found: " + curp));
	}
	
	@GetMapping("/name")
	public ResponseEntity<Page<Student>> findByName(@RequestParam(name = "name", required = true) String name,
		@PageableDefault(page = 0, size = 10) @SortDefault.SortDefaults({
		@SortDefault(sort = "name", direction = Direction.ASC) }) Pageable pageable) {
		Page<Student> students = studentService.findByNameAndActive(name, true, pageable);
		return ResponseEntity.ok(students);
	}
	
	@PostMapping()
	@ResponseStatus(value = HttpStatus.CREATED)
	public Student create(@Valid @RequestBody Student entity){
		
		Student student = studentService.findByCurpAndActive(entity.getCurp(), true).orElse(null);
		if (student != null) {
			throw new StudentAlreadyExistsException("Student already exists with curp: " + entity.getCurp());
		}
		return studentService.save(entity);
	}
	
	@PutMapping()
	public ResponseEntity<Student> update(@Valid @RequestBody Student entity){
		try {
			studentService.save(entity);
		}catch(DataIntegrityViolationException ex) {
			throw new StudentAlreadyExistsException("Student already exists with curp: " + entity.getCurp());
		}
		return new ResponseEntity<>(entity, HttpStatus.OK);
	}
	
	@DeleteMapping("/{curp}")
	public void delete(@PathVariable(name = "curp", required = true) String curp){
		Optional<Student> optStudent = studentService.findByCurpAndActive(curp, true);
		if(!optStudent.isPresent()) {
			throw new StudentNotFoundException("Student not found: " + curp);
		}else {
			Student student = optStudent.get();
			student.setDeleted(new Date());
			student.setActive(false);
			studentService.save(student);
		}
	}
	
	@GetMapping("/valida/curp/{curp}")
	public ResponseEntity<String> validaCurp(@PathVariable(name = "curp", required = true) String curp){
		if(curp.length() != 18) {
			throw new StudentArgumentNotValidException("Longitude not valid for curp: " + curp);
		}
		String url = "https://apimarket.mx/api/renapo/grupo/valida-curp?curp=" + curp;
		String token = "760825e9-73eb-4d0c-b1d5-83a47d6de581";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setBearerAuth(token);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		if (response.getStatusCode() == HttpStatus.OK) {
			return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
		}else {
			throw new StudentArgumentNotValidException(response.getStatusCode().toString());
		}
			
	}

}