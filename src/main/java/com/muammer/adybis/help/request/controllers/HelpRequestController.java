package com.muammer.adybis.help.request.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muammer.adybis.base.auth.AuthUtils;
import com.muammer.adybis.common.BaseController;
import com.muammer.adybis.common.ApiHTTPResponseCatalog;
import com.muammer.adybis.help.request.dtos.HelpRequestDetailRequest;
import com.muammer.adybis.help.request.dtos.HelpRequestDetailResponse;
import com.muammer.adybis.help.request.dtos.HelpRequestRequest;
import com.muammer.adybis.help.request.dtos.VictimHelpRequestResponse;
import com.muammer.adybis.help.request.services.HelpRequestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-url}/help-requests")
public class HelpRequestController implements BaseController<HelpRequestDetailRequest, UUID> {
        private final HelpRequestService HELP_REQUEST_SERVICE;

        @Operation(tags = { "Admin", "Dispatcher" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.CREATED_CODE, description = ApiHTTPResponseCatalog.CREATED_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.CONFLICT_CODE, description = ApiHTTPResponseCatalog.CONFLICT_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.FORBIDDEN_CODE, description = ApiHTTPResponseCatalog.FORBIDDEN_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin,dispatcher"),
                        })
        })
        @PreAuthorize("hasAnyAuthority('dispatcher','admin')")
        @PostMapping()
        @Override
        public ResponseEntity<?> createData(@RequestBody HelpRequestDetailRequest request) {

                HelpRequestDetailResponse response = HELP_REQUEST_SERVICE.saveRequestAndReturnDto(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);

        }

        @Operation(tags = { "Victim" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.CREATED_CODE, description = ApiHTTPResponseCatalog.CREATED_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.CONFLICT_CODE, description = ApiHTTPResponseCatalog.CONFLICT_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.FORBIDDEN_CODE, description = ApiHTTPResponseCatalog.FORBIDDEN_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.FORBIDDEN_CODE, description = "Please activated 2FA services!")
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "victim"),
                        })
        })
        @PreAuthorize("hasAnyAuthority('victim')")
        @PostMapping("/victim")
        public ResponseEntity<?> createData(@RequestBody HelpRequestRequest request) {
                if (AuthUtils.getCurrentUser2FAEnableValue()) {
                        VictimHelpRequestResponse response = HELP_REQUEST_SERVICE
                                        .saveVictimRequestAndReturnDto(request);
                        return ResponseEntity.status(HttpStatus.CREATED).body(response);
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Please activated 2FA services!");
        }

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Help Requests found!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),

        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin"), }) })
        @PreAuthorize("hasAnyAuthority('admin')")
        @GetMapping()
        @Override
        public ResponseEntity<?> findAll() {
                return ResponseEntity.status(HttpStatus.OK).body(HELP_REQUEST_SERVICE.findAllAndReturnDetailDtos());
        }

        @Operation(tags = { "Admin", "Dispatcher" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Help Requests found!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin,dispatcher"),
                        })
        })
        @PreAuthorize("hasAnyAuthority('admin','dispatcher')")
        @GetMapping("/pending")
        public ResponseEntity<?> findOnlyAllThosePending() {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(HELP_REQUEST_SERVICE.findByStatusAndReturnDtos());
        }

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Help Request found!"),
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
                return ResponseEntity.status(HttpStatus.OK).body(HELP_REQUEST_SERVICE.findByIdAndReturnDetailDto(id));
        }

        @Operation(tags = { "Admin", "Viewer" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Help Requests found!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin,viewer"),
                        })
        })
        @PreAuthorize("hasAnyAuthority('viewer','admin')")
        @GetMapping("/past-records")
        public ResponseEntity<?> findAllPastHelpRequests() {
                return ResponseEntity.status(HttpStatus.OK).body(HELP_REQUEST_SERVICE.findAllPastRecord());
        }

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Help Request updated!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin"),
                        })
        })
        @PreAuthorize("hasAnyAuthority('admin')")
        @PutMapping("/{id}")
        @Override
        public ResponseEntity<?> updateData(@RequestBody HelpRequestDetailRequest req, @PathVariable UUID id) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(HELP_REQUEST_SERVICE.updateAdminAndReturnDto(req, id));
        }

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Help Request deleted!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin"),
                        })
        })
        @PreAuthorize("hasAuthority('admin')")
        @DeleteMapping("/{id}")
        @Override
        public ResponseEntity<?> deleteById(@PathVariable UUID id) {
                HELP_REQUEST_SERVICE.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Help request deleted!"));
        }
}
