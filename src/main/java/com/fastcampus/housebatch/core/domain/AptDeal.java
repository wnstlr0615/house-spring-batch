package com.fastcampus.housebatch.core.domain;

import com.fastcampus.housebatch.core.dto.AptDealDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AptDeal extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aptDealId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apt_id")
    private Apt apt;

    @Column(nullable = false)
    private double exclusiveArea;

    @Column(nullable = false)
    private LocalDate dealDate;

    @Column(nullable = false)
    private Long dealAmount;

    @Column(nullable = false)
    private int floor;

    @Column(nullable = false)
    private boolean dealCanceled=false;

    @Column
    private LocalDate dealCanceledDate;

    public static AptDeal from(AptDealDto dto, Apt apt){
        return AptDeal.builder()
                .apt(apt)
                .exclusiveArea(dto.getExclusiveArea())
                .dealDate(dto.getDealDate())
                .dealAmount(dto.getDealAmount())
                .floor(dto.getFloor())
                .dealCanceled(dto.isDealCanceled())
                .dealCanceledDate(dto.getDealCanceledDate())
                .build();
    }

    public void update(AptDealDto dto){
        this.dealCanceled= dto.isDealCanceled();
        this.dealCanceledDate=dto.getDealCanceledDate();
    }
}
