package com.muammer.adybis.rescue.team.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.muammer.adybis.common.AbstractBaseService;
import com.muammer.adybis.rescue.team.RescueTeamLocationMapper;
import com.muammer.adybis.rescue.team.dtos.RescueTeamLocationRequest;
import com.muammer.adybis.rescue.team.dtos.RescueTeamLocationResponse;
import com.muammer.adybis.rescue.team.models.RescueTeamLocation;
import com.muammer.adybis.rescue.team.repositories.RescueTeamLocationRepository;

@Service
public class RescueTeamLocationService
        extends AbstractBaseService<RescueTeamLocation, RescueTeamLocationRequest, RescueTeamLocationResponse, UUID> {
    private final RescueTeamLocationRepository RESCUE_TEAM_LOC_REPO;

    public RescueTeamLocationService(RescueTeamLocationRepository rescueTeamLocationRepository,
            RescueTeamLocationMapper rescueTeamLocationMapper) {
        super(rescueTeamLocationRepository, rescueTeamLocationMapper, RescueTeamLocation.class);
        this.RESCUE_TEAM_LOC_REPO = rescueTeamLocationRepository;
    }

    public long rescueTeamLocationCount() {
        return RESCUE_TEAM_LOC_REPO.count();
    }

}
