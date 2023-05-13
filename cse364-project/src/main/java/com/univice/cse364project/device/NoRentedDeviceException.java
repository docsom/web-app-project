package com.univice.cse364project.device;

public class NoRentedDeviceException extends RuntimeException{
    NoRentedDeviceException(){
        super("You have no rented device.");
    }
}
