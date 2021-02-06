package com.frontent.app.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            // TODO: create constant or from config file
            char[] storePwd = "secure".toCharArray();
            // TODO: close resource
            InputStream resource = getClass().getResourceAsStream("/frontent.jks");
            keyStore.load(resource, storePwd);
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new IllegalStateException("Exception occurred while loading keystore");
        }
    }

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("frontent", "secure".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new IllegalStateException("Exception occurred while retrieving private ket from keystore");
        }
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("frontent").getPublicKey();
        } catch (KeyStoreException e) {
            throw new IllegalStateException("Exception occurred while retrieving public ket from keystore");
        }
    }

    public boolean validateToken(String jwt) {
        Jwts.parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(jwt);
        return true;
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
