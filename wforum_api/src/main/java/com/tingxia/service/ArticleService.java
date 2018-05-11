package com.tingxia.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tingxia.config.WForumConfig;
import com.tingxia.dao.ArticleMapper;
import com.tingxia.dao.CategorieMapper;
import com.tingxia.dao.CommentMapper;
import com.tingxia.dao.UserMapper;
import com.tingxia.pojo.Article;
import com.tingxia.pojo.Comment;
import com.tingxia.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CategorieMapper categorieMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private WForumConfig wForumConfig;

    public List<Article> getArticleList(Integer pageNo,Integer pageSize,Integer cate,Integer status,Long startAt,Long endAt){
        Date sd = null,ed = null;
        if(startAt != null){
            sd = new Date(startAt);
        }
        if(endAt != null){
            ed = new Date(endAt);
        }
        PageHelper.startPage(pageNo, pageSize, "created_at desc,browse_count desc");
        List<Article> articles = articleMapper.selectArticleList(cate,status,null,sd,ed);
        return articles;
    }

    public List<Article> getMax(Integer szie,String by){
        StringBuffer sb = new StringBuffer(by.replaceAll(" ",""));
        sb.append("_count desc ,created_at desc");
        PageHelper.startPage(1, szie, sb.toString());
        List<Article> articles = articleMapper.selectArticleList(null,1,null,null,null);
        return articles;
    }

    public List<Article> getUserArticle(Integer userId,Integer nowUserId,Integer pageNo,Integer pageSize,Integer cate){
        return getUserArticle(userId,nowUserId,pageNo,pageSize,cate,"created_at desc,browse_count desc");
    }

    public List<Article> getUserArticle(Integer userId,Integer nowUserId,Integer pageNo,Integer pageSize,Integer cate,String order){
        Integer status = 1;
        if(nowUserId == null || nowUserId.equals(-1)){
            status = null;
        }
        else if(userId.equals(nowUserId) || userMapper.selectByPrimaryKey(nowUserId).getRole().equals(2)){
            status = null;
        }
        PageHelper.startPage(pageNo, pageSize, order);
        return articleMapper.selectArticleList(cate,status,userId,null,null);
    }

    public List<Article> getTopList(){
        return articleMapper.selectTopArticle();
    }
    public Article getArticle(Integer id,Integer userId){
        if(id == null){
            return null;
        }

        Article article = articleMapper.selectAllByPrimaryKey(id);
        Boolean canRead = checkCanRead(article,userId);
        if(!canRead){
            article.setContent("# 当前话题不可浏览！");
        }
        if(article !=null && article.getStatus().equals(1)){
            articleMapper.changCount(id,1,2);
        }
        return article;
    }
    @Transactional
    public int createArticle(Integer id, String name, String content, Integer[] categories,Integer userId,Boolean isNew){
        ArrayList<Integer> categoriesL = new ArrayList<>();
        for(int i = 0;i<categories.length;i++){
            categoriesL.add(categories[i]);
        }
        return createArticle( id, name, content, categoriesL, userId, isNew);
    }
    @Transactional
    public int createArticle(Integer id, String name, String content, List<Integer> categories,Integer userId,Boolean isNew) {
        //校验标题，内容是否合法
        if(name == null||"".equals(name)){
            return -1;
        }
        if(content == null || "".equals(content)){
            return -2;
        }
        if(name.length()>200){
            return -3;
        }
        if(content.length()>6000){
            return -4;
        }
        //检测用户是否存在，避免出错
        User user =userMapper.selectByPrimaryKey(userId);
        if(user == null ||user.getId() == null){
            return -5;
        }
        Article article = new Article();
        article.setName(name.trim());
        article.setContent(content);
        // 测试为1
        article.setStatus(1);
        article.setUpdatedAt(new Date());
        Integer resNo ;
        if(isNew){
            article.setCreatedAt(new Date());
            article.setUserId(userId);
            article.setBrowseCount(0);
            article.setCollectCount(0);
            article.setCommentCount(0);
            article.setContentType(1);
            resNo = articleMapper.insert(article);
            if(resNo>0){
                userMapper.changCount(userId,1,2);
                userMapper.changCount(userId,10,0);
            }
        }else{
            article.setId(id);
            resNo = articleMapper.updateByPrimaryKeySelective(article);
        }
        if(resNo>0){
            //设置分类
            categorieMapper.deleteArticleCategory(article.getId());
            categorieMapper.insertArticleCategoryId(article.getId(),categories);
            return article.getId();
        }else{
            return 0;
        }
    }

    public List<Comment> getArticleComments(Integer id,Integer userId) {
        if(userId > 0){
            return commentMapper.selectCommentsWithUp(id,userId);
        }
        return commentMapper.selectComments(id);
    }
    @Transactional
    public Boolean changStatus(Integer id, Integer status) {
        Article article = new Article();
        article.setId(id);
        article.setStatus(status);
        return articleMapper.updateByPrimaryKeySelective(article)>0;
    }
    @Transactional
    public boolean delete(Integer id, Integer userId) {
        Article article = articleMapper.selectByPrimaryKey(id);
        if(article == null){
            return false;
        }
        // 非管理员以及作者
        if(!article.getUserId().equals(userId) && !userMapper.selectUserPublicInfo(userId).getRole().equals(2)){
            return false;
        }
        if(articleMapper.deleteByPrimaryKey(id)>0){
            articleMapper.deleteTop(id);
            return true;
        }
        return false;
    }

    public Boolean checkCanRead(Article article, Integer userId) {
        Boolean canRead =true;
        // 文章审核未通过或未审核
        if(article !=null && !article.getStatus().equals(1)){
            // 游客
            if(userId.equals(-1)){
                canRead = false;
            }else{
                User user = userMapper.selectByPrimaryKey(userId);
                // 不是作者也不是管理员
                if(!user.getId().equals(article.getUserId()) && !user.getRole().equals(2)){
                    canRead = false;
                }
            }

        }
        return canRead;
    }
    @Transactional
    public Integer setArticleToTop(Integer articleId) {
        int r = articleMapper.selectTopArticleCount();
        if(r < wForumConfig.getMaxTopArticleCount()){
            if(articleMapper.selectIsTop(articleId)>0){
                return -2;
            }
            return articleMapper.insertTop(articleId);
        }
        return -1;
    }
    @Transactional
    public Boolean deleteTop(Integer id){
        return articleMapper.deleteTop(id)>0;
    }
}
