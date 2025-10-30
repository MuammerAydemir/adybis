package com.muammer.adybis.common;

import java.util.List;
import java.util.UUID;

import org.mapstruct.MappingTarget;
import com.muammer.adybis.user.dtos.UserResponse;
import com.muammer.adybis.user.services.UserService;

public interface BaseMapper<E, RQ, RS> {

    default RS toDto(E entity) {
        throw new UnsupportedOperationException("toDto() not implemented");
    };

    default List<RS> toDtoList(List<E> entities) {
        throw new UnsupportedOperationException("toDtoList() not implemented");
    };

    default E toEntity(RQ req) {
        throw new UnsupportedOperationException("toEntity() not implemented");
    };

    default List<E> toEntityList(List<RQ> req) {
        throw new UnsupportedOperationException("toEntityList() not implemented");
    };

    default void updateEntityFromDto(RQ dto, @MappingTarget E entity) {
        throw new UnsupportedOperationException("updateEntityFromDto() not implemented");
    };

    default UserResponse mapUser(UUID id, UserService userService) {
        if (id == null)
            throw new IllegalArgumentException("Id cannot be null!");
        return userService.findByIdAndReturnResponseDto(id);
    }
}
