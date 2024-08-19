package com.bside.demo.service;

import java.nio.file.Path;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {
	Path storeFile(MultipartFile file, String name);
	Resource loadFileAsResource(String fileName);
	Path storeFile(String url, String name);
}
