package com.demo.security.service;

import com.demo.security.dao.UserDao;
import com.demo.security.entity.User;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Random;
import java.util.UUID;

@Service
public class MyUserDetailServiceImpl implements UserDetailsService {

    private  final UserDao userDao;

    @Autowired
    public MyUserDetailServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.loadUserByUsername(username);
        if(ObjectUtils.isEmpty(user)) {
            throw new RuntimeException("用户不存在");
        }
        user.setRoles(userDao.getRolesByUid(user.getId()));
        return user;
    }

    public User loadUserByGiteeID(String id, String name){
        User user = userDao.loadUserByGiteeID(id);
        //新用户
        if (ObjectUtils.isEmpty(user)){
            try {
                String username = name + UUID.randomUUID();
                userDao.createUserByUsername(username);
                userDao.addUserGiteeInfoByUsername(username,id,name);
                userDao.addUserRoleByUsername(username,"ROLE_user");
                user = userDao.loadUserByGiteeID(id);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        user.setRoles(userDao.getRolesByUid(user.getId()));
        return user;
    }
}