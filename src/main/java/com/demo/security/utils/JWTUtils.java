package com.demo.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.demo.security.entity.Role;

import java.util.*;


public class JWTUtils {
    public static String TOKEN_HEADER = "token";

    //过期时间
    private static int EXPIRITION_DAY =7;

    private static String ROLE = "role";

    private static String SIGN = "#N!&SI#^";

    public static String createToken(String username, List<Role> roles){
        ArrayList<String> rolesList = new ArrayList<>();
        for(Role role:roles){
            rolesList.add(role.getName());
        }
        Calendar instance = Calendar.getInstance();
        //过期时间设为7天
        instance.add(Calendar.DATE,EXPIRITION_DAY);
        String token = JWT.create()
                .withArrayClaim("role",rolesList.toArray(new String[0]))
                .withClaim("username",username)
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SIGN));
        return token;
    }

    public static HashMap<String,Object> decode(String token){
        HashMap<String, Object> map = new HashMap<>();
        DecodedJWT verify;
        try{
            verify = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
        }catch (Exception e){
            e.printStackTrace();
            return map;
        }
        String username = verify.getClaim("username").asString();
        String[] roles = verify.getClaim("role").asArray(String.class);
        map.put("username",username);
        map.put("roles",roles);
        return map;
    }

    public static void setExpiritionDay(int expiritionDay) {
        EXPIRITION_DAY = expiritionDay;
    }

    public static void setRole(String role) {
        JWTUtils.ROLE = role;
    }

    public static void setSign(String sign) {
        JWTUtils.SIGN = sign;
    }
}
