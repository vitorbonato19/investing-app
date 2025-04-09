package com.investing.api.repository;

import com.investing.api.entity.Access;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessRepository extends JpaRepository<Access, Long> {

    @Query(nativeQuery = true, value = "select count(*) from access")
    Long countData();

    Access findByName(String name);
}
