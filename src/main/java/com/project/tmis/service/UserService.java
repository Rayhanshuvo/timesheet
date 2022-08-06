package com.project.tmis.service;


import com.project.tmis.dto.Response;
import com.project.tmis.dto.UserDto;
import com.project.tmis.entity.User;

import java.util.List;

public interface UserService {
    Response save(UserDto userDto);

    User update(User user);

    User getById(Long id);

    User getByName(String name);

    String delete(Long id);

    List<User> list();
}
