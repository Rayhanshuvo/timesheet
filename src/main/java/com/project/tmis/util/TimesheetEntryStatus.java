package com.project.tmis.util;

public enum TimesheetEntryStatus {
    PENDING_APPROVAL("Pending Approval"),
    APPROVED("Approved"),
    DECLINED("Declined");

    private String status;
    TimesheetEntryStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return this.status;
    }
}
