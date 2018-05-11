package com.tingxia.dao;

import com.tingxia.pojo.File;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(File record);

    File selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(File record);

    int updateByPrimaryKey(File record);

    File selectByMD5(@Param("md5") String md5);

    int changeUserCount(@Param("id") Integer id, @Param("offset") Integer offset);
}