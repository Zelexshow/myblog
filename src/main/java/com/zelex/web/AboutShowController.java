package com.zelex.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author Zelex
 * @Date 2021/2/24 11:12
 * @Version 1.0
 */
@Controller
public class AboutShowController {
    @GetMapping("/about")
    public String aboutMe(){
        return "/about";
    }
}
