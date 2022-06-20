package com.example.test.web.admin;

import com.example.test.dao.UserRepository;
import com.example.test.handler.ControllerExceptionHandler;
import com.example.test.po.User;
import com.example.test.service.BlogService;
import com.example.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import threadPool.ThreadPool;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;

    ///登录逻辑
    @GetMapping
    public String loginPage(){
        return "/admin/login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession httpSession,
                        RedirectAttributes redirectAttributes){
        User user=userService.checkUser(username,password);
        if(user==null){
            redirectAttributes.addFlashAttribute("message","用户名或密码错误");
            return "redirect:/admin";
        }else {
            httpSession.setAttribute("user",user);
            Date date=new Date();
            System.out.println(date.toString());
            ThreadPool.insertEmail(user.getEmail(),
                    user.getUsername(),
                    "个人博客系统",
                    "你已经登录个人博客系统，如果非本人操作，请修改密码。时间："+date.toString());

            return "redirect:/admin/index";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession){
        httpSession.removeAttribute("user");
        return "redirect:/admin";
    }
    ///首页逻辑
    @GetMapping("/index")
    public String index(HttpSession session,Model model){
        model.addAttribute("footerInfo",blogService.footerInfo());
        model.addAttribute("user",session.getAttribute("user"));
        return "/admin/index";
    }
}
