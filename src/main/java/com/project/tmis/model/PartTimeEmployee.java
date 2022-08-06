package com.project.tmis.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "part_time_employee")
@Data
public class PartTimeEmployee extends Employee{

    @Column(name = "pay_rate")
    private double payRate;
    @Column(name = "pay_cap")
    private double payCap;
}
