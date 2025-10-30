package com.muammer.adybis.user.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muammer.adybis.base.auth.AuthUtils;
import com.muammer.adybis.base.auth.dtos.TwoFactorRequest;
import com.muammer.adybis.base.auth.twoFactorAuth.enums.TwoFactorMethod;
import com.muammer.adybis.base.auth.twoFactorAuth.services.TwoFactorAuthService;
import com.muammer.adybis.common.ApiHTTPResponseCatalog;
import com.muammer.adybis.user.models.User;
import com.muammer.adybis.user.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("${app.baseUrl}/users/user")
@RequiredArgsConstructor
public class UserController {
        private final TwoFactorAuthService TWO_FACTOR_AUTH_SERVICE;
        private final UserService USER_SERVICE;

        @Operation(tags = { "Victim" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = ApiHTTPResponseCatalog.OK_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.UNAUTHORIZED_CODE, description = ApiHTTPResponseCatalog.UNAUTHORIZED_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "victim"),
                        })
        })
        @PreAuthorize("hasAnyAuthority('victim')")
        @GetMapping("/activate/2FA")
        public ResponseEntity<?> update2FA()
                        throws MessagingException {
                User user = USER_SERVICE.findById(AuthUtils.getCurrentUserId());
                if (user.isTwoFAEnabled() == true) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(Map.of("message", "Two-factor authentication is already enabled!"));
                }
                TWO_FACTOR_AUTH_SERVICE.generatedCodeAndSend(TwoFactorMethod.EMAIL, user.getUsername());
                return ResponseEntity.status(HttpStatus.OK).body(Map.ofEntries(
                                Map.entry("message", "Two-factor authentication code sent. Please verify."),
                                Map.entry("user_id", user.getId())));

        }

        @Operation(tags = { "Victim" }, responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = ApiHTTPResponseCatalog.OK_CODE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.UNAUTHORIZED_CODE, description = ApiHTTPResponseCatalog.UNAUTHORIZED_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
        }, extensions = {
                        @Extension(name = "roles", properties = {
                                        @ExtensionProperty(name = "roles", value = "victim"),
                        })
        })
        @PreAuthorize("hasAnyAuthority('victim')")
        @PostMapping("activated/2fa")
        public ResponseEntity<?> verify2FA(@RequestBody TwoFactorRequest request) {

                boolean verified = TWO_FACTOR_AUTH_SERVICE.verifyCode(request.getUserId(), request.getCode());

                if (!verified) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .body(Map.of("error", "2FA code invalid or expired"));
                }

                User user = USER_SERVICE.findById(request.getUserId());
                user.setTwoFAEnabled(true);
                USER_SERVICE.save(user);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(Map.of("message", "Two-factor authentication is now enabled!"));
        }
}
