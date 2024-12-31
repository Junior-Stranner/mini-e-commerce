package br.com.judev.backend.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

   public String generateToken(UserDetails userDetails){
       Map<String, Object> claims = new HashMap<>();
       return createToken(claims , userDetails.getPassword());
   }

  private String createToken(Map<String, Object> claims, String subject){
      return Jwts.builder()
              .setClaims(claims)
              .setSubject(subject)
              .setIssuedAt(new Date(System.currentTimeMillis()))
              .setExpiration(new Date(System.currentTimeMillis()+expiration))
              .signWith(SignatureAlgorithm.HS256, secret)
              .compact();
   }
}