package com.univice.cse364project.device;

public class WrongAuthenticationIdException extends RuntimeException{
    WrongAuthenticationIdException(){
        super("AuthenticationId is wrong.");
    }
}
