package com.example.test.service;

import com.example.test.dao.CommentRepository;
import com.example.test.po.Blog;
import com.example.test.po.Comment;
import com.example.test.util.ImageGallery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    public void insert(Blog blog,Long replyId,String replyName,String replyEmail,String replyMessage){
        Comment comment=new Comment();
        comment.setBlog(blog);
        comment.setNickname(replyName);
        comment.setEmail(replyEmail);
        comment.setContent(replyMessage);
        comment.setCreatetime(new Date());
        comment.setAvatar(ImageGallery.randImage());
        if(replyId!=-1){
            Comment fatComment=commentRepository.findById(replyId).get();
            comment.setParentComment(fatComment);
        }
        commentRepository.save(comment);
    }
}
