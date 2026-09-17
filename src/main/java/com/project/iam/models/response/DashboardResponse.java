package com.project.iam.models.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DashboardResponse{
    private Long totalRequest;
    private Long inProgress;
    private Long waitingManager;
    private Long waitingAdmin;
    private Long approved;
    private Long rejected;

    public void recalculate(){
        this.inProgress = this.waitingAdmin + this.waitingManager;
        this.totalRequest = this.inProgress + this.approved + this.rejected;
    }
}
