package com.fastcampus.housebatch.job.apt;

import com.fastcampus.housebatch.adapter.ApartmentApiResource;
import com.fastcampus.housebatch.core.dto.AptDealDto;
import com.fastcampus.housebatch.core.repository.LawdRepository;
import com.fastcampus.housebatch.validator.LawdCdParameterValidator;
import com.fastcampus.housebatch.validator.YearMonthParameterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AptDealInsertJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ApartmentApiResource apartmentApiResource;
    private final LawdRepository lawdRepository;
    @Bean
    public Job aptDealInsertJob(
            Step aptDealInsertStep,
            Step guLawdCdStep,
            Step contextPrintStep
    ){
        return jobBuilderFactory.get("aptDealInsertJob")
                .incrementer(new RunIdIncrementer())
                .validator(aptDealJobParameterValidator())
                .start(guLawdCdStep)
                .next(contextPrintStep)
                .next(aptDealInsertStep)
//                .start(aptDealInsertStep)
                .build();
    }
    private JobParametersValidator aptDealJobParameterValidator(){
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
        validator.setValidators(
                Arrays.asList(
                        new YearMonthParameterValidator()
                )
        );
        return validator;
    }
    @Bean
    @JobScope
    public Step guLawdCdStep(Tasklet guLawdCdTasklet){
            return stepBuilderFactory.get("guLawdCdStep")
                    .tasklet(guLawdCdTasklet)
                    .build();
    }

    @Bean
    @StepScope
    public Tasklet guLawdCdTasklet(){
        return (contribution, chunkContext) ->{
            StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
            ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();

            List<String> guLawdCds = lawdRepository.findDistinctGuLawdCd();
            executionContext.putString("guLawdCd", guLawdCds.get(0));

            return RepeatStatus.FINISHED;
        };
    }
    @Bean
    @JobScope
    public Step contextPrintStep(Tasklet contextPrintTasklet){
        return stepBuilderFactory.get("contextPrintStep")
                .tasklet(contextPrintTasklet)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet contextPrintTasklet(
            @Value("#{jobExecutionContext['guLawdCd']}") String guLawdCd
    ){
        return (contribution, chunkContext) -> {
            System.out.println("[contextPrintTasklet] guLawdCd : "+ guLawdCd);
            return RepeatStatus.FINISHED;
        };
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
            @Value("#{jobParameters['filePath']}") String filePath,
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
    public ItemWriter<AptDealDto> aptDealWriter(){
        return items -> {
            items.forEach(System.out::println);
            System.out.println("=========================");
        };
    }
}
