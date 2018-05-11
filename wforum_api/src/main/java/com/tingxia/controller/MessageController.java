package com.tingxia.controller;

import com.github.pagehelper.PageInfo;
import com.tingxia.pojo.Message;
import com.tingxia.service.MessageService;
import com.tingxia.util.RESTfulAPIUtil;
import com.tingxia.util.StatusCode;
import com.tingxia.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息
 */
@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserUtil userUtil;

    @RequestMapping(value = "/unread",method = {RequestMethod.GET})
    public Map unRead(HttpServletRequest req){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId ==-1){
            resTfulAPIUtil.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            resTfulAPIUtil.setMassage("登陆超时！");
            return resTfulAPIUtil.getAPI();
        }
        PageInfo<Message> messages = new PageInfo<>(messageService.getMessages(userId,false,1,20));
        resTfulAPIUtil.putData("total",messages.getTotal());
        resTfulAPIUtil.putData("pageNo",messages.getPageNum());
        resTfulAPIUtil.putData("pageSize",messages.getPageSize());
        resTfulAPIUtil.putData("messages",messages.getList());
        return resTfulAPIUtil.getAPI();
    }
    @RequestMapping(value = "/read/{id}",method = {RequestMethod.GET})
    public Map read(HttpServletRequest req,@PathVariable Integer id){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId ==-1){
            resTfulAPIUtil.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            resTfulAPIUtil.setMassage("登陆超时！");
            return resTfulAPIUtil.getAPI();
        }
        if(!messageService.setRead(userId,id)){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("数据更新失败!");
        }
        return resTfulAPIUtil.getAPI();
    }
}
