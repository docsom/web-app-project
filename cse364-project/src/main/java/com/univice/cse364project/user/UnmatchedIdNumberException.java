package com.univice.cse364project.user;

public class UnmatchedIdNumberException extends RuntimeException {
    UnmatchedIdNumberException(){super("Given Id number is not matched.");}
}
