package com.tingxia.dao;

import com.tingxia.pojo.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CommentMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(Comment record);

    Comment selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<Comment> selectComments(@Param("articleId") Integer articleId);

    List<Comment> selectCommentsWithUp(@Param("articleId") Integer articleId,@Param("userId") Integer userId);

    List<Comment> selectReplies(@Param("articleId") Integer articleId,@Param("parentId") Integer parentId);

    List<Comment> selectRepliesWithUp(@Param("articleId") Integer articleId,@Param("parentId") Integer parentId,@Param("userId") Integer userId);

    List<Comment> selectUserComments(@Param("userId") Integer userId);

    int selectReplyCount(@Param("commentId") Integer commentId);

    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    List<Comment> selectCommentList(@Param("startAt") Date startAt, @Param("endAt") Date endAt, @Param("status") Integer status);

    int selectUp(@Param("type") Integer type, @Param("targetId") Integer targetId, @Param("userId") Integer userId);

    int deleteUp(@Param("type") Integer type, @Param("targetId") Integer targetId, @Param("userId") Integer userId);

    int insertUp(@Param("type") Integer type, @Param("targetId") Integer targetId, @Param("userId") Integer userId);

    int updateUps(@Param("commentId") Integer commentId, @Param("offset") Integer offset);
}