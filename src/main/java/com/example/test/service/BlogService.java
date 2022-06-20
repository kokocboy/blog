package com.example.test.service;

import com.example.test.dao.BlogRepository;
import com.example.test.dao.TypeRepository;
import com.example.test.po.Blog;
import com.example.test.po.Tag;
import com.example.test.po.Type;
import com.example.test.po.User;
import com.example.test.util.MarkdownUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.swing.plaf.basic.BasicListUI;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private TypeRepository typeRepository;

    public  void insert(Blog blog){
        blogRepository.save(blog);
    }

    //筛选推荐=true，发布状态=true,排序观看次数
    public Page<Blog> queryIndexShowBlogs(Pageable pageable){
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates=new ArrayList<>();
                ///约束条件一：推荐为true
                predicates.add(criteriaBuilder.equal(root.<Type>get("recommend"),true ));
                ///约束条件二：发布状态为true
                predicates.add(criteriaBuilder.equal(root.<Type>get("published"), true));
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }
    public Page<Blog> queryAll(Pageable pageable){
        return blogRepository.findAll(pageable);
    }

    public List<Blog> queryAllList(){
        return blogRepository.findAll();
    }
    public Page<Blog> queryAllByFilterTitleOrTypeId(String title,Long typeId,Pageable pageable){
        Blog blog=new Blog();
        System.out.println(title);
        if (title != "-1") {
           // blog.setTitle(title);
        }
        if(typeId!=-1){
            blog.setType(typeRepository.getById(typeId));
        }
        Example<Blog> example=Example.of(blog);
        List<Blog> list=blogRepository.findAll(example);
        return blogRepository.findAll(pageable);
    }
    ///Blog分页查询，附带过滤某些特征
    public Page<Blog> listBlog(Pageable pageable, Blog blog, User user) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates=new ArrayList<>();
                if(blog.getTitle()!=null){///中缀相同匹配
                    predicates.add(criteriaBuilder.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                if(blog.getType()!=null){///typeId等于匹配
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"), blog.getType().getId()));
                }
                if(user!=null&&user.getType()!=0){
                    predicates.add(criteriaBuilder.equal(root.<Blog>get("user"),user));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    ///Blog分页查询，附带过滤出不等于typeId的博客
    public Page<Blog> pageBlogByTypeId(Pageable pageable, Long typeId) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates=new ArrayList<>();
                if(typeId==-1){///typeId等于匹配
                    predicates.add(criteriaBuilder.notEqual(root.<Type>get("type").get("id"), typeId));
                }else {
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"), typeId));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    ///Blog分页查询，附带过滤出不等于tagId的博客
    public Page<Blog> pageBlogByTagId(Pageable pageable, Long tagId) {
        System.out.println("test陈工");
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates=new ArrayList<>();
                if(tagId==-1){
                    predicates.add(criteriaBuilder.notEqual(root.<Type>get("id"), tagId));
                    query.where(predicates.toArray(new Predicate[predicates.size()]));
                    return null;
                }else{
                    return criteriaBuilder.equal(root.join("tags").get("id"),tagId);
                }
            }
        },pageable);
    }



    public void delete(long id){
        blogRepository.deleteById(id);
    }


    public Blog queryBlogById(Long id){
        return blogRepository.findById(id).get();
    }


    ///最新top5的博客
    public List<Blog> queryToByUpdateTime(Integer number){
        Sort sort=Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable= PageRequest.of(0,number,sort);
        List<Blog> te=blogRepository.findTopBy(pageable);
        return blogRepository.findTopBy(pageable);
    }
    public Blog showBLogByMarkdown(Blog blog){
        Blog showBlog=new Blog();
        BeanUtils.copyProperties(blog,showBlog);
        showBlog.setContent(MarkdownUtils.markdownToHtmlExtensions(showBlog.getContent()));
        return  showBlog;
    }


    ////footerInfo
    public List<Integer> footerInfo(){
        List<Integer> ans=new ArrayList<>();
        List<Blog> listBligs=blogRepository.findAll();
        ans.add(listBligs.size());
        Integer num=0;
        for(Blog blog:listBligs){
            if(blog.getComments()!=null){
                num+=blog.getComments().size();
            }
        }
        ans.add(num);
        num=0;
        for(Blog blog:listBligs){
            num+=blog.getViews();
        }
        ans.add(num);
        return ans;
    }

}
