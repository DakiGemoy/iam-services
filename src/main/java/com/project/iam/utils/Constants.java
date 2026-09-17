package com.project.iam.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.project.iam.utils.Constants.MasterStatus.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MasterStatus{
        public static String P_MANAGER = "PENDING_MANAGER";
        public static String P_ADMIN = "PENDING_ADMIN";
        public static String APPROVED = "APPROVED";
        public static String REJECTED = "REJECTED";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MasterAction{
        public static String ACTION_ADD = "ADD";
        public static String ACTION_REVOKE = "REVOKE";
    }

    @Getter
    @AllArgsConstructor
    public enum StatusFlow{
        PEND_MANAGER(P_MANAGER, P_ADMIN,"Manager"),
        PEND_ADMIN(P_ADMIN, APPROVED,"Admin"),
        ;

        private final String statusName;
        private final String statusIfApproved;
        private final String roleCanAccess;
    }
}
