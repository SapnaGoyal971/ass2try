package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public void  CreateUser(User user) {
        userRepository.save(user);
    }

    public List<User> ReadUser(Long userid){
        List<User> us=new ArrayList<>();
        userRepository.findById(userid).ifPresent(us::add);
        return us;
    }

    public void UpdateUser(User user){
        userRepository.save(user);
    }

    public void DeleteUser(Long userid){
        userRepository.deleteById(userid);
    }

}
