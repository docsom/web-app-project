package com.univice.cse364project.inquiry;

public class WrongAuthenticationIdException extends RuntimeException{
    WrongAuthenticationIdException(){
        super("AuthenticationId is wrong.");
    }
}
