package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.PermissionService;
import vn.hoidanit.jobhunter.util.error.ExistedException;
import vn.hoidanit.jobhunter.util.error.IdNotFoundException;

@RestController
@RequestMapping("api/v1/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping
    public ResponseEntity<Permission> create(@RequestBody @Valid Permission permission) throws ExistedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.handleCreatePermission(permission));
    }

    @PutMapping
    public ResponseEntity<Permission> update(
            @RequestBody @Valid Permission permission) throws ExistedException, IdNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(this.permissionService.handleUpdatePermission(permission));
    }

    @GetMapping
    public ResponseEntity<ResultPaginationDTO> fetchPage(
            @Filter Specification<Permission> specification, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.permissionService.handleGetPermissionPage(specification, pageable));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> delete(@PathVariable("id") Long id) throws  IdNotFoundException{
        this.permissionService.handleDeletePermission(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
