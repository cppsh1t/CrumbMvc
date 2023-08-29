package com.crumb.mvc.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.crumb.mvc.security.UserDetail;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    //Jwt秘钥
    private static String keyCode = "abcdefghijklmn";

    public static void setKeyCode(String code) {
        keyCode = code;
    }


    //根据用户信息创建Jwt令牌
    public static String createJwt(UserDetail user){
        Algorithm algorithm = Algorithm.HMAC256(keyCode);
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.HOUR, 24 * 7);
        return JWT.create()
                .withClaim("username", user.getUsername())  //配置JWT自定义信息
                .withClaim("role", user.getRole())
                .withExpiresAt(calendar.getTime())  //设置过期时间
                .withIssuedAt(now)    //设置创建创建时间
                .sign(algorithm);   //最终签名
    }

    public static String createJwt(UserDetail user, Date expireDate){
        Algorithm algorithm = Algorithm.HMAC256(keyCode);
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        return JWT.create()
                .withClaim("username", user.getUsername())  //配置JWT自定义信息
                .withClaim("role", user.getRole())
                .withExpiresAt(expireDate)  //设置过期时间
                .withIssuedAt(now)    //设置创建创建时间
                .sign(algorithm);   //最终签名
    }

    //根据Jwt验证并解析用户信息
    public static UserDetail resolveJwt(String token){
        Algorithm algorithm = Algorithm.HMAC256(keyCode);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT verify = jwtVerifier.verify(token);  //对JWT令牌进行验证，看看是否被修改
            Map<String, Claim> claims = verify.getClaims();  //获取令牌中内容
            if(new Date().after(claims.get("exp").asDate())) //如果是过期令牌则返回null
                return null;
            else
                //重新组装为UserDetails对象，包括用户名、授权信息等
                return new UserDetail(claims.get("username").asString(), "", claims.get("role").asString());
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}
