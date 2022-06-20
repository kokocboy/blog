package com.example.test.web.admin;

import com.example.test.service.BlogService;
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
public class TypeController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    private final int pageSize=3;
    @GetMapping("/typeList")
    public String typeManager(HttpSession session, @PageableDefault(size = pageSize,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable,
                              Model model){
        model.addAttribute("typePages",typeService.queryAll(pageable));
        model.addAttribute("footerInfo",blogService.footerInfo());
        model.addAttribute("user",session.getAttribute("user"));
        return "/admin/typeList";
    }
    @PostMapping("/typeAdd/submit")
    public String submit(HttpSession session,
                         RedirectAttributes attributes,
                         @RequestParam String name,
                         @RequestParam(required = false,defaultValue ="-1") Long id){
        if(name!=null&&name!=""){
            if(id==-1){
                typeService.insert(name);
            }
            else {
                typeService.update(id,name);
            }
            attributes.addFlashAttribute("message","恭喜，操作成功！");
        }else{
            attributes.addFlashAttribute("message","不能新增空名称");
            return "redirect:/admin/typeAdd";
        }
        return "redirect:/admin/typeList";
    }
    @GetMapping("/typeAdd/back")
    public String back(){
        return "redirect:/admin/typeList";
    }
    @GetMapping("/typeDelete")
    public String Delete(@RequestParam long id){
        typeService.Delete(id);
        return "redirect:/admin/typeList";
    }
    @GetMapping("/typeEdit")
    public String typeEdit(@RequestParam Long id,
                           @RequestParam String name,
                           RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("id",id);
        redirectAttributes.addFlashAttribute("name",name);
        return "redirect:/admin/typeAdd";
    }
    @GetMapping("/typeAdd")
    public String typeAdd(HttpSession session,Model model){
        model.addAttribute("footerInfo",blogService.footerInfo());
        model.addAttribute("user",session.getAttribute("user"));
        return "/admin/typeAdd";
    }
}
