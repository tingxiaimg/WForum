package com.tingxia.dao;

import com.tingxia.pojo.Career;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareerMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(Career record);

    Career selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(Career record);

    int updateByPrimaryKey(Career record);

    List<Career> selectByUser(@Param("id") Integer id);
}