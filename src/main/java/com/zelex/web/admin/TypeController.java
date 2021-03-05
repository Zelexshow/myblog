package com.zelex.web.admin;

import com.zelex.po.Type;
import com.zelex.service.TypeService;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @Author Zelex
 * @Date 2021/2/18 18:54
 * @Version 1.0
 */
@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;
    //跳转到标签总页面
    @GetMapping("/types")
    public String type(@PageableDefault(size = 3,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
                       Model model){
        Page<Type> types = typeService.listType(pageable);
        model.addAttribute("page",types);
        return "admin/types";
    }
    //跳转到新建标签页面
    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }

    //编辑标签按钮触发
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable("id") Long id,Model model){
        model.addAttribute("type",typeService.getType(id));

        //还是跳转到添加分类的页面（只是公用一个前端而已）
        return "admin/types-input";
    }
    //新增标签
    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result, RedirectAttributes redirectAttributes){
        //首先到数据库中查询以下是否存在分类
        Type typeByName = typeService.getTypeByName(type.getName());
        if (typeByName!=null){
            result.rejectValue("name","nameError","不能添加重复的分类");
        }

        if (result.hasErrors()){
            return "admin/types-input";
        }
        Type t = typeService.saveType(type);
        if (t == null){
            redirectAttributes.addFlashAttribute("message","新增失败！");
        }else{
            redirectAttributes.addFlashAttribute("message","新增成功！");
        }
        return "redirect:/admin/types";
    }
    //修改提交
    @PostMapping("/types/{id}")
    public String EditPost(@Valid Type type, BindingResult result,@PathVariable Long id, RedirectAttributes redirectAttributes){
        //首先到数据库中查询以下是否存在分类
        Type typeByName = typeService.getTypeByName(type.getName());
        if (typeByName!=null){
            result.rejectValue("name","nameError","不能添加重复的分类");
        }

        if (result.hasErrors()){
            return "admin/types-input";
        }
        Type t = typeService.updateType(id,type);
        if (t == null){
            redirectAttributes.addFlashAttribute("message","修改失败！");
        }else{
            redirectAttributes.addFlashAttribute("message","修改成功！");
        }
        return "redirect:/admin/types";
    }

    //删除
    @GetMapping("/types/{id}/delete")
    public String deleteType(@PathVariable Long id,RedirectAttributes redirectAttributes){

        typeService.deleteType(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/types";
    }


}
