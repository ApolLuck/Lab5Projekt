package com.example.lab2projekt.domain.repositories;

import com.example.lab2projekt.domain.Objects.Entities.CoverType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoverTypeRepository extends JpaRepository<CoverType, Integer> {
    @Modifying
    @Transactional
    @Query("update CoverType c set c.nazwa = :nowaNazwa where upper(c.nazwa) = upper(:staraNazwa)")
    void updateCoverTypeName(@Param("nowaNazwa") String nowaNazwa, @Param("staraNazwa") String staraNazwa);

}
