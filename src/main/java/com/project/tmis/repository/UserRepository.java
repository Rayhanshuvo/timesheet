package com.project.tmis.repository;

import com.project.tmis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("from User where activeStatus =(:activeStatus)")
    List<User> list(@Param("activeStatus") Integer activeStatus);

    @Query("from User where activeStatus = (:activeStatus) and id =(:id)")
    User getByIdAndActiveStatusTrue(@Param("activeStatus") Integer activeStatus, @Param("id") Long id);

    @Query("from User where activeStatus = (:activeStatus) and userName =(:userName)")
    User getByUserNameAndActiveStatusTrue(@Param("activeStatus") Integer activeStatus, @Param("userName") String userName);


}
