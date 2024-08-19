package com.bside.demo.exception;

public class StudentDataIntegrityViolationException extends RuntimeException{
	
    private static final long serialVersionUID = 1L;

	public StudentDataIntegrityViolationException(String message){
        super(message);
    }
}