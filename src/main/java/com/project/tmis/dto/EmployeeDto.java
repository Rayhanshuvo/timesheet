package com.project.tmis.dto;

import lombok.Data;

@Data
public class EmployeeDto {
    private String username;
    private String name;
    private String email;
    private String dob;
    private String isPartTimer;
    private double salary;
    private double payRate;
    private double payCap;
}
