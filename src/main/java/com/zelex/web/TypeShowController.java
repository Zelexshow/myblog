package com.zelex.web;

import com.zelex.po.Type;
import com.zelex.service.BlogService;
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
//用于前端分类页的展示
@Controller
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model){
        List<Type> types = typeService.listTypeTop(1000);
        if (id == -1){//表示显示的是首页，不指定指定id
            id=types.get(0).getId();
        }
        BlogQuery blogQuery=new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types",types);
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        model.addAttribute("activeTypeId",id);//id用于页面显示是否是标记页
        return "types";
    }
}
