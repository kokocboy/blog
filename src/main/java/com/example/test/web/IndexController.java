package com.example.test.web;

import com.example.test.po.Blog;
import com.example.test.service.BlogService;
import com.example.test.service.TagService;
import com.example.test.service.TypeService;
import com.example.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 4,sort = {"views"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){

        model.addAttribute("pages",blogService.queryIndexShowBlogs(pageable));
        model.addAttribute("types",typeService.listTypeAll());
        model.addAttribute("tags",tagService.listTagAll());
        model.addAttribute("blogTop",blogService.queryToByUpdateTime(4));
        model.addAttribute("footerInfo",blogService.footerInfo());
        return "index";
    }



    ////关于我页面，由于设计逻辑较少，就没有单纯新建一个类来处理
    @GetMapping("/about")
    public String about(Model model){
        model.addAttribute("footerInfo",blogService.footerInfo());

        return "/about";
    }

    @GetMapping("/cboyTest")
    public String cboyTest(Model model){
        model.addAttribute("footerInfo",blogService.footerInfo());
        return "/cboyTest";
    }

}
