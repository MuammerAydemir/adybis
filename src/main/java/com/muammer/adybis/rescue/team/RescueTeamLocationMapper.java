package com.muammer.adybis.rescue.team;

import java.util.List;
import java.util.UUID;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import com.muammer.adybis.base.exceptions.NoAvailableUserException;
import com.muammer.adybis.common.BaseMapper;
import com.muammer.adybis.rescue.team.dtos.RescueTeamLocationRequest;
import com.muammer.adybis.rescue.team.dtos.RescueTeamLocationResponse;
import com.muammer.adybis.rescue.team.models.RescueTeamLocation;
import com.muammer.adybis.user.dtos.UserResponse;
import com.muammer.adybis.user.services.UserService;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class RescueTeamLocationMapper
        implements BaseMapper<RescueTeamLocation, RescueTeamLocationRequest, RescueTeamLocationResponse> {
    @Autowired
    protected UserService userService;

    @Override
    @Named("toDto")
    @Mapping(source = "rescueTeamId", target = "rescueTeam", qualifiedByName = "mapUserResponse")
    public abstract RescueTeamLocationResponse toDto(RescueTeamLocation entity);

    @Override
    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<RescueTeamLocationResponse> toDtoList(List<RescueTeamLocation> entities);

    @Override
    @IterableMapping(qualifiedByName = "toEntity")
    public abstract List<RescueTeamLocation> toEntityList(List<RescueTeamLocationRequest> req);

    @Override
    public abstract void updateEntityFromDto(RescueTeamLocationRequest dto, @MappingTarget RescueTeamLocation entity);

    @Override
    @Named("toEntity")
    public abstract RescueTeamLocation toEntity(RescueTeamLocationRequest req);

    @Named("mapUserResponse")
    protected UserResponse mapUserResponse(UUID id) {
        if (id == null) {
            throw new NoAvailableUserException("User cannot be null!");
        }
        return userService.findByIdAndReturnResponseDto(id);
    }

}