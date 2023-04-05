package com.univice.cse364project.movie;

public class RatingInvalidException extends RuntimeException{
    RatingInvalidException(){ super("Value of the rating is invalid.");}
}
