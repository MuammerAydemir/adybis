package com.muammer.adybis.role.controllers;

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
import com.muammer.adybis.role.dtos.RoleRequest;
import com.muammer.adybis.role.services.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-url}/roles")
public class RoleController implements BaseController<RoleRequest, UUID> {
        private final RoleService ROLE_SERVICE;

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Role created!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.CONFLICT_CODE, description = ApiHTTPResponseCatalog.CONFLICT_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin")
                        })
        })
        @PreAuthorize("hasAnyAuthority('admin')")
        @PostMapping()
        @Override
        public ResponseEntity<?> createData(@RequestBody RoleRequest req) {
                return ResponseEntity.status(HttpStatus.OK).body(ROLE_SERVICE.saveAndReturnBasicDto(req));
        }

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Role updated!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin")
                        })
        })
        @PreAuthorize("hasAnyAuthority('admin')")
        @PutMapping("/role/{id}")
        @Override
        public ResponseEntity<?> updateData(@RequestBody RoleRequest req, @PathVariable UUID id) {
                return ResponseEntity.status(HttpStatus.OK).body(ROLE_SERVICE.updateDatasAndReturnDto(req, id));
        }

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Roles found!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin")
                        })
        })
        @PreAuthorize("hasAnyAuthority('admin')")
        @GetMapping()
        @Override
        public ResponseEntity<?> findAll() {
                return ResponseEntity.status(HttpStatus.OK).body(ROLE_SERVICE.findAllAndReturnDtos());
        }

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Role found!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin")
                        })
        })
        @PreAuthorize("hasAnyAuthority('admin')")
        @GetMapping("/role/{id}")
        @Override
        public ResponseEntity<?> findById(@PathVariable UUID id) {
                return ResponseEntity.status(HttpStatus.OK).body(ROLE_SERVICE.findByIdAndReturnDto(id));
        }

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Role deleted!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin")
                        })
        })
        @DeleteMapping("/role/{id}")
        @Override
        public ResponseEntity<?> deleteById(@PathVariable UUID id) {
                ROLE_SERVICE.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Role deleted!"));
        }

}
