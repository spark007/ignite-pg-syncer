package org.ruphile.bigdata.controller;


import org.ruphile.bigdata.entity.User;
import org.ruphile.bigdata.entity.UserKey;
import org.ruphile.bigdata.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/query-by-id")
    public String queryUser(String id) {
        User user = userService.getUserById(new UserKey(id));
        if (user == null) {
            return "User record not found";
        }
        return user.toString();
    }

    @PostMapping("/add/{id}")
    public String addUser(@PathVariable String id, @RequestBody User user) {
        userService.putUser(new UserKey(id), user);
        return "success";
    }


    /*@GetMapping("/filter")
    public String testFilter() {
        userService.testFilter();
        return "success";
    }*/
}
