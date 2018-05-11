package com.tingxia.controller;

import com.github.pagehelper.PageInfo;
import com.tingxia.pojo.Comment;
import com.tingxia.service.CommentService;
import com.tingxia.util.RESTfulAPIUtil;
import com.tingxia.util.StatusCode;
import com.tingxia.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 评论
 */
@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserUtil userUtil;

    //用户评论列表
    @RequestMapping("/user/{id}")
    public Map userCommentList(@PathVariable int id, @RequestParam(defaultValue = "1") int pageNo,@RequestParam(defaultValue = "20") int pageSize){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        PageInfo<Comment> pageInfo = new PageInfo(commentService.getUserComments(id,pageNo,pageSize));
        resTfulAPIUtil.putData("comments",pageInfo.getList());
        resTfulAPIUtil.putData("pageNo",pageInfo.getPageNum());
        resTfulAPIUtil.putData("pageSize",pageInfo.getPageSize());
        resTfulAPIUtil.putData("total",pageInfo.getTotal());
        return resTfulAPIUtil.getAPI();
    }
    // 回复
    @RequestMapping("/reply/{commentId}")
    public Map replyList(HttpServletRequest req,@PathVariable Integer commentId,Integer articleId){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        resTfulAPIUtil.putData("replies",commentService.getCommentReplies(articleId,commentId,userUtil.getUserId(req)));
        return resTfulAPIUtil.getAPI();
    }

    // 添加评论
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public Map create(HttpServletRequest req,Integer articleId,String content,String sourceName){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId ==-1){
            resTfulAPIUtil.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            resTfulAPIUtil.setMassage("登陆超时！");
            return resTfulAPIUtil.getAPI();
        }
        // 判断是否为管理员
        Comment comment = commentService.createComment(articleId,content,userId);
        if(comment == null){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("不可评论！");
        }else{
            resTfulAPIUtil.putData("comment",comment);
        }
        return resTfulAPIUtil.getAPI();
    }
    // 添加回复
    @RequestMapping(value = "/reply/create",method = {RequestMethod.POST})
    public Map createReply(HttpServletRequest req,Integer replyUserId,Integer articleId,Integer parentId,String content){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId ==-1){
            resTfulAPIUtil.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            resTfulAPIUtil.setMassage("登陆超时！");
            return resTfulAPIUtil.getAPI();
        }
        Comment comment = commentService.createReply(articleId,content,userId,parentId,replyUserId);
        if(comment == null){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("不可回复！");
        }else{
            resTfulAPIUtil.putData("comment",comment);
        }
        return resTfulAPIUtil.getAPI();
    }

    // 修改评论或回复
    @RequestMapping(value = "/update",method = {RequestMethod.PUT})
    public Map update(HttpServletRequest req,Integer id, String content){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId ==-1){
            resTfulAPIUtil.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            resTfulAPIUtil.setMassage("登陆超时！");
            return resTfulAPIUtil.getAPI();
        }
        if (!commentService.updateComment(userId,id,content)){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("更新数据失败！");
        }
        return resTfulAPIUtil.getAPI();
    }
    @RequestMapping(value = "/delete/{id}",method = {RequestMethod.DELETE})
    public Map delete(HttpServletRequest req,@PathVariable Integer id){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId ==-1){
            resTfulAPIUtil.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            resTfulAPIUtil.setMassage("登陆超时！");
            return resTfulAPIUtil.getAPI();
        }
        if(!commentService.deleteComment(id,userId)){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
        }
        return resTfulAPIUtil.getAPI();
    }
    // 点赞
    @RequestMapping(value = "/up", method = {RequestMethod.POST})
    public Map up(HttpServletRequest req,String type,Integer id){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId ==-1){
            resTfulAPIUtil.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            resTfulAPIUtil.setMassage("登陆超时！");
            return resTfulAPIUtil.getAPI();
        }
        if(type == null || "".equals(type)){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("参数错误！");
            return resTfulAPIUtil.getAPI();
        }
        if(!commentService.up(type,id,userId)){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("类型错误或数据更新错误！");
            return resTfulAPIUtil.getAPI();
        }
        return resTfulAPIUtil.getAPI();
    }

// -----------------------管理员接口----------------------

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public Map comments(HttpServletRequest req,
                        @RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo,
                        @RequestParam(value = "pageSize",defaultValue = "200") Integer pageSize,
                        @RequestParam(value = "startAt",required = false) Long startAt,
                        @RequestParam(value = "endAt",required = false) Long endAt,
                        @RequestParam(value = "status",required = false) Integer status){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Map r = userUtil.adminUtil(req,resTfulAPIUtil);
        if(r != null){
            return r;
        }
        PageInfo<Comment> comments = new PageInfo(commentService.getCommentList(pageNo,pageSize,startAt,endAt,status));
        resTfulAPIUtil.putData("comments",comments.getList());
        resTfulAPIUtil.putData("pageSize",comments.getPageSize());
        resTfulAPIUtil.putData("pageNo",comments.getPageNum());
        resTfulAPIUtil.putData("total",comments.getTotal());
        return resTfulAPIUtil.getAPI();
    }
    //更新状态
    @RequestMapping(value = "update/status/{id}", method = {RequestMethod.PUT})
    public Map updateStatus(HttpServletRequest req,@PathVariable Integer id,@RequestParam(value = "status") Integer status){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Map r = userUtil.adminUtil(req,resTfulAPIUtil);
        if(r != null){
            return r;
        }
        if(status == null){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("必须有status");
        }else if(status >= 0){
            if (!commentService.changeStatus(id,status)){
                resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
                resTfulAPIUtil.setMassage("数据更新失败！");
            }
        }else{
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("status需大于等于0");
        }
        return resTfulAPIUtil.getAPI();
    }
}
