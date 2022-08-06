package com.project.tmis.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BaseDto {
    private Long id;
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
    private Integer activeStatus;
}
