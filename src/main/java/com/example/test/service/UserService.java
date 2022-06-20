package com.example.test.service;

import com.example.test.dao.UserRepository;
import com.example.test.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User checkUser(String name,String password){
        User user=userRepository.findByUsernameAndPassword(name,password);
        if(user!=null){
            if(!user.isConfirm()) return null;
        }
        return user;
    }

    public List<User> queryppq(){return userRepository.findAll();};
    public List<User> queryAll(){
        List<User> list=userRepository.findAll(),ans=new ArrayList<>();
        for(User user:list){
            if(user.isConfirm()){
                ans.add(user);
            }else{
                Jedis jedis = new Jedis("47.109.21.195",6379);
                jedis.auth("1072303326ygq");
                String value=jedis.get(user.getId().toString());
                jedis.close();
                System.out.println(value+"  ="+user.getId());
                if(value==null){
                    userRepository.delete(user);
                }
            }
        }
        return ans;
    }
    public void deleteById(Long id){
        userRepository.deleteById(id);
        return ;
    }
    public User queryById(Long id){
        return userRepository.findById(id).get();
    }
    public void save(User user){
        userRepository.save(user);
        return;
    }
}
