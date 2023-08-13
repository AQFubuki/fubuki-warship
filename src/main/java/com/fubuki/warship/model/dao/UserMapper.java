package com.fubuki.warship.model.dao;

import com.fubuki.warship.model.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User row);

    int insertSelective(User row);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User row);

    int updateByPrimaryKey(User row);

    User selectByName(String userName);

    User selectLogin(@Param("userName") String userName
            , @Param("password") String password);
}