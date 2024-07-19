package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;
import vn.hoidanit.jobhunter.domain.User;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User newUser = this.userService.handleCreateuser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        this.userService.handeDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("deleted");
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetUserById(id));
    }

    @GetMapping
    public ResponseEntity<ResultPaginationDTO> getAll(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional
    ) {
        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";

        int currentPage = Integer.parseInt(sCurrent) - 1;
        int pageSize = Integer.parseInt(sPageSize);
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        ResultPaginationDTO listUser = this.userService.handeGetAllUser(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listUser);
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody User user) {
        User putUser = this.userService.handleUpdateUser(user);
        return ResponseEntity.ok(putUser);
    }
}