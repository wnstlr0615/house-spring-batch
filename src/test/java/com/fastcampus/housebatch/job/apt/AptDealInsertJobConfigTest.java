package com.fastcampus.housebatch.job.apt;

import com.fastcampus.housebatch.BatchTestConfig;
import com.fastcampus.housebatch.adapter.ApartmentApiResource;
import com.fastcampus.housebatch.core.repository.LawdRepository;
import com.fastcampus.housebatch.core.service.AptDealService;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@SpringBatchTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {AptDealInsertJobConfig.class, BatchTestConfig.class})
class AptDealInsertJobConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    private AptDealService aptDealService;

    @MockBean
    private LawdRepository lawdRepository;

    @MockBean
    private ApartmentApiResource apartmentApiResource;



    @Test
    public void success() throws Exception{
        //given
        when(lawdRepository.findDistinctGuLawdCd())
                .thenReturn(Collections.singletonList("41135"));
        when(apartmentApiResource.getResource(anyString(), any()))
                .thenReturn(new ClassPathResource("test-api-response.xml"));
        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters(Maps.newHashMap("yearMonth", new JobParameter("2021-07"))));

        //then
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        verify(aptDealService, times(2)).upsert(any());
    }

    @Test
    public void fail_whenYearMonthNotExist() throws Exception{
        //given
        when(lawdRepository.findDistinctGuLawdCd())
                .thenReturn(Collections.singletonList("41135"));
        when(apartmentApiResource.getResource(anyString(), any()))
                .thenReturn(new ClassPathResource("test-api-response.xml"));
        //when
        assertThrows(JobParametersInvalidException.class, () -> jobLauncherTestUtils.launchJob());

        //then
        verify(aptDealService, never()).upsert(any());
    }
}