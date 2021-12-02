package com.fastcampus.housebatch.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FakeSendService implements SendService{
    @Override
    public void send(String email, String message) {
        log.info("{} \n {}", email, message);
    }
}
