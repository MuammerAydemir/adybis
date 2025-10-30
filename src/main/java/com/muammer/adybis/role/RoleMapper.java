package com.muammer.adybis.role;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.muammer.adybis.common.BaseMapper;
import com.muammer.adybis.role.dtos.RoleResponse;
import com.muammer.adybis.role.models.Role;
import com.muammer.adybis.role.dtos.RoleBasicResponse;
import com.muammer.adybis.role.dtos.RoleRequest;
import com.muammer.adybis.user.dtos.UserResponse;
import com.muammer.adybis.user.models.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class RoleMapper implements BaseMapper<Role, RoleRequest, RoleResponse> {

    @Override
    @Named("toEntity")
    @Mapping(target = "users", ignore = true)
    public abstract Role toEntity(RoleRequest req);

    @Override
    @Named("toDto")
    @Mapping(target = "users", source = "users", qualifiedByName = "toUserDtoList")
    public abstract RoleResponse toDto(Role role);

    @Named("toBasicDto")
    public abstract RoleBasicResponse toBasicDto(Role role);

    @Override
    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<RoleResponse> toDtoList(List<Role> entities);

    @Override
    @IterableMapping(qualifiedByName = "toEntity")
    public abstract List<Role> toEntityList(List<RoleRequest> req);

    @Override
    public abstract void updateEntityFromDto(RoleRequest dto, @MappingTarget Role entity);

    @Named("toUserDto")
    UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(),
                user.getBirthdayDate(),
                user.getBloodType(), user.isChronicIllnesses(), user.isPeopleWithDisabilities(),
                user.getSpecialCaseDescription());
    }

    @Named("toUserDtoList")
    List<UserResponse> toUserDtoList(List<User> users) {
        return users.stream()
                .map(this::toUserResponse)
                .toList();
    }
}
