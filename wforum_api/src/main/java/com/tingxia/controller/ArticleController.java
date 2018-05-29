package com.tingxia.controller;

import com.github.pagehelper.PageInfo;
import com.tingxia.pojo.Article;
import com.tingxia.service.ArticleService;
import com.tingxia.util.RESTfulAPIUtil;
import com.tingxia.util.StatusCode;
import com.tingxia.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 文章
 */
@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserUtil userUtil;
    /**
     * @Desc 文章列表
     * @Author tingXia
     * @return JSON
     */
    @RequestMapping("/list")
    public Map List(@RequestParam(required = false) final Integer cate
            ,@RequestParam(defaultValue = "200") final Integer pageSize
            ,@RequestParam(defaultValue = "1") final Integer pageNo){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        PageInfo<Article> pageInfo = new PageInfo(articleService.getArticleList(pageNo,pageSize,cate,1,null,null));
        resTfulAPIUtil.putData("totalCount",pageInfo.getTotal());
        resTfulAPIUtil.putData("pageNo",pageInfo.getPageNum());
        resTfulAPIUtil.putData("pageSize",pageInfo.getPageSize());
        resTfulAPIUtil.putData("isHasNextPage",pageInfo.isHasNextPage());
        resTfulAPIUtil.putData("articles",pageInfo.getList());
        return resTfulAPIUtil.getAPI();
    }

    /**
     * @Desc 获取最高的(by comment or browse)
     * @return JSON
     */
    @RequestMapping(value = "/max{size}",method = {RequestMethod.GET})
    public Map listMax(@PathVariable(value = "size",required = false) Integer size,@RequestParam(defaultValue = "comment") String by){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        if(size == null || size<=0){
            size = 10;
        }
        if(!"comment".equals(by)&&!"browse".equals(by)){
            resTfulAPIUtil.setStatusCode(StatusCode.INVALID_REQUEST);
            resTfulAPIUtil.setMassage("参数by的值只允许为comment，browse；不能为"+by+"。");
        }else{
            HashMap<String,Object> articleData = new HashMap<>();
            PageInfo<Article> pageInfo = new PageInfo<>(articleService.getMax(size,by));
            articleData.put("articles" ,pageInfo.getList());
            articleData.put("total" ,pageInfo.getSize());
            resTfulAPIUtil.setData(articleData);
        }
        return resTfulAPIUtil.getAPI();
    }
    //置顶文章
    @RequestMapping("/top/global")
    public Map tops(){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        HashMap<String,Object> articleData = new HashMap<>();
        articleData.put("articles",articleService.getTopList());
        resTfulAPIUtil.setData(articleData);
        return resTfulAPIUtil.getAPI();
    }

    @RequestMapping("/info/{id:\\d*}") //ok
    public Map info(HttpServletRequest req,@PathVariable Integer id){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        Article article = articleService.getArticle(id,userId);
        Boolean canRead = articleService.checkCanRead(article,userId);
        resTfulAPIUtil.putData("article",article);
        if(canRead){
            resTfulAPIUtil.putData("comments",articleService.getArticleComments(id,userId));
        }else{
            resTfulAPIUtil.putData("comments",null);
        }

        return resTfulAPIUtil.getAPI();
    }
    @RequestMapping("/user/{userId}") //ok
    public Map userArticleList(HttpServletRequest req,
                               @PathVariable Integer userId,
                               @RequestParam(defaultValue = "5") Integer pageSize,
                               @RequestParam(defaultValue = "false") Boolean desc,
                               @RequestParam(defaultValue = "1") Integer orderType,
                               @RequestParam(defaultValue = "1") Integer pageNo){
        StringBuffer order = new StringBuffer("");
        switch(orderType){
            case 0: order.append("created_at");break;
            case 1: order.append("browse_count");break;
            case 3: order.append("comment_count");break;
            case 4: order.append("collect_count");break;
            default: order.append("created_at");
        }
        if(desc){
            order.append(" desc");
        }
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        // 当前用户
        Integer nowUserId = userUtil.getUserId(req);
        PageInfo<Article> articles = new PageInfo<>(articleService.getUserArticle(userId,nowUserId,pageNo,
                pageSize,null,order.toString()));
        resTfulAPIUtil.putData("articles",articles.getList());
        resTfulAPIUtil.putData("pageNo",articles.getPageNum());
        resTfulAPIUtil.putData("pageSize",articles.getPageSize());
        resTfulAPIUtil.putData("total",articles.getTotal());
        return resTfulAPIUtil.getAPI();

    }

    @RequestMapping(value = "/create",method = {RequestMethod.POST})
    public Map create(HttpServletRequest req,
                      @RequestParam("name") String name,
                      @RequestParam("content") String content,
                      @RequestParam("categories") Integer[] categories){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId == -1){
            resTfulAPIUtil.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            resTfulAPIUtil.setMassage("登陆超时！");
            return resTfulAPIUtil.getAPI();
        }
        Integer articleId = articleService.createArticle(null,name,content,categories,userId,true);
        if(articleId > 0){
            resTfulAPIUtil.putData("id",articleId);
        }else{
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
        }
        return resTfulAPIUtil.getAPI();
    }
    //更新文章
    //返回文章
    @RequestMapping(value = "/update",method = {RequestMethod.PUT})
    public Map update(HttpServletRequest req,
                      @RequestParam Integer id,
                      @RequestParam String name,
                      @RequestParam String content,
                      @RequestParam Integer[] categories){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        if(id == null||categories == null){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
        }else{
            // 需要获取id
            Integer userId = userUtil.getUserId(req);
            if(userId ==-1){
                resTfulAPIUtil.setStatusCode(StatusCode.LOGIN_TIMEOUT);
                resTfulAPIUtil.setMassage("登陆超时！");
                return resTfulAPIUtil.getAPI();
            }
            int rId = articleService.createArticle(id,name,content,categories,userId,false);
            if(rId > 0){
                resTfulAPIUtil.putData("id",rId);
            }else{
                resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
                if(rId == 0){
                    resTfulAPIUtil.setMassage("数据库执行失败！");
                }else if(rId == -1||rId == -3){
                    resTfulAPIUtil.setMassage("标题不合法！");
                }else if(rId == -2 ||rId == -4){
                    resTfulAPIUtil.setMassage("内容不合法！");
                }else{
                    resTfulAPIUtil.setMassage("未知错误！");
                }
            }

        }
        return resTfulAPIUtil.getAPI();
    }
    //删除文章
    @RequestMapping(value = "/delete/{id}",method = {RequestMethod.DELETE})
    public Map delete(HttpServletRequest req,@PathVariable Integer id){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Integer userId = userUtil.getUserId(req);
        if(userId ==-1){
            resTfulAPIUtil.setStatusCode(StatusCode.LOGIN_TIMEOUT);
            resTfulAPIUtil.setMassage("登陆超时！");
            return resTfulAPIUtil.getAPI();
        }
        if(!articleService.delete(id,userId)){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("数据更新失败！");
        }
        return resTfulAPIUtil.getAPI();
    }

//************************************管理员接口****************************************
    /**
     * @Desc 管理员获取文章列表
     * @return JSON
     */
    @RequestMapping(value = "/admin/list",method = {RequestMethod.GET})
    @ResponseBody
    public Map allList(HttpServletRequest req,
                       @RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
                       @RequestParam(value = "pageSize",defaultValue = "200") Integer pageSize,
                       @RequestParam(value = "cate",required = false) Integer cate,
                       @RequestParam(value = "status",required = false) Integer status,
                       @RequestParam(value = "startAt",required = false) Long startAt,
                       @RequestParam(value = "endAt",required = false) Long endAt){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Map r = userUtil.adminUtil(req,resTfulAPIUtil);
        if(r != null){
            return r;
        }
        PageInfo<Article> pageInfo = new PageInfo(articleService.getArticleList(pageNo,pageSize,cate,status,startAt,endAt));
        resTfulAPIUtil.putData("total",pageInfo.getTotal());
        resTfulAPIUtil.putData("pageNo",pageInfo.getPageNum());
        resTfulAPIUtil.putData("pageSize",pageInfo.getPageSize());
        resTfulAPIUtil.putData("articles",pageInfo.getList());
        return resTfulAPIUtil.getAPI();
    }

    /**
     * 更新文章状态
     * @return JSON
     */
    @RequestMapping(value = "/status/update",method = {RequestMethod.PUT})
    @ResponseBody
    public Map updateStatus(HttpServletRequest req,
                            @RequestParam Integer id,
                            @RequestParam Integer status){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Map r = userUtil.adminUtil(req,resTfulAPIUtil);
        if(r != null){
            return r;
        }
        if(status == null){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
        }else{
            articleService.changStatus(id,status);
        }
        return resTfulAPIUtil.getAPI();
    }

    //置顶文章
    @RequestMapping(value = "/top/{id}",method = {RequestMethod.POST})
    public Map top(HttpServletRequest req,@PathVariable Integer id){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Map r = userUtil.adminUtil(req,resTfulAPIUtil);
        if(r != null){
            return r;
        }
        Integer articleRe = articleService.setArticleToTop(id);
        if(articleRe.equals(0)){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("数据库执行失败！");
        }else if(articleRe.equals(-1)){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("超过规定最大数量！");
        }else if(articleRe.equals(-2)) {
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("话题已置顶！");
        }
        return resTfulAPIUtil.getAPI();
    }

    //删除置顶
    @RequestMapping(value = "deltop/{id}",method = {RequestMethod.DELETE})
    public Map deleteTop(HttpServletRequest req,@PathVariable Integer id){
        RESTfulAPIUtil resTfulAPIUtil = new RESTfulAPIUtil();
        Map r = userUtil.adminUtil(req,resTfulAPIUtil);
        if(r != null){
            return r;
        }
        if(!articleService.deleteTop(id)){
            resTfulAPIUtil.setStatusCode(StatusCode.ERROR);
            resTfulAPIUtil.setMassage("执行错误！");
        }
        return resTfulAPIUtil.getAPI();
    }
}
