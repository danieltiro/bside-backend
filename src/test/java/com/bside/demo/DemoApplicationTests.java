package com.bside.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bside.demo.entity.Student;
import com.bside.demo.exception.StudentAlreadyExistsException;
import com.bside.demo.exception.StudentArgumentNotValidException;
import com.bside.demo.exception.StudentNotFoundException;
import com.bside.demo.repository.IStudentRepository;
import com.bside.demo.service.StudentService;

@ExtendWith(MockitoExtension.class)
class DemoApplicationTests {
	
	@Mock
	private IStudentRepository studentRepository;
	
	@InjectMocks
	private StudentService studentService;
	
	@BeforeEach
	void setup() {
		
	}
	
	@Test 
	void testGetStudentByName() {
		Pageable pageable = PageRequest.of(0, 10);
		Student student = new Student("Daniel", "Shot", "TIBD841213HMCRNN01", true, new Date(), new Date());
		List<Student> students = new ArrayList<>();
		students.add(student);
		when(studentService.findByNameAndActive("dan", true, pageable)).thenReturn(new PageImpl<>(students, pageable, students.size()));
		Page<Student> page = studentService.findByNameAndActive("dan", true, pageable);
		assertThat(page).isNotNull();
		assertEquals(page.getContent().size(), 1);
		assertEquals(page.getContent().get(0).getCurp(), "TIBD841213HMCRNN01");
		assertEquals(page.getContent().get(0).getName(), "Daniel");
		verify(studentRepository).findByNameContainingIgnoreCaseAndActive("dan", true, pageable);
	}
	
	@Test
	void testGetStudentByCurp() {
		Student student = new Student("Daniel", "Shot", "TIBD841213HMCRNN01", true, new Date(), new Date());
		when(studentService.findByCurpAndActive(anyString(), anyBoolean())).thenReturn(Optional.of(student));
		Student student2 = studentService.findByCurpAndActive("TIBD841213HMCRNN01", true).orElse(null);
		assertThat(student2).isNotNull();
		assertEquals(student2.getCurp(), student.getCurp());
		verify(studentRepository).findByCurpIgnoreCaseAndActive("TIBD841213HMCRNN01", true);
	}
	
	@Test
	void testSaveStudent() {
		Student student = new Student("Daniel", "Shot", "TIBD841213HMCRNN01", true, new Date(), new Date());
		when(studentService.save(student)).thenReturn(student);
		studentService.save(student);
		assertNotNull(student);
		assertNotNull(student.getCreated());
		assertNotNull(student.getName());
		assertNotNull(student.getCurp());
		verify(studentRepository).save(student);
	}
	
	@Test
	void testUpdateStudent() {
		Student student = new Student("Daniel", "Shot", "TIBD841213HMCRNN01", true, new Date(), new Date());
		student.setId(UUID.randomUUID());
		when(studentService.save(student)).thenReturn(student);
		studentService.save(student);
		assertNotNull(student);
		assertNotNull(student.getId());
		assertNotNull(student.getCreated());
		assertNotNull(student.getName());
		assertNotNull(student.getCurp());
		verify(studentRepository).save(student);
	}
	
	@Test
	void testThrowStudentAlreadyExistsExceptionOnSaveStudent() {
		Student student = new Student("Daniel", "Shot", "TIBD841213HMCRNN01", true, new Date(), new Date());
		when(studentService.save(student)).thenThrow(new StudentAlreadyExistsException(""));
		assertThatThrownBy(() -> studentService.save(student)).isInstanceOf(StudentAlreadyExistsException.class);
		verify(studentRepository).save(student);
	}
	
	@Test
	void testThrowStudentArgumentNotValidExceptionOnSaveStudent() {
		Student student = new Student("Daniel", "Shot", "", true, new Date(), new Date());
		when(studentService.save(student)).thenThrow(new StudentArgumentNotValidException(""));
		assertThatThrownBy(() -> studentService.save(student)).isInstanceOf(StudentArgumentNotValidException.class);
		verify(studentRepository).save(student);
	}
	
	@Test
	void testThrowStudentNotFoundExceptionOnSaveStudent() {
		when(studentService.findByCurpAndActive("TIBD841213HMCRNN01", true)).thenThrow(new StudentNotFoundException(""));
		assertThatThrownBy(() -> studentService.findByCurpAndActive("TIBD841213HMCRNN01", true)).isInstanceOf(StudentNotFoundException.class);
		verify(studentRepository).findByCurpIgnoreCaseAndActive("TIBD841213HMCRNN01", true);
	}
}
