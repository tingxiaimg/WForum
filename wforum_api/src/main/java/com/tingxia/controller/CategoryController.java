package com.tingxia.controller;

import com.tingxia.pojo.Category;
import com.tingxia.service.CategoryService;
import com.tingxia.util.RESTfulAPIUtil;
import com.tingxia.util.StatusCode;
import com.tingxia.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 分类
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    UserUtil userUtil;

    @RequestMapping("/list")
    public Map list(){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        HashMap<String,Object> categoryData = new HashMap<>();
        categoryData.put("categories",categoryService.getCategorie());
        resTfulAPIUtil.setData(categoryData);
        return resTfulAPIUtil.getAPI();
    }
    //管理员接口
    @RequestMapping(value = "create", method = {RequestMethod.POST})
    public Map create(HttpServletRequest req,Integer parentId,String name){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Map r = userUtil.adminUtil(req,resTfulAPIUtil);
        if(r != null){
            return r;
        }
        Category category = categoryService.create(parentId,name);
        if(category == null){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("未知错误！");
        }
        resTfulAPIUtil.putData("category",category);
        return resTfulAPIUtil.getAPI();
    }

    @RequestMapping(value = "update", method = {RequestMethod.POST})
    public Map update(HttpServletRequest req,Integer parentId,String name,Integer id){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Map r = userUtil.adminUtil(req,resTfulAPIUtil);
        if(r != null){
            return r;
        }
        Category category = categoryService.update(id,parentId,name);
        if(category == null){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("未知错误！");
        }
        resTfulAPIUtil.putData("category",category);
        return resTfulAPIUtil.getAPI();
    }

}
