package com.zelex.web.admin;

import com.zelex.po.User;
import com.zelex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @Author Zelex
 * @Date 2021/2/17 15:37
 * @Version 1.0
 */
//用于检查登录的controller
@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String LoginPage(){
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session,
                        RedirectAttributes attributes){
        User result=null;
        result = userService.checkUser(username, password);
        if (result != null){
            result.setPassword(null);//存放密码不安全
            session.setAttribute("user",result);
            return "admin/index";
        }else{
            attributes.addFlashAttribute("message","用户名密码错误！");
            return "redirect:/admin";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
