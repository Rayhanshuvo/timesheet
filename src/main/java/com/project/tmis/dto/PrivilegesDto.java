package com.project.tmis.dto;

import com.project.tmis.entity.BaseEntity;
import lombok.Data;

@Data
public class PrivilegesDto extends BaseEntity {
    private String name;
    private String endPoint;
}
