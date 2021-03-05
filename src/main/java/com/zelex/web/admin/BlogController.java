package com.zelex.web.admin;

import com.zelex.po.Blog;
import com.zelex.po.Type;
import com.zelex.po.User;
import com.zelex.service.BlogService;
import com.zelex.service.TagService;
import com.zelex.service.TypeService;
import com.zelex.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author Zelex
 * @Date 2021/2/18 11:51
 * @Version 1.0
 */
@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    //用于显示整体页面的
    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model){

        Page<Blog> page = blogService.listBlog(pageable, blog);
        List<Type> types = typeService.listType();
        model.addAttribute("page",page);
        model.addAttribute("types",types);
        return LIST;
    }
    //相较于上一个方法，返回值的格式相当于是只更新页面一部分的值
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog,Model model){

        Page<Blog> page = blogService.listBlog(pageable, blog);
        model.addAttribute("page",page);
        return "admin/blogs :: blogList";
    }

    //新建博客
    @GetMapping("/blogs/input")
    public String input(Model model){
        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());//预增加一个博客
        return INPUT;
    }

    public void setTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }
    //修改博客
    @GetMapping("/blogs/{id}/input")
    public String EditInput(@PathVariable Long id, Model model){
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);//预增加一个博客
        return INPUT;
    }
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes redirectAttributes,HttpSession session){
        blog.setUser((User)session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b ;
        if (blog.getId() == null){
            b=blogService.saveBlog(blog);
        }else {
            b=blogService.updateBlog(blog.getId(),blog);
        }
        if (b == null){
            redirectAttributes.addFlashAttribute("message","新增失败！");
        }else{
            redirectAttributes.addFlashAttribute("message","新增成功！");
        }
        return REDIRECT_LIST;
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }
}
