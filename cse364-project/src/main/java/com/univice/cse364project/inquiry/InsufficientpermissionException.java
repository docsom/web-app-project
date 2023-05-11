package com.univice.cse364project.inquiry;

public class InsufficientpermissionException extends RuntimeException{
    InsufficientpermissionException(){
        super("Permission is not sufficient.");
    }
}
