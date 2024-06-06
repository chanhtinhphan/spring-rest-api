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
        return this.userRepository.save(user);
    }

    public void handeDeleteUser(Long id) {
        this.userRepository.deleteById(id);
    }
}
