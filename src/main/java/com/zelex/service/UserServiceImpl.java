package com.zelex.service;

import com.zelex.dao.UserRepository;
import com.zelex.po.User;
import com.zelex.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Zelex
 * @Date 2021/2/17 15:31
 * @Version 1.0
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String userName, String passWord) {
        User user = userRepository.findByUsernameAndPassword(userName, MD5Utils.code(passWord));
        return user;
    }
}
