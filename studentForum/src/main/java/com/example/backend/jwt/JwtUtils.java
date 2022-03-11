package com.example.backend.jwt;

import cn.dev33.satoken.action.SaTokenActionDefaultImpl;
import com.example.backend.common.exception.ApiAsserts;
import com.example.backend.common.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * jwt工具类
 */
@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    private final int EXPIRATION = 60 * 60 * 1000;

    /**
     * 生成token字符串
     *
     * @param user_id 用户id
     * @return token字符串
     */
    public String generateJwtToken(String user_id) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")

                .setSubject("pxfj-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))

                .claim("id", user_id)

                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    /**
     * 检验token是否有效
     *
     * @param token 登录用户携带的token
     * @return boolean
     */
    public boolean checkToken(String token) {
        if (StringUtils.isEmpty(token)) return false;
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace("Bearer", ""));
        } catch (Exception e) {
            ApiAsserts.fail(e.getMessage());
        }
        return true;
    }

    /**
     * 从token中取出用户id
     *
     * @param request 前端请求
     * @return String
     */
    public String getUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        if (!checkToken(token)) return "";
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace("Bearer", ""));
        Claims claims = claimsJws.getBody();
        return (String) claims.get("id");
    }
}