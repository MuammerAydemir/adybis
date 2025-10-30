package com.muammer.adybis.help.point.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.muammer.adybis.base.auth.AuthUtils;
import com.muammer.adybis.common.AbstractBaseService;
import com.muammer.adybis.help.point.HelpPointMapper;
import com.muammer.adybis.help.point.dtos.HelpPointAdminResponse;
import com.muammer.adybis.help.point.dtos.HelpPointRequest;
import com.muammer.adybis.help.point.dtos.HelpPointResponse;
import com.muammer.adybis.help.point.models.HelpPoint;
import com.muammer.adybis.help.point.repositories.HelpPointRepository;

import jakarta.transaction.Transactional;

@Service
public class HelpPointService extends AbstractBaseService<HelpPoint, HelpPointRequest, HelpPointResponse, UUID> {
    private final HelpPointRepository HELP_POINT_REPO;
    private final HelpPointMapper HELP_POINT_MAPPER;

    public HelpPointService(HelpPointRepository helpPointRepository, HelpPointMapper helpPointMapper) {
        super(helpPointRepository, helpPointMapper, HelpPoint.class);
        this.HELP_POINT_REPO = helpPointRepository;
        this.HELP_POINT_MAPPER = helpPointMapper;
    }

    @Transactional
    public HelpPointAdminResponse saveAndReturnAdminDto(HelpPointRequest request) {
        UUID currentUser = AuthUtils.getCurrentUserId();
        HelpPoint entity = HELP_POINT_MAPPER.toEntity(request);
        entity.setCreatedById(currentUser);
        HelpPoint saved = HELP_POINT_REPO.save(entity);
        HelpPointAdminResponse response = HELP_POINT_MAPPER.toDetailDto(saved);
        return response;
    }

    public long helpPointCount() {
        return HELP_POINT_REPO.count();
    }
}
