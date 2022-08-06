package com.project.tmis.repository;

import com.project.tmis.model.Employee;
import com.project.tmis.model.TimesheetEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TimeSheetRepository extends CrudRepository<TimesheetEntry,Long> {

    public List<TimesheetEntry> findByUpdatedBetween(Date startDate, Date endDate);
    public List<TimesheetEntry> findByEmpId(Employee empId);
    public List<TimesheetEntry> findByEmpIdAndUpdatedBetween(Employee empId,Date startDate, Date endDate);
}
