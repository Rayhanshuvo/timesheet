package com.project.tmis.service;

import com.project.tmis.dto.LoginDto;
import com.project.tmis.dto.Response;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    Response login(LoginDto loginDto, HttpServletRequest request);
    Response handlePasswordResetRequest(String username, HttpServletRequest request);
    Response resetPassword(String token, String password, HttpServletRequest request);
}
