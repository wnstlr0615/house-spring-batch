package com.fastcampus.housebatch.core.service;

import com.fastcampus.housebatch.core.domain.Apt;
import com.fastcampus.housebatch.core.domain.AptDeal;
import com.fastcampus.housebatch.core.dto.AptDealDto;
import com.fastcampus.housebatch.core.repository.AptDealRepository;
import com.fastcampus.housebatch.core.repository.AptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AptDealService {
    private final AptDealRepository aptDealRepository;
    private final AptRepository aptRepository;

    @Transactional
    public void upsert(AptDealDto dto){
        Apt apt = getAptorNew(dto);
        saveAptDeal(dto, apt);
    }

    private void saveAptDeal(AptDealDto dto, Apt apt) {
        AptDeal aptDeal = aptDealRepository.findAptDealByAptAndExclusiveAreaAndDealDateAndDealAmountAndFloor(
                apt, dto.getExclusiveArea(), dto.getDealDate(), dto.getDealAmount(), dto.getFloor()
        ).orElse(AptDeal.from(dto, apt));
        aptDeal.update(dto);
        aptDealRepository.save(aptDeal);
    }

    private Apt getAptorNew(AptDealDto dto) {
        Apt apt = aptRepository.findAptByAptNameAndJibun(
                dto.getAptName(),
                dto.getJibun()
        ).orElse(Apt.of(dto));
        aptRepository.save(apt);
        return apt;
    }
}
