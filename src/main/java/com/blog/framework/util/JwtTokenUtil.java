package com.blog.framework.util;

import com.blog.framework.dto.LoginReqDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
//@RequiredArgsConstructor
public class JwtTokenUtil {
    private final Key key;
    private  long expirationTime;
    private  RedisUtil redisUtil;

    public JwtTokenUtil(
            @Value("${jwt.secret.key}") String secretKey,
            @Value("${jwt.expiration_time}") long accessTokenExpTime, RedisUtil redisUtil
    ) {
        this.redisUtil = redisUtil;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expirationTime = accessTokenExpTime;
    }

    public String createAccessToken(LoginReqDTO reqDTO) {
        return createToken(reqDTO);
    }
    /**
     * JWT 생성
     * @param reqDTO
     * @return JWT String
     */
    private String createToken(LoginReqDTO reqDTO) {
        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now + 1800000);
        String accessToken = Jwts.builder()
                .setSubject(reqDTO.getEmail())
                .claim("email", reqDTO.getEmail())
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return accessToken;
    }

    /**
     * Token에서 User ID 추출
     * @param token
     * @return User ID
     */
    public String getUserId(String token) {
        return parseClaims(token).get("email", String.class);
    }


    /**
     * JWT 검증
     * @param token
     * @return IsValidate
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            if (redisUtil.hasKeyBlackList(token)){
                // TODO 에러 발생시키는 부분 수정
                throw new RuntimeException("Logout Token");
            }
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }


    /**
     * JWT Claims 추출
     * @param accessToken
     * @return JWT Claims
     */
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}
