package com.project.tmis.entity;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class Privileges extends BaseEntity{
    private String name;
    private String endPoint;
}
