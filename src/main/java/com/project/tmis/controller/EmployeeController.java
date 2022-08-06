package com.project.tmis.controller;

import com.project.tmis.dto.EmployeeDto;
import com.project.tmis.model.Employee;
import com.project.tmis.model.FullTimeEmployee;
import com.project.tmis.model.PartTimeEmployee;
import com.project.tmis.repository.EmployeeRepository;
import com.project.tmis.repository.FullTimeEmployeeRepository;
import com.project.tmis.repository.PartTimeEmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    PartTimeEmployeeRepository partTimeEmployeeRepository;

    @Autowired
    FullTimeEmployeeRepository fullTimeEmployeeRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    private void saveAnEmployee(EmployeeDto employeeDto) throws Exception{
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if(employeeDto.getIsPartTimer() == "true"){
            PartTimeEmployee partTimeEmployee = new PartTimeEmployee();
            BeanUtils.copyProperties(employeeDto, partTimeEmployee);
            partTimeEmployee.setPartTimer(true);
            partTimeEmployee.setDob(employeeDto.getDob() != null ? dateFormat.parse(employeeDto.getDob()): null);
            partTimeEmployeeRepository.save(partTimeEmployee);
        }
        else{
            FullTimeEmployee fullTimeEmployee = new FullTimeEmployee();
            BeanUtils.copyProperties(employeeDto, fullTimeEmployee);
            fullTimeEmployee.setPartTimer(false);
            fullTimeEmployee.setDob(employeeDto.getDob() != null ? dateFormat.parse(employeeDto.getDob()): null);
            fullTimeEmployeeRepository.save(fullTimeEmployee);
        }
    }

    // save employee-only for admin user
    @PostMapping("/employee")
    public void saveEmployee(@RequestBody EmployeeDto employeeDto) throws Exception{
        saveAnEmployee(employeeDto);
    }
    // get employee by id -only for admin user
    @GetMapping("/employee")
    public Employee getEmployee(@RequestParam("id") int id) throws Exception{
        return employeeRepository.findById(id).get();
    }
    // get all employees -only for employee user- salary will not fetch
    @GetMapping("/employees")
    public Iterable<Employee> getEmployees() throws Exception{
        return employeeRepository.findAll();
    }

    //Fetch all employees(fullTime+partTime) for admin- salary will be visible here
    @GetMapping("/all-employees")
    public List<EmployeeDto> getAllEmployees() throws Exception{
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Iterable<FullTimeEmployee> fullTimeEmployees = fullTimeEmployeeRepository.findAll();
        Iterable<PartTimeEmployee> partTimeEmployees = partTimeEmployeeRepository.findAll();
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        fullTimeEmployees.forEach( x -> {
            EmployeeDto emp = new EmployeeDto();
            BeanUtils.copyProperties(x, emp);
            emp.setDob(dateFormat.format(x.getDob()));
            emp.setIsPartTimer(x.isPartTimer()? "true" : "false");
            employeeDtos.add(emp);
        });

        partTimeEmployees.forEach( x -> {
            EmployeeDto emp = new EmployeeDto();
            BeanUtils.copyProperties(x, emp);
            emp.setDob(dateFormat.format(x.getDob()));
            emp.setIsPartTimer(x.isPartTimer()? "true" : "false");
            employeeDtos.add(emp);
        });

        return employeeDtos;
    }
// salary will not be visible here , this controller can be used by employee if action needed
    @PostMapping("/employees")
    public void updateAllEmployees(@RequestBody List<EmployeeDto> employeeDtos) throws Exception{
        employeeDtos.forEach(x -> {
            try {
                saveAnEmployee(x);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
