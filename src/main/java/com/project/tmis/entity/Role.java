package com.project.tmis.entity;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Role extends BaseEntity {
    private String name;
}
