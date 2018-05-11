package com.tingxia.controller;

import com.tingxia.service.FileService;
import com.tingxia.util.RESTfulAPIUtil;
import com.tingxia.util.StatusCode;
import com.tingxia.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;
    @Autowired
    private UserUtil userUtil;

    @RequestMapping(value = "/upload",method = {RequestMethod.POST})
    public Map upload(HttpServletRequest req,@RequestParam("file") MultipartFile file){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId ==-1){
            resTfulAPIUtil.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            resTfulAPIUtil.setMassage("登陆超时！");
            return resTfulAPIUtil.getAPI();
        }
        String url = fileService.saveFile(file,userId);
        if("".equals(url)){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("系统错误！");
        }else{
            resTfulAPIUtil.putData("url",url);
        }
        return resTfulAPIUtil.getAPI();
    }
    @RequestMapping(value = "/read/{id}")
    public void read(@PathVariable Integer id, HttpServletResponse response){
        try {
            FileInputStream fi = fileService.getFile(id,response);
            if(fi == null){
                System.out.println("文件未读取成功！");
                response.setStatus(404);
                return;
            }
            // 下载response.addHeader("Content-Disposition", "attachment; filename=" + name);
            response.setContentLength(fi.available());
            response.setCharacterEncoding("utf-8");
            OutputStream out = response.getOutputStream();
            byte[] b=new byte[1024*1024];
            while(fi.read(b)>0){
                out.write(b);
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @RequestMapping(value = "/user/avatar/{type}/{id}",method = {RequestMethod.GET})
    public void avatar(@PathVariable String type,@PathVariable Integer id,HttpServletResponse response) throws IOException{
        if(type == null){
            response.setStatus(404);
            return;
        }
        String contentType="image/"+type;
        response.setCharacterEncoding("utf-8");
        response.setContentType(contentType);
        FileInputStream fi = null;
        try {
            fi = fileService.getAvatar(id);
            if(fi != null){
                response.setContentLength(fi.available());
                OutputStream out = response.getOutputStream();
                byte[] b=new byte[1024*1024];
                while(fi.read(b)>0){
                    out.write(b);
                }
                out.flush();
                out.close();
            }else{
                response.setStatus(404);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if(fi != null){
                try{
                    fi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }
    //上传头像，需登录
    @RequestMapping(value = "/upload/avatar",method = {RequestMethod.POST})
    public Map uploadAvatar(@RequestParam("file") MultipartFile file,HttpServletRequest req){
        RESTfulAPIUtil res = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId ==-1){
            res.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            res.setMassage("登陆超时或未登录！");
            return res.getAPI();
        }
        String url = fileService.saveAvatar(userId,file);
        if("".equals(url)){
            res.setStatusCode(StatusCode.ERROR);
            res.setMassage("系统错误！");
            return res.getAPI();
        }
        res.putData("url",url);
        return res.getAPI();
    }

}
