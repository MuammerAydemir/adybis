package com.muammer.adybis.help.request;

import java.util.List;
import java.util.UUID;

import org.mapstruct.AfterMapping;
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
import com.muammer.adybis.common.enums.StatusType;
import com.muammer.adybis.help.request.dtos.HelpRequestDetailRequest;
import com.muammer.adybis.help.request.dtos.HelpRequestDetailResponse;
import com.muammer.adybis.help.request.dtos.HelpRequestRequest;
import com.muammer.adybis.help.request.dtos.HelpRequestResponse;
import com.muammer.adybis.help.request.dtos.VictimHelpRequestResponse;
import com.muammer.adybis.help.request.models.HelpRequest;
import com.muammer.adybis.user.dtos.UserResponse;
import com.muammer.adybis.user.services.UserService;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class HelpRequestMapper implements BaseMapper<HelpRequest, HelpRequestRequest, HelpRequestResponse> {

    @Autowired
    protected UserService userService;

    @Override
    @Named("toDto")
    @Mapping(source = "victimId", target = "victim", qualifiedByName = "mapUserResponse")
    public abstract HelpRequestResponse toDto(HelpRequest entity);

    @Named("toDetailDto")
    @Mapping(source = "victimId", target = "victim", qualifiedByName = "mapUserResponse")
    public abstract HelpRequestDetailResponse toDetailDto(HelpRequest entity);

    @Named("toVictimDto")
    public abstract VictimHelpRequestResponse toVictimDto(HelpRequest entity);

    @Override
    @Named("toEntity")
    public abstract HelpRequest toEntity(HelpRequestRequest req);

    @Named("toEntityFromDetail")
    public abstract HelpRequest toEntityFromDetail(HelpRequestDetailRequest req);

    @IterableMapping(qualifiedByName = "toDetailDto")
    public abstract List<HelpRequestDetailResponse> toDetailDtoList(List<HelpRequest> entities);

    @Override
    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<HelpRequestResponse> toDtoList(List<HelpRequest> entities);

    @Override
    @IterableMapping(qualifiedByName = "toEntity")
    public abstract List<HelpRequest> toEntityList(List<HelpRequestRequest> req);

    @Override
    public abstract void updateEntityFromDto(HelpRequestRequest dto, @MappingTarget HelpRequest entity);

    public abstract void updateEntityFromDto(HelpRequestDetailRequest dto, @MappingTarget HelpRequest entity);

    @Named("mapUserResponse")
    protected UserResponse mapUserResponse(UUID id) {
        if (id == null) {
            throw new NoAvailableUserException("User cannot be null!");
        }
        return userService.findByIdAndReturnResponseDto(id);
    }

    @AfterMapping
    protected void setDefaults(@MappingTarget HelpRequest entity) {
        if (entity.getStatus() == null) {
            entity.setStatus(StatusType.PND.label);
        }
    }

}
