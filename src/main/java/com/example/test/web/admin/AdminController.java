package com.example.test.web.admin;

import com.example.test.po.Blog;
import com.example.test.po.Tag;
import com.example.test.po.Type;
import com.example.test.po.User;
import com.example.test.service.BlogService;
import com.example.test.service.TagService;
import com.example.test.service.TypeService;
import com.sun.jdi.LongValue;
import net.bytebuddy.implementation.bytecode.StackSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final int pageSize=3;

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/indexManager")
    public String Manager(HttpSession session, @PageableDefault(size = pageSize,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
                          @RequestParam(required = false,defaultValue ="-1") String title,
                          @RequestParam(required = false,defaultValue ="-1") Long typeId,
                          @RequestParam(required = false,defaultValue ="-1") Long callBackTypeId,
                          Model model){

        ///传递前端参数
        if(!title.equals("-1")){
            model.addAttribute("callBackTitle",title);
        }

        Long acceptTypeId=Long.parseLong(String.valueOf(-1));
        if(typeId!=-1||callBackTypeId!=-1){///处理分类搜索后，保留原有信息逻辑
            acceptTypeId=typeId;
            if(typeId==-1){
                acceptTypeId=callBackTypeId;
            }
        }
        model.addAttribute("callBackTypeId",acceptTypeId);
        model.addAttribute("blogPages",blogService.listBlog(pageable,getBlog(title,acceptTypeId),(User)session.getAttribute("user")));//博客分页
        model.addAttribute("types",typeService.listTypeAll());//分类下拉框
        model.addAttribute("footerInfo",blogService.footerInfo());
        model.addAttribute("user",session.getAttribute("user"));
        return "/admin/indexManager";
    }

    ///将前端的参数构造Blog对象，进行储存
    @PostMapping("/blogSubmit")
    public String submit(HttpSession session,
                         @RequestParam String flag,
                         @RequestParam(defaultValue = "-1",required = false) Long blogId,
                         @RequestParam String title,
                         @RequestParam String content,
                         @RequestParam String firstPicture,
                         @RequestParam boolean recommend,
                         @RequestParam boolean shareStatement,
                         @RequestParam boolean appreciation,
                         @RequestParam boolean commentabled,
                         @RequestParam String description,
                         @RequestParam Long typeId,
                         @RequestParam List<Long> tagIds,
                         @RequestParam boolean published){
        Blog blog=(blogId==-1?new Blog():blogService.queryBlogById(blogId));//编辑老博客导致信息遗漏，bug已经修复
        blog.setFlag(flag==""?"原创":flag);
        blog.setTitle(title);
        blog.setContent(content);
        blog.setType(typeService.queryById(typeId));
        blog.setTags(tagService.queryByListId(tagIds));
        blog.setFirstPicture(firstPicture);
        blog.setRecommend(recommend);
        blog.setShareStatement(shareStatement);
        blog.setAppreciation(appreciation);
        blog.setCommentabled(commentabled);
        blog.setPublished(published);
        blog.setDescription(description);

        Date date=new Date();
        if(blogId==-1){
            blog.setCreateTime(date);
            blog.setViews(1);
            blog.setUser((User)session.getAttribute("user"));
        }
        blog.setUpdateTime(date);

        blogService.insert(blog);


        return "redirect:/admin/indexManager";
    }
    @GetMapping("/test")
    public String test(){
        return "/admin/test";
    }

    @GetMapping("/blogDelete")
    public String blogDelete(@RequestParam long id){
        blogService.delete(id);
        return "redirect:/admin/indexManager";
    }
    @GetMapping("/blogBack")
    public String blogBack(){
        return "redirect:/admin/indexManager";
    }
    @GetMapping("/blogEdit")//由于编辑需要博客的参数，所有需要提前获得博客的所有参数，再进入：/indexRelease
    public String blogEdit(@RequestParam Long id,
                           RedirectAttributes redirectAttributes){
        Blog blog=blogService.queryBlogById(id);
        blog.init();
        redirectAttributes.addFlashAttribute("blog",blog);
        return "redirect:/admin/indexRelease";
    }

    @GetMapping("/indexRelease")
    public String Release(HttpSession session,Model model){
        model.addAttribute("tags",tagService.listTagAll());
        model.addAttribute("types",typeService.listTypeAll());
        model.addAttribute("footerInfo",blogService.footerInfo());
        model.addAttribute("user",session.getAttribute("user"));
        return "/admin/indexRelease";
    }

    @GetMapping("/indexSearch")
    public String indexSearch(@RequestParam(required = false,defaultValue ="-1") String title,
                              @RequestParam(required = false,defaultValue ="-1") Long typeId,RedirectAttributes redirectAttributes){
       // logger.info("title={} type={}",title,typeId);
        return "redirect:/admin/indexManager";
    }

    public Blog getBlog(String title,Long id){
        Blog blog=new Blog();
        if(!title.equals("-1")){
            blog.setTitle(title);
        }
        if(id!=-1){
            blog.setType(typeService.queryById(id));
        }
        return blog;
    }
}
