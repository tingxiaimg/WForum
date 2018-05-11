package com.tingxia.controller;

import com.github.pagehelper.PageInfo;
import com.tingxia.pojo.Collect;
import com.tingxia.pojo.Folder;
import com.tingxia.service.CollectService;
import com.tingxia.util.RESTfulAPIUtil;
import com.tingxia.util.StatusCode;
import com.tingxia.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 收藏
 */
@RestController
@RequestMapping("/collects")
public class CollectController {
    @Autowired
    private CollectService collectService;
    @Autowired
    private UserUtil userUtil;
    //获取收藏列表
    @RequestMapping("/list")
    public Map collectsLis(Integer folderId,Integer userId,Integer pageNo,Integer pageSize){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        resTfulAPIUtil.putData("folder",collectService.getFolder(folderId,userId));
        PageInfo<Collect> collects = new PageInfo<>(collectService.getFolderCollects(folderId,userId,pageNo,pageSize));
        resTfulAPIUtil.putData("collects",collects.getList());
        resTfulAPIUtil.putData("total",collects.getTotal());
        resTfulAPIUtil.putData("pageNo",collects.getPageNum());
        resTfulAPIUtil.putData("pageSize",collects.getPageSize());
        return resTfulAPIUtil.getAPI();
    }
    //获取收藏夹以及资源
    @RequestMapping("/folders/withsource")
    public Map foldersWithSource(HttpServletRequest req){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId == -1){
            resTfulAPIUtil.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            resTfulAPIUtil.setMassage("登陆超时！");
            return resTfulAPIUtil.getAPI();
        }
        resTfulAPIUtil.putData("folders",collectService.getFoldersWithCollect(userId));
        return resTfulAPIUtil.getAPI();
    }
    // 获取用户收藏夹列表
    @RequestMapping("/user/{id}/folders")
    public Map folders(@PathVariable Integer id){

        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        if(id == null){
            resTfulAPIUtil.putData("folders",null);
            return resTfulAPIUtil.getAPI();
        }
        resTfulAPIUtil.putData("folders",collectService.getUserFolder(id));
        return resTfulAPIUtil.getAPI();
    }
    @RequestMapping("/create")
    public Map createCollect(HttpServletRequest req,Integer articleId,Integer folderId){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId == -1){
            resTfulAPIUtil.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            resTfulAPIUtil.setMassage("登陆超时！");
            return resTfulAPIUtil.getAPI();
        }
        Collect collect = collectService.addCollect(articleId,folderId,userId);
        if(collect == null){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("已被收藏！");
        }
        resTfulAPIUtil.putData("collect",collect);
        return resTfulAPIUtil.getAPI();
    }
    @RequestMapping(value = "/folder/create",method = {RequestMethod.POST})
    public Map createFolder(HttpServletRequest req,String name,Integer parentId){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId == -1){
            resTfulAPIUtil.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            resTfulAPIUtil.setMassage("登陆超时！");
            return resTfulAPIUtil.getAPI();
        }
        Folder folder = collectService.addFolder(name,parentId,userId);
        if(folder == null){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("未知错误！");
        }
        resTfulAPIUtil.putData("folder",folder);
        return resTfulAPIUtil.getAPI();
    }
    @RequestMapping(value = "/delete/{id}",method = {RequestMethod.DELETE})
    public Map deleteCollect(HttpServletRequest req,@PathVariable Integer id){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId == -1){
            resTfulAPIUtil.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            resTfulAPIUtil.setMassage("登陆超时！");
            return resTfulAPIUtil.getAPI();
        }
        if(collectService.delete(id,userId).equals(0)){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("未知错误！");
        }
        return resTfulAPIUtil.getAPI();
    }
}
