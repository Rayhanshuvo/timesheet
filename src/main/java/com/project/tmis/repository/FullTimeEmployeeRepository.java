package com.project.tmis.repository;

import com.project.tmis.model.FullTimeEmployee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FullTimeEmployeeRepository extends CrudRepository<FullTimeEmployee, Integer> {

}
