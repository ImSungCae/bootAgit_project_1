package com.bootagit_project_1.jwt;

import com.bootagit_project_1.config.security.MyUserDetails;
import com.bootagit_project_1.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key; // JWT 서명을 위한 시크릿
    private final long accessTokenExpTime; // AccessToken의 만료시간
    private final long refreshTokenExpTime; // RefreshToken의 만료시간

    // application.properties에서 secret.key 값을 가져와서 key에 저장
    public JwtTokenProvider(@Value("${jwt.secret.key}") String secretKey,
                            @Value("${jwt.expiration_time.accessTokenExpTime}") long accessTokenTime,
                            @Value("${jwt.expiration_time.refreshTokenExpTime}") long refreshTokenTime){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenTime;
        this.refreshTokenExpTime = refreshTokenTime;
    }




    // JWT 생성
    private String createToken(User user, String authorities , long expireTime){
        // JWT Claims에 사용자 정보와 권한 저장
        Claims claims = Jwts.claims();
        claims.put("username",user.getUsername());
        claims.put("email",user.getEmail());
        claims.put("auth",authorities);

        // 만료 시간 설정
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenVaildity = now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenVaildity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 사용자 아이디 추출
    public Long getUserId(String token){
        return parseClaims(token).get("username",Long.class);
    }

    // User 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
    public JwtToken generateToken(Authentication authentication){
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        // 권한 가져오기
        String authorities = myUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
//                .map(auth -> auth.startsWith("ROLE_") ? auth : "ROLE_" + auth)
                .collect(Collectors.joining(","));

        User user = myUserDetails.getUser();

        // Access Token 생성
        String accessToken = createToken(user,authorities,accessTokenExpTime);

        // Refresh Token 생성
        String refreshToken = createToken(user,authorities,refreshTokenExpTime);

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }




    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken){
        // JWT 토큰 복호화
        Claims claims = parseClaims(accessToken);
        if(claims.get("auth") == null){
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어 Authentication return
        // UserDetails interface, User: UserDetails 를 구현한 class
        UserDetails principal = MyUserDetails.create((String)claims.get("username"),null,authorities);
        log.info("Authentication 객체 생성 : {}", principal);
        return new UsernamePasswordAuthenticationToken(principal,"",authorities);

    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (SecurityException | MalformedJwtException e){
            log.info("Invalid JWT token",e);
        }catch (ExpiredJwtException e){
            log.info("Expired JWT token",e);
        }catch (UnsupportedJwtException e){
            log.info("Unsupported JWT token",e);
        }catch (IllegalArgumentException e ){
            log.info("JWT claims string is empty",e);
        }
        return false;
    }

    // accessToken
    private Claims parseClaims(String accessToken){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

}
