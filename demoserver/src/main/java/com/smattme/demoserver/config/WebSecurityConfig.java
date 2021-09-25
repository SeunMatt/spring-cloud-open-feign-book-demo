package com.smattme.demoserver.config;


import static com.smattme.demoserver.config.Routes.NON_SECURED;
import static com.smattme.demoserver.config.Routes.SECURED;
import static com.smattme.demoserver.config.Routes.Exception.DEFAULT_ERROR;

import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smattme.demoserver.filters.BasicAuthenticationFilter;
import com.smattme.demoserver.filters.SignatureAuthenticationFilter;
import com.smattme.demoserver.filters.TokenAuthenticationFilter;
import com.smattme.demoserver.helpers.GenericResponse;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    private ObjectMapper objectMapper;
    private PasswordEncoder passwordEncoder;
   
    
    @Value("${spring.security.static-api-token}")
    private String staticApiToken;
    
    @Value("${spring.security.static-signature-secret}")
    private String staticSignatureSecret;

    @Autowired
    public WebSecurityConfig(ObjectMapper objectMapper, PasswordEncoder passwordEncoder) {
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

   
        http.authorizeRequests()
                .antMatchers(NON_SECURED + "/**").permitAll()
                .antMatchers(SECURED + "/**").authenticated()
                .and()
                .authorizeRequests().anyRequest().permitAll()
                .and()
                .addFilterBefore(new BasicAuthenticationFilter(authenticationManagerBean()), AnonymousAuthenticationFilter.class)
                .addFilterBefore(new TokenAuthenticationFilter(authenticationManagerBean()), AnonymousAuthenticationFilter.class)
                .addFilterBefore(new SignatureAuthenticationFilter(authenticationManagerBean()), AnonymousAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(((httpServletRequest, httpServletResponse, e) -> {
                    logger.error("Authentication entry point denied: " + e.getMessage(), e);
                    httpServletResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                    httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                    httpServletResponse.getOutputStream().write(objectMapper.writeValueAsBytes(GenericResponse.generic401UnauthorizedRequest("Unauthorized!")));
                }))
                .accessDeniedHandler(((httpServletRequest, httpServletResponse, e) -> {
                    logger.error("Authentication access denied handler : " + e.getMessage(), e);
                    httpServletResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                    httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                    httpServletResponse.getOutputStream().write(objectMapper.writeValueAsBytes(GenericResponse.generic401UnauthorizedRequest("Access Denied!")));
                }))
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	
    	auth.authenticationProvider(new TokenAuthenticationProvider(staticApiToken));
    	
    	auth.authenticationProvider(new SignatureAuthenticationProvider(staticSignatureSecret));
    	
    	auth.authenticationProvider(new BasicAuthenticationProvider(passwordEncoder, (username) -> {
    		if(!StringUtils.equals(username, "user")) throw new UsernameNotFoundException("Username " + username + " not found");
    		return new User("user", passwordEncoder.encode("password"), Collections.emptyList());
    	}));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(NON_SECURED + "/**", DEFAULT_ERROR);
    }


}
