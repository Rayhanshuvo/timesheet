package com.project.tmis.service.impl;


import com.project.tmis.dto.Response;
import com.project.tmis.dto.UserDto;
import com.project.tmis.entity.User;
import com.project.tmis.enums.ActiveStatus;
import com.project.tmis.repository.RoleRepository;
import com.project.tmis.repository.UserRepository;
import com.project.tmis.service.UserService;
import com.project.tmis.util.ResponseBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final String root = "User";
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
    }

    @Override
    public Response save(UserDto userDto) {
        User user, userInfo;
        userInfo = userRepository.getByUserNameAndActiveStatusTrue(ActiveStatus.ACTIVE.getValue(),userDto.getUserName());
        if (userInfo != null){
            return ResponseBuilder.getSuccessResponse(HttpStatus.IM_USED, root + "name already Used", null);
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user = modelMapper.map(userDto, User.class);
        user = userRepository.save(user);
        if (user.getCreatedBy() == null) {
            user.setRoles(roleRepository.getByName("ROLE_USER"));
            userRepository.save(user);
        }
        if (user != null) {
            return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, root + " Has been Created", null);
        }
        return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getById(Long id) {
        return userRepository.getByIdAndActiveStatusTrue(ActiveStatus.ACTIVE.getValue(), id);
    }

    @Override
    public User getByName(String name) {
        return userRepository.getByUserNameAndActiveStatusTrue(ActiveStatus.ACTIVE.getValue(), name);
    }

    @Override
    public String delete(Long id) {
        User user = userRepository.getByIdAndActiveStatusTrue(ActiveStatus.ACTIVE.getValue(), id);
        user.setActiveStatus(ActiveStatus.DELETE.getValue());
        return null;
    }

    @Override
    public List<User> list() {
        return userRepository.list(ActiveStatus.ACTIVE.getValue());
    }
}
