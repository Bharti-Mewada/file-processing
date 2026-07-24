package com.fileProcessing.utility;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fileProcessing.entity.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(FileValidationException.class)
	public ErrorResponse handleFileValidation(FileValidationException fileException) {
		ErrorResponse errResponse = new ErrorResponse();
		errResponse.setMessage(fileException.getMessage());
		return errResponse;
	}

}
