package com.demo.security.filter;

import com.demo.security.utils.JWTUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //从请求头中获取token
        String token = request.getHeader(JWTUtils.TOKEN_HEADER);
        //没有直接跳过过滤器
        if(ObjectUtils.isEmpty(token)){
            chain.doFilter(request,response);
            return;
        }
        //将token中的用户名和权限用户组放入Authentication对象,在之后实现鉴权
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(token));
        super.doFilterInternal(request, response, chain);
    }

    //解析token获取用户信息
    private UsernamePasswordAuthenticationToken getAuthentication(String token){
        HashMap<String, Object> tokenInfo = JWTUtils.decode(token);
        if(ObjectUtils.isEmpty(tokenInfo)){
            return null;
        }
        String username = (String) tokenInfo.get("username");
        String[] roles = (String[]) tokenInfo.get("roles");
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for(String role:roles){
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return new UsernamePasswordAuthenticationToken(username,null,authorities);

    }
}
