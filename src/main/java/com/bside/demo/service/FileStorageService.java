package com.bside.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bside.demo.exception.FileStorageException;
import com.bside.demo.exception.StudentFileNotFoundException;

import lombok.extern.java.Log;

@Log
@Service("fileStorageService")
public class FileStorageService implements IFileStorageService{
	
	private final Path fileStorageLocation;

    public FileStorageService(Environment env){
        this.fileStorageLocation = Paths.get(env.getProperty("file.upload-dir")).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Path storeFile(MultipartFile file, String name) {
        try {
            Path targetLocation = this.fileStorageLocation.resolve(name);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation;
        } catch (IOException e) {
        	log.severe(e.getMessage());
            throw new FileStorageException("Could not store file " + name, e);
        }
    }
    
    public Path storeFile(String url, String name) {
    	try(InputStream in = new URL(url).openStream()){
            Path targetLocation = this.fileStorageLocation.resolve(name);
            Files.copy(in, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation;
        } catch (IOException e) {
        	log.severe(e.getMessage());
            throw new FileStorageException("Could not store file " + name, e);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new StudentFileNotFoundException("File not found " + fileName);
            }
        } catch (StudentFileNotFoundException | MalformedURLException ex) {
            throw new StudentFileNotFoundException("File not found " + fileName);
        }
    }

}
