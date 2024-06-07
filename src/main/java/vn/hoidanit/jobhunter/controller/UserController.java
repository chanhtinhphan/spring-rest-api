package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.domain.User;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return this.userService.handleCreateuser(user);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        this.userService.handeDeleteUser(id);
        return "deleted";
    }

    @GetMapping("/{id}")
    public User get(@PathVariable("id") Long id) {
        return this.userService.handleGetUserById(id);
    }

    @GetMapping
    public List<User> getAll() {
        return this.userService.handeGetAllUser();
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return this.userService.handleUpdateUser(user);
    }

}