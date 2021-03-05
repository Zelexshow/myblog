package com.zelex.web;

import com.zelex.po.Tag;
import com.zelex.po.Type;
import com.zelex.service.BlogService;
import com.zelex.service.TagService;
import com.zelex.service.TypeService;
import com.zelex.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Author Zelex
 * @Date 2021/2/23 12:04
 * @Version 1.0
 */
//用于前端标签页的展示
@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model){
        List<Tag> tags = tagService.listTagTop(1000);
        if (id == -1){//表示显示的是首页，不指定指定id
            id=tags.get(0).getId();
        }
        model.addAttribute("tags",tags);
        model.addAttribute("page",blogService.listBlog(id,pageable));
        model.addAttribute("activeTagId",id);//id用于页面显示是否是标记页
        return "tags";
    }
}
