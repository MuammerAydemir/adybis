package com.muammer.adybis.help.point.controllers;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muammer.adybis.common.ApiHTTPResponseCatalog;
import com.muammer.adybis.common.BaseController;
import com.muammer.adybis.help.point.dtos.HelpPointRequest;
import com.muammer.adybis.help.point.services.HelpPointService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-url}/help-points")
public class HelpPointController implements BaseController<HelpPointRequest, UUID> {

        private final HelpPointService HELP_POINT_SERVICE;

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Help Point created!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.CONFLICT_CODE, description = ApiHTTPResponseCatalog.CONFLICT_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin"),
                        })
        })
        @PreAuthorize("hasAnyAuthority('admin')")
        @PostMapping()
        @Override
        public ResponseEntity<?> createData(@RequestBody HelpPointRequest req) {
                return ResponseEntity.status(HttpStatus.OK).body(HELP_POINT_SERVICE.saveAndReturnAdminDto(req));
        }

        @Operation(tags = { "Admin", "Dispatcher", "Rescue Team", "Viewer", "Victim" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Help Points found!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin,dispatcher,dispatcher,rescue_team,victim,viewer"),
                        })
        })
        @PreAuthorize("hasAnyAuthority('dispatcher','admin','rescue_team','victim','viewer')")
        @GetMapping()
        @Override
        public ResponseEntity<?> findAll() {
                return ResponseEntity.status(HttpStatus.OK).body(HELP_POINT_SERVICE.findAllAndReturnDtos());
        }

        @Operation(tags = { "Admin", "Dispatcher", "Rescue Team" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Help Point found!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin,dispatcher,rescue_team"),

                        })
        })
        @PreAuthorize("hasAnyAuthority('dispatcher','admin','rescue_team')")
        @GetMapping("/{id}")
        @Override
        public ResponseEntity<?> findById(@PathVariable UUID id) {
                return ResponseEntity.status(HttpStatus.OK).body(HELP_POINT_SERVICE.findByIdAndReturnDto(id));
        }

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Help Point updated!"),
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
        public ResponseEntity<?> updateData(@RequestBody HelpPointRequest req, @PathVariable UUID id) {
                return ResponseEntity.status(HttpStatus.OK).body(HELP_POINT_SERVICE.updateDatasAndReturnDto(req, id));
        }

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Help Point deleted!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin"),
                        })
        })
        @PreAuthorize("hasAuthority('admin')")
        @DeleteMapping("/{id}")
        @Override
        public ResponseEntity<?> deleteById(@PathVariable UUID id) {
                HELP_POINT_SERVICE.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Help point deleted!"));
        }

}
