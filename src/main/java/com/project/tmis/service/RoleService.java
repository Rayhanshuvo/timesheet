package com.project.tmis.service;


import com.project.tmis.dto.Response;
import com.project.tmis.dto.RoleDto;

public interface RoleService {
    Response save(RoleDto roleDto);

    Response update(Long id, RoleDto roleDto);

    Response getById(Long id);

    Response del(Long id);

    Response getAll();
}
