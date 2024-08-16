package com.bside.demo.exception;

public class StudentArgumentNotValidException extends RuntimeException{
	
    private static final long serialVersionUID = 1L;

	public StudentArgumentNotValidException(String message){
        super(message);
    }
}