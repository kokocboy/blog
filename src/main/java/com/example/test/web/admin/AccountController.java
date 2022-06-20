package com.example.test.web.admin;


import com.example.test.po.Blog;
import com.example.test.po.User;
import com.example.test.service.BlogService;
import com.example.test.service.UserService;
import com.example.test.util.ImageGallery;
import emailUtil.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import redis.clients.jedis.Jedis;
import threadPool.ThreadPool;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AccountController {
    /*



     */
    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;

    @GetMapping("/accountList")
    public String accountList(HttpSession session, Model model){
        System.out.println("合法进入 accountList");

        model.addAttribute("account",userService.queryAll());
        model.addAttribute("footerInfo",blogService.footerInfo());
        model.addAttribute("user",session.getAttribute("user"));
        return "/admin/accountList";
    }
    @GetMapping("/accountAdd")
    public String accountAdd(HttpSession session, Model model,
                             @RequestParam(required = false,defaultValue = "-1") Long userId){
        User user=(User) session.getAttribute("user");
        System.out.println(user);
        if(user==null||user.getType()>1){///兜底直接访问url地址,但是用户权限不够
            return "redirect:/admin/accountList";
        }
        if(userId!=-1){
            System.out.println("设置编辑信息="+userId);
            model.addAttribute("formMessage",userService.queryById(userId));
        }
        model.addAttribute("footerInfo",blogService.footerInfo());
        model.addAttribute("user",session.getAttribute("user"));
        return "/admin/accountAdd";
    }
    @PostMapping("/accountAdd")
    public String accountAdd(HttpSession session,@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String email,
                             @RequestParam(required = false,defaultValue = "-1") Long formMessageId,
                             @RequestParam(required = false,defaultValue = "-1") Integer permission){
        System.out.println("username="+username+" password="+password+" email="+email+" permission="+permission);
        User user=(formMessageId==-1?new User():userService.queryById(formMessageId));
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setType(permission);
        Date date=new Date();
        if(formMessageId==-1){
            user.setCreateTime(date);
            user.setAvatar(ImageGallery.randAvatarImage());
        }
        user.setUpdateTime(date);
        System.out.println("user="+user);
        userService.save(user);
        List<User> list=userService.queryppq();
        Long id= Long.valueOf(0);
        for(User user1:list){
            if(user1.getId()>id){
                id=user1.getId();
            }
        }
        insertJedis(user.getId().toString(),"true");
        User admin=(User) session.getAttribute("user");
        user=userService.queryById(id);
        ThreadPool.insertEmail(
                user.getEmail(),
                user.getUsername(),
                "个人博客系统",
                "请点击网址：http://localhost:8080/admin/accountConfirmPage?value="+id.toString()
                        +"&username="+admin.getUsername()+" 时间："+date.toString());
        return "redirect:/admin/accountList";
    }

    @PostMapping("/accountDelete")
    public String accountDelete(@RequestParam Long userId,@RequestParam Long adminId){
        System.out.println("deleteId="+userId+" adminType="+adminId);
        if(userService.queryById(userId).getType()>userService.queryById(adminId).getType()){///对delete user兜底策略
            userService.deleteById(userId);
        }
        return "redirect:/admin/accountList";
    }

    @GetMapping("/accountEdit")
    public String accountEdit(@RequestParam Long userId, @RequestParam Long adminId, RedirectAttributes redirectAttributes){
        System.out.println("编辑Id="+userId+" 使用Id="+adminId);
        if(userService.queryById(adminId).getType()!=0){//兜底
            return "redirect:/admin/accountList";
        }
        redirectAttributes.addAttribute("userId",userId);
        return "redirect:/admin/accountAdd";
    }
    @GetMapping("/accountConfirmPage")
    public String accountConfirmPage(@RequestParam(required = false,defaultValue = "-1") Long value,
                                     @RequestParam(required = false,defaultValue = "null") String username,
                                     Model model){
        model.addAttribute("userId",value);
        model.addAttribute("username",username);
        return "/admin/accountConfirmPage";
    }
    @PostMapping("/accountReplyPage")
    public String accountReplyPage(@RequestParam(required = false,defaultValue = "-1") Long userId){
        User user= userService.queryById(userId);
        if(user!=null){
            user.setConfirm(true);
            if(getJedis(user.getId().toString())!=null){
                userService.save(user);
            }
        }
        System.out.println("value="+userId);
        return "redirect:/admin";
    }


    ///redis操作 过期操作
    public static void insertJedis(String key,String value){
        Jedis jedis = new Jedis("47.109.21.195",6379);
        jedis.auth("1072303326ygq");
        jedis.setex(key,60*10,value);
        jedis.close();
    }
    public static String getJedis(String key){
        Jedis jedis = new Jedis("47.109.21.195",6379);
        jedis.auth("1072303326ygq");
        String value=jedis.get(key);
        jedis.close();
        return value;
    }


}
