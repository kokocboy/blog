package com.example.test.service;

import com.example.test.dao.TagRepository;
import com.example.test.dao.TypeRepository;
import com.example.test.po.Tag;
import com.example.test.po.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.util.Comparator;
import java.util.List;

@Controller
public class TagService
{
    @Autowired
    private TagRepository tagRepository;

    public void insert(String name){
        Tag tag=new Tag();
        tag.setName(name);
        tagRepository.save(tag);
    }

    public Page<Tag> queryAll(Pageable pageable){
        return tagRepository.findAll(pageable);
    }
    public void Delete(long id){
        Tag tag=new Tag();
        tag.setId(id);
        tagRepository.delete(tag);
    }
    public Tag queryById(Long id){
        return tagRepository.findById(id).get();
    }

    public void update(Long id,String name){
        Tag tag=new Tag();
        tag.setId(id);
        tag.setName(name);
        tagRepository.save(tag);
    }

    public List<Tag> listTagAll(){
        List<Tag> list=tagRepository.findAll();
        list.sort(new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                return o2.getBlogs().size()-o1.getBlogs().size();
            }
        });
        return list;
    }


    public List<Tag> queryByListId(List<Long> list){
        return tagRepository.findAllById(list);
    }

}
