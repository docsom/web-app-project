package com.univice.cse364project.user;

public class NoUserException extends RuntimeException {
    NoUserException(){super("There is no user using given id.");}
}
