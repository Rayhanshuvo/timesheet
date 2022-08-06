package com.project.tmis.controller;

import com.project.tmis.dto.EmployeeMonthlySalaryDto;
import com.project.tmis.dto.TimesheetEntryDto;
import com.project.tmis.model.Employee;
import com.project.tmis.model.PartTimeEmployee;
import com.project.tmis.model.TimesheetEntry;
import com.project.tmis.repository.PartTimeEmployeeRepository;
import com.project.tmis.repository.TimeSheetRepository;
import com.project.tmis.util.TimesheetEntryStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class TimesheetController {

    @Autowired
    TimeSheetRepository timeSheetRepository;

    @Autowired
    PartTimeEmployeeRepository partTimeEmployeeRepository;

    private void saveATimesheet(TimesheetEntryDto timesheetEntryDto, TimesheetEntryStatus entryStatus) throws Exception{
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        TimesheetEntry timesheetEntry = new TimesheetEntry();
        BeanUtils.copyProperties(timesheetEntryDto, timesheetEntry);
        Employee emp = new Employee();
        emp.setEmpId(timesheetEntryDto.getEmpId());
        timesheetEntry.setEmpId(emp);
        timesheetEntry.setEntryStatusValue(entryStatus.getStatus());
        timesheetEntry.setUpdated(dateFormat.parse(timesheetEntryDto.getUpdatedDate()));
        timesheetEntry.setStartTime(new Timestamp(dateFormat.parse(timesheetEntryDto.getStartTime()).getTime()));
        timesheetEntry.setEndTime(new Timestamp(dateFormat.parse(timesheetEntryDto.getEndTime()).getTime()));
        timeSheetRepository.save(timesheetEntry);
    }
// save time sheet by employee-this is for single entry- emloyee user
    @PostMapping("/timesheet")
    public void saveTimesheet(@RequestBody TimesheetEntryDto timesheetEntryDto) throws Exception {
        saveATimesheet(timesheetEntryDto, TimesheetEntryStatus.PENDING_APPROVAL);
    }
    // save time sheet by employee-this is for multiple entry- emloyee user
    @PostMapping("/timesheets")
    public void saveTimesheets(@RequestBody List<TimesheetEntryDto> timesheetEntryDtos) throws Exception {
        timesheetEntryDtos.forEach(x -> {
            try {
                saveATimesheet(x, TimesheetEntryStatus.PENDING_APPROVAL);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
// Approve the sheet-single entry- accessed by admin user only
    @PostMapping("/approve-timesheet")
    public void approveTimesheets(@RequestBody TimesheetEntryDto timesheetEntryDto) throws Exception {
        saveATimesheet(timesheetEntryDto, TimesheetEntryStatus.APPROVED);
    }
    // Approve the sheet-multiple entry- accessed by admin user only
    @PostMapping("/approve-timesheets")
    public void approveTimesheets(@RequestBody List<TimesheetEntryDto> timesheetEntryDtos) throws Exception {
        timesheetEntryDtos.forEach(x -> {
            try {
                saveATimesheet(x, TimesheetEntryStatus.APPROVED);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    // for admin user
    @GetMapping("/all-timesheets")
    public List<TimesheetEntryDto> getTimesheets() throws Exception {
        Iterable<TimesheetEntry>  timesheetEntries= timeSheetRepository.findAll();
        return getTimesheetEntryDtos(timesheetEntries);
    }

    // for admin user
    @GetMapping("/timesheets-by-date")
    public List<TimesheetEntryDto> getTimesheets(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Iterable<TimesheetEntry>  timesheetEntries= timeSheetRepository.findByUpdatedBetween(dateFormat.parse(startDate), dateFormat.parse(endDate));
        return getTimesheetEntryDtos(timesheetEntries);
    }

    // for  user && ADMIN
    @GetMapping("/timesheets-by-emp")
    public List<TimesheetEntryDto> getTimesheetsByEmployee(@RequestParam("empId") int empId) throws Exception {
        Employee emp = new Employee();
        emp.setEmpId(empId);
        Iterable<TimesheetEntry>  timesheetEntries= timeSheetRepository.findByEmpId(emp);
        return getTimesheetEntryDtos(timesheetEntries);
    }

    // for  user && ADMIN
    @GetMapping("/timesheets-emp-date")
    public List<TimesheetEntryDto> getTimesheetsByEmployeeBetweenDate(@RequestParam("empId") int empId, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Employee emp = new Employee();
        emp.setEmpId(empId);
        Iterable<TimesheetEntry>  timesheetEntries= timeSheetRepository.findByEmpIdAndUpdatedBetween(emp, dateFormat.parse(startDate), dateFormat.parse(endDate));
        return getTimesheetEntryDtos(timesheetEntries);
    }

    // for admin user
    @GetMapping("/emp-monthly-salary")
    public List<EmployeeMonthlySalaryDto> getEmployeeMonthlySalary(@RequestParam("month") int month, @RequestParam("year") int year) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        Date firstDate = dateFormat.parse("01-"+(month<10?"0"+month:month)+"-"+year+ " 00:00:00");
        Date lastDate = null;

            if(month == 12){
                lastDate = dateFormat.parse("01-01-"+(year+1)+ " 00:00:00");
            }
            else{
                lastDate = dateFormat.parse("01-"+ (month<9?"0"+(month+1):month+1)+"-"+year+ " 00:00:00");
            }

        final Date lastDate1 = lastDate;
        List<EmployeeMonthlySalaryDto> employeeMonthlySalaryDtos = new ArrayList<>();
        Iterable<PartTimeEmployee> partTimeEmployees = partTimeEmployeeRepository.findAll();
        partTimeEmployees.forEach(pEmployee -> {
            long salary = 0;
            EmployeeMonthlySalaryDto employeeMonthlySalaryDto = new EmployeeMonthlySalaryDto();
            Iterable<TimesheetEntry>  timesheetEntries= timeSheetRepository.findByEmpIdAndUpdatedBetween(pEmployee,firstDate, lastDate1);
            employeeMonthlySalaryDto.setEmpId(pEmployee.getEmpId());
            employeeMonthlySalaryDto.setName(pEmployee.getName());
            employeeMonthlySalaryDto.setYear(year);
            employeeMonthlySalaryDto.setMonth(month);
            for(TimesheetEntry t : timesheetEntries){
                salary = ((Double)(pEmployee.getPayRate() * t.getEffectiveWorkingHours())).longValue() + salary;
                employeeMonthlySalaryDto.setMonthSalary(salary);
            }
            employeeMonthlySalaryDtos.add(employeeMonthlySalaryDto);
        });
        return employeeMonthlySalaryDtos;
    }

    private List<TimesheetEntryDto> getTimesheetEntryDtos(Iterable<TimesheetEntry>  timesheetEntries) throws Exception{
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        List<TimesheetEntryDto> timesheetEntryDtos = new ArrayList<>();
        timesheetEntries.forEach(x -> {
            TimesheetEntryDto timesheetEntryDto = new TimesheetEntryDto();
            BeanUtils.copyProperties(x, timesheetEntryDto);
            timesheetEntryDto.setUpdatedDate(dateFormat.format(x.getUpdated()));
            timesheetEntryDto.setStartTime(dateFormat.format(x.getStartTime()));
            timesheetEntryDto.setEndTime(dateFormat.format(x.getEndTime()));
            timesheetEntryDto.setEmpId(x.getEmpId().getEmpId());
            timesheetEntryDtos.add(timesheetEntryDto);

        });
        return timesheetEntryDtos;
    }
}
