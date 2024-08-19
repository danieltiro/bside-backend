package com.bside.demo.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bside.demo.entity.Attachment;
import com.bside.demo.entity.Student;
import com.bside.demo.repository.IAttachmentRepository;
import com.bside.demo.repository.IStudentRepository;

import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;

@Log
@Service("attachmentService")
public class AttachmentService implements IAttachmentService {
	
	@Autowired
	IAttachmentRepository attachmentRepository;

	@Transactional
	@Override
	public Attachment save(Attachment entity) {
		return attachmentRepository.save(entity);
	}
	@Override
	public Page<Attachment> findByStudentIdAndActiveAndKey(UUID id, Boolean active, String key, Pageable pageable) {
		return attachmentRepository.findByStudentIdAndActiveAndKeyContainingIgnoreCase(id, active, key, pageable);
	}
	@Override
	public Page<Attachment> findByIdAndActive(UUID id, Boolean active, Pageable pageable) {
		return attachmentRepository.findByIdAndActive(id, active, pageable);
	}
	@Override
	public Optional<Attachment> findById(UUID id) {
		return attachmentRepository.findById(id);
	}
	
	

}
