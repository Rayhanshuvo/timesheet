package com.project.tmis.entity;

import com.project.tmis.enums.ActiveStatus;
import com.project.tmis.util.SecurityUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(updatable = false)
    private Long createdBy;
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    private Long updatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;
    private Integer activeStatus;

    @PrePersist
    public void setPreInsertData() {
        this.createdBy = SecurityUtils.getCurrentAuthenticatedUserId();
        this.createdAt = new Date();
        this.activeStatus = ActiveStatus.ACTIVE.getValue();
    }

    @PreUpdate
    public void setPreUpdateData() {
        this.updatedBy = SecurityUtils.getCurrentAuthenticatedUserId();
        this.updateAt = new Date();
        if (this.activeStatus == null) {
            this.activeStatus = ActiveStatus.ACTIVE.getValue();
        }
        if (this.activeStatus != ActiveStatus.DELETE.getValue()) {
            this.activeStatus = ActiveStatus.ACTIVE.getValue();
        }

    }
}
