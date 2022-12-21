package com.demo.security.filter;

import com.demo.exception.KaptchaNotMatchException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.util.ObjectUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String redisParam = "demo:verifyCode:uuid:";

    public static final String FORM_KAPTCHA_KEY = "kaptcha";

    public static final String FORM_UUID_KEY = "uuid";
    private String kaptcha=FORM_KAPTCHA_KEY;

    private String uuid = FORM_UUID_KEY;

    public String getuuidParamter() {
        return uuid;
    }

    public void setKaptchaParamter(String kaptcha) {
        this.kaptcha = kaptcha;
    }

    public void setuuidParamter(String uuid) {
        this.uuid = uuid;
    }

    public String getKaptchaParamter() {
        return kaptcha;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            try {
                Map<String, String> userinfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                String username = userinfo.get(getUsernameParameter());
                String password = userinfo.get(getPasswordParameter());
                String kaptcha = userinfo.get(getKaptchaParamter());
                String uuid = userinfo.get(getuuidParamter());
                String kaptchaRedis = stringRedisTemplate.opsForValue().get(redisParam + uuid);
                if(!ObjectUtils.isEmpty(kaptchaRedis)&&!ObjectUtils.isEmpty(kaptcha)&&kaptchaRedis.equals(kaptcha)){
                    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
                    setDetails(request, authRequest);
                    return this.getAuthenticationManager().authenticate(authRequest);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new KaptchaNotMatchException("验证码错误或过期");
    }
}
