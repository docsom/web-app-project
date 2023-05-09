package com.univice.cse364project.user;

import org.apache.commons.text.WordUtils;

public class WrongPasswordException extends RuntimeException{
    WrongPasswordException(){super("Password is wrong.");}
}
