package com.fastcampus.housebatch.core.repository;

import com.fastcampus.housebatch.core.domain.Lawd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LawdRepository extends JpaRepository<Lawd, Long> {
    Optional<Lawd> findByLawdCd(String lawdCd);
    @Query(value = "select distinct substring(l.lawdCd, 1, 5)  from Lawd as l  where l.exist=true  and l.lawdCd  not like '%00000000'")
    List<String> findDistinctGuLawdCd();
}
