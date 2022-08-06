package com.project.tmis.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "employee_payment_history")
@Data
public class EmployeePaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int payId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "empId")
    Employee emp;

    @Column(name = "payment_date")
    private Date paymentDate;

    @Column(name = "payment_amount")
    private double paymentAmount;

}
