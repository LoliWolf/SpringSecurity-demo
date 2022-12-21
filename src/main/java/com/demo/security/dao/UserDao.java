package com.demo.security.dao;

import com.demo.security.entity.Role;
import com.demo.security.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {
    //根据用户名查询用户
    User loadUserByUsername(@Param("username") String username);

    //根据用户id查询角色
    List<Role> getRolesByUid(@Param("uid") Integer uid);

    //根据giteeid查用户
    User loadUserByGiteeID(@Param("giteeID") String giteeID);

    //根据用户名创建新用户
    Integer createUserByUsername(@Param("username") String username);

    //绑定用户和gitee信息
    Integer addUserGiteeInfoByUsername(@Param("username") String username,@Param("giteeID") String giteeID,@Param("giteeName") String giteeName);

    //按用户名添加角色
    Integer addUserRoleByUsername(@Param("username") String username,@Param("role") String role);
}