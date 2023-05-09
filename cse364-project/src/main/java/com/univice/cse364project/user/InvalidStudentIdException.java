package com.univice.cse364project.user;

public class InvalidStudentIdException extends RuntimeException{
    InvalidStudentIdException(){super("Given student id is invalid.");}
}
