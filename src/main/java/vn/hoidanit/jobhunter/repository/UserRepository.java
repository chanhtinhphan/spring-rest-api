package vn.hoidanit.jobhunter.repository;

import vn.hoidanit.jobhunter.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    User findByEmail(String email);

    void deleteById(Long id);
}