package com.univice.cse364project.user;

public class ExistingIdException extends RuntimeException{
    ExistingIdException(){super("Given id is already used.");}
}
