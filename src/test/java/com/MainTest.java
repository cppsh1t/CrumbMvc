package com;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class MainTest {


    @Test
    public void test() {
        String jwtKey = "abcdefghijklmn";                 //使用一个JWT秘钥进行加密
        Algorithm algorithm = Algorithm.HMAC256(jwtKey);  //创建HMAC256加密算法对象
        String jwtToken = JWT.create()
                .withClaim("id", 1)   //向令牌中塞入自定义的数据
                .withClaim("name", "lbw")
                .withClaim("role", "nb")
                .withExpiresAt(new Date(2024, Calendar.FEBRUARY, 1))  //JWT令牌的失效时间
                .withIssuedAt(new Date())   //JWT令牌的签发时间
                .sign(algorithm);    //使用上面的加密算法进行加密，完成签名

        String[] split = jwtToken.split("\\.");
        for (int i = 0; i < split.length - 1; i++) {
            String s = split[i];
            byte[] decode = Base64.getDecoder().decode(s);
            System.out.println(new String(decode));
        }
    }
}
