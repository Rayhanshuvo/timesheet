package com.project.tmis.controller;

import com.project.tmis.annotations.ApiController;
import com.project.tmis.dto.LoginDto;
import com.project.tmis.dto.Response;
import com.project.tmis.dto.UserDto;
import com.project.tmis.service.AuthService;
import com.project.tmis.service.UserService;
import com.project.tmis.util.UrlConstraint;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ApiController
@RequestMapping(UrlConstraint.AuthManagement.ROOT)
public class AuthController {

    private final AuthService authService;

    private final UserService userService;


    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping(UrlConstraint.AuthManagement.LOGIN)
    public Response login(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        return authService.login(loginDto, request);
    }

    @PostMapping(UrlConstraint.AuthManagement.REGISTER)
    public Response registerUser(@RequestBody UserDto userDto) {
        return userService.save(userDto);
    }


    @GetMapping(UrlConstraint.AuthManagement.RESET_PASSWORD)

    public Response requestResetPassword(@RequestParam("userName") String userName, HttpServletRequest request) {
        return authService.handlePasswordResetRequest(userName, request);
    }



    @PutMapping(UrlConstraint.AuthManagement.RESET_PASSWORD)
    public Response resetPassword(@RequestParam("token") String token,
                                  @RequestBody LoginDto password, HttpServletRequest request) {
        return authService.resetPassword(token, password.getPassword(), request);
    }


}
