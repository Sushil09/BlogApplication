package com.springboot.blog.springbootblogrestapi.security;

import com.springboot.blog.springbootblogrestapi.exceptions.BlogApiException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpiration;

    //generate Token

    public String generateToken(Authentication authentication){
        String userName= authentication.getName();
        Date currentDate= new Date();
        Date expiryDate = new Date(currentDate.getTime()+jwtExpiration);

        String token = Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        return token;
    }

    //get userName from token

    public String getUserNameFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // validate JWT token

    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(token).parseClaimsJws(token);
            return true;
        }catch (SignatureException exception){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Invalid JWT signature");
        }catch (MalformedJwtException exception){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Invalid JWT signature");
        }catch (ExpiredJwtException exception){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Invalid JWT signature");
        }catch (UnsupportedJwtException exception){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Invalid JWT signature");
        }catch (IllegalArgumentException exception){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Invalid JWT signature");
        }
    }
}
