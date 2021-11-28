package com.fastcampus.housebatch.core.service;

import com.fastcampus.housebatch.core.domain.Lawd;
import com.fastcampus.housebatch.core.repository.LawdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LawdService {
    private final LawdRepository lawdRepository;

    @Transactional
    public void upsert(Lawd lawd){
        Optional<Lawd> optionalLawd = lawdRepository.findByLawdCd(lawd.getLawdCd());
        if(optionalLawd.isPresent()){
            Lawd lawdEntity = optionalLawd.get();
            lawdEntity.update(lawd);
        }else{
            lawdRepository.save(lawd);
        }
    }

}
