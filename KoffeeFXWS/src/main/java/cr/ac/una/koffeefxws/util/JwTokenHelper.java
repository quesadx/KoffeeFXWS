/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author quesadx
 */
public class JwTokenHelper {
    private static JwTokenHelper jwTokenHelper = null;
    private static final long EXPIRATION_LIMIT = 1;
    private static final long EXPIRATION_RENEWAL_LIMIT = 2;
    private static final String AUTHENTICATION_SCHEME = "Bearer ";
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    private JwTokenHelper(){
        
    }
    
    public static JwTokenHelper getInstance(){
        if(jwTokenHelper == null){
            jwTokenHelper = new JwTokenHelper();
        }
        return jwTokenHelper;
    }
    
    public String generatePrivateKey(String username){
        return AUTHENTICATION_SCHEME + Jwts.builder().
                setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(getExpirationDate(false))
                .claim("rnt", AUTHENTICATION_SCHEME + Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(getExpirationDate(true))
                    .claim("rnw", true)
                    .signWith(key)
                    .compact())
                .signWith(key)
                .compact();
    }
    
    private Date getExpirationDate(boolean isRenewal){
        long currentTimeInMillis = System.currentTimeMillis();
        long expMilliseconds = TimeUnit.MINUTES.toMillis(EXPIRATION_LIMIT);
        if(isRenewal){
            expMilliseconds = TimeUnit.MINUTES.toMillis(EXPIRATION_RENEWAL_LIMIT);
        }
        return new Date(currentTimeInMillis + expMilliseconds);
    }
    
    public Claims claimKey(String privateKey) throws ExpiredJwtException, MalformedJwtException {
        return Jwts.parser().setSigningKey(key)
                .parseClaimsJws(privateKey)
                .getBody();
    }
    
    
}
