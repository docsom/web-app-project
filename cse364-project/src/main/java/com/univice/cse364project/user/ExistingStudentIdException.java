package com.univice.cse364project.user;

public class ExistingStudentIdException extends RuntimeException {
    ExistingStudentIdException(){super("Given student id is already used.");}
}
