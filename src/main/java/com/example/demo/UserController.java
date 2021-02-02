package com.example.demo;

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
    public String adduser(@RequestBody UserwithoutId user){
        User u = new User();
        u=u.getdetailsfromUserwithoutId(user);

        try {
            userService.CreateUser(u);    //create new user
            return "User added Successfully with user id: "+u.getUserID();
        }
        catch(DataIntegrityViolationException d){  //in case of same phoneNumber or same EmailId or userName
            return "User already exists";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{usid}")
    public Response getuser(@PathVariable Long usid){
        List<User> u= userService.ReadUser(usid);
        Response response = new Response(); //this is created because we want 3 details(username, mobilenumber, emailid) out of given 6 details of user.

        if(u.isEmpty()){                         //in case of user with given userID does not exist
            return response;
        }
        else{                                  //in order to return the details of user with given user id
            response.emailID=u.get(0).getEmailID();
            response.userName=u.get(0).getUserName();
            response.mobileNumber=u.get(0).getMobileNumber();
            return response;
        }
    }

    @RequestMapping(method = RequestMethod.PUT,value = "/user/{usid}")
    public String updateuser(@RequestBody UserwithoutId u, @PathVariable Long usid){
        User user = new User();
        user=user.getdetailsfromUserwithoutId(u);
        user.setUserID(usid);

        Response uss=  getuser(usid); // calling getuser(usid) to check if user exist or not with user id: usid

        if(uss.userName==null)     //in case of user with given userID doesn't exist
            return "User doesn't exist";

            userService.UpdateUser(user);  //Update user with given userID
            return "User updated";
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/user/{usid}")
    public String deleteuser(@PathVariable Long usid){
        try {
            userService.DeleteUser(usid);  //Delete user with given userID
            return "User deleted";
        }
         catch(EmptyResultDataAccessException d){ //in case of user with given user id doesn't exist
            return "User doesn't exists";
        }
    }

}
