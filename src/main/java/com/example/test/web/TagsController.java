package com.example.test.web;

import com.example.test.po.Blog;
import com.example.test.po.Tag;
import com.example.test.service.BlogService;
import com.example.test.service.TagService;
import com.example.test.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TagsController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 4,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(required = false,defaultValue = "-1") Long tagId,
                        @RequestParam(required = false,defaultValue = "-1") Long acceptTagId,
                        Model model){
        tagId=(tagId==acceptTagId?-1:tagId);
        model.addAttribute("pages",blogService.pageBlogByTagId(pageable,tagId));
        model.addAttribute("tags",tagService.listTagAll());
        model.addAttribute("acceptTagId",tagId);
        model.addAttribute("footerInfo",blogService.footerInfo());
        return "/tags";
    }
}
