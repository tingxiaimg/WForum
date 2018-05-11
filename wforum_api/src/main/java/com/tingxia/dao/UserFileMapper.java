package com.tingxia.dao;

import com.tingxia.pojo.UserFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFileMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(UserFile record);

    UserFile selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(UserFile record);

    int updateByPrimaryKey(UserFile record);
}