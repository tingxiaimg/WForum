package com.tingxia.dao;

import com.tingxia.pojo.School;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(School record);

    School selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(School record);

    int updateByPrimaryKey(School record);

    List<School> selectByUser(@Param("id") Integer id);

}