package com.example.demo;

import com.example.demo.Classes.Response;
import com.example.demo.Classes.User;
import com.example.demo.Classes.UserWithoutId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(method = RequestMethod.POST, value = "/user")
    public String addUser(@RequestBody UserWithoutId userWithoutId){
        User userWithId = new User();
        userWithId=userWithId.getDetailsFromUserWithoutId(userWithoutId);

        try {
            userService.createUser(userWithId);    //create new user
            return "User added Successfully with user id: "+userWithId.getUserID();
        }
        catch(DataIntegrityViolationException d){  //in case of same phoneNumber or same EmailId or userName
            return "User already exists";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}")
    public Response getUser(@PathVariable Long userId){
        List<User> userList= userService.readUser(userId);
        Response response = new Response(); //this is created because we want 3 details(username, mobilenumber, emailid) out of given 6 details of user.

        if(userList.isEmpty()){                         //in case of user with given userID does not exist
            return response;
        }
        else{                                  //in order to return the details of user with given user id
            response.emailID=userList.get(0).getEmailID();
            response.userName=userList.get(0).getUserName();
            response.mobileNumber=userList.get(0).getMobileNumber();
            return response;
        }
    }

    @RequestMapping(method = RequestMethod.PUT,value = "/user/{usid}")
    public String updateUser(@RequestBody UserWithoutId userWithoutId, @PathVariable Long userId){
        User userWithId = new User();
        userWithId=userWithId.getDetailsFromUserWithoutId(userWithoutId);
        userWithId.setUserID(userId);

        Response response=  getUser(userId); // calling getUser(userId) to check if user exist or not with user id: usid

        if(response.userName==null)     //in case of user with given userID doesn't exist
            return "User doesn't exist";

            userService.updateUser(userWithId);  //Update user with given userID
            return "User updated";
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/user/{userId}")
    public String deleteUser(@PathVariable Long userId){
        try {
            userService.deleteUser(userId);  //Delete user with given userID
            return "User deleted";
        }
         catch(EmptyResultDataAccessException d){ //in case of user with given user id doesn't exist
            return "User doesn't exists";
        }
    }

}
