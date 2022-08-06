package com.project.tmis.dto;

import lombok.Data;

@Data
public class EmployeeMonthlySalaryDto {
    int empId;
    String name;
    int month;
    int year;
    long monthSalary;
}
