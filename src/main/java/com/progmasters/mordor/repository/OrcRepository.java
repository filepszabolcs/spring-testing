package com.progmasters.mordor.repository;

import com.progmasters.mordor.domain.Orc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrcRepository extends JpaRepository<Orc, Long> {
    List<Orc> findByOrderByKillCountDesc();
}

