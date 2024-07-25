package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;
import vn.hoidanit.jobhunter.util.error.ExistedException;
import vn.hoidanit.jobhunter.util.error.IdNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Role handleCreateRole(Role role) throws ExistedException {
        if (this.roleRepository.existsByName(role.getName()))
            throw new ExistedException("role has been existed");
        if (role.getPermissions() != null) {
            List<Long> reqPermissionIds = role.getPermissions().stream()
                    .map(permission -> permission.getId())
                    .collect(Collectors.toList());
            List<Permission> permissionsInDB = this.permissionRepository.findByIdIn(reqPermissionIds);
            role.setPermissions(permissionsInDB);
        }
        return this.roleRepository.save(role);
    }

    public Role handleUpdateRole(Role role) throws IdNotFoundException, ExistedException {
        if (!this.roleRepository.existsById(role.getId()))
            throw new IdNotFoundException("role not found");
//        if (this.roleRepository.existsByName(role.getName()))
//            throw new ExistedException("role has been existed");
        Role roleInDB = this.roleRepository.findById(role.getId()).get();
        roleInDB.setName(role.getName());
        roleInDB.setDescription(role.getDescription());
        roleInDB.setActive(role.isActive());
        if (role.getPermissions() != null) {
            List<Long> reqPermissionIds = role.getPermissions().stream()
                    .map(permission -> permission.getId())
                    .collect(Collectors.toList());
            List<Permission> permissionsInDB = this.permissionRepository.findByIdIn(reqPermissionIds);
            roleInDB.setPermissions(permissionsInDB);
        }
        return this.roleRepository.save(roleInDB);
    }
    public ResultPaginationDTO handleGetRolePage(Specification<Role> specification, Pageable pageable){
        Page<Role> rolePage = this.roleRepository.findAll(specification,pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta= new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber()+1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(rolePage.getTotalPages());
        meta.setTotal(rolePage.getTotalElements());

        result.setMeta(meta);
        result.setResult(rolePage.getContent());
        return result;
    }

    public  void handleDeleteRole(Long id) throws IdNotFoundException{
        if (!this.roleRepository.existsById(id))
            throw new IdNotFoundException("role not found");
        this.roleRepository.deleteById(id);
    }

}
