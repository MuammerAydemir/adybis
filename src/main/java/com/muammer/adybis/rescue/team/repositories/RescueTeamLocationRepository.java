package com.muammer.adybis.rescue.team.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.muammer.adybis.rescue.team.models.RescueTeamLocation;

@Repository
public interface RescueTeamLocationRepository extends JpaRepository<RescueTeamLocation, UUID> {

}
