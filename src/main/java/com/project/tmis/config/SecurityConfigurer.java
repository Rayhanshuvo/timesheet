package com.project.tmis.config;

import com.project.tmis.filter.JwtAuthenticationFilter;
import com.project.tmis.service.impl.CustomUserDetailsService;
import com.project.tmis.util.UrlConstraint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final MyAuthenticationEntryPoint myAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfigurer(CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder, MyAuthenticationEntryPoint myAuthenticationEntryPoint, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.myAuthenticationEntryPoint = myAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String allPrefix = "/*";
        http.cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(myAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/*timesheet*").permitAll()
                .antMatchers("/*emp-monthly-salary*").permitAll()
                .antMatchers(UrlConstraint.AuthManagement.ROOT + "/**",
                        UrlConstraint.AuthManagement.RESET_PASSWORD
                )
                .permitAll()
                .antMatchers(UrlConstraint.UserManagement.ROOT + "/**").hasAnyRole("SUPER_ADMIN")
                .antMatchers(UrlConstraint.RoleManagement.ROOT + "/**").hasAnyRole("SUPER_ADMIN")
                .antMatchers(HttpMethod.POST,"/employee").hasAnyRole("SUPER_ADMIN")
                .antMatchers(HttpMethod.GET,"/employee").hasAnyRole("SUPER_ADMIN")
                .antMatchers(HttpMethod.GET,"/employees").hasAnyRole("USER")
                .antMatchers(HttpMethod.GET,"/all-employees").hasAnyRole("SUPER_ADMIN")
                .antMatchers(HttpMethod.POST,"/employees").hasAnyRole("USER")
                .antMatchers(HttpMethod.POST,"/timesheet").hasAnyRole("USER","SUPER_ADMIN")
                .antMatchers(HttpMethod.POST,"/timesheets").hasAnyRole("USER","SUPER_ADMIN")
                .antMatchers(HttpMethod.POST,"/approve-timesheet").hasAnyRole("SUPER_ADMIN")
                .antMatchers(HttpMethod.POST,"/approve-timesheets").hasAnyRole("SUPER_ADMIN")
                .antMatchers(HttpMethod.POST,"/all-timesheets").hasAnyRole("SUPER_ADMIN")
                .antMatchers(HttpMethod.POST,"/timesheets-by-date").hasAnyRole("SUPER_ADMIN")
                .antMatchers(HttpMethod.GET,"/timesheets-by-emp").hasAnyRole("SUPER_ADMIN","USER")
                .antMatchers(HttpMethod.GET,"/timesheets-emp-date").hasAnyRole("SUPER_ADMIN","USER")
                .antMatchers(HttpMethod.GET,"/emp-monthly-salary").hasAnyRole("SUPER_ADMIN")

                .anyRequest()
                .authenticated();
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}