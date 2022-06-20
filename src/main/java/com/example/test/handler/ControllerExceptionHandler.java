package com.example.test.handler;

import com.example.test.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControllerExceptionHandler implements ErrorController {

    @Autowired
    private BlogService blogService;
    @Autowired
    HttpServletRequest request;


    @RequestMapping("/error")
    public String getErrorPath(Model model) {
        model.addAttribute("footerInfo",blogService.footerInfo());
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        System.out.println("错误代码"+statusCode);
        switch (statusCode) {
            case 404:
                return "/error/c404";
            default:
                return "/error/c500";
        }
    }
}