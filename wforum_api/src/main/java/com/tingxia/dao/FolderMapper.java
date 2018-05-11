package com.tingxia.dao;

import com.tingxia.pojo.Folder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(Folder record);

    Folder selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(Folder record);

    int updateByPrimaryKey(Folder record);

    List<Folder> selectChildren(@Param("id") Integer id);

    List<Folder> selectByUserId(@Param("userId") Integer userId);

    List<Folder> selectFoldersWithCollects(@Param("userId") Integer userId);

    Folder selectWithCollects(@Param("id") Integer id,@Param("userId") Integer userId);
}