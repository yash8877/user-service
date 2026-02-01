package com.casestudy.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
	    ErrorResponse error = new ErrorResponse(
	        LocalDateTime.now(),
	        HttpStatus.NOT_FOUND.value(),
	        "UserException",
	        ex.getMessage()
	    );
	    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	

	@ExceptionHandler(UserAuthorizationException.class)
	public ResponseEntity<ErrorResponse> handleAuthorisationException(UserAuthorizationException ex){
	    ErrorResponse error = new ErrorResponse(
		        LocalDateTime.now(),
		        HttpStatus.FORBIDDEN.value(),
		        "UserAuthorizationException",
		        ex.getMessage()
		    );
		    return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(UpdateNotAllowedException.class)
	public ResponseEntity<ErrorResponse> handleUpdateNotAllowed(UpdateNotAllowedException ux){
		ErrorResponse response = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.FORBIDDEN.value(),
				"Access Denied",
				ux.getMessage()
		);
		return new ResponseEntity<>(response,HttpStatus.FORBIDDEN);
	}
	

    
	
	//by default if any other exception happend
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error, Something went wrong",
            ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
