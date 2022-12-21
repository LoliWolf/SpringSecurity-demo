package com.demo.security.config;

import com.demo.security.filter.JWTAuthenticationFilter;
import com.demo.security.filter.JWTAuthorizationFilter;
import com.demo.security.handler.*;
import com.demo.security.service.MyUserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//@EnableWebSecurity
public class SecurityConfig {

    private final MyUserDetailServiceImpl userDetailService;

    //构造注入自定义UserDetailService
    @Autowired
    public SecurityConfig(MyUserDetailServiceImpl myUserDetailService) {
        this.userDetailService = myUserDetailService;
    }


    //注入AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager){
        JWTAuthenticationFilter filter = new JWTAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setUsernameParameter("username");
        filter.setPasswordParameter("password");
        filter.setFilterProcessesUrl("/login");
        filter.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());

        return filter;
    }

    @Bean
    public JWTAuthorizationFilter jwtAuthorizationFilter(AuthenticationManager authenticationManager){
        JWTAuthorizationFilter filter = new JWTAuthorizationFilter(authenticationManager);
        return filter;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .antMatchers("/test").hasRole("admin")
                .mvcMatchers("/vc.png").permitAll()
                .mvcMatchers("/t").permitAll()
                .anyRequest().authenticated()
                .and().oauth2Login().successHandler(new OAuth2AuthenticationSuccessHandler(userDetailService)).failureHandler(new MyAuthenticationFailureHandler())
//                .and().oauth2Client().authorizationCodeGrant().authorizationRequestRepository(new CookieOAuth2AuthorizationRequestRepository())
                .and().formLogin()
                .and().exceptionHandling().accessDeniedHandler(new MyAccessDeniedHandler()).authenticationEntryPoint(new MyAuthenticationEntryPoint())
                .and().userDetailsService(userDetailService)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterAt(jwtAuthenticationFilter(http.getSharedObject(AuthenticationManager.class)), UsernamePasswordAuthenticationFilter.class);
        http.addFilter(jwtAuthorizationFilter(http.getSharedObject(AuthenticationManager.class)));

        return http.build();

    }
}
