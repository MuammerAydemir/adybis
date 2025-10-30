package com.muammer.adybis.help.request.services;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.muammer.adybis.base.auth.AuthUtils;
import com.muammer.adybis.base.exceptions.NoAvailableHelpPointException;
import com.muammer.adybis.base.exceptions.NoAvailableRescueTeamException;
import com.muammer.adybis.common.AbstractBaseService;
import com.muammer.adybis.common.GeoService;
import com.muammer.adybis.common.enums.StatusType;
import com.muammer.adybis.help.point.HelpPointMapper;
import com.muammer.adybis.help.point.dtos.HelpPointResponse;
import com.muammer.adybis.help.point.models.HelpPoint;
import com.muammer.adybis.help.point.services.HelpPointService;
import com.muammer.adybis.help.request.HelpRequestMapper;
import com.muammer.adybis.help.request.dtos.HelpRequestDetailRequest;
import com.muammer.adybis.help.request.dtos.HelpRequestDetailResponse;
import com.muammer.adybis.help.request.dtos.HelpRequestRequest;
import com.muammer.adybis.help.request.dtos.HelpRequestResponse;
import com.muammer.adybis.help.request.dtos.VictimHelpRequestResponse;
import com.muammer.adybis.help.request.models.HelpRequest;
import com.muammer.adybis.help.request.repositories.HelpRequestRepository;
import com.muammer.adybis.rescue.team.RescueTeamLocationMapper;
import com.muammer.adybis.rescue.team.dtos.RescueTeamLocationResponse;
import com.muammer.adybis.rescue.team.models.RescueTeamLocation;
import com.muammer.adybis.rescue.team.services.RescueTeamLocationService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class HelpRequestService
        extends AbstractBaseService<HelpRequest, HelpRequestRequest, HelpRequestResponse, UUID> {
    private final HelpPointService HELP_POINT_SERVICE;
    private final GeoService GEO_SERVICE;
    private final HelpPointMapper HELP_POINT_MAPPER;
    private final RescueTeamLocationMapper RESCUE_TEAM_LOCATION_MAPPER;
    private final RescueTeamLocationService RESCUE_TEAM_SERVICE;
    private final HelpRequestMapper HELP_REQUEST_MAPPER;
    private final HelpRequestRepository HELP_REQUEST_REPOSITORY;

    public HelpRequestService(HelpRequestRepository helpRequestRepository, HelpRequestMapper helpRequestMapper,
            HelpPointService helpPointService, GeoService geoService, HelpPointMapper helpPointMapper,
            RescueTeamLocationMapper rescueTeamLocationMapper, RescueTeamLocationService rescueTeamLocationService) {
        super(helpRequestRepository, helpRequestMapper, HelpRequest.class);
        this.HELP_REQUEST_REPOSITORY = helpRequestRepository;
        this.HELP_POINT_SERVICE = helpPointService;
        this.GEO_SERVICE = geoService;
        this.HELP_POINT_MAPPER = helpPointMapper;
        this.RESCUE_TEAM_LOCATION_MAPPER = rescueTeamLocationMapper;
        this.RESCUE_TEAM_SERVICE = rescueTeamLocationService;
        this.HELP_REQUEST_MAPPER = helpRequestMapper;
    }

    public HelpRequestDetailResponse saveRequestAndReturnDto(HelpRequestDetailRequest request) {
        HelpRequest entity = HELP_REQUEST_MAPPER.toEntityFromDetail(request);
        HELP_REQUEST_REPOSITORY.save(entity);
        HelpPointResponse nearestHelpPoint = findNearestHelpPoint(entity);
        RescueTeamLocationResponse nearestRescueTeam = findNearestRescueTeam(entity);
        HelpRequestDetailResponse response = HELP_REQUEST_MAPPER.toDetailDto(entity);
        response.setNearestHelpPoint(nearestHelpPoint);
        response.setNearestRescueTeam(nearestRescueTeam);
        return response;
    }

    public VictimHelpRequestResponse saveVictimRequestAndReturnDto(HelpRequestRequest request) {
        UUID currentUserId = AuthUtils.getCurrentUserId();
        HelpRequest entity = HELP_REQUEST_MAPPER.toEntity(request);
        entity.setVictimId(currentUserId);
        HELP_REQUEST_REPOSITORY.save(entity);
        HelpPointResponse nearestHelpPoint = findNearestHelpPoint(entity);
        VictimHelpRequestResponse response = HELP_REQUEST_MAPPER.toVictimDto(entity);
        response.setNearestHelpPoint(nearestHelpPoint);
        return response;
    }

    public HelpRequestDetailResponse updateAdminAndReturnDto(HelpRequestDetailRequest request, UUID id) {
        HelpRequest entity = HELP_REQUEST_REPOSITORY.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User id cannot be null!"));
        HELP_REQUEST_MAPPER.updateEntityFromDto(request, entity);
        HelpRequest updatedEntity = repository.save(entity);
        return HELP_REQUEST_MAPPER.toDetailDto(updatedEntity);
    }

    public void updateStatus(String type, UUID id) {
        HelpRequest entity = HELP_REQUEST_REPOSITORY.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User id cannot be null!"));
        entity.setStatus(type);
        HELP_REQUEST_REPOSITORY.save(entity);
    }

    public long helpRequestCount() {
        return HELP_REQUEST_REPOSITORY.count();
    }

    public List<HelpRequestDetailResponse> findAllAndReturnDetailDtos() {
        List<HelpRequest> entities = super.findAll();
        return entities.stream()
                .map(entity -> {
                    HelpRequestDetailResponse dto = HELP_REQUEST_MAPPER.toDetailDto(entity);

                    dto.setNearestHelpPoint(findNearestHelpPoint(entity));
                    dto.setNearestRescueTeam(findNearestRescueTeam(entity));

                    return dto;
                })
                .toList();
    }

    public HelpRequestDetailResponse findByIdAndReturnDetailDto(UUID id) {
        HelpRequest entity = super.findById(id);
        HelpRequestDetailResponse response = HELP_REQUEST_MAPPER.toDetailDto(entity);
        HelpPointResponse nearestHelpPoint = findNearestHelpPoint(entity);
        RescueTeamLocationResponse nearestRescueTeam = findNearestRescueTeam(entity);
        response.setNearestHelpPoint(nearestHelpPoint);
        response.setNearestRescueTeam(nearestRescueTeam);
        return response;
    }

    public List<HelpRequestDetailResponse> findByStatusAndReturnDtos() {
        List<HelpRequest> entities = HELP_REQUEST_REPOSITORY.findByStatus(StatusType.PND.label);
        if (entities == null || entities.isEmpty()) {
            throw new EntityNotFoundException("Pending help request not found!");
        }
        List<HelpRequestDetailResponse> responses = HELP_REQUEST_MAPPER.toDetailDtoList(entities);
        for (int i = 0; i < responses.size(); i++) {
            HelpRequest entity = entities.get(i);
            HelpRequestDetailResponse dto = responses.get(i);

            dto.setNearestHelpPoint(findNearestHelpPoint(entity));
            dto.setNearestRescueTeam(findNearestRescueTeam(entity));
        }
        return responses;
    }

    public List<HelpRequestResponse> findAllPastRecord() {
        List<HelpRequestResponse> pastRecords = super.findAllAndReturnDtos().stream()
                .filter(dto -> Objects.equals(dto.getStatus(), StatusType.CP.label))
                .toList();
        if (pastRecords.isEmpty()) {
            throw new EntityNotFoundException("Completed help request not found!");
        }
        return pastRecords;
    }

    private HelpPointResponse findNearestHelpPoint(HelpRequest request) {
        List<HelpPoint> helpPoints = HELP_POINT_SERVICE.findAll();
        HelpPoint nearestHelpPoint = GEO_SERVICE.findNearest(request.getLatitude(), request.getLongitude(),
                helpPoints, HelpPoint::getLatitude, HelpPoint::getLongitude,
                () -> new NoAvailableHelpPointException("No available help point found!"));
        return HELP_POINT_MAPPER.toDto(nearestHelpPoint);
    }

    private RescueTeamLocationResponse findNearestRescueTeam(HelpRequest request) {
        List<RescueTeamLocation> rescueTeams = RESCUE_TEAM_SERVICE.findAll();
        RescueTeamLocation nearestRescuTeam = GEO_SERVICE.findNearest(request.getLatitude(), request.getLongitude(),
                rescueTeams, RescueTeamLocation::getLatitude, RescueTeamLocation::getLongitude,
                () -> new NoAvailableRescueTeamException("No available help point found!"));
        return RESCUE_TEAM_LOCATION_MAPPER.toDto(nearestRescuTeam);
    }

}
