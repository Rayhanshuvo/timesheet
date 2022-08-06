package com.project.tmis.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RoleDto extends BaseDto {
    @NotEmpty(message = "Name is mandatory")
    private String name;
}
