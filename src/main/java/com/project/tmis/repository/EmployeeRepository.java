package com.project.tmis.repository;

import com.project.tmis.model.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

}
