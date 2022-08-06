package com.project.tmis.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "employee_account")
@Data
public class EmployeeAccount implements Serializable {
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "empId")
    Employee emp;

    private double earned;
    private double paid;
}
