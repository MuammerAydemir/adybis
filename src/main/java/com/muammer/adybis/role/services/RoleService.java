package com.muammer.adybis.role.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.muammer.adybis.common.AbstractBaseService;
import com.muammer.adybis.role.RoleMapper;
import com.muammer.adybis.role.dtos.RoleBasicResponse;
import com.muammer.adybis.role.dtos.RoleRequest;
import com.muammer.adybis.role.dtos.RoleResponse;
import com.muammer.adybis.role.models.Role;
import com.muammer.adybis.role.repositories.RoleRepository;

import jakarta.transaction.Transactional;

@Service
public class RoleService extends AbstractBaseService<Role, RoleRequest, RoleResponse, UUID> {
    private final RoleRepository ROLE_REPO;
    private final RoleMapper ROLE_MAPPER;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        super(roleRepository, roleMapper, Role.class);
        this.ROLE_REPO = roleRepository;
        this.ROLE_MAPPER = roleMapper;
    }

    @Transactional
    public RoleBasicResponse saveAndReturnBasicDto(RoleRequest request) {
        Role savedEntity = super.saveAndReturnEntity(request);
        return ROLE_MAPPER.toBasicDto(savedEntity);
    }

    @Transactional
    public Role findRoleByName(String name) {
        return ROLE_REPO.findByName(name).orElseThrow(() -> new IllegalArgumentException("Username cannot be null!"));
    }

}
