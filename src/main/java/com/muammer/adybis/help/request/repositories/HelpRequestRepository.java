package com.muammer.adybis.help.request.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.muammer.adybis.help.request.models.HelpRequest;
import java.util.List;

@Repository
public interface HelpRequestRepository extends JpaRepository<HelpRequest, UUID> {
    List<HelpRequest> findByStatus(String status);
}
