package com.univice.cse364project.device;

public class WrongRentedDeviceException extends RuntimeException{
    WrongRentedDeviceException(){
        super("This device is not your rented device.");
    }
}
