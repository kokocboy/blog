package com.example.test.po;


import javax.persistence.*;
import java.security.Identity;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_type" )//数据库相关，默认表
public class Type
{
    @Id//确定主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//确定主键策略为自增长
    private Long id;
    private String name;

    @OneToMany(mappedBy = "type")//关系被维护端
    private List<Blog> blogs=new ArrayList<>();
    public Type() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
