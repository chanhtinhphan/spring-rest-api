package vn.hoidanit.jobhunter.repository;

import vn.hoidanit.jobhunter.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
}