package com.example.test.web;

import com.example.test.po.Blog;
import com.example.test.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

class ArchivesContent{
    private int total;
    private List<List<Blog>>lists;
    ArchivesContent(){
        lists=new ArrayList<>(new ArrayList<>());
    }
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<List<Blog>> getLists() {
        return lists;
    }

    public void setLists(List<List<Blog>> lists) {
        this.lists = lists;
    }
    public void addList(List<Blog> blogs){
        lists.add(blogs);
    }
}
@Controller
public class ArchivesController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String archives(Model model){
        List<Blog> list=blogService.queryAllList();
        list.sort(new Comparator<Blog>() {
            @Override
            public int compare(Blog o1, Blog o2) {
                return o1.getCreateTime().compareTo(o2.getCreateTime())*-1;
            }
        });

        //归档排序逻辑
        ArchivesContent archivesContent=new ArchivesContent();
        archivesContent.setTotal(list.size());
        Set<Integer>se=new HashSet<>();
        List<Blog>lists=new ArrayList<>();
        for(Blog blog:list){
            Integer year=dateGetYear(blog.getCreateTime());
            if(!se.contains(year)&&!se.isEmpty()){
                List<Blog>temp=new ArrayList<>();
                temp.addAll(lists);
                archivesContent.addList(temp);
                lists.clear();
            }
            se.add(year);
            lists.add(blog);
        }
        if(lists!=null&&!lists.isEmpty()){
            archivesContent.addList(lists);
        }
        model.addAttribute("archivesContent",archivesContent);
        model.addAttribute("footerInfo",blogService.footerInfo());
        return "/archives";
    }

    public Integer dateGetYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return Integer.valueOf(calendar.get(Calendar.YEAR));
    }
}
