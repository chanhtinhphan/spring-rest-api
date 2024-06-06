package vn.hoidanit.jobhunter.service;

import vn.hoidanit.jobhunter.domain.User;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateuser(User user) {
        return userRepository.save(user);
    }
}
