package com.univice.cse364project.device;

public class NoDeviceException extends RuntimeException{
    NoDeviceException(){
        super("There is no device using given id.");
    }
}
