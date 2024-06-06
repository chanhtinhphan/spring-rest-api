package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.domain.User;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public User user() {
        User newUser = new User();
        newUser.setName("Hiake");
        newUser.setEmail("jdsa@gmail.com");
        newUser.setPassword("432432");
        return this.userService.handleCreateuser(newUser);

    }

}