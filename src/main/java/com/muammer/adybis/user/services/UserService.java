package com.muammer.adybis.user.services;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.muammer.adybis.base.auth.dtos.RegisterRequest;
import com.muammer.adybis.common.AbstractBaseService;
import com.muammer.adybis.role.models.Role;
import com.muammer.adybis.role.services.RoleService;
import com.muammer.adybis.user.UserMapper;
import com.muammer.adybis.user.dtos.UserDetailRequest;
import com.muammer.adybis.user.dtos.UserDetailResponse;
import com.muammer.adybis.user.dtos.UserResponse;
import com.muammer.adybis.user.enums.BloodTypesEnums;
import com.muammer.adybis.user.models.User;
import com.muammer.adybis.user.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserService extends AbstractBaseService<User, UserDetailRequest, UserDetailResponse, UUID> {

    private final UserRepository USER_REPOSITORY;
    private final RoleService ROLE_SERVICE;
    private final UserMapper USER_MAPPER;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, UserMapper userMapper, RoleService roleService) {
        super(userRepository, userMapper, User.class);
        this.USER_REPOSITORY = userRepository;
        this.ROLE_SERVICE = roleService;
        this.USER_MAPPER = userMapper;
    }

    @Transactional
    @Override
    public UserDetailResponse saveAndReturnDto(UserDetailRequest request) {
        UUID victimId = ROLE_SERVICE.findRoleByName("victim").getId();
        boolean isValid = Arrays.stream(BloodTypesEnums.values())
                .anyMatch(e -> e.getLabel().equals(request.getBloodType().toLowerCase()));
        boolean isLegalAge = 15 < (LocalDate.now().getYear() - request.getBirthdayDate().getYear());
        if (!isValid) {
            throw new IllegalArgumentException("Blood type is not acceptable!");
        }
        if (!isLegalAge) {
            throw new IllegalArgumentException("It must be at least 15 years old!");
        }
        if (request.getRoleIds() == null || request.getRoleIds().isEmpty()) {
            request.setRoleIds(List.of(victimId));
        }
        return super.saveAndReturnDto(request);
    }

    @Transactional
    public User save(RegisterRequest request) {
        Role victim = ROLE_SERVICE.findRoleByName("victim");
        boolean isValid = Arrays.stream(BloodTypesEnums.values())
                .anyMatch(e -> e.getLabel().equals(request.getBloodType().toLowerCase()));
        boolean isLegalAge = 15 < (LocalDate.now().getYear() - request.getBirthdayDate().getYear());
        if (!isValid) {
            throw new IllegalArgumentException("Blood type is not acceptable!");
        }
        if (!isLegalAge) {
            throw new IllegalArgumentException("You must be at least 15 years old!");
        }
        User user = USER_MAPPER.toEntityFromRegister(request);
        user.setRoles(List.of(victim));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return super.save(user);
    }

    @Transactional
    public UserResponse findByIdAndReturnResponseDto(UUID id) {
        UserResponse response = USER_MAPPER.toBasicDto(super.findById(id));
        return response;
    }

    @Transactional
    public User findUserByUsername(String username) {
        return USER_REPOSITORY.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Username cannoot be null or empty!"));
    }

    @Transactional
    public boolean existUsername(String name) {
        return USER_REPOSITORY.existsByUsername(name);
    }

    @Transactional
    public long userCount() {
        return USER_REPOSITORY.count();
    }

    @Transactional
    public List<UUID> usersIdByRoleName(String name) {
        return USER_REPOSITORY.findUsersIdByRoleName(name);
    }

    @Transactional
    public void enableAccountVerification(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("User id cannot be null!");
        }
        User user = super.findById(id);
        if (user == null) {
            throw new EntityNotFoundException("User not found!");
        }
        user.setVerified(true);
        super.save(user);
    }

}
