package com.demo.security.handler;

import com.demo.security.entity.User;
import com.demo.security.service.MyUserDetailServiceImpl;
import com.demo.security.utils.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final MyUserDetailServiceImpl userDetailsService;

    public OAuth2AuthenticationSuccessHandler(MyUserDetailServiceImpl myUserDetailService) {
        this.userDetailsService = myUserDetailService;
    }

    //拿着第三方登录的用户去找user，找到就返回user的token，没找到就创建新user,设置user角色

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String, Object> result = new HashMap<>();
        result.put("msg", "登录成功");
        result.put("status", 200);
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        User user = userDetailsService.loadUserByGiteeID(oAuth2User.getAttributes().get("id").toString(),oAuth2User.getAttributes().get("login").toString());
        String token = JWTUtils.createToken(user.getUsername(), user.getRoles());
        result.put("token",token);
        response.setContentType("application/json;charset=UTF-8");
        String s = new ObjectMapper().writeValueAsString(result);
        response.getWriter().println(s);
    }
}
