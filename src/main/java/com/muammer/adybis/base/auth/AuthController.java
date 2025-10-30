package com.muammer.adybis.base.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muammer.adybis.base.auth.dtos.AuthRequest;
import com.muammer.adybis.base.auth.dtos.AuthResponse;
import com.muammer.adybis.base.auth.dtos.RegisterRequest;
import com.muammer.adybis.base.auth.dtos.TwoFactorRequest;
import com.muammer.adybis.base.auth.twoFactorAuth.enums.TwoFactorMethod;
import com.muammer.adybis.base.auth.twoFactorAuth.services.TwoFactorAuthService;
import com.muammer.adybis.base.security.JwtService;
import com.muammer.adybis.common.ApiHTTPResponseCatalog;
import com.muammer.adybis.user.models.User;
import com.muammer.adybis.user.services.UserService;
import com.muammer.adybis.user.services.Impls.UserDetailsImpl;
import com.muammer.adybis.user.services.Impls.UserDetailsServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Tag(name = "Authentication API", description = "User Authentication Processes")
@RequestMapping("${api.base-url}/auth")
@RequiredArgsConstructor
public class AuthController {

        private final AuthenticationManager AUTHEN_MANAGER;

        private final UserDetailsServiceImpl USER_DETAILS_SERVICE_IMPL;

        private final JwtService JWT_SERVICE;

        private final TwoFactorAuthService TWO_FACTOR_AUTH_SERVICE;

        private final UserService USER_SERVICE;

        @Operation(responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = ApiHTTPResponseCatalog.OK_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.UNAUTHORIZED_CODE, description = ApiHTTPResponseCatalog.UNAUTHORIZED_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.NOT_FOUND_CODE, description = ApiHTTPResponseCatalog.NOT_FOUND_MESSAGE),
        })
        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody AuthRequest authRequest)
                        throws AuthenticationException, MessagingException {

                AUTHEN_MANAGER.authenticate(
                                new UsernamePasswordAuthenticationToken(authRequest.getUsername().toLowerCase(),
                                                authRequest.getPassword()));

                final UserDetailsImpl USER_DETAILS = USER_DETAILS_SERVICE_IMPL
                                .loadUserByUsername(authRequest.getUsername().toLowerCase());
                if (!USER_DETAILS.isVerified()) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .body(Map.of("message", "User account not verified!"));
                }
                if (USER_DETAILS.is2FAEnabled()) {
                        TWO_FACTOR_AUTH_SERVICE.generatedCodeAndSend(TwoFactorMethod.EMAIL, USER_DETAILS.getUsername());
                        return ResponseEntity.status(HttpStatus.OK).body(Map.ofEntries(
                                        Map.entry("message", "Two-factor authentication code sent. Please verify."),
                                        Map.entry("user_id", USER_DETAILS.getUserId())));
                }
                final String TOKEN = JWT_SERVICE.generateToken(USER_DETAILS);

                return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(TOKEN));

        }

        @Operation(responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.CREATED_CODE, description = ApiHTTPResponseCatalog.CREATED_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.UNAUTHORIZED_CODE, description = ApiHTTPResponseCatalog.UNAUTHORIZED_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.CONFLICT_CODE, description = ApiHTTPResponseCatalog.CONFLICT_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
        })
        @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody RegisterRequest authRequest) throws Exception {
                User user = USER_DETAILS_SERVICE_IMPL.register(authRequest);
                if (!user.isVerified()) {
                        TWO_FACTOR_AUTH_SERVICE.generatedCodeAndSend(TwoFactorMethod.EMAIL, user.getUsername());

                        return ResponseEntity.status(HttpStatus.ACCEPTED)
                                        .body(Map.of(
                                                        "message", "Verify code sent. Please verify.",
                                                        "user_id", user.getId()));
                }
                return ResponseEntity.status(HttpStatus.CREATED).body("User successfully created!");
        }

        @Operation(responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = ApiHTTPResponseCatalog.OK_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.UNAUTHORIZED_CODE, description = ApiHTTPResponseCatalog.UNAUTHORIZED_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
        })
        @PostMapping("/account/verify")
        public ResponseEntity<?> verifyAccount(@RequestBody TwoFactorRequest request) {
                boolean verified = TWO_FACTOR_AUTH_SERVICE.verifyCode(request.getUserId(), request.getCode());

                if (!verified) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .body(Map.of("error", "Invalid or expired code"));
                }

                USER_SERVICE.enableAccountVerification(request.getUserId());

                return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Account verified successfully!"));
        }

        @Operation(responses = {
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.OK_CODE, description = ApiHTTPResponseCatalog.OK_CODE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.BAD_REQUEST_CODE, description = ApiHTTPResponseCatalog.BAD_REQUEST_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.UNAUTHORIZED_CODE, description = ApiHTTPResponseCatalog.UNAUTHORIZED_MESSAGE),
                        @ApiResponse(responseCode = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_CODE, description = ApiHTTPResponseCatalog.INTERNAL_SERVER_ERROR_MESSAGE),
        })
        @PostMapping("/login/2fa")
        public ResponseEntity<?> verifyLogin2FA(@RequestBody TwoFactorRequest request) {

                boolean verified = TWO_FACTOR_AUTH_SERVICE.verifyCode(request.getUserId(), request.getCode());

                if (!verified) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .body(Map.of("error", "2FA code invalid or expired"));
                }

                User user = USER_SERVICE.findById(request.getUserId());
                UserDetailsImpl details = USER_DETAILS_SERVICE_IMPL.loadUserByUsername(user.getUsername());
                String token = JWT_SERVICE.generateToken(details);

                return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(token));
        }

}
