package com.univice.cse364project.employee;

import com.univice.cse364project.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
}
