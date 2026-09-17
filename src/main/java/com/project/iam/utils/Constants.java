package com.project.iam.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
        public static String ACTION_APPROVE = "APPROVE";
        public static String ACTION_REJECT = "REJECT";
    }
}
