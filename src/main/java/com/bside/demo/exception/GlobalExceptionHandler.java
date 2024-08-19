package com.bside.demo.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.atn.ErrorInfo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler({StudentNotFoundException.class})
    public ResponseEntity<Object> handleStudentNotFoundException(StudentNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
	
	@ExceptionHandler({StudentAlreadyExistsException.class})
    public ResponseEntity<Object> handleStudentAlreadyExistsException(StudentAlreadyExistsException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }
	
	@ExceptionHandler({StudentDataIntegrityViolationException.class})
	public ResponseEntity<Object> duplicateEmailException(HttpServletRequest req, StudentDataIntegrityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(error -> {
        	return error.getField() + ": " + error.getDefaultMessage();
        }).collect(Collectors.toList());
        
        List<String> globalErrors = ex.getBindingResult().getGlobalErrors().stream().map(error -> {
        	return error.getObjectName() + ": " + error.getDefaultMessage();
        }).collect(Collectors.toList());
        errors.addAll(globalErrors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
	}

	
}
