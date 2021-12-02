package com.fastcampus.housebatch.core.dto;

import com.fastcampus.housebatch.core.domain.AptDeal;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AptDto {
    private String name;
    private Long price;
    public static AptDto of(AptDeal aptDeal) {
        return AptDto.builder()
                .name(aptDeal.getApt().getAptName())
                .price(aptDeal.getDealAmount())
                .build();
    }
}
