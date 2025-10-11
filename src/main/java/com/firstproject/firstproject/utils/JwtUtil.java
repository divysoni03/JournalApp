package com.firstproject.firstproject.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JwtUtil {

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /* creating JWT token */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject) /* defines how this is identified ex., here user is identified with unique username */
                .header().empty().add("typ", "JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 )) /* adding milSec * sec * number of min <- here 5 mins*/
                .signWith(getSigningKey()) /* here in default we are using HMACSHA256 encoding algorithm, where it encodes the content with signingKey/secret key which we provided*/
                .compact();
    }

    private String SECRET_KEY = "53d2440d0949a77ee9a82e188a4e4580"; /*randomly generated from any website*/

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes()); /*converted our string secret key into jwt signing key*/
    }


    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public String extractClaims(String token, String claim) {
        return extractAllClaims(token).get(claim, String.class);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) /* verify the token with signing key */
                .build()
                .parseSignedClaims(token) /* extracting claims from the token */
                .getPayload(); /* getting only data/payload */
    }


    /* here we have already checked the token with signing key so we don't have to compare it again with username,
    * so just check the expiration time */
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
