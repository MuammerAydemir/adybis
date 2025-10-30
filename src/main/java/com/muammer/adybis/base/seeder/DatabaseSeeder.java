package com.muammer.adybis.base.seeder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.muammer.adybis.help.point.models.HelpPoint;
import com.muammer.adybis.help.point.services.HelpPointService;
import com.muammer.adybis.help.request.dtos.HelpRequestDetailResponse;
import com.muammer.adybis.help.request.models.HelpRequest;
import com.muammer.adybis.help.request.services.HelpRequestService;
import com.muammer.adybis.rescue.assignment.models.RescueAssignment;
import com.muammer.adybis.rescue.assignment.services.RescueAssigmentService;
import com.muammer.adybis.rescue.team.models.RescueTeamLocation;
import com.muammer.adybis.rescue.team.services.RescueTeamLocationService;
import com.muammer.adybis.role.models.Role;
import com.muammer.adybis.role.services.RoleService;
import com.muammer.adybis.user.services.UserService;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final HelpPointService HELP_POINT_SERVICE;
    private final UserService USER_SERVICE;
    private final RoleService ROLE_SERVICE;
    private final RescueTeamLocationService RESCUE_TEAM_LOC_SER;
    private final HelpRequestService HELP_REQUEST_SERVICE;
    private final RescueAssigmentService RESCUE_ASSIGNMENT_SERVICE;

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedHelpPoints();
        seedRescueTeamLocations();
        seedHelpRequests();
        seedRescueAssignments();
    }

    private void seedUsers() {
        if (USER_SERVICE.userCount() == 0) {
            Role victim = ROLE_SERVICE.findRoleByName("victim");
            Role admin = ROLE_SERVICE.findRoleByName("admin");
            Role rescueTeam = ROLE_SERVICE.findRoleByName("rescue_team");
            Role dispatcher = ROLE_SERVICE.findRoleByName("dispatcher");
            Role viewer = ROLE_SERVICE.findRoleByName("viewer");
            USER_SERVICE.saveAll(List.of(
                    UserFactory.createAdmin(List.of(admin), "admin123?", "adminUser"),
                    UserFactory.createRescueTeam(List.of(rescueTeam), "res1cu123?", "rescueTeam1"),
                    UserFactory.createRescueTeam(List.of(rescueTeam), "res2cu123?", "rescueTeam2"),
                    UserFactory.createDispatcher(List.of(dispatcher), "dispac123?", "dispatcherUser"),
                    UserFactory.createViewer(List.of(viewer), "viewer123?", "viewerUser"),
                    UserFactory.createVictim(List.of(victim), "vic1tim123?", "victimUser1"),
                    UserFactory.createVictim(List.of(victim), "vic2tim123?", "victimUser2")));
            log.info("✅ Test users created successfully.");
        } else {
            log.info("🤙 Users already exist!");
        }
    }

    private void seedHelpPoints() {
        if (HELP_POINT_SERVICE.helpPointCount() == 0) {
            List<UUID> dispatcherIds = USER_SERVICE.usersIdByRoleName("dispatcher");
            List<HelpPoint> points = Stream.generate(() -> HelpPointFactory.createRandomHelpPoint(dispatcherIds.get(0)))
                    .limit(5)
                    .toList();
            HELP_POINT_SERVICE.saveAll(points);
            log.info("✅ Test help point created successfully.");
        } else {
            log.info("🤙 Help Points already exist!");
        }
    }

    private void seedRescueTeamLocations() {
        if (RESCUE_TEAM_LOC_SER.rescueTeamLocationCount() == 0) {
            List<UUID> teamIds = USER_SERVICE.usersIdByRoleName("rescue_team");
            List<RescueTeamLocation> locations = teamIds.stream().map(RescueTeamFactory::createRandomRescueTeamLocation)
                    .toList();
            RESCUE_TEAM_LOC_SER.saveAll(locations);
            log.info("✅ Test rescue team created successfully.");
        } else {
            log.info("🤙 Rescue Team Locations already exist!");
        }
    }

    private void seedHelpRequests() {
        if (HELP_REQUEST_SERVICE.helpRequestCount() == 0) {
            UUID victimFirst = USER_SERVICE.usersIdByRoleName("victim").get(0);
            UUID victimSecond = USER_SERVICE.usersIdByRoleName("victim").get(1);
            List<HelpRequest> requests = Stream.generate(() -> HelpRequestFactory.createRandomHelpRequest(victimFirst))
                    .limit(5)
                    .collect(Collectors.toList());
            requests.addAll(
                    Stream.generate(() -> HelpRequestFactory.createRandomExpiredHelpRequest(victimSecond)).limit(3)
                            .collect(Collectors.toList()));
            HELP_REQUEST_SERVICE.saveAll(requests);
            log.info("✅ Test help requests created successfully.");
        } else {
            log.info(" 🤙 Help Requests already exist!");
        }
    }

    private void seedRescueAssignments() {

        if (RESCUE_ASSIGNMENT_SERVICE.RescueAssignmentCount() == 0) {
            List<UUID> helpRequestIds = HELP_REQUEST_SERVICE.findByStatusAndReturnDtos().stream()
                    .map(HelpRequestDetailResponse::getId).toList();
            List<UUID> teamIds = USER_SERVICE.usersIdByRoleName("rescue_team");
            List<UUID> dispatcherIds = USER_SERVICE.usersIdByRoleName("dispatcher");

            if (helpRequestIds.isEmpty() || teamIds.isEmpty() || dispatcherIds.isEmpty()) {
                log.warn("There is insufficient data to create a rescue assignment!");
                return;
            }
            List<RescueAssignment> assignments = new ArrayList<>();

            for (int i = 0; i < teamIds.size() && i < helpRequestIds.size(); i++) {
                UUID teamId = teamIds.get(i);
                UUID helpRequestId = helpRequestIds.get(i);
                UUID dispatcherId = dispatcherIds.get(i % dispatcherIds.size());

                assignments.add(
                        RescueAssignmentFactory.createRandomRescueAssignment(
                                helpRequestId,
                                teamId,
                                dispatcherId));
            }
            RESCUE_ASSIGNMENT_SERVICE.saveAll(assignments);
            log.info("✅ Test rescue assignments created successfully.");
        } else {
            log.info(" 🤙 Rescue Team Assignment already exist!");
        }
    }

    @PreDestroy
    public void cleanup() {
        RESCUE_TEAM_LOC_SER.deleteAll();
        HELP_POINT_SERVICE.deleteAll();
        HELP_REQUEST_SERVICE.deleteAll();
        USER_SERVICE.deleteAll();
        log.info(" 🗑️ Users deleted!");
        log.info(" 🗑️ Help Points deleted!");
        log.info(" 🗑️ Help Requests deleted!");
        log.info(" 🗑️ Rescue Team Locations deleted!");
    }
}
