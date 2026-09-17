package com.project.iam.services;

import com.project.iam.config.exception.ProcessException;
import com.project.iam.config.process.JwtUtil;
import com.project.iam.entities.*;
import com.project.iam.models.request.ApprovalSubmissionRequest;
import com.project.iam.models.request.SubmissionRequest;
import com.project.iam.models.response.AccessResp;
import com.project.iam.models.response.AccessSubmissionResp;
import com.project.iam.models.response.BasePaginationResponse;
import com.project.iam.models.response.SimpleSubmissionDetailDto;
import com.project.iam.repositories.*;
import com.project.iam.utils.GlobalUtility;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.project.iam.utils.Constants.MasterAction.*;
import static com.project.iam.utils.Constants.MasterStatus.*;

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
        return accessCatalogRepository.findAll().stream()
                .map(
                        a->AccessResp.builder()
                                .catalog(a.getAccessName()).baseUrl(a.getBaseUrl())
                                .build())
                .toList();
    }

    public List<AccessResp> getAllPrivateAccessData(){
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

        if (requestAccessRepository.existsByCreatedBy_IdAndStatusNotIn(userId,List.of(APPROVED,REJECTED)))
            throw new ProcessException(HttpStatus.BAD_REQUEST, "You still have an active submission");

        RequestAccessEntity submission = new RequestAccessEntity();
        submission.setStatus(P_MANAGER);
        submission.setAssignedRole(roleManager);
        submission.setLastUpdated(LocalDateTime.now());
        submission.setCreatedBy(userData);
        submission.setReasonSubmission(request.reason());

        //assign to manager if exists
        if (userData.getManager()!=null)
            submission.setAssignedUser(userData.getManager());

        requestAccessRepository.save(submission);

        //saving detail submission
        List<RequestAccessDetailEntity> listAccess = new ArrayList<>();
        List<SimpleSubmissionDetailDto> catalogReqs = new ArrayList<>();
        for (var item : request.items()){
            if (!item.action().equals(ACTION_ADD) || !item.action().equals(ACTION_REVOKE))
                throw new ProcessException(HttpStatus.BAD_REQUEST,"Action not known");

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
            catalogReqs.add(SimpleSubmissionDetailDto.builder().action(item.action()).catalogName(item.accessCatalogName()).build());

            listAccess.add(accessRequest);
        }
        requestDetailRepository.saveAll(listAccess);

        //saving history
        RequestAccessHistoryEntity history = new RequestAccessHistoryEntity();
        history.setRequestAccess(submission);
        history.setStatus(submission.getStatus());
        history.setUpdatedAt(LocalDateTime.now());
        history.setOldAccessCatalog(buildOldCatalogAccess(userId));
        history.setNewAccessCatalog((new ObjectMapper().writeValueAsString(catalogReqs)));
        history.setUpdatedBy(userId);
        history.setAssignedUser(submission.getAssignedUser());
        history.setAssignedRole(submission.getAssignedRole());
        history.setReasonSubmission(submission.getReasonSubmission());

        requestHistoryRepository.save(history);
    }

    private String buildOldCatalogAccess(Long userId){
        List<UserAccessEntity> userAccess = userAccessRepository.findByUserEntity_Id(userId);
        List<String> resp = userAccess.stream().map(u->u.getCatalogEntity().getAccessName()).toList();
        return (new ObjectMapper()).writeValueAsString(resp);
    }

    private String buildNewCatalogAccess(Long submissionid){
        List<RequestAccessDetailEntity> submissionDetail = requestDetailRepository.findByRequestAccess_Id(submissionid);
        List<SimpleSubmissionDetailDto> convert = submissionDetail.stream()
                .map(s->SimpleSubmissionDetailDto.builder()
                        .action(s.getAction())
                        .catalogName(s.getCatalog().getAccessName())
                    .build())
                .toList();
        return (new ObjectMapper()).writeValueAsString(convert);
    }

    public BasePaginationResponse<?> getPrivateSubmission(Integer size, Integer page){
        Claims claim = jwtUtil.getClaimsFromToken();
        Long userId = ((Integer) claim.get("idUser")).longValue();

        Sort sort = Sort.by("lastUpdated").descending();
        Pageable paging = PageRequest.of(page-1, size,sort);
        List<RequestAccessEntity> requests = requestAccessRepository.findByCreatedBy_Id(userId,paging);
        Long totalData = requestAccessRepository.countByCreatedBy_Id(userId);

        return BasePaginationResponse.builder()
                .code("00")
                .message("Success get pagination")
                .data(
                        requests.stream().map(
                                rc-> {
                                    UserEntity userUpdated = rc.getUpdatedBy()==null ? rc.getCreatedBy() : userRepository.findById(rc.getUpdatedBy()).orElseThrow();
                                    return AccessSubmissionResp.builder()
                                            .submissionId(rc.getId())
                                            .status(rc.getStatus())
                                            .lastUpdatedBy(userUpdated.getUsername())
                                            .lastUpdated(rc.getLastUpdated())
                                            .build();
                                }).toList()
                ).totalPage((long) Math.ceil((double) totalData /size))
                .totalData(totalData)
                .currentPage(page)
                .build();
    }

    public List<AccessSubmissionResp> getListNeedApprove(){
        Claims claim = jwtUtil.getClaimsFromToken();
        Long userId = ((Integer) claim.get("idUser")).longValue();
        List<String> roles = GlobalUtility.getCurrentCredRole();
        List<String> listStatus = new ArrayList<>();
        for (var r : roles){
            switch (r){
                case "ROLE_MANAGER":
                    listStatus.add(P_MANAGER);
                    break;
                case "ROLE_ADMIN":
                    listStatus.add(P_ADMIN);
                    break;
            }
        }
        List<RequestAccessEntity> submission = requestAccessRepository.findByAssignedUser_IdOrStatusInAndCreatedBy_IdNot(userId, listStatus, userId);
        return submission.stream()
                .map(s-> {
                    UserEntity userUpdated = s.getUpdatedBy()==null ? s.getCreatedBy() : userRepository.findById(s.getUpdatedBy()).orElseThrow();
                    return AccessSubmissionResp.builder()
                            .status(s.getStatus())
                            .submissionId(s.getId())
                            .lastUpdated(s.getLastUpdated())
                            .build();
                }).toList();
    }

    @Transactional
    public void updateSubmission(Integer submissionId, ApprovalSubmissionRequest request){
        Claims claim = jwtUtil.getClaimsFromToken();
        Long userId = ((Integer) claim.get("idUser")).longValue();
        List<String> roles = GlobalUtility.getCurrentCredRole();
        RequestAccessEntity submission = requestAccessRepository.findByIdAndStatusNotInAndCreatedBy_IdNot(submissionId.longValue(),List.of(APPROVED,REJECTED),userId).orElseThrow(()->new ProcessException(HttpStatus.NOT_FOUND,"Request not found"));
        String oldCatalog = buildOldCatalogAccess(submission.getCreatedBy().getId());
        String newCatalog = buildNewCatalogAccess(submission.getId());

        if (submission.getAssignedUser()!=null && !Objects.equals(submission.getAssignedUser().getId(), userId))
            throw new ProcessException(HttpStatus.BAD_REQUEST,"You are not allowed to make a change of this request");

        if (roles.stream().noneMatch(r->r.contains(submission.getAssignedRole().getRoleName())))
            throw new ProcessException(HttpStatus.BAD_REQUEST,"You are not allowed to make a change of this request");

        if (request.action().equals(ACTION_APPROVE)){
            if (submission.getStatus().equals(P_MANAGER)){
                submission.setStatus(P_ADMIN);
                submission.setAssignedRole(roleRepository.findByRoleName("ADMIN").orElseThrow());
            } else if (submission.getStatus().equals(P_ADMIN)){
                submission.setStatus(APPROVED);
                applyChangesSubmissionAccess(submission);
            }
        } else if (request.action().equals(ACTION_REJECT)) {
            submission.setStatus(REJECTED);
        } else
            throw new ProcessException(HttpStatus.BAD_REQUEST,"Action not known");

        submission.setNotes(request.notes()!=null ? request.notes() : null);
        submission.setLastUpdated(LocalDateTime.now());
        submission.setUpdatedBy(userId);

        requestAccessRepository.save(submission);

        //save history
        RequestAccessHistoryEntity history = new RequestAccessHistoryEntity();
        history.setRequestAccess(submission);
        history.setUpdatedAt(LocalDateTime.now());
        history.setStatus(submission.getStatus());
        history.setUpdatedBy(userId);
        history.setNotes(submission.getNotes());
        history.setReasonSubmission(submission.getReasonSubmission());
        history.setOldAccessCatalog(oldCatalog);
        history.setNewAccessCatalog(newCatalog);
        history.setAssignedRole(submission.getAssignedRole());

        requestHistoryRepository.save(history);
    }

    private void applyChangesSubmissionAccess(RequestAccessEntity submission){
        List<RequestAccessDetailEntity> details = requestDetailRepository.findByRequestAccess_Id(submission.getId());

        for (var d : details){
            if (d.getAction().equals(ACTION_ADD)){
                UserAccessEntity userAccessNew = new UserAccessEntity();
                userAccessNew.setCreatedAt(LocalDateTime.now());
                userAccessNew.setCatalogEntity(d.getCatalog());
                userAccessNew.setUserEntity(submission.getCreatedBy());

                userAccessRepository.save(userAccessNew);
            } else
                userAccessRepository.deleteByUserEntity_IdAndCatalogEntity_Id(submission.getCreatedBy().getId(), d.getCatalog().getId());
        }
    }
}
