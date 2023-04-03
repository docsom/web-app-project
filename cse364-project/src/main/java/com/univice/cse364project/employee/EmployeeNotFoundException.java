package com.univice.cse364project.employee;

public class EmployeeNotFoundException extends RuntimeException {
    EmployeeNotFoundException(Long id) {
        super("Could not find employee " + id);
    }
}
