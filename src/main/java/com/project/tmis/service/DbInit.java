package com.project.tmis.service;

import com.project.tmis.entity.Role;
import com.project.tmis.entity.User;
import com.project.tmis.enums.ActiveStatus;
import com.project.tmis.repository.RoleRepository;
import com.project.tmis.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Configuration
public class DbInit {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public DbInit(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @PostConstruct
    public void saveUser() {
        String userName = "admin";
        String superAdmin = "ROLE_SUPER_ADMIN";
        String userRoleName = "ROLE_USER";
        Role role, userRole;
        User user;
        role = roleRepository.findByName(superAdmin);
        if (role == null) {
            role = new Role();
            role.setName(superAdmin);
            role = roleRepository.save(role);
        }
        userRole = roleRepository.findByName(userRoleName);
        if (userRole == null) {
            userRole = new Role();
            userRole.setName(userRoleName);
            roleRepository.save(userRole);
        }
        user = userRepository.getByUserNameAndActiveStatusTrue(ActiveStatus.ACTIVE.getValue(), userName);
        if (user == null) {
            user = new User();
            user.setUserName(userName);
            user.setPassword(passwordEncoder.encode("Admin123"));
            user.setEmail("rayhanshuvo@gmail.com");
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
       /* providerInfoService.importProviderDataViaApi();*/
    }
}
