package com.casestudy.exception;

public class UpdateNotAllowedException extends RuntimeException{
    public UpdateNotAllowedException(String msg){
        super(msg);
    }
}
