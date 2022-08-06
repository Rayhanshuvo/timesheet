package com.project.tmis.controller;

import com.project.tmis.annotations.ApiController;
import com.project.tmis.dto.Response;
import com.project.tmis.dto.UserDto;
import com.project.tmis.repository.RoleRepository;
import com.project.tmis.service.UserService;
import com.project.tmis.util.UrlConstraint;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@ApiController
@RequestMapping(value = UrlConstraint.UserManagement.ROOT)
public class UserController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @PostMapping
    public Response saveUser(@RequestBody UserDto userDto) {
        return userService.save(userDto);
    }
}
