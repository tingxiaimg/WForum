package com.tingxia.dao;

import com.tingxia.pojo.Article;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ArticleMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(Article record);

    Article selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);

    List<Article> selectArticleList(@Param("cate") Integer cate,
                                    @Param("status") Integer status,
                                    @Param("userId") Integer userId,
                                    @Param("startAt") Date startAt,
                                    @Param("endAt") Date endAt);
    //查询文章所有信息，不包含评论
    Article selectAllByPrimaryKey(@Param("id") Integer id);
    //查询置顶文章
    List<Article> selectTopArticle();

    /**
     *
     * @param id
     * @param offset
     * @param target 1.collect,2.browse,3.comment
     * @return
     */
    //变化收藏数
    int changCount(@Param("id") Integer id,@Param("offset") Integer offset,@Param("target") Integer target);


    int selectTopArticleCount();

    int deleteTop(@Param("id") Integer id);

    int insertTop(@Param("articleId") Integer articleId);

    int selectIsTop(@Param("articleId") Integer articleId);
}