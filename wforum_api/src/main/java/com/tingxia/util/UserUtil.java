package com.tingxia.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tingxia.dao.UserMapper;
import com.tingxia.pojo.User;
import com.tingxia.redis.UserRedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Component
public class UserUtil {

    @Autowired
    UserRedisDao userRedisDao;
    @Autowired
    UserMapper userMapper;

    /**
     * 检测用户是否登陆，返回用户id
     * @param req
     * @return {int} 用户id -1检测异常（未登录或token无效）
     */
    /* public int getUserId(HttpServletRequest req){
        return 187;
    } */
    public int getUserId(HttpServletRequest req){

        String token = req.getHeader("token");
        //头部未获取到token，或token为空
        if("".equals(token)||null == token){
            Cookie[] cookies = req.getCookies();
            if(cookies == null){
                return -1;
            }
            for(int i=0; i<cookies.length;i++){
                if(cookies[i].getName().equals("token")){
                    token = cookies[i].getValue();
                }
            }
        }
        if(null == token || "".equals(token)){
            return -1;
        }
        //验证token
        try {
            DecodedJWT jwt = JWT.decode(token);
            Claim claim = jwt.getClaim("uid");
            if(claim.isNull()){
                return -1;
            }
            Integer uid = claim.asInt();
            if (uid == null){return -1;}
            String secret = userRedisDao.getUserTokenSecret(uid);
            if(secret == null || "".equals(secret)){
                return -1;
            }
            try {
                Algorithm algorithm = Algorithm.HMAC256(secret);
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("tingxia")
                        .build();
                verifier.verify(token);
                return uid;
            } catch (UnsupportedEncodingException | JWTVerificationException exception){
                // exception.printStackTrace();
                return -1;
            } catch (Exception e){
                e.printStackTrace();
                return -1;
            }
        } catch (JWTDecodeException exception){
            // exception.printStackTrace();
            return -1;
        }
    }
    public User getUser(Integer userId){
        return userRedisDao.getUserById(userId);
    }
    public String saveUser(User user){
        return userRedisDao.addUserToken(user);
    }
    public void deleteUser(Integer id){
        userRedisDao.removeUser(id);
    }

    public int getAdminId(HttpServletRequest req){
        Integer userId = getUserId(req);
        if(userId.equals(-1)){
            return -1;
        }else{
            User user = userMapper.selectByPrimaryKey(userId);
            if(user.getRole().equals(2)){
                return userId;
            }else{
                return 0;
            }
        }
    }

    public Map adminUtil(HttpServletRequest req, RESTfulAPIUtil resTfulAPIUtil){
        Integer userId = getAdminId(req);
        if(userId.equals(-1)){
            resTfulAPIUtil.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            resTfulAPIUtil.setMassage("登陆超时！");
            return resTfulAPIUtil.getAPI();
        }else if(userId.equals(0)){
            resTfulAPIUtil.setStatusCode(StatusCode.UNAUTHORIZED);
            resTfulAPIUtil.setMassage("无权限访问！");
            return resTfulAPIUtil.getAPI();
        }
        return null;
    }

}
