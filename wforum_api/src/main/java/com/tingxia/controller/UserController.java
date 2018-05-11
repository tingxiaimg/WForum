package com.tingxia.controller;

import com.github.pagehelper.PageInfo;
import com.tingxia.pojo.Career;
import com.tingxia.pojo.School;
import com.tingxia.pojo.User;
import com.tingxia.service.UserService;
import com.tingxia.util.RESTfulAPIUtil;
import com.tingxia.util.StatusCode;
import com.tingxia.util.UserUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 用户
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    private UserUtil userUtil;
    //登陆
    @RequestMapping(value = "/signIn",method = {RequestMethod.POST})
    public Map signIn(HttpServletRequest req,
                      HttpServletResponse rsp,
                      @RequestParam(required = false) String loginType,
                      @RequestParam String signInInput,
                      @RequestParam String password){
        RESTfulAPIUtil res = new RESTfulAPIUtil();
        Integer id = userUtil.getUserId(req);
        if(id > -1){
            userUtil.deleteUser(id);
        }
        User user = null;
        if("email".equals(loginType)){
            user=userService.signInWithEmail(signInInput,password);
        }else if("name".equals(loginType)){
            user=userService.signInWithName(signInInput,password);
        }else{
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("参数有误！");
            return res.getAPI();
        }
        if(user != null){
            if(user.getStatus().equals(1)) {
                if (userUtil.getUser(user.getId()) != null) {
                    userUtil.deleteUser(user.getId());
                }
                String token = userUtil.saveUser(user);
                if (token != null && !"".equals(token)) {
                    rsp.setHeader("token", token);
                    Cookie cookie = new Cookie("token", token);
                    cookie.setMaxAge(60 * 60);
                    cookie.setPath("/");
                    rsp.addCookie(cookie);
                    res.putData("user", user);
                    res.putData("token",token);
                    res.putData("token_maxAge",60*60);
                    res.putData("token_path","/");
                } else {
                    res.setStatusCode(StatusCode.ERROR);
                    res.setMassage("签证失败！");
                    return res.getAPI();
                }
            }else{
                res.setStatusCode(StatusCode.ERROR);
                res.setMassage("用户被禁止使用！");
            }
        }else{
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("密码或账号错误！");
        }
        return res.getAPI();
    }

    //注册
    @RequestMapping(value = "/signUp",method = {RequestMethod.POST})
    public Map signUp(HttpServletRequest req,String name,String password,String email){
        RESTfulAPIUtil res = new RESTfulAPIUtil();
        Integer id = userUtil.getUserId(req);
        if(id > -1){
            res.setStatusCode(StatusCode.LOGGED);
            res.setMassage("已登陆，请先注销！");
            return res.getAPI();
        }
        userService.addUser(name.trim(),password.trim(),email,res);
        return res.getAPI();
    }

    //登出
    @RequestMapping(value = "/signOut",method = {RequestMethod.POST})
    public Map signOut(HttpServletRequest req){
        RESTfulAPIUtil res = new RESTfulAPIUtil();
        Integer id = userUtil.getUserId(req);
        if( id > -1 ){
            userUtil.deleteUser(id);
        }
        res.setStatusCode(StatusCode.OK);
        res.setMassage("注销成功");
        return res.getAPI();
    }

    //获取当前用户信息
    @RequestMapping(value = "/info",method = {RequestMethod.GET})
    public Map secretInfo(HttpServletRequest req){
        RESTfulAPIUtil res = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId != -1){
            User user = userService.getUser(userId);
            res.putData("user",user);
        }else{
            res.putData("user",null);
        }
        return res.getAPI();
    }

    //获取积分前{num}几用户
    @RequestMapping(value = "/score/top{num}",method = {RequestMethod.GET})
    public Map scoreTop(@PathVariable(required = false) Integer num){
        if(num == null || num<=0){
            num = 5;
        }
        RESTfulAPIUtil res = new RESTfulAPIUtil();
        List<User> users = userService.getScoreTop(num);
        res.putData("users",users);
        return res.getAPI();
    }

    //返回用户详情信息(教育经历、职业经历等)，包含一些私密字段，需要登录
    @RequestMapping("/info/detail")
    public Map infoDetail(HttpServletRequest req){
        RESTfulAPIUtil res = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId ==-1){
            res.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            res.setMassage("登陆超时或未登录！");
        }else{
            res.putData("user",userService.getUserDetailInfo(userId));
        }
        return res.getAPI();
    }

    //获取用户公开信息
    @RequestMapping("/info/public/{id}")
    public Map publicInfo(@PathVariable Integer id){
        RESTfulAPIUtil res = new RESTfulAPIUtil();
        res.putData("user",userService.getUser(id));
        return res.getAPI();
    }

    //添加工作经历
    @RequestMapping(value = "/career/add", method = {RequestMethod.POST})
    public Map addCareer(HttpServletRequest req,String company,String title){
        RESTfulAPIUtil res = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId ==-1){
            res.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            res.setMassage("登陆超时或未登录！");
            return res.getAPI();
        }
        if(company == null || "".equals(company)){
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("公司或组织名不能为空！");
            return res.getAPI();
        }
        if(title == null || "".equals(title)){
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("职位不能为空！");
            return res.getAPI();
        }
        Career career = userService.addCategory(userId,company,title);
        if(career == null){
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("添加失败！");
        }else{
            res.putData("career",career);
        }
        return res.getAPI();
    }

    //添加教育经历
    @RequestMapping(value = "/school/add",method = {RequestMethod.POST})
    public Map addSchool(HttpServletRequest req,String name,String speciality){
        RESTfulAPIUtil res = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId ==-1){
            res.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            res.setMassage("登陆超时或未登录！");
            return res.getAPI();
        }
        if(name == null || "".equals(name)){
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("学校或机构名不能为空！");
            return res.getAPI();
        }
        if(speciality == null || "".equals(speciality)){
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("专业不能为空！");
            return res.getAPI();
        }
        School school = userService.addSchool(userId,name,speciality);
        if(school == null){
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("添加失败！");
        }else{
            res.putData("school",school);
        }
        return res.getAPI();
    }

    //更新信息
    @RequestMapping(value= "/update/{type}",method = {RequestMethod.PUT})
    public Map updateInfo(HttpServletRequest req,@PathVariable String type,String source){
        RESTfulAPIUtil res = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId ==-1){
            res.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            res.setMassage("登陆超时或未登录！");
            return res.getAPI();
        }
        if(source == null || "".equals(source)){
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("参数不合法！");
            return res.getAPI();
        }
        if(!userService.updateInfo(userId,type,source)){
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("更新错误！");
            return res.getAPI();
        }
        if("sex".equals(type)){
            res.putData(type, Integer.parseInt(source));
        }else {
            res.putData(type, source);
        }
        return res.getAPI();
    }

    //删除工作经历
    @RequestMapping(value = "career/delete/{id}",method = {RequestMethod.DELETE})
    public Map deleteCareer(HttpServletRequest req,@PathVariable Integer id){
        RESTfulAPIUtil res = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId ==-1){
            res.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            res.setMassage("登陆超时或未登录！");
            return res.getAPI();
        }
        if(!userService.deleteCareer(id,userId)){
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("执行失败！");
        }else{
            res.putData("id",id);
        }
        return res.getAPI();
    }

    //删除教育经历
    @RequestMapping(value = "/school/delete/{id}",method = {RequestMethod.DELETE})
    public Map deleteSchool(HttpServletRequest req,@PathVariable Integer id){
        RESTfulAPIUtil res = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId ==-1){
            res.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            res.setMassage("登陆超时或未登录！");
            return res.getAPI();
        }
        if(!userService.deleteSchool(id,userId)){
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("执行失败！");
        }else{
            res.putData("id",id);
        }
        return res.getAPI();
    }

// ---------------------管理员接口---------------------------
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public Map getUsers(HttpServletRequest req,
                        @RequestParam(value = "pageSize",defaultValue = "50") Integer pageSize,
                        @RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
                        @RequestParam(value = "role",required = false) Integer role,
                        @RequestParam(value = "status",required = false) Integer status,
                        @RequestParam(value = "startAt",required = false) Long startAt,
                        @RequestParam(value = "endAt",required = false) Long endAt){
        RESTfulAPIUtil res = new RESTfulAPIUtil();
        Map r = userUtil.adminUtil(req,res);
        if(r != null){
            return r;
        }
        PageInfo<User> users = new PageInfo<>(userService.getUsers(pageSize,pageNo,role,status,startAt,endAt));
        res.putData("users",users.getList());
        res.putData("total",users.getTotal());
        res.putData("pageNo",users.getPageNum());
        res.putData("pageSize",users.getPageSize());
        return res.getAPI();
    }

}
