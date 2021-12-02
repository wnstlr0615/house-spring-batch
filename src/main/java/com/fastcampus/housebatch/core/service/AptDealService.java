package com.fastcampus.housebatch.core.service;

import com.fastcampus.housebatch.core.domain.Apt;
import com.fastcampus.housebatch.core.domain.AptDeal;
import com.fastcampus.housebatch.core.dto.AptDealDto;
import com.fastcampus.housebatch.core.dto.AptDto;
import com.fastcampus.housebatch.core.repository.AptDealRepository;
import com.fastcampus.housebatch.core.repository.AptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
                dto.getJiBun()
        ).orElse(Apt.of(dto));
        aptRepository.save(apt);
        return apt;
    }
    public List<AptDto> findByGuLawdCdAndDealDate(String guLawdCd, LocalDate dealDate){
        return getAptDtos(guLawdCd, dealDate);
    }

    private List<AptDto> getAptDtos(String guLawdCd, LocalDate dealDate) {
        return aptDealRepository.findByDealCanceledIsFalseAndDealDateEquals(dealDate)
                .stream()
                .filter(aptDeal -> aptDeal.getApt().getGuLawdCd().equals(guLawdCd))
                .map(AptDto::of)
                .collect(Collectors.toList());
    }
}
