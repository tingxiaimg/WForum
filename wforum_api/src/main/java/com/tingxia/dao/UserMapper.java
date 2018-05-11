package com.tingxia.dao;

import com.tingxia.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(User record);

    /**
     * 包含密码，不包含教育和工作信息
     */
    User selectByPrimaryKey(@Param("id") Integer id);

    User selectByEmail(@Param("email") String email);

    User selectByName(@Param("name") String name);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectUserPublicInfo(@Param("id")Integer id);

    User selectUserDetailInfo(@Param("id")Integer id);

    List<User> selectUserList(@Param("role") Integer role,@Param("status") Integer status,@Param("startAt") Date startAt,@Param("endAt") Date endAt);

    /**
     *
     * @param id id
     * @param offset 偏移量
     * @param target 0.score,1.collect,2.article,3.comment
     * @return
     */
    //变化收藏数
    int changCount(@Param("id") Integer id,@Param("offset") Integer offset,@Param("target") Integer target);

    int updateSex(@Param("id") Integer id,@Param("man") boolean man);

    int updateInfo(@Param("id") Integer id,@Param("type") String type,@Param("source") String source);
}