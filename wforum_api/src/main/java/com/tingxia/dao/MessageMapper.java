package com.tingxia.dao;

import com.tingxia.pojo.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(Message record);

    Message selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

    /**
     * @param id 用户id
     * @param isRead 可选0,1
     * @return
     */
    List<Message> selectByUser(@Param("id") Integer id,@Param("isRead") Boolean isRead);

    int updateReaded(@Param("userId") Integer userId, @Param("id") Integer id);
}