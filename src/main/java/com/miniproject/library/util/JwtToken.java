package com.miniproject.library.util;

import com.miniproject.library.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
@RequiredArgsConstructor
public class JwtToken {
    private static final String key = "MyKey";
    public static String getToken(User user){
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("username",user.getUsername());
        claims.put("roles",user.getRole().name());
        long nowMilis = System.currentTimeMillis();
        Date now = new Date(nowMilis);
        Date expireDate = new Date(nowMilis+36000000);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public static Claims getAllClaimsFromToken(String jwtToken) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken).getBody();
    }

}
