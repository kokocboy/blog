package com.example.test.web.admin;

import com.example.test.service.BlogService;
import com.example.test.service.TagService;
import com.example.test.service.TypeService;
import com.example.test.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/*
分类控制层
 */
@Controller
@RequestMapping("/admin")
public class TagController {
    @Autowired
    private  BlogService blogService;
    @Autowired
    private TagService tagService;
    private final int pageSize=3;
    @GetMapping("/tagList")
    public String tagManager(HttpSession session, @PageableDefault(size = pageSize,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable,
                             Model model){
        model.addAttribute("tagPages",tagService.queryAll(pageable));
        model.addAttribute("footerInfo",blogService.footerInfo());
        model.addAttribute("user",session.getAttribute("user"));
        return "/admin/tagList";
    }
    @PostMapping("/tagAdd/submit")
    public String submit(HttpSession session,RedirectAttributes attributes,
                         @RequestParam String name,
                         @RequestParam(required = false,defaultValue ="-1") Long id){

        if(name!=null&&name!=""){
            if(id==-1){
                tagService.insert(name);
            }
            else {
                tagService.update(id,name);
            }
            attributes.addFlashAttribute("message","恭喜，操作成功！");
        }else{
            attributes.addFlashAttribute("message","不能新增空标签");
            return "redirect:/admin/tagAdd";
        }
        return "redirect:/admin/tagList";
    }
    @GetMapping("/tagAdd/back")
    public String back(){
        return "redirect:/admin/tagList";
    }
    @GetMapping("/tagDelete")
    public String Delete(@RequestParam long id){
        tagService.Delete(id);
        return "redirect:/admin/tagList";
    }

    @GetMapping("/tagEdit")
    public String tagEdit(@RequestParam Long id,
                          @RequestParam String name,
                          RedirectAttributes redirectAttributes){
        System.out.println("-----"+id+"  "+name);
        redirectAttributes.addFlashAttribute("id",id);
        redirectAttributes.addFlashAttribute("name",name);
        return "redirect:/admin/tagAdd";
    }
    @GetMapping("/tagAdd")
    public String tagAdd(HttpSession session,Model model){
        model.addAttribute("footerInfo",blogService.footerInfo());
        model.addAttribute("user",session.getAttribute("user"));
        return "/admin/tagAdd";
    }
}
