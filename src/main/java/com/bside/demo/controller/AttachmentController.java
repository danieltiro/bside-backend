package com.bside.demo.controller;

import java.lang.ProcessHandle.Info;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bside.demo.entity.Attachment;
import com.bside.demo.service.IAttachmentService;
import com.bside.demo.service.IFileStorageService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Log
@CrossOrigin(origins = { "*", "*" })
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/attachment")
public class AttachmentController {
	
	@Autowired
    private IFileStorageService fileStorageService;
	
	@Autowired
    private IAttachmentService attachmentService;

	@GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID id, HttpServletRequest request) {
		log.info("<< downloadFile");
		Attachment file = attachmentService.findById(id).orElse(null);
		if(file == null) {
			return ResponseEntity.notFound().build();
		}
        Resource resource = fileStorageService.loadFileAsResource(file.getId().toString());
        String contentType = null;
        contentType = request.getServletContext().getMimeType(file.getName());
        if(contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }
}
