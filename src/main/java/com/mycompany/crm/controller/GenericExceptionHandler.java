package com.mycompany.crm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mycompany.crm.service.api.GenericBusinessServiceException;

@ControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {
 
    @ExceptionHandler(value = { GenericBusinessServiceException.class })
  public  ResponseEntity<ErrorInfo> handleKnownExceptions(GenericBusinessServiceException ex, WebRequest request) {
    	
    	
    	ErrorInfo error = new ErrorInfo();
    	

    	error.setMessage(ex.getMessage());
    	
      return ResponseEntity.status(ex.getClass().getAnnotation(ResponseStatus.class).code()).body(error);
    }
    
    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorInfo handleUnknownExceptions(Exception ex, WebRequest request) {
      	ErrorInfo error = new ErrorInfo();
      	
      	error.setMessage(ex.getMessage());
      	
        return error;
      }
    
}