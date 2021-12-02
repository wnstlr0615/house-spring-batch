package com.fastcampus.housebatch.core.repository;

import com.fastcampus.housebatch.core.domain.Apt;
import com.fastcampus.housebatch.core.domain.AptDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AptDealRepository extends JpaRepository<AptDeal,Long> {
    Optional<AptDeal> findAptDealByAptAndExclusiveAreaAndDealDateAndDealAmountAndFloor(
            Apt apt, Double exclusiveArea, LocalDate dealDate, Long dealAmount,  Integer floor
    );

    @Query( "select ad from AptDeal as ad join fetch ad.apt where ad.dealCanceled = false and ad.dealDate=:dealDate")
    List<AptDeal> findByDealCanceledIsFalseAndDealDateEquals(@Param("dealDate") LocalDate dealDate);
}
