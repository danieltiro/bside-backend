package com.bside.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bside.demo.entity.Attachment;
import com.bside.demo.entity.Student;
import com.bside.demo.repository.IAttachmentRepository;
import com.bside.demo.repository.IStudentRepository;
import com.google.common.io.Files;

import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;

@Log
@Service("studentService")
public class StudentService implements IStudentService {
	
	@Autowired
	IStudentRepository studentRepository;
	
	@Autowired
	IAttachmentRepository attachmentRepository;
	
	@Autowired
	IFileStorageService fileStorageService;

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
	
	@Transactional
	@Override
	public HashMap<String, Object> saveWithAttachments(Student entity, MultipartFile[] multiparts,String key, String description) {
		Student student = studentRepository.save(entity);
		HashMap<String, Object> result =  new HashMap<String, Object>();
		List<Attachment> attachments = new ArrayList<>();
		if(multiparts != null) {
			String contentType = "application/octet-stream";
			for(int i=0 ;i<multiparts.length; i++){
				try {
					contentType = java.nio.file.Files.probeContentType(new java.io.File(multiparts[i].getOriginalFilename()).toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
				Attachment attachment = new Attachment();
				attachment.setStudent(student);
				attachment.setName(StringUtils.cleanPath(multiparts[i].getOriginalFilename()));
				attachment.setKey(key);
				attachment.setDescription(description);
				attachment.setExtension(Files.getFileExtension(multiparts[i].getOriginalFilename()));
				attachment.setSize(Double.valueOf(String.valueOf(multiparts[i].getSize())));
				attachment.setContentType(contentType);
				attachmentRepository.save(attachment);
				fileStorageService.storeFile(multiparts[i], attachment.getId().toString());
				attachments.add(attachment);
				
				if(attachment.getKey().equals("avatar")) {
					student.setAvatar(attachment.getId().toString());
					studentRepository.save(student);
				}
			}
		}
		result.put("student", student);
		result.put("attachments", attachments);
		return result;
	}

}
