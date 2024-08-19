package com.bside.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.bside.demo.entity.Attachment;
import com.bside.demo.entity.Student;

@Repository
public interface IAttachmentRepository extends PagingAndSortingRepository<Attachment, UUID>, JpaRepository<Attachment, UUID> {
	Page<Attachment> findByStudentIdAndActiveAndKeyContainingIgnoreCase(UUID id, Boolean active, String key, Pageable pageable);
	Page<Attachment> findByIdAndActive(UUID id, Boolean active, Pageable pageable);
	Attachment save(Attachment attachment);
}
