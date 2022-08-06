package com.project.tmis.repository;

import com.project.tmis.model.PartTimeEmployee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartTimeEmployeeRepository extends CrudRepository<PartTimeEmployee, Integer> {

}
