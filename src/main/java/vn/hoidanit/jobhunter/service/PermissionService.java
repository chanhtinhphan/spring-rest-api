package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.util.error.ExistedException;
import vn.hoidanit.jobhunter.util.error.IdNotFoundException;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission handleCreatePermission(Permission permission) throws ExistedException {
        if (this.permissionRepository.existsByModuleAndApiPathAndMethod(
                permission.getModule(), permission.getApiPath(), permission.getMethod()
        )) throw new ExistedException("permission have been existed");
        return this.permissionRepository.save(permission);
    }

    public Permission handleUpdatePermission(Permission permission) throws ExistedException, IdNotFoundException {
        if (!this.permissionRepository.existsById(permission.getId()))
            throw new IdNotFoundException("Permission id not found");
        if (this.permissionRepository.existsByModuleAndApiPathAndMethod(
                permission.getModule(), permission.getApiPath(), permission.getMethod()
        )) throw new ExistedException("permission have been existed");
        Permission permissionInDB = this.permissionRepository.findById(permission.getId()).get();
        permissionInDB.setName(permission.getName());
        permissionInDB.setApiPath(permission.getApiPath());
        permissionInDB.setMethod(permission.getMethod());
        permissionInDB.setModule(permission.getModule());
        return this.permissionRepository.save(permissionInDB);
    }

    public ResultPaginationDTO handleGetPermissionPage(Specification<Permission> specification, Pageable pageable) {
        Page<Permission> permissionPage = this.permissionRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(permissionPage.getTotalPages());
        meta.setTotal(permissionPage.getTotalElements());

        result.setMeta(meta);
        result.setResult(permissionPage.getContent());
        return result;
    }

    public void handleDeletePermission(Long id) throws IdNotFoundException {
        if (!this.permissionRepository.existsById(id))
            throw new IdNotFoundException("Permission id not found");
        Permission currentPermission = this.permissionRepository.findById(id).get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));
        // delete by object
        this.permissionRepository.save(currentPermission);
    }
}
