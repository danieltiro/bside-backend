package com.bside.demo.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bside.demo.entity.Attachment;
import com.bside.demo.entity.Student;

public interface IAttachmentService{
	Page<Attachment> findByStudentIdAndActiveAndKey(UUID id, Boolean active, String key, Pageable pageable);
	Page<Attachment> findByIdAndActive(UUID id, Boolean active, Pageable pageable);
	Attachment save(Attachment attachment);
	Optional<Attachment> findById(UUID id);
}
