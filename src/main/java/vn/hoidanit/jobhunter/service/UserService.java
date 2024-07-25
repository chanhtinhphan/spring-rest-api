package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResFetchUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.error.ExistedException;
import vn.hoidanit.jobhunter.util.error.IdNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, CompanyRepository companyRepository, RoleService roleService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
    }

    public ResCreateUserDTO handleCreateuser(User user) throws ExistedException {
        if (this.userRepository.findByEmail(user.getEmail()) != null) {
            throw new ExistedException("User has exsisted in db");
        }

        if (user.getCompany() != null) {
            Company companyDB = this.companyRepository.findById(user.getCompany().getId()).orElse(null);
            user.setCompany(companyDB);
        }
        if (user.getRole() != null) {
            Role roleDB = this.roleRepository.findById(user.getRole().getId()).orElse(null);
            user.setRole(roleDB);
        }
        User savedUser = this.userRepository.save(user);
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(savedUser.getId());
        res.setName(savedUser.getName());
        res.setAge(savedUser.getAge());
        res.setAddress(savedUser.getAddress());
        res.setGender(savedUser.getGender());
        res.setEmail(savedUser.getEmail());
        res.setCreatedAt(savedUser.getCreatedAt());
        if (savedUser.getCompany() != null) {
            ResCreateUserDTO.Company resCompany = new ResCreateUserDTO.Company();
            resCompany.setId(savedUser.getCompany().getId());
            resCompany.setName(savedUser.getCompany().getName());
            res.setCompany(resCompany);
        }
        return res;
    }

    public void handeDeleteUser(Long id) throws IdNotFoundException {
        this.userRepository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        this.userRepository.deleteById(id);
    }

    public ResFetchUserDTO handleGetUserById(Long id) throws IdNotFoundException {
        User user = this.userRepository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        ResFetchUserDTO res = new ResFetchUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setCreatedAt(user.getCreatedAt());
        if (user.getCompany() != null) {
            ResFetchUserDTO.Company resCompany = new ResFetchUserDTO.Company();
            resCompany.setId(user.getCompany().getId());
            resCompany.setName(user.getCompany().getName());
            res.setCompany(resCompany);
        }
        return res;
    }

    public ResultPaginationDTO handeGetAllUser(Specification<User> specification, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        result.setMeta(meta);

        List<ResFetchUserDTO> listRes = pageUser.getContent().stream()
                .map(user -> convertToFetchDTO(user))
                .collect(Collectors.toList());

        result.setResult(listRes);
        return result;
    }

    private static ResFetchUserDTO convertToFetchDTO(User user) {
        return new ResFetchUserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getGender(),
                user.getAddress(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                new ResFetchUserDTO.Company(
                        user.getCompany() != null ? user.getCompany().getId() : 0,
                        user.getCompany() != null ? user.getCompany().getName() : null),
                new ResFetchUserDTO.Role(
                        user.getRole() != null ? user.getRole().getId() : 0,
                        user.getRole() != null ? user.getRole().getName() : null)
        );
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public ResUpdateUserDTO handleUpdateUser(User user) throws IdNotFoundException {
        User currentUser = this.userRepository.findById(user.getId())
                .orElseThrow(() -> new IdNotFoundException("Id not found"));
        currentUser.setName(user.getName());
        currentUser.setGender(user.getGender());
        currentUser.setAddress(user.getAddress());
        currentUser.setAge(user.getAge());


        if (user.getCompany() != null) {
            Company companyDB = this.companyRepository.findById(user.getCompany().getId()).orElse(null);
            currentUser.setCompany(companyDB);
        }
        if (user.getRole() != null) {
            Role roleDB = this.roleRepository.findById(user.getRole().getId()).orElse(null);
            currentUser.setRole(roleDB);
        }

        User updatedUser = this.userRepository.save(currentUser);
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        res.setId(updatedUser.getId());
        res.setName(updatedUser.getName());
        res.setGender(updatedUser.getGender());
        res.setAddress(updatedUser.getAddress());
        res.setAge(updatedUser.getAge());
        res.setUpdatedAt(updatedUser.getUpdatedAt());
        if (currentUser.getCompany() != null) {
            ResUpdateUserDTO.Company resCompany = new ResUpdateUserDTO.Company();
            resCompany.setId(currentUser.getCompany().getId());
            resCompany.setName(currentUser.getCompany().getName());
            res.setCompany(resCompany);
        }
        return res;
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
