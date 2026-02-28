package com.url_shortener_sb.security.jwt;

public class JwtAuthenticationResponse {
    private String token;

    public JwtAuthenticationResponse() {

    }

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken() {
        this.token = token;
    }
}
