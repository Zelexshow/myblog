package com.zelex.web;


import com.zelex.NotFoundException;
import com.zelex.service.BlogService;
import com.zelex.service.TagService;
import com.zelex.service.TypeService;
import com.zelex.vo.BlogQuery;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Zelex
 * @Date 2021/2/10 19:39
 * @Version 1.0
 */
@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         Model model){
    model.addAttribute("page",blogService.listBlog(pageable));
    model.addAttribute("types",typeService.listTypeTop(6));
    model.addAttribute("tags",tagService.listTagTop(10));
    model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(8));
        return "index";
    }
    @PostMapping("/search")
    public String search(@PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model){

        model.addAttribute("page",blogService.listBlog("%"+query+"%",pageable));
        model.addAttribute("query",query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model){
        model.addAttribute("blog",blogService.getAndConvert(id));
        return "blog";
    }
    //用于页脚返回最新的三条博客
    @GetMapping("/footer/newblog")
    public String newBlogList(Model model){
        model.addAttribute("newblogs",blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }
}
