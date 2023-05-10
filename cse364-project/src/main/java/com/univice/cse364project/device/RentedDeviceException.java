package com.univice.cse364project.device;

public class RentedDeviceException extends RuntimeException{
    RentedDeviceException(){
        super("This device is already rented.");
    }
}
