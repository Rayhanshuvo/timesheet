package com.project.tmis.service.impl;

import com.project.tmis.dto.LoginDto;
import com.project.tmis.dto.LoginResponseDto;
import com.project.tmis.dto.Response;
import com.project.tmis.entity.User;
import com.project.tmis.enums.ActiveStatus;
import com.project.tmis.filter.JwtTokenProvider;
import com.project.tmis.repository.UserRepository;
import com.project.tmis.service.AuthService;
import com.project.tmis.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final MailService mailService;
    @Value("${application.url}")
    private String applicationUrl;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,UserRepository userRepository,MailService mailService,PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository=userRepository;
        this.mailService=mailService;
        this.passwordEncoder= passwordEncoder;
    }

    @Override
    public Response login(LoginDto loginDto, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        if (authentication.isAuthenticated()) {
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setToken(jwtTokenProvider.generateToken(authentication, request));
            loginResponseDto.setUsername(authentication.getName());
            loginResponseDto.setRole(String.valueOf(authentication.getAuthorities()));
            return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "Logged In Success", loginResponseDto);
        }
        return ResponseBuilder.getFailureResponse(HttpStatus.BAD_REQUEST, "Invalid Username or password");
    }

    @Override
    public Response handlePasswordResetRequest(String userName, HttpServletRequest request) {
        User user = userRepository.getByUserNameAndActiveStatusTrue(ActiveStatus.ACTIVE.getValue(), userName);
        if (user == null) {
            return ResponseBuilder.getFailureResponse(HttpStatus.UNAUTHORIZED, "Invalid Username");
        }

        String token = jwtTokenProvider.generateToken(userName, request);

        mailService.sendNonHtmlMail(new String[]{user.getEmail()}, "PASSWORD RESET REQUEST",
                applicationUrl.replaceAll("/$", "") + "/reset-password?token=" + token);
        return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "Check Your Mail Address For Resting Password", null);
    }




    @Override
    public Response resetPassword(String token, String password, HttpServletRequest request) {
        boolean isValid = jwtTokenProvider.isValidateToken(token, request);
        if (!isValid) {
            return ResponseBuilder.getFailureResponse(HttpStatus.BAD_REQUEST, "You are not authorized to do this action!");
        }
        User user = userRepository.getByUserNameAndActiveStatusTrue(ActiveStatus.ACTIVE.getValue(), jwtTokenProvider.extractUsername(token));
        if (user == null) {
            return ResponseBuilder.getFailureResponse(HttpStatus.UNAUTHORIZED, "Invalid Username");
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "Password Reset SuccessFully", null);
    }
}
