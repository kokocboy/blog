package com.example.test.web;

import com.example.test.po.Blog;
import com.example.test.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/search")
    public String search(@PageableDefault(size = 4,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam(required = false,defaultValue = "-1") String searchName,
                         Model model){
        Blog blog=new Blog();
        System.out.println("test="+searchName);
        if(!searchName.equals("-1")){
            blog.setTitle(searchName);
        }
        model.addAttribute("pages",blogService.listBlog(pageable,blog,null));
        model.addAttribute("footerInfo",blogService.footerInfo());
        return "/search";
    }
}
