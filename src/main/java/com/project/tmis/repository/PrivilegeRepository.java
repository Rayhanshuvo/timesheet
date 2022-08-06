package com.project.tmis.repository;

import com.project.tmis.entity.Privileges;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privileges, Long> {
}
