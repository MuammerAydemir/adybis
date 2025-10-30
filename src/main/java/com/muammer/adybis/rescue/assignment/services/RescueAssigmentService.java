package com.muammer.adybis.rescue.assignment.services;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.muammer.adybis.base.auth.AuthUtils;
import com.muammer.adybis.base.exceptions.InvalidResourceException;
import com.muammer.adybis.base.exceptions.NoAvailableRescueTeamException;
import com.muammer.adybis.common.AbstractBaseService;
import com.muammer.adybis.common.enums.StatusType;
import com.muammer.adybis.help.request.services.HelpRequestService;
import com.muammer.adybis.rescue.assignment.RescueAssignmentMapper;
import com.muammer.adybis.rescue.assignment.dtos.OnlyAssignedResponse;
import com.muammer.adybis.rescue.assignment.dtos.RescueAssignmentDetailRequest;
import com.muammer.adybis.rescue.assignment.dtos.RescueAssignmentDetailResponse;
import com.muammer.adybis.rescue.assignment.dtos.RescueAssignmentRequest;
import com.muammer.adybis.rescue.assignment.dtos.RescueAssignmentResponse;
import com.muammer.adybis.rescue.assignment.models.RescueAssignment;
import com.muammer.adybis.rescue.assignment.repositories.RescueAssignmentRepository;

@Service
public class RescueAssigmentService
        extends
        AbstractBaseService<RescueAssignment, RescueAssignmentRequest, RescueAssignmentResponse, UUID> {

    private final RescueAssignmentMapper RESCUE_ASSIGNMENT_MAPPER;
    private final HelpRequestService HELP_REQUEST_SERVICE;
    private final RescueAssignmentRepository RESCUE_ASSIGNMENT_REPO;

    public RescueAssigmentService(RescueAssignmentRepository rescueAssignmentRepository,
            RescueAssignmentMapper rescueAssignmentMapper, HelpRequestService helpRequestService) {
        super(rescueAssignmentRepository, rescueAssignmentMapper, RescueAssignment.class);
        this.RESCUE_ASSIGNMENT_REPO = rescueAssignmentRepository;
        this.RESCUE_ASSIGNMENT_MAPPER = rescueAssignmentMapper;
        this.HELP_REQUEST_SERVICE = helpRequestService;
    }

    public RescueAssignmentResponse saveRescueAssignment(RescueAssignmentRequest request) {
        if (request == null || request.toString().isEmpty()) {
            throw new IllegalArgumentException("Rescue Assignment request cannot be null!");
        }
        UUID currentUser = AuthUtils.getCurrentUserId();
        RescueAssignment entity = RESCUE_ASSIGNMENT_MAPPER.toEntity(request);
        entity.setCreatedBy(currentUser);
        HELP_REQUEST_SERVICE.updateStatus(StatusType.AS.label, request.getHelpRequestId());
        super.save(entity);
        return RESCUE_ASSIGNMENT_MAPPER.toDto(entity);
    }

    public RescueAssignmentDetailResponse saveRescueAssignmentAndReturnDetailDto(
            RescueAssignmentDetailRequest request) {
        if (request == null || request.toString().isEmpty()) {
            throw new IllegalArgumentException("Rescue Assignment request cannot be null!");
        }
        UUID currentUser = AuthUtils.getCurrentUserId();
        RescueAssignment entity = RESCUE_ASSIGNMENT_MAPPER.toEntityFromDetail(request);
        entity.setCreatedBy(currentUser);
        super.save(entity);
        return RESCUE_ASSIGNMENT_MAPPER.toDetailDto(entity);
    }

    public RescueAssignmentDetailResponse updateDatasAndReturnDto(RescueAssignmentDetailRequest request, UUID id) {
        RescueAssignment entity = super.findById(id);
        RESCUE_ASSIGNMENT_MAPPER.updateEntityFromDetailDto(request, entity);
        if (entity.getComplatedAt() != null) {
            entity.setStatus(StatusType.CP.label);
            HELP_REQUEST_SERVICE.updateStatus(StatusType.CP.label, entity.getHelpRequestId());
        }
        RescueAssignment updatedEntity = super.save(entity);
        RescueAssignmentDetailResponse response = RESCUE_ASSIGNMENT_MAPPER.toDetailDto(updatedEntity);
        return response;
    }

    public List<OnlyAssignedResponse> findOnlyRescuesAssigned(UUID id) {
        if (id == null)
            throw new InvalidResourceException("Rescue team ID cannot be null!");
        List<RescueAssignment> rescueAssignments = super.findAll();
        List<RescueAssignment> matchList = rescueAssignments.stream()
                .filter(Objects::nonNull)
                .filter(res -> id.equals(res.getRescueTeamId()))
                .toList();
        if (matchList.isEmpty())
            throw new NoAvailableRescueTeamException("No assignments found for rescue team: " + id);
        return RESCUE_ASSIGNMENT_MAPPER.toOnlyRequestListDtos(matchList);
    }

    public long RescueAssignmentCount() {
        return RESCUE_ASSIGNMENT_REPO.count();
    }
}
