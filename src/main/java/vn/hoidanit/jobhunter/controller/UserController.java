package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResFetchUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdNotFoundException;
import vn.hoidanit.jobhunter.util.error.ExistedException;
import vn.hoidanit.jobhunter.domain.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> create(@RequestBody @Valid User user) throws ExistedException {
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        ResCreateUserDTO newUser = this.userService.handleCreateuser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws IdNotFoundException {
        this.userService.handeDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    @ApiMessage("Fetch user by id")
    public ResponseEntity<ResFetchUserDTO> get(@PathVariable("id") Long id) throws IdNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetUserById(id));
    }

    @GetMapping
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAll(
            @Filter Specification<User> specification,
            Pageable pageable
    ) {
        ResultPaginationDTO listUser = this.userService.handeGetAllUser(specification, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listUser);
    }

    @PutMapping
    @ApiMessage("update user")
    public ResponseEntity<ResUpdateUserDTO> update(@RequestBody User user) throws IdNotFoundException {
        ResUpdateUserDTO putUser = this.userService.handleUpdateUser(user);
        return ResponseEntity.ok(putUser);
    }
}