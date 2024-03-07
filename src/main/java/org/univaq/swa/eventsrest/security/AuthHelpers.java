package org.univaq.swa.eventsrest.security;

import jakarta.ws.rs.core.UriInfo;

/**
 *
 * Una classe di utilità di supporto all'autenticazione 
 * qui usiamo JWT per tutte le operazioni
 *
 */
public class AuthHelpers {
    
    private static AuthHelpers instance = null;
    private final JWTHelpers jwt;
    
    public AuthHelpers() {
        jwt = JWTHelpers.getInstance();
    }
    
    public boolean authenticateUser(String username, String password) {
        return true;
    }
    
    public String issueToken(UriInfo context, String username) {
        return jwt.issueToken(context, username);
    }
    
    public void revokeToken(String token) {
        jwt.revokeToken(token);
    }
    
    public String validateToken(String token) {
        return jwt.validateToken(token);
    }
    
    public static AuthHelpers getInstance() {
        if (instance == null) {
            instance = new AuthHelpers();
        }
        return instance;
    }
    
}
