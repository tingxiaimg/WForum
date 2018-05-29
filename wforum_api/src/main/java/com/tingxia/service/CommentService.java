package com.tingxia.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tingxia.dao.ArticleMapper;
import com.tingxia.dao.CommentMapper;
import com.tingxia.dao.MessageMapper;
import com.tingxia.dao.UserMapper;
import com.tingxia.pojo.Article;
import com.tingxia.pojo.Comment;
import com.tingxia.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private MessageMapper messageMapper;

    public List<Comment> getUserComments(int userId, int pageNo, int pageSize){
        Page page = PageHelper.startPage(pageNo,pageSize);
        return commentMapper.selectUserComments(userId);
    }

    public List<Comment> getCommentReplies(Integer articleId,Integer commentId,Integer userId){
        if(userId != null && userId > 0){
            return commentMapper.selectRepliesWithUp(articleId,commentId,userId);
        }
        return commentMapper.selectReplies(articleId,commentId);
    }

    @Transactional
    public Comment createComment(Integer articleId,String content,Integer userId){
        Comment comment = saveComment("article",content,userId,articleId,null,null);
        if(comment != null && comment.getId()!=0){
            articleMapper.changCount(articleId,1,3);
        }else{
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }
        return comment;
    }

    @Transactional
    public Comment createReply(Integer articleId,String content,Integer userId,Integer parentId,Integer replyUserId){
        return saveComment("comment",content,userId,articleId,replyUserId,parentId);
    }
    @Transactional
    public Boolean deleteComment(Integer id,Integer userId){
        //检测是否为当前用户的评论，避免出错
        Comment comment = commentMapper.selectByPrimaryKey(id);
        if(comment.getId().equals(id) && comment.getUserId().equals(userId)){
            // 删除评论
            int i = commentMapper.deleteByPrimaryKey(id);
            // 减少用户回复数
            userMapper.changCount(userId,-1,3);
            if(comment.getSourceName().equals("article")){
                articleMapper.changCount(comment.getArticleId(),-1,3);
            }
            if( i <1 ){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }else{
                return true;
            }
        }
        return false;
    }

    private Comment saveComment(String sourceName,String content,Integer userId,Integer articleId,Integer replyUserId,Integer parentId){
        Article article = articleMapper.selectByPrimaryKey(articleId);
        Comment comment = new Comment();
        if(article ==null){
            return null;
        }
        if(!article.getStatus().equals(1)){
            return null;
        }
        if(!"article".equals(sourceName) && !"comment".equals(sourceName)){
            return null;
        }
        comment.setSourceName(sourceName);
        comment.setArticleId(articleId);
        comment.setReplyUserId(replyUserId);
        comment.setParentId(parentId);
        comment.setContent(content);
        comment.setStatus(1);
        comment.setUserId(userId);
        comment.setUps(0);
        comment.setContentType(1);
        comment.setCreatedAt(new Date());
        comment.setUpdatedAt(new Date());
        commentMapper.insert(comment);
        userMapper.changCount(userId,1,3);
        // 评论话题，更新话题最后评论人。
        if("article".equals(sourceName)){
            Article article1 = new Article();
            article1.setId(article.getId());
            article1.setLastUserId(userId);
            article1.setLastCommentAt(new Date());
            articleMapper.updateByPrimaryKeySelective(article1);
        }
        // 自己评论自己的话题，不必消息通知。
        if("article".equals(sourceName) && article.getUserId().equals(userId)){
            return commentMapper.selectByPrimaryKey(comment.getId());
        }
        //添加消息
        Message message = new Message();
        message.setArticleId(articleId);
        message.setFromUserId(userId);
        message.setReaded(false);

        message.setSourceName("comment");
        message.setCreatedAt(new Date());
        message.setUpdatedAt(new Date());

        if("article".equals(sourceName)){
            message.setSourceId(comment.getId());
            message.setToUserId(article.getUserId());
            message.setType("comment");
            // 被评论加三分
            userMapper.changCount(article.getUserId(),3,0);
            // 评论者加两分
            userMapper.changCount(userId,2,0);
        }else{
            message.setSourceId(comment.getParentId());
            message.setToUserId(replyUserId);
            message.setType("reply");
            // 被回复，作者加一分
            userMapper.changCount(article.getUserId(),1,0);
            // 被回复者加一分
            userMapper.changCount(replyUserId,1,0);
        }
        messageMapper.insert(message);

        return commentMapper.selectByPrimaryKey(comment.getId());
    }
    @Transactional
    public boolean updateComment(Integer userId, Integer id, String content) {

        if(content == null){
            return false;
        }
        if (commentMapper.selectByPrimaryKey(id).getUserId().equals(userId) && !"".equals(content)){
            Comment comment = new Comment();
            comment.setId(id);
            comment.setContent(content);
            comment.setUpdatedAt(new Date());
            return commentMapper.updateByPrimaryKeySelective(comment)>0;
        }
        return false;
    }

    public Boolean changeStatus(Integer id, Integer status) {
        return commentMapper.updateStatus(id,status)>0;
    }

    public List<Comment> getCommentList(Integer pageNo, Integer pageSize, Long startAt, Long endAt,Integer status) {
        Date sd = null,ed = null;
        if(startAt != null){
            sd = new Date(startAt);
        }
        if(endAt != null){
            ed = new Date(endAt);
        }
        PageHelper.startPage(pageNo,pageSize);
        return commentMapper.selectCommentList(sd,ed,status);
    }
    @Transactional
    public boolean up(String type, Integer targetId, Integer userId) {
        Integer upType = 0;
        Boolean isArticle = false;
        if("article".equals(type)){
            upType = 1;
            isArticle = true;
        }else if("comment".equals(type)){
            upType = 2;
        }else if("reply".equals(type)){
            upType = 3;
        }else{
            return false;
        }
        int r = commentMapper.selectUp(upType,targetId,userId);
        // 有，删除
        if(r > 0){
            if(commentMapper.deleteUp(upType,targetId,userId) > 0){
                if(isArticle){
                    return true;
                }else{
                    return commentMapper.updateUps(targetId,-1) > 0;
                }
            }
        }else{ // 没有删除
            if(commentMapper.insertUp(upType,targetId,userId) > 0){
                if(isArticle){
                    return true;
                }else{
                    return commentMapper.updateUps(targetId,1) > 0;
                }
            }
        }
        return false;
    }
}
