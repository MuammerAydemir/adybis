package com.muammer.adybis.rescue.assignment.controllers;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muammer.adybis.base.auth.AuthUtils;
import com.muammer.adybis.common.ApiHTTPResponseCatalog;
import com.muammer.adybis.common.BaseController;
import com.muammer.adybis.rescue.assignment.dtos.RescueAssignmentDetailRequest;
import com.muammer.adybis.rescue.assignment.dtos.RescueAssignmentRequest;
import com.muammer.adybis.rescue.assignment.services.RescueAssigmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-url}/rescue-assignments")
public class RescueAssignmentController implements BaseController<RescueAssignmentDetailRequest, UUID> {

        private final RescueAssigmentService RESCUE_ASSIGNMENT_SERVICE;

        @Operation(tags = { "Dispatcher" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Rescue Assignment created!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.CONFLICT_CODE, description = ApiHTTPResponseCatalog.CONFLICT_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "dispatcher"),
                        })
        })
        @PreAuthorize("hasAnyAuthority('dispatcher')")
        @PostMapping()
        public ResponseEntity<?> createData(@RequestBody RescueAssignmentRequest req) {
                return ResponseEntity.status(HttpStatus.OK).body(RESCUE_ASSIGNMENT_SERVICE.saveRescueAssignment(req));
        }

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Rescue Assignment created!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.CONFLICT_CODE, description = ApiHTTPResponseCatalog.CONFLICT_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin"),
                        })
        })
        @PreAuthorize("hasAnyAuthority('admin')")
        @PostMapping("/admin")
        @Override
        public ResponseEntity<?> createData(@RequestBody RescueAssignmentDetailRequest req) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(RESCUE_ASSIGNMENT_SERVICE.saveRescueAssignmentAndReturnDetailDto(req));
        }

        @Operation(tags = { "Admin", "Dispatcher" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Rescue Assignments found!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin,dispatcher"),
                        })
        })
        @PreAuthorize("hasAnyAuthority('admin','dispatcher')")
        @GetMapping()
        @Override
        public ResponseEntity<?> findAll() {
                return ResponseEntity.status(HttpStatus.OK).body(RESCUE_ASSIGNMENT_SERVICE.findAllAndReturnDtos());
        }

        @Operation(tags = { "Rescue Team" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Rescue Assignments found!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "rescue_team"),
                        })
        })
        @PreAuthorize("hasAnyAuthority('rescue_team')")
        @GetMapping("/my-assignments")
        public ResponseEntity<?> findAllByRescueTeam() {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(RESCUE_ASSIGNMENT_SERVICE.findOnlyRescuesAssigned(AuthUtils.getCurrentUserId()));
        }

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Rescue Assignment found!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin"),
                        })
        })
        @PreAuthorize("hasAnyAuthority('admin')")
        @GetMapping("/{id}")
        @Override
        public ResponseEntity<?> findById(@PathVariable UUID id) {
                return ResponseEntity.status(HttpStatus.OK).body(RESCUE_ASSIGNMENT_SERVICE.findByIdAndReturnDto(id));
        }

        @Operation(tags = { "Admin", "Dispatcher" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Rescue Assignment updated!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin,dispatcher"),
                        })
        })
        @PreAuthorize("hasAnyAuthority('admin','dispatcher')")
        @PutMapping("/{id}")
        @Override
        public ResponseEntity<?> updateData(@RequestBody RescueAssignmentDetailRequest req, @PathVariable UUID id) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(RESCUE_ASSIGNMENT_SERVICE.updateDatasAndReturnDto(req, id));
        }

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Rescue Assignment deleted!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin")
                        })
        })
        @PreAuthorize("hasAuthority('admin')")
        @DeleteMapping("/{id}")
        @Override
        public ResponseEntity<?> deleteById(@PathVariable UUID id) {
                RESCUE_ASSIGNMENT_SERVICE.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Rescue Assignment deleted!"));
        }

}
