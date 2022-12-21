package com.demo.security.service;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.Assert;
import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

/**
 * oauth2授权请求存储库
 *
 * @author Loli_Wolf
 * @date 2022/12/06
 */
public class CookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final String DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME = CookieOAuth2AuthorizationRequestRepository.class.getName() + ".AUTHORIZATION_REQUEST";
    private final String cookieAttributeName;

    public CookieOAuth2AuthorizationRequestRepository() {
        this.cookieAttributeName = DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME;
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Assert.notNull(request, "request cannot be null");
        return this.getAuthorizationRequest(request);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");
        if (authorizationRequest == null) {
            this.removeAuthorizationRequest(request, response);
        } else {
            Cookie uid = new Cookie(this.cookieAttributeName, Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(authorizationRequest)));
            uid.setPath("/");
            uid.setHttpOnly(true);
            uid.setMaxAge(3600);
            response.addCookie(uid);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return null;
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");
        OAuth2AuthorizationRequest originalRequest = getAuthorizationRequest(request);
        Assert.notNull(originalRequest,"originalRequest cannot be null");
        return originalRequest;
    }

    private OAuth2AuthorizationRequest getAuthorizationRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(cookieAttributeName)){
                return (OAuth2AuthorizationRequest)SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue()));
            }
        }
        return null;
    }

}
