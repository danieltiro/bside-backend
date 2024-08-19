package com.bside.demo.exception;

public class StudentFileNotFoundException extends RuntimeException {
	
    private static final long serialVersionUID = 1L;

	public StudentFileNotFoundException(String message) {
        super(message);
    }

    public StudentFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}