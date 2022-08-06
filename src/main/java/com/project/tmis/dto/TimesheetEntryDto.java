package com.project.tmis.dto;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class TimesheetEntryDto {
    Long timesheetId;
    int empId;
    String startTime;
    String endTime;
    String updatedDate;
    float effectiveWorkingHours;
    String entryStatusValue;
}
