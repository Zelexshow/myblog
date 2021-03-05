package com.zelex.web.admin;

import com.zelex.po.Tag;
import com.zelex.po.Type;
import com.zelex.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


/**
 * @Author Zelex
 * @Date 2021/2/19 19:41
 * @Version 1.0
 */

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    //首先是首页显示
    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 3,sort = {"id"},direction = Sort.Direction.DESC)Pageable pageable, Model model){
        Page<Tag> tags = tagService.listTag(pageable);
        model.addAttribute("page",tags);
        return "admin/tags";
    }

    //跳转到新增
    @GetMapping("/tags/input")
    public String input(Model model){
        model.addAttribute("tag",new Tag());

        return "admin/tags-input";
    }
    //编辑标签按钮触发
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id,Model model){
        model.addAttribute("tag",tagService.getTag(id));

        //还是跳转到添加标签的页面（只是公用一个前端而已）
        return "admin/tags-input";
    }
    //新增标签
    @PostMapping("/tags")
    public String postTags(@Valid Tag tag, BindingResult result, RedirectAttributes redirectAttributes){
        Tag t1 = tagService.getTagByName(tag.getName());
        if (t1 != null){
            result.rejectValue("name","nameError","不能添加重复标签");
        }
        if (result.hasErrors()){
            return "admin/tags-input";
        }
        Tag t = tagService.saveTag(tag);
        if (t==null){
            redirectAttributes.addFlashAttribute("message","新增失败！");
        }else{
            redirectAttributes.addFlashAttribute("message","新增成功！");
        }
        return "redirect:/admin/tags";
    }
    
    //修改提交
    @PostMapping("/tags/{id}")
    public String EditPost(@Valid Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes redirectAttributes){
        //首先到数据库中查询以下是否存在分类
        Tag tagByName = tagService.getTagByName(tag.getName());
        if (tagByName!=null){
            result.rejectValue("name","nameError","不能添加重复的标签");
        }

        if (result.hasErrors()){
            return "admin/tags-input";
        }
        Tag t = tagService.updateTag(id,tag);
        if (t == null){
            redirectAttributes.addFlashAttribute("message","修改失败！");
        }else{
            redirectAttributes.addFlashAttribute("message","修改成功！");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String deleteTag(@PathVariable Long id,RedirectAttributes redirectAttributes){
        tagService.deleteTag(id);
        redirectAttributes.addFlashAttribute("message","删除标签成功！");
        return "redirect:/admin/tags";
    }

}
