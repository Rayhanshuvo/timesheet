package com.project.tmis.model;

import com.project.tmis.util.TimesheetEntryStatus;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "timesheet_entry")
@Data
public class TimesheetEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="timesheet_id")
    Long timesheetId;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "empId")
    Employee empId;

    @Column(name="start_time")
    Timestamp startTime;
    @Column(name="end_time")
    Timestamp endTime;

    Date updated;
    @Column(name = "eff_work_hrs")
    float effectiveWorkingHours;

    @Transient
    TimesheetEntryStatus entryStatus;

    @Column(name = "entry_status")
    String entryStatusValue;
}
