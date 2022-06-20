package com.example.test.web;

import com.example.test.service.BlogService;
import com.example.test.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TypesController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size = 4,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(required = false,defaultValue = "-1") Long typeId,
                        @RequestParam(required = false,defaultValue = "-1") Long acceptTypeId,
                        Model model){
        typeId=(typeId==acceptTypeId?-1:typeId);
        model.addAttribute("pages",blogService.pageBlogByTypeId(pageable,typeId));
        model.addAttribute("types",typeService.listTypeAll());
        model.addAttribute("acceptTypeId",typeId);
        model.addAttribute("footerInfo",blogService.footerInfo());
        return "/types";
    }
}
