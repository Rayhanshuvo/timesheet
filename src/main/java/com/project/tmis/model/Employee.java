package com.project.tmis.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int empId;
    @Column(nullable = false)
    private String username;
    private String name;
    @Email(regexp = ".+[@].+[\\.].+")
    private String email;
    private Date dob;

    @Column(name = "is_part_timer")
    private boolean isPartTimer;
}
