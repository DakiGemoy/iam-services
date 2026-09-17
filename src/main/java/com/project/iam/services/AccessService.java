package com.project.iam.services;

import com.project.iam.models.response.AccessResp;
import com.project.iam.repositories.UserAccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessService {

    private final UserAccessRepository userAccessRepository;

    public List<AccessResp> check(Long userId){
        return userAccessRepository.findByUserEntity_Id(userId).stream().map(
                ua -> AccessResp.builder()
                        .catalog(ua.getCatalogEntity().getAccessName())
                        .baseUrl(ua.getCatalogEntity().getBaseUrl())
                        .build()
        ).toList();
    }

}
