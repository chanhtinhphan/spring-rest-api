package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.util.error.ExistedException;
import vn.hoidanit.jobhunter.util.error.IdNotFoundException;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<Role> create(@RequestBody @Valid Role role) throws ExistedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.handleCreateRole(role));
    }

    @PutMapping
    public ResponseEntity<Role> update(@RequestBody @Valid Role role) throws IdNotFoundException, ExistedException {
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.handleUpdateRole(role));
    }

    @GetMapping
    public ResponseEntity<ResultPaginationDTO> getPage(
            @Filter Specification<Role> specification, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.handleGetRolePage(specification, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws IdNotFoundException {
        this.roleService.handleDeleteRole(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
