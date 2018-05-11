package com.tingxia.redis;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.tingxia.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Repository
public class UserRedisDao {
    // inject the actual template
    private @Autowired
    RedisTemplate redisTemplate;
    private StringRedisTemplate template;

    @Autowired
    public UserRedisDao(StringRedisTemplate template) {
        this.template = template;
    }

    public User getUserById(Integer id) {
        if(redisTemplate.hasKey("user:"+id)){
            return (User)redisTemplate.opsForValue().get("user:"+id);
        }
        return null;
    }
    /**
     *
     * @param user
     * @return token
     */
    public String addUserToken(User user){
        String secret = getSecret();
        String token="";
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Calendar nowTime= Calendar.getInstance();
            nowTime.add(Calendar.MINUTE,60);
            token = JWT.create()
                    .withClaim("uid",user.getId())
                    .withIssuer("tingxia")
                    .withExpiresAt(nowTime.getTime())
                    .sign(algorithm);
        } catch (UnsupportedEncodingException | JWTCreationException exception){
            System.out.println("token error");
            return "";
        }
        addUser(user);
        redisTemplate.opsForValue().set("secret:"+user.getId(), secret,65*60, TimeUnit.SECONDS);
        return token;
    }


    public void addUser(User user) {
        redisTemplate.opsForValue().set("user:"+user.getId(), user,65*60, TimeUnit.SECONDS);
    }
    public void removeUser(Integer id){
        if(redisTemplate.hasKey("user:"+id)){
            redisTemplate.delete("user:"+id);
        }
        removeTokenSecret(id);
    }
    public String getUserTokenSecret(Integer id) {
        if(redisTemplate.hasKey("secret:"+id)){
            ValueOperations<String,String> ho = redisTemplate.opsForValue();
            return ho.get("secret:"+id);
        }
        return "";
    }

    public void removeTokenSecret(Integer id){
        if(redisTemplate.hasKey("secret:"+id)){
            redisTemplate.delete("secret:"+id);
        }

    }

    private String getSecret(){
        return getSecret(8);
    }
    private String getSecret(final int length){
        //产生随机数
        Random random=new Random();
        StringBuffer stringBuffer=new StringBuffer();
        //循环length次
        for(int i=0; i<length; i++){
            //产生0-2个随机数，既与a-z，A-Z，0-9三种可能
            int number=random.nextInt(3);
            long result=0;
            switch(number){
                //如果number产生的是数字0；
                case 0:
                    //产生A-Z的ASCII码
                    result=Math.round(Math.random()*25+65);
                    //将ASCII码转换成字符
                    stringBuffer.append(String.valueOf((char)result));
                    break;
                case 1:
                    //产生a-z的ASCII码
                    result=Math.round(Math.random()*25+97);
                    stringBuffer.append(String.valueOf((char)result));
                    break;
                case 2:
                    //产生0-9的数字
                    stringBuffer.append(String.valueOf
                            (new Random().nextInt(10)));
                    break;
            }
        }
        return stringBuffer.toString();
    }

}