package com.tingxia.dao;

import com.tingxia.pojo.Collect;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CollectMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(Collect record);

    Collect selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(Collect record);

    // map folderId,userId
    List<Collect> selectFolderCollects(Map<String,Integer> p);

    int selectFolderCollectCount(@Param("folderId") Integer folderId);

    int checkExist(@Param("articleId") Integer articleId, @Param("userId") Integer userId);

    int selectIsCollect(@Param("articleId") Integer articleId, @Param("userId") Integer userId);
}