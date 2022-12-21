//package com.demo.security.utils;
//
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//
//@Component
//public class OAuth2InfoUtils implements InitializingBean {
//    @Value("${spring.security.oauth2.client.registration.github.client-id}")
//    private String githubClientID;
//
//    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
//    private String githubClientSecret;
//
//    @Value("${spring.security.oauth2.client.registration.github.redirect-uri}")
//    private String githubRedirectURI;
//
//    @Value("${oauth2.gitee.client-id}")
//    private String giteeClientID;
//
//    @Value("${oauth2.gitee.client-secret}")
//    private String giteeClientSecret;
//
//    @Value("${oauth2.gitee.redirect-uri}")
//    private String giteeRedirectURI;
//
//    public  HashMap<String,String> url;
//
//    public  HashMap<String,String> clientID;
//
//    public  HashMap<String,String> clientSecret;
//
//    public  HashMap<String,String> clientRedirectURL;
//
//    public  HashMap<String,String> getInfoURL;
//
//    public OAuth2InfoUtils() {
//        url = new HashMap<>();
//        clientID = new HashMap<>();
//        clientSecret = new HashMap<>();
//        clientRedirectURL = new HashMap<>();
//        getInfoURL = new HashMap<>();
//    }
//
//    public HashMap<String, String> getUrl() {
//        return url;
//    }
//
//    public HashMap<String, String> getClientID() {
//        return clientID;
//    }
//
//    public HashMap<String, String> getClientSecret() {
//        return clientSecret;
//    }
//
//    public HashMap<String, String> getClientRedirectURL() {
//        return clientRedirectURL;
//    }
//
//    public HashMap<String, String> getGetInfoURL() {
//        return getInfoURL;
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        url.put("github","https://github.com/login/oauth/access_token");
//        clientID.put("github",githubClientID);
//        clientSecret.put("github",githubClientSecret);
//        clientRedirectURL.put("github",githubRedirectURI);
//        getInfoURL.put("github","https://api.github.com/user");
//        url.put("gitee","https://gitee.com/oauth/token");
//        clientID.put("gitee",giteeClientID);
//        clientSecret.put("gitee",giteeClientSecret);
//        clientRedirectURL.put("gitee",giteeRedirectURI);
//        getInfoURL.put("gitee","https://gitee.com/api/v5/user");
//    }
//}
