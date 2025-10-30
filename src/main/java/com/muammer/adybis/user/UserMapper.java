package com.muammer.adybis.user;

import java.time.LocalDateTime;
import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import com.muammer.adybis.base.auth.dtos.RegisterRequest;
import com.muammer.adybis.common.BaseMapper;
import com.muammer.adybis.role.dtos.RoleBasicResponse;
import com.muammer.adybis.role.models.Role;
import com.muammer.adybis.role.services.RoleService;
import com.muammer.adybis.user.dtos.UserDetailRequest;
import com.muammer.adybis.user.dtos.UserDetailResponse;
import com.muammer.adybis.user.dtos.UserResponse;
import com.muammer.adybis.user.models.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class UserMapper implements BaseMapper<User, UserDetailRequest, UserDetailResponse> {
    @Autowired
    private RoleService ROLE_SERVICE;

    @Override
    @Named("toEntity")
    @Mapping(target = "roles", ignore = true)
    public abstract User toEntity(UserDetailRequest req);

    @Override
    @Named("toDto")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "toRoleBasicDtoList")
    public abstract UserDetailResponse toDto(User user);

    @Override
    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<UserDetailResponse> toDtoList(List<User> entities);

    @Override
    @IterableMapping(qualifiedByName = "toEntity")
    public abstract List<User> toEntityList(List<UserDetailRequest> req);

    @Override
    public abstract void updateEntityFromDto(UserDetailRequest dto, @MappingTarget User entity);

    @Named("toBasicDto")
    public abstract UserResponse toBasicDto(User user);

    @Named("toRoleBasicDto")
    RoleBasicResponse toRoleBasicResponse(Role role) {
        return new RoleBasicResponse(role.getId(), role.getName(), role.getDescription(), role.getCreatedAt());
    }

    @Named("toEntityFromRegister")
    public abstract User toEntityFromRegister(RegisterRequest req);

    @Named("toRoleBasicDtoList")
    List<RoleBasicResponse> toRoleBasicDtoList(List<Role> roles) {
        return roles.stream()
                .map(this::toRoleBasicResponse)
                .toList();
    }

    @AfterMapping
    protected void lowercaseUsername(@MappingTarget User user) {
        if (user.getUsername() != null) {
            user.setUsername(user.getUsername().toLowerCase());
        }
    }

    @AfterMapping
    protected void mapRoles(UserDetailRequest request, @MappingTarget User user) {
        if (request.getRoleIds() != null) {
            List<Role> roleEntities = request.getRoleIds().stream()
                    .map(id -> ROLE_SERVICE.findById(id))
                    .toList();

            user.setRoles(roleEntities);
        }
    }

    @AfterMapping
    protected void setDefaults(@MappingTarget User user) {
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        if (user.getUpdatedAt() == null) {
            user.setUpdatedAt(LocalDateTime.now());
        }
    }

}
