package com.galaxy.common.util;

import com.galaxy.common.bean.UserBean;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
public class JwtUtil {

    private final static long TOKEN_timeout = 1000 * 60 * 60 * 5;
    private final static String TOKEN_SECRET = "zhejiushigece@332@";


    /**
     * 生成jwt
     */
    public static String getTokenSecret(String id, String username,String phone,String avatar) {

        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(username) || StringUtils.isEmpty(phone)
        || StringUtils.isEmpty(avatar)){
            return null;
        }

        String token = Jwts.builder().setSubject("test-user")
                .claim("id",id)
                .claim("phone",phone)
                .claim("username",username)
                .claim("avatar",avatar)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +TOKEN_timeout))
                .signWith(SignatureAlgorithm.HS256,TOKEN_SECRET).compact();


        return token;

    }

    public static String getTokenSecret(Integer id, String username) {

        if (id ==null || StringUtils.isEmpty(username)){
            return null;
        }

        String token = Jwts.builder().setSubject("admin-user")
                .claim("id",id)
                .claim("username",username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +TOKEN_timeout))
                .signWith(SignatureAlgorithm.HS256,TOKEN_SECRET).compact();


        return token;

    }

    /**
     *  获取jwt内容
     *
     */
 public static UserBean getJwt(HttpServletRequest request) {

     try {
         String token = request.getHeader("token");
         UserBean userBean = new UserBean();
         Claims claims = Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token).getBody();
         String id = (String) claims.get("id");
         String phone = (String) claims.get("phone");
         String username = (String) claims.get("username");
         String avatar = (String) claims.get("avatar");
         userBean.setId(id);
         userBean.setMobile(phone);
         userBean.setUsername(username);
         userBean.setAvatar(avatar);
         return userBean;
     }catch (Exception e){
         e.printStackTrace();
         log.error("token 解析失败 可能已经过期");
    return null;
     }
 }
    public static UserBean getJwtForAdmin(HttpServletRequest request) {

        try {
            String token = request.getHeader("token");
            UserBean userBean = new UserBean();
            Claims claims = Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token).getBody();
            Integer id = (Integer) claims.get("id");
            String username = (String) claims.get("username");
            userBean.setId(String.valueOf(id));
            userBean.setUsername(username);
            return userBean;
        }catch (Exception e){
            e.printStackTrace();
            log.error("token 解析失败 可能已经过期");
            return null;
        }
    }

    public static UserBean getJwtForAdmin(String token) {

        try {
            UserBean userBean = new UserBean();
            Claims claims = Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token).getBody();
            Integer id = (Integer) claims.get("id");
            String username = (String) claims.get("username");
            userBean.setId(String.valueOf(id));
            userBean.setUsername(username);
            return userBean;
        }catch (Exception e){
            e.printStackTrace();
            log.error("token 解析失败 可能已经过期");
            return null;
        }
    }

    public static UserBean getJwt(String token) {

        try {
            UserBean userBean = new UserBean();
            Claims claims = Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token).getBody();
            String id = (String) claims.get("id");
            String phone = (String) claims.get("phone");
            String username = (String) claims.get("username");
            String avatar = (String) claims.get("avatar");
            userBean.setId(id);
            userBean.setMobile(phone);
            userBean.setUsername(username);
            userBean.setAvatar(avatar);
            return userBean;
        }catch (Exception e){
            e.printStackTrace();
            log.error("token 解析失败 可能已经过期");
            return null;
        }
    }




    /**
     * 检验是否有效
     * @param request
     */

    public static boolean checkToken(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader("token");
            if(org.springframework.util.StringUtils.isEmpty(jwtToken)) return false;
            Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



}
