package com.fastcampus.housebatch.core.domain;

import com.fastcampus.housebatch.core.dto.AptDealDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Apt extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aptId;

    @Column(nullable = false, length = 40)
    private String aptName;

    @Column(nullable = false, length = 20)
    private String jibun;

    @Column(nullable = false, length = 40)
    private String dong;

    @Column(nullable = false, length = 5)
    private String guLawdCd;

    @Column(nullable = false)
    private int builtYear;

    public static Apt of(AptDealDto dto){
        return Apt.builder()
                .aptName(dto.getAptName().trim())
                .jibun(dto.getJiBun().trim())
                .dong(dto.getDong().trim())
                .guLawdCd(dto.getRegionalCode().trim())
                .builtYear(dto.getBuiltYear())
                .build();
    }
}
