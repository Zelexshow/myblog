package com.zelex.web;

import com.zelex.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author Zelex
 * @Date 2021/2/24 10:18
 * @Version 1.0
 */
@Controller
public class ArchiveController {

    @Autowired
    private BlogService blogService;
    @GetMapping("/archives")
    public String archives(Model model){
        model.addAttribute("archiveMap",blogService.archiveBlog());
        model.addAttribute("blogCount",blogService.countBlog());//一共多少博客数
        return "archives";
    }


}
