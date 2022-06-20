package com.example.test.service;

import com.example.test.dao.TypeRepository;
import com.example.test.po.Tag;
import com.example.test.po.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import java.util.Comparator;
import java.util.List;

@Controller
public class TypeService
{
    @Autowired
    private TypeRepository typeRepository;

    public void insert(String name){
        Type type=new Type();
        type.setName(name);
        typeRepository.save(type);
    }

    public Page<Type> queryAll(Pageable pageable){
        return typeRepository.findAll(pageable);
    }
    public void Delete(long id){
        Type type=new Type();
        type.setId(id);
        typeRepository.delete(type);
    }

    public void update(Long id,String name){
        Type type=new Type();
        type.setId(id);
        type.setName(name);
        typeRepository.save(type);
    }
    public Type queryById(Long id){
        Type type=typeRepository.findById(id).get();
        System.out.println(type.getName()+"    "+type.getBlogs().size());
        return typeRepository.findById(id).get();
    }
    ///根据type使用数量进行排序
    public List<Type> listTypeAll(){
        List<Type> list=typeRepository.findAll();
        list.sort(new Comparator<Type>() {
            @Override
            public int compare(Type o1, Type o2) {
                return o2.getBlogs().size()-o1.getBlogs().size();
            }
        });
        return list;
    }


}
