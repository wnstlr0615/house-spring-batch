package com.fastcampus.housebatch.job.apt;

import com.fastcampus.housebatch.adapter.ApartmentApiResource;
import com.fastcampus.housebatch.core.dto.AptDealDto;
import com.fastcampus.housebatch.core.repository.LawdRepository;
import com.fastcampus.housebatch.core.service.AptDealService;
import com.fastcampus.housebatch.validator.YearMonthParameterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.time.YearMonth;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AptDealInsertJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ApartmentApiResource apartmentApiResource;
    @Bean
    public Job aptDealInsertJob(
            Step guLawdCdStep,
            Step aptDealInsertStep
            ){
        return jobBuilderFactory.get("aptDealInsertJob")
                .incrementer(new RunIdIncrementer())
                .validator(new YearMonthParameterValidator())
                .start(guLawdCdStep)
                .on("CONTINUABLE").to(aptDealInsertStep).next(guLawdCdStep)
                .from(guLawdCdStep)
                .on("*").end()
                .end().build();
    }

    @Bean
    @JobScope
    public Step guLawdCdStep(Tasklet guLawdCdTasklet){
            return stepBuilderFactory.get("guLawdCdStep")
                    .tasklet(guLawdCdTasklet)
                    .build();
    }
/**
 * ExecutionContext에 저장할 데이터
 * 1. guLawdCdList - 구 코드 리스트
 * 2. guLawdCd - 구 코드 -> 다음 스텝에서 활용할 값
 * 3. itemCount - 남아있는 구 코드의 갯수
 * */
    @Bean
    @StepScope
    public Tasklet guLawdCdTasklet(LawdRepository lawdRepository){
        return new GuLawdTasklet(lawdRepository);
    }

    @Bean
    @JobScope
    public Step aptDealInsertStep(
            StaxEventItemReader<AptDealDto> aptDealResourceReader,
            ItemWriter<AptDealDto> aptDealWriter){
        return stepBuilderFactory.get("aptDealInsertStep")
                .<AptDealDto, AptDealDto>chunk(10)
                .reader(aptDealResourceReader)
                .writer(aptDealWriter)
                .build();
    }

    @Bean
    @StepScope
    public StaxEventItemReader<AptDealDto> aptDealResourceReader(
            Jaxb2Marshaller aptDealDtoMarshaller,
            @Value("#{jobExecutionContext['guLawdCd']}") String guLawdCd,
            @Value("#{jobParameters['yearMonth']}") String yearMonth
    ){
        return new StaxEventItemReaderBuilder<AptDealDto>()
                .name("aptDealResourceReader")
                .resource(apartmentApiResource.getResource(guLawdCd, YearMonth.parse(yearMonth)))
                .addFragmentRootElements("item")
                .unmarshaller(aptDealDtoMarshaller)
                .build();
    }

    @Bean
    @StepScope
    public Jaxb2Marshaller aptDealDtoMarshaller(){
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setClassesToBeBound(AptDealDto.class);
        return jaxb2Marshaller;
    }

    @Bean
    @StepScope
    public ItemWriter<AptDealDto> aptDealWriter(AptDealService aptDealService){
        return items -> items.forEach(aptDealService::upsert);
    }
}
