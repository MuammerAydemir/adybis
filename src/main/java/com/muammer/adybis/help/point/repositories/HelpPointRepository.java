package com.muammer.adybis.help.point.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.muammer.adybis.help.point.models.HelpPoint;

@Repository
public interface HelpPointRepository extends JpaRepository<HelpPoint, UUID> {

}
