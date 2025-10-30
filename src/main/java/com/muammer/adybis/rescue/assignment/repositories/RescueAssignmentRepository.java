package com.muammer.adybis.rescue.assignment.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.muammer.adybis.rescue.assignment.models.RescueAssignment;

@Repository
public interface RescueAssignmentRepository extends JpaRepository<RescueAssignment, UUID> {

}
