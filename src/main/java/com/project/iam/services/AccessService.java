package com.project.iam.services;

import com.project.iam.config.exception.ProcessException;
import com.project.iam.config.process.JwtUtil;
import com.project.iam.entities.*;
import com.project.iam.models.request.SubmissionRequest;
import com.project.iam.models.response.AccessResp;
import com.project.iam.repositories.*;
import com.project.iam.utils.GlobalUtility;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.project.iam.utils.Constants.MasterAction.ACTION_ADD;
import static com.project.iam.utils.Constants.MasterAction.ACTION_REVOKE;
import static com.project.iam.utils.Constants.MasterStatus.APPROVED;
import static com.project.iam.utils.Constants.MasterStatus.P_MANAGER;

@Service
@RequiredArgsConstructor
public class AccessService {

    private final UserAccessRepository userAccessRepository;
    private final RequestAccessRepository requestAccessRepository;
    private final RequestAccessDetailRepo requestDetailRepository;
    private final AccessCatalogRepository accessCatalogRepository;
    private final RequestAccessHistoryRepo requestHistoryRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;

    public List<AccessResp> getAllAccessData(){
        return userAccessRepository.findByUserEntity_Username(GlobalUtility.getCurrentCredUsername()).stream().map(
                ua -> AccessResp.builder()
                        .catalog(ua.getCatalogEntity().getAccessName())
                        .baseUrl(ua.getCatalogEntity().getBaseUrl())
                        .build()
        ).toList();
    }

    @Transactional
    public void postSubmissionAccess(SubmissionRequest request){
        Claims claim = jwtUtil.getClaimsFromToken();
        Long userId = ((Integer) claim.get("idUser")).longValue();
        UserEntity userData = userRepository.findById(userId).orElseThrow();
        RoleEntity roleManager = roleRepository.findByRoleName("MANAGER").orElseThrow(()->new ProcessException(HttpStatus.NOT_FOUND,"Role not found"));

        if (requestAccessRepository.existsByCreatedBy_IdAndStatusNot(userId,APPROVED))
            throw new ProcessException(HttpStatus.BAD_REQUEST, "You still have an active submission");

        RequestAccessEntity submission = new RequestAccessEntity();
        submission.setStatus(P_MANAGER);
        submission.setAssignedRole(roleManager);
        submission.setLastUpdated(LocalDateTime.now());
        submission.setCreatedBy(userData);

        //assign to manager if exists
        if (userData.getManager()!=null)
            submission.setAssignedUser(userData.getManager());

        requestAccessRepository.save(submission);

        //saving detail submission
        List<RequestAccessDetailEntity> listAccess = new ArrayList<>();
        List<String> catalogReqName = new ArrayList<>();
        for (var item : request.items()){
            AccessCatalogEntity catalog = accessCatalogRepository.findByAccessName(item.accessCatalogName()).orElseThrow(()->new ProcessException(HttpStatus.NOT_FOUND,"Catalog "+item.accessCatalogName()+" not found"));

            if (item.action().equals(ACTION_ADD) &&
                    userAccessRepository.existsByUserEntity_IdAndCatalogEntity_Id(userId,catalog.getId()))
                throw new ProcessException(HttpStatus.BAD_REQUEST,"Access to "+item.accessCatalogName()+" already exists");

            if (item.action().equals(ACTION_REVOKE) &&
                    !userAccessRepository.existsByUserEntity_IdAndCatalogEntity_Id(userId,catalog.getId()))
                throw new ProcessException(HttpStatus.BAD_REQUEST,"Access to "+item.accessCatalogName()+" never exists");

            RequestAccessDetailEntity accessRequest = new RequestAccessDetailEntity();
            accessRequest.setAction(item.action());
            accessRequest.setCatalog(catalog);
            accessRequest.setRequestAccess(submission);
            catalogReqName.add(item.accessCatalogName());

            listAccess.add(accessRequest);
        }
        requestDetailRepository.saveAll(listAccess);

        //saving history
        RequestAccessHistoryEntity history = new RequestAccessHistoryEntity();
        history.setRequestAccess(submission);
        history.setStatus(submission.getStatus());
        history.setUpdatedAt(LocalDateTime.now());
        history.setOldAccessCatalog(buildOldCatalogAccess(userId));
        history.setNewAccessCatalog((new ObjectMapper().writeValueAsString(catalogReqName)));
        history.setUpdatedBy(userId);
        history.setAssignedUser(submission.getAssignedUser());
        history.setAssignedRole(submission.getAssignedRole());

        requestHistoryRepository.save(history);
    }

    private String buildOldCatalogAccess(Long userId){
        List<UserAccessEntity> userAccess = userAccessRepository.findByUserEntity_Id(userId);
        List<String> resp = userAccess.stream().map(u->u.getCatalogEntity().getAccessName()).toList();
        return (new ObjectMapper()).writeValueAsString(resp);
    }

}
