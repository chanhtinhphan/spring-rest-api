package vn.hoidanit.jobhunter.service;

import vn.hoidanit.jobhunter.domain.User;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateuser(User user) {
        return this.userRepository.save(user);
    }

    public void handeDeleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public User handleGetUserById(Long id) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (optionalUser.isPresent())
            return optionalUser.get();
        return null;
    }

    public List<User> handeGetAllUser() {
        return this.userRepository.findAll();
    }

    // override all field

    // public User handleUpdateUser(User user) {
    // if (this.userRepository.findAll().contains(user)) {
    // return this.userRepository.save(user);
    // }
    // return null;
    // }

    public User handleUpdateUser(User user) {
        User currentUser = this.handleGetUserById(user.getId());
        if (currentUser != null) {
            currentUser.setEmail(user.getEmail());
            currentUser.setName(user.getName());
            currentUser.setPassword(user.getPassword());
            return this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

}
