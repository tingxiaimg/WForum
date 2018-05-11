package com.tingxia.service;

import com.github.pagehelper.PageHelper;
import com.tingxia.dao.CareerMapper;
import com.tingxia.dao.SchoolMapper;
import com.tingxia.dao.UserMapper;
import com.tingxia.pojo.Career;
import com.tingxia.pojo.Category;
import com.tingxia.pojo.School;
import com.tingxia.util.RESTfulAPIUtil;
import com.tingxia.util.StatusCode;
import com.tingxia.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CareerMapper careerMapper;
    @Autowired
    private SchoolMapper schoolMapper;

    private final String[] types = {"signature","avatarUrl","coverUrl","location","introduce"};

    // 获取排名用户
    public List<User> getScoreTop(Integer num){
        return getUserList(1,num,"score desc,created_at");
    }

    public User getUserDetailInfo(Integer userId){
        return userMapper.selectUserDetailInfo(userId);
    }

    public User getUser(Integer id){
        return userMapper.selectUserPublicInfo(id);
    }

//--------------------------------登录注册--------------------------------
    private boolean checkUserEmail(String email){
        return userMapper.selectByEmail(email) != null;
    }

    private boolean checkUserName(String name){
        return userMapper.selectByName(name) != null;
    }

    @Transactional
    public void addUser(String name,String password,String email,RESTfulAPIUtil res) {
        //检查邮箱和用户名是否存在
        if(checkUserEmail(email)){
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("邮箱已注册！");
            return;
        }
        if(checkUserName(name)){
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("昵称已存在！");
            return;
        }
        // 检测邮箱和用户名格式
        if(name.length()<4 || name.length() > 10){
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("用户名长度不合适！");
            return;
        }else{
            Boolean flag = false;
            try{
                String check = "^[a-zA-Z]+$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(name);
                flag = matcher.matches();
            }catch(Exception e){
                flag = false;
            }
            if(!flag){
                res.setStatusCode(StatusCode.ERROR);
                res.setMassage("用户名只能为字母！");
                return;
            }
        }
        if(!checkEmail(email)){
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("邮箱不合法！");
            return;
        }
        //检查密码
        if(password.length()<6 || password.length() > 20){
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("密码长度不合适！");
            return;
        }else{
            Boolean flag = false;
            try{
                String check = "^[a-z0-9A-Z]+$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(password);
                flag = matcher.matches();
            }catch(Exception e){
                flag = false;
            }
            if(!flag){
                res.setStatusCode(StatusCode.ERROR);
                res.setMassage("密码只能为数字或字母！");
                return;
            }
        }

        //记录
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPass(transformPassword(password));
        user.setRole(1);
        user.setScore(0);
        user.setStatus(1);
        user.setAvatarUrl("/files/user/avatar/png/0");
        user.setSex(0);
        user.setArticleCount(0);
        user.setCollectCount(0);
        user.setCommentCount(0);

        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        if(userMapper.insert(user) == 0){
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("数据库执行失败！");
        }

    }

    public User signInWithEmail(String email,String password){
        if(checkEmail(email)){
            User user = userMapper.selectByEmail(email);
            if(user != null && user.getPass().equals(transformPassword(password))){
                return user;
            }
        }
        return null;
    }

    public User signInWithName(String name,String password){
        User user = userMapper.selectByName(name);
        if(user != null && user.getPass().equals(transformPassword(password))){
            return user;
        }
        return null;
    }

//---------------------------------------------------------------------------


    private List<User> getUserList(Integer pageNo,Integer pageSize,String order){
        PageHelper.startPage(pageNo,pageSize,order);
        return userMapper.selectUserList(null, 1, null, null);
    }
    private List<User> getUserList(Integer pageNo,Integer pageSize){
        return getUserList(pageNo,pageSize,null);
    }

    private boolean checkEmail(String email){
        boolean flag = false;
        try{
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    private String transformPassword(String password){
        MessageDigest md5 = null;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            return password;
        }
        char[] charArray = password.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++){
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public List<User> getUsers(Integer pageSize, Integer pageNo, Integer role, Integer status, Long startAt, Long endAt) {
        Date sd = null,ed = null;
        if(role.equals(0)){
            role = null;
        }
        if(startAt != null) {
            sd = new Date(startAt);
        }
        if(endAt != null) {
            ed = new Date(endAt);
        }
        PageHelper.startPage(pageNo,pageSize);
        return userMapper.selectUserList(role,status,sd,ed);
    }

    @Transactional
    public Boolean updateInfo(Integer userId,String type,String source){
        if("sex".equals(type)){
            try{
                Integer sex = Integer.parseInt(source);
                return userMapper.updateSex(userId,sex.equals(0))>0;
            }catch (NumberFormatException e){
                return false;
            }
        }
        else if(isAllowed(type)){
            return userMapper.updateInfo(userId,type,source)>0;
        }
        return false;
    }

    private Boolean isAllowed(String type){
        for(int i =0 ;i<types.length;i++){
            if(type.equals(types[i])){
                return true;
            }
        }
        return false;
    }

    @Transactional
    public Career addCategory(Integer userId, String company, String title) {
        Career career = new Career();
        career.setCompany(company);
        career.setTitle(title);
        career.setUserId(userId);
        career.setCreatedAt(new Date());
        career.setUpdatedAt(new Date());
        if(careerMapper.insert(career)>0){
            return career;
        }
        return null;
    }
    @Transactional
    public Boolean deleteCareer(Integer id,Integer userId){
        Career career = careerMapper.selectByPrimaryKey(id);
        if(career != null && career.getUserId().equals(userId)){
            return careerMapper.deleteByPrimaryKey(id)>0;
        }
        return false;
    }

    @Transactional
    public School addSchool(Integer userId, String name, String speciality) {
        School school = new School();
        school.setName(name);
        school.setSpeciality(speciality);
        school.setUserId(userId);
        school.setCreatedAt(new Date());
        school.setUpdatedAt(new Date());
        if(schoolMapper.insert(school)>0){
            return school;
        }
        return null;
    }
    @Transactional
    public Boolean deleteSchool(Integer id,Integer userId){
        School school = schoolMapper.selectByPrimaryKey(id);
        if(school != null && school.getUserId().equals(userId)){
            return schoolMapper.deleteByPrimaryKey(id)>0;
        }
        return false;
    }
}
