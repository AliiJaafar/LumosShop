package com.dgpad.security.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class ClientOAuth2User implements OAuth2User {

    private final String clientName;
    private final OAuth2User oAuth2User;

    public ClientOAuth2User(OAuth2User oAuth2User, String clientName) {
        this.clientName = clientName;
        this.oAuth2User = oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }


    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }
    public String getFullName() {
        return oAuth2User.getAttribute("name");

    }

    public String getEmail() {
        return oAuth2User.getAttribute("email");

    }

    public String getLocal() {
        return oAuth2User.getAttribute("locale");
    }

    public String getClientName() {
        return clientName;
    }
}
