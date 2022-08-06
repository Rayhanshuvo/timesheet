package com.project.tmis.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "full_time_employee")
@Data
public class FullTimeEmployee extends Employee{
    private double salary;
}
