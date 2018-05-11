package com.tingxia.dao;

import com.tingxia.pojo.Category;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategorieMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(Category record);

    Category selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category> selectByArticle(@Param("id") Integer id);

    List<Category> selectAll(@Param("sequence") Integer sequence,@Param("parentId") Integer parentId);

    //插入文章分类
    Integer insertArticleCategoryId(@Param("articleId") Integer articleId,@Param("categoryIds") List<Integer> categoryIds);

    int deleteArticleCategory(@Param("articleId") Integer articleId);

}