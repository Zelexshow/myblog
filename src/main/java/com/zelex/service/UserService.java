package com.zelex.service;

import com.zelex.po.User;

/**
 * @Author Zelex
 * @Date 2021/2/17 15:30
 * @Version 1.0
 */
public interface UserService {
    User checkUser(String userName, String passWord);
}
