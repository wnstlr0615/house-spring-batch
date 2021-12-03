package com.fastcampus.housebatch.job.notify;

import com.fastcampus.housebatch.BatchTestConfig;
import com.fastcampus.housebatch.adapter.FakeSendService;
import com.fastcampus.housebatch.core.domain.AptNotification;
import com.fastcampus.housebatch.core.domain.Lawd;
import com.fastcampus.housebatch.core.dto.AptDto;
import com.fastcampus.housebatch.core.repository.AptNotificationRepository;
import com.fastcampus.housebatch.core.repository.LawdRepository;
import com.fastcampus.housebatch.core.service.AptDealService;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {AptNotificationJobConfig.class, BatchTestConfig.class})
class AptNotificationJobConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private AptNotificationRepository aptNotificationRepository;

    @MockBean
    private AptDealService aptDealService;

    @MockBean
    private LawdRepository lawdRepository;

    @MockBean
    private FakeSendService fakeSendService;

    @AfterEach
    public void tearDown(){
        aptNotificationRepository.deleteAll();
    }

    @Test
    public void success() throws Exception{
        //given
        LocalDate dealDate=LocalDate.now().minusDays(1);
        String guLawdCd = "11110";
        String email = "jun@naver.com";
        String anotherEmail="kkk@naver.com";
        givenAptNotification(guLawdCd, email, true);
        givenAptNotification(guLawdCd, email, false);
        givenLawdCd(guLawdCd);
        givenAptDeal(guLawdCd, dealDate);

        //when

        JobExecution execution = jobLauncherTestUtils.launchJob(
                new JobParameters(
                        Maps.newHashMap(
                                "dealDate", new JobParameter(dealDate.toString()
                                )
                        )
                )
        );
        //then
        assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        verify(fakeSendService, times(1)).send(eq(email), any());
        verify(fakeSendService, never()).send(eq(anotherEmail), anyString());
    }

    private void givenAptNotification(String guLawdCd, String email, boolean enabled){
        aptNotificationRepository.save(
                createdAptiNotification(guLawdCd, email, enabled)
        );
    }

    private void givenLawdCd(String guLawdCd){
        String lawdCd = guLawdCd + "00000";
        when(lawdRepository.findByLawdCd(lawdCd))
                .thenReturn(
                        Optional.of(
                                Lawd.builder()
                                        .lawdCd(lawdCd)
                                        .lawdDong("경기도 성남시 분당구")
                                        .exist(true)
                                        .build()
                        )
                );
    }

    private void givenAptDeal(String guLawdCd, LocalDate dealDate) {
        when(aptDealService.findByGuLawdCdAndDealDate(guLawdCd, dealDate))
                .thenReturn(
                        Arrays.asList(
                                AptDto.builder()
                                        .name("IT아파트")
                                        .price(20000L)
                                        .build(),
                                AptDto.builder()
                                        .name("탄천아파트")
                                        .price(15000L)
                                        .build()
                        )
                );
    }

    private AptNotification createdAptiNotification(String guLawdCd, String email, boolean enabled) {
        return AptNotification.builder()
                .email(email)
                .guLawdCd(guLawdCd)
                .enabled(enabled)
                .build();
    }
}