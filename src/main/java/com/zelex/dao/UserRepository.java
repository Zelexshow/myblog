package com.zelex.dao;

import com.zelex.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author Zelex
 * @Date 2021/2/17 15:33
 * @Version 1.0
 */
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsernameAndPassword(String username,String password);
}
