package com.footballbe.repository;


import com.footballbe.entity.X;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface XRepository extends JpaRepository<X, Long> {

    @Query("SELECT x FROM X x ORDER BY x.value")
    X findX();
}
