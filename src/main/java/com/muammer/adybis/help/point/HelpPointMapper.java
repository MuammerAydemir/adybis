package com.muammer.adybis.help.point;

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

import com.muammer.adybis.common.BaseMapper;
import com.muammer.adybis.help.enums.HelpPointType;
import com.muammer.adybis.help.point.dtos.HelpPointAdminResponse;
import com.muammer.adybis.help.point.dtos.HelpPointRequest;
import com.muammer.adybis.help.point.dtos.HelpPointResponse;
import com.muammer.adybis.help.point.models.HelpPoint;
import com.muammer.adybis.user.dtos.UserResponse;
import com.muammer.adybis.user.services.UserService;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class HelpPointMapper implements BaseMapper<HelpPoint, HelpPointRequest, HelpPointResponse> {
    @Autowired
    protected UserService userService;

    @Override
    @Named("toDto")
    public abstract HelpPointResponse toDto(HelpPoint entity);

    @Named("toDetailDto")
    @Mapping(source = "createdById", target = "createdBy", qualifiedByName = "mapUserResponse")
    public abstract HelpPointAdminResponse toDetailDto(HelpPoint entity);

    @Override
    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<HelpPointResponse> toDtoList(List<HelpPoint> entities);

    @Override
    @IterableMapping(qualifiedByName = "toEntity")
    public abstract List<HelpPoint> toEntityList(List<HelpPointRequest> req);

    @Override
    public abstract void updateEntityFromDto(HelpPointRequest dto, @MappingTarget HelpPoint entity);

    @Override
    @Named("toEntity")
    public abstract HelpPoint toEntity(HelpPointRequest req);

    @Named("mapUserResponse")
    protected UserResponse mapUserResponse(UUID id) {
        if (id == null)
            return null;
        return userService.findByIdAndReturnResponseDto(id);
    }

    @AfterMapping
    protected void setDefaults(@MappingTarget HelpPoint entity) {
        if (entity.getType() == null) {
            entity.setType(HelpPointType.TN.label);
        }
    }

}
