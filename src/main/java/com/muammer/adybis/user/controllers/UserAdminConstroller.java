package com.muammer.adybis.user.controllers;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.muammer.adybis.common.ApiHTTPResponseCatalog;
import com.muammer.adybis.common.BaseController;
import com.muammer.adybis.user.dtos.UserDetailRequest;
import com.muammer.adybis.user.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("${api.base-url}/admin/users")
public class UserAdminConstroller implements BaseController<UserDetailRequest, UUID> {
        private final UserService USER_SERVICE;

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "User created!"),
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
        public ResponseEntity<?> createData(@RequestBody UserDetailRequest user) {
                return ResponseEntity.status(HttpStatus.CREATED).body(USER_SERVICE.saveAndReturnDto(user));
        }

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "User updated!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin")
                        })
        })
        @PreAuthorize("hasAnyAuthority('admin')")
        @PutMapping("/user/{id}")
        @Override
        public ResponseEntity<?> updateData(@RequestBody UserDetailRequest req, @PathVariable UUID id) {
                return ResponseEntity.status(HttpStatus.OK).body(USER_SERVICE.updateDatasAndReturnDto(req, id));
        }

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "User found!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin")
                        })
        })
        @PreAuthorize("hasAnyAuthority('admin')")
        @GetMapping("/user/{id}")
        @Override
        public ResponseEntity<?> findById(@PathVariable UUID id) {
                return ResponseEntity.status(HttpStatus.OK).body(USER_SERVICE.findByIdAndReturnDto(id));
        }

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "Users found!"),
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
                return ResponseEntity.status(HttpStatus.OK).body(USER_SERVICE.findAllAndReturnDtos());
        }

        @Operation(tags = { "Admin" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = "User deleted!"),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "admin")
                        })
        })
        @PreAuthorize("hasAnyAuthority('admin')")
        @DeleteMapping("/user/{id}")
        @Override
        public ResponseEntity<?> deleteById(@PathVariable UUID id) {
                USER_SERVICE.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "User deleted!"));
        }

}
