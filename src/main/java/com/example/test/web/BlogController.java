package com.example.test.web;

import com.example.test.po.Blog;
import com.example.test.po.Comment;
import com.example.test.service.BlogService;
import com.example.test.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/blog")
    public String blog(@RequestParam Long blogId,
                            Model model){
        Blog blog=blogService.queryBlogById(blogId);
        List<Comment> list=blog.getComments();
        for(Comment comment:list){
            List<Comment> listSon=comment.getReplyComments();
            if(listSon==null){
                continue;
            }
        }
        if(blog.getViews()==null){
            blog.setViews(1);
        }
        else {
            blog.setViews(blog.getViews()+1);
        }
        blogService.insert(blog);


        model.addAttribute("blog",blogService.showBLogByMarkdown(blog));
        model.addAttribute("comments",blog.getComments());
        model.addAttribute("footerInfo",blogService.footerInfo());
        return "/blog";
    }
    @PostMapping("/commentSubmit")
    public String commentSubmit(@RequestParam Long blogId,
                                @RequestParam(required = false,defaultValue = "-1") Long replyId,
                                @RequestParam String replyName,
                                @RequestParam String replyEmail,
                                @RequestParam String replyMessage,
                                RedirectAttributes redirectAttributes){
        commentService.insert(blogService.queryBlogById(blogId),replyId,replyName,replyEmail,replyMessage);
        redirectAttributes.addAttribute("blogId",blogId);
        return "redirect:/blog";
    }
}
