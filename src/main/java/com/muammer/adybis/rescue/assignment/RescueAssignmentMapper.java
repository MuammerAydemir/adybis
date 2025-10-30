package com.muammer.adybis.rescue.assignment;

import java.util.List;
import java.util.UUID;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import com.muammer.adybis.base.exceptions.NoAvailableHelpRequestException;
import com.muammer.adybis.base.exceptions.NoAvailableUserException;
import com.muammer.adybis.common.BaseMapper;
import com.muammer.adybis.help.request.dtos.HelpRequestResponse;
import com.muammer.adybis.help.request.services.HelpRequestService;
import com.muammer.adybis.rescue.assignment.dtos.OnlyAssignedResponse;
import com.muammer.adybis.rescue.assignment.dtos.RescueAssignmentDetailRequest;
import com.muammer.adybis.rescue.assignment.dtos.RescueAssignmentDetailResponse;
import com.muammer.adybis.rescue.assignment.dtos.RescueAssignmentRequest;
import com.muammer.adybis.rescue.assignment.dtos.RescueAssignmentResponse;
import com.muammer.adybis.rescue.assignment.models.RescueAssignment;
import com.muammer.adybis.user.dtos.UserResponse;
import com.muammer.adybis.user.services.UserService;;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class RescueAssignmentMapper
                implements BaseMapper<RescueAssignment, RescueAssignmentRequest, RescueAssignmentResponse> {
        @Autowired
        protected UserService userService;
        @Autowired
        protected HelpRequestService helpRequestService;

        @Override
        @Named("toDto")
        @Mappings({
                        @Mapping(source = "helpRequestId", target = "helpRequest", qualifiedByName = "mapHelpRequestResponse"),
                        @Mapping(source = "rescueTeamId", target = "rescueTeam", qualifiedByName = "mapUserResponse")
        })
        public abstract RescueAssignmentResponse toDto(RescueAssignment entity);

        @Named("toDetailDto")
        @Mappings({
                        @Mapping(source = "helpRequestId", target = "helpRequest", qualifiedByName = "mapHelpRequestResponse"),
                        @Mapping(source = "rescueTeamId", target = "rescueTeam", qualifiedByName = "mapUserResponse"),
                        @Mapping(source = "createdBy", target = "createdBy", qualifiedByName = "mapUserResponse")
        })
        public abstract RescueAssignmentDetailResponse toDetailDto(RescueAssignment entity);

        @Override
        public abstract void updateEntityFromDto(RescueAssignmentRequest dto, @MappingTarget RescueAssignment entity);

        public abstract void updateEntityFromDetailDto(RescueAssignmentDetailRequest dto,
                        @MappingTarget RescueAssignment entity);

        @Override
        @Named("toEntity")
        public abstract RescueAssignment toEntity(RescueAssignmentRequest req);

        @Named("toEntityFromDetail")
        public abstract RescueAssignment toEntityFromDetail(RescueAssignmentDetailRequest req);

        @Named("toOnlyRequest")
        @Mappings({
                        @Mapping(source = "helpRequestId", target = "helpRequest", qualifiedByName = "mapHelpRequestResponse"),
        })
        public abstract OnlyAssignedResponse toOnlyRequestDto(RescueAssignment req);

        @IterableMapping(qualifiedByName = "toOnlyRequest")
        public abstract List<OnlyAssignedResponse> toOnlyRequestListDtos(List<RescueAssignment> req);

        @Override
        @IterableMapping(qualifiedByName = "toDto")
        public abstract List<RescueAssignmentResponse> toDtoList(List<RescueAssignment> entities);

        @IterableMapping(qualifiedByName = "toDetailDto")
        public abstract List<RescueAssignmentDetailResponse> toDetailDtoList(List<RescueAssignment> entities);

        @Override
        @IterableMapping(qualifiedByName = "toEntity")
        public abstract List<RescueAssignment> toEntityList(List<RescueAssignmentRequest> req);

        @Named("mapUserResponse")
        protected UserResponse mapUserResponse(UUID id) {
                if (id == null) {
                        throw new NoAvailableUserException("User cannot be null!");
                }
                return userService.findByIdAndReturnResponseDto(id);
        }

        @Named("mapHelpRequestResponse")
        protected HelpRequestResponse mapHelpRequestResponse(UUID id) {
                if (id == null) {
                        throw new NoAvailableHelpRequestException("Help Request cannot be null!");
                }
                return helpRequestService.findByIdAndReturnDto(id);
        }

}
