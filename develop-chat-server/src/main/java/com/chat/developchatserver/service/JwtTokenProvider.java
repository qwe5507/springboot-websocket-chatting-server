package com.chat.developchatserver.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private long tokenValidMilisecond = 1000L * 60 * 60; // 1시간만 토큰 유효

    /**
     * 이름으로 Jwt Token을 생성한다.
     */
    // JWT 토큰 생성
    public String generateToken(String name) {
        Date now = new Date();
        String jwtToken = JWT.create()
                .withSubject("bank") //토큰 제목
                .withExpiresAt(new Date(now.getTime() + tokenValidMilisecond)) // 토큰 만료시간 (현재 시간 + 7일 뒤)
                .withClaim("id", name)
                .sign(Algorithm.HMAC256(secretKey));

        return jwtToken;
    }

    /**
     * Jwt Token을 복호화 하여 이름을 얻는다.
     */
    public String getUserNameFromJwt(String jwt) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(jwt);
        String name = decodedJWT.getClaim("id").asString();
        return name;
    }

    /**
     * Jwt Token의 유효성을 체크한다.
     */
    public boolean validateToken(String jwt) {
        return JWT.require(Algorithm.HMAC256(secretKey)).build().verify(jwt) != null;
    }

}
