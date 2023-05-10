package com.univice.cse364project.device;

public class MoreDeviceException extends RuntimeException{
    MoreDeviceException(){
        super("You can rent only one device.");
    }
}
