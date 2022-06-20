package com.example.test.po;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_user" )//数据库相关，默认表
public class User
{
    @Id
    @GeneratedValue
    private Long id;
    private String nickname;//昵称
    private String username;//用户名
    private String password;//密码
    private String email;//邮箱
    private String avatar;//头像
    private Integer type;//类型(超级用户0 高级用户1 普通用户2)
    /*
    0：拥有1的权限，并且可以修改任何用户（橘红色）
    1：拥有2的权限，并且新增用户权限（紫色）
    2：只能查看有账号用户基本信息 （蓝色）
     */
    @Temporal(TemporalType.TIMESTAMP)//指定时间类型，全格式
    private Date createTime;//创建日期
    @Temporal(TemporalType.TIMESTAMP)//指定时间类型，全格式
    private Date updateTime;//更新日期
    private boolean confirm;

    @OneToMany(mappedBy = "user")
    List<Blog> blogs=new ArrayList<>();
    public User() {
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", type=" + type +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
