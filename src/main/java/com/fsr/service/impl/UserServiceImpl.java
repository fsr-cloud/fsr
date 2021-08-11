package com.fsr.service.impl;

import com.fsr.dao.UserDao;
import com.fsr.entity.Perms;
import com.fsr.entity.User;
import com.fsr.service.UserService;
import com.fsr.utils.Md5HashUtil;
import com.fsr.utils.SaltUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService  {
    @Autowired
    private UserDao userDao ;
    @Override
    public void register(User user) {
        //处理业务调用dao
        //1.生成随机盐
        String salt = SaltUtil.getSalt(8);
        //2.将随机盐保存到数据
        user.setSalt(salt);
        //3.明文密码进行md5 + salt + hash散列
        String password=Md5HashUtil.getMd5Hash(user.getPassword(),salt,1024);
        user.setPassword(password);
        userDao.save(user);
    }
    @Override
    public User findByUserName(String username) {
        return userDao.findByUserName(username);
    }
    @Override
    public List<Perms> findPermsByRoleId(String id) {
        return userDao.findPermsByRoleId(id);
    }
    @Override
    public User findRolesByUserName(String username) {
        return userDao.findRolesByUserName(username);
    }
}
