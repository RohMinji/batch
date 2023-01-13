package com.example.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	// tag::jobstep[]
	@Bean
	public Job sampleDb2DbChunkJob(JobRepository jobRepository, Step sampleDb2DbChunkStep) {
		return new JobBuilder("sampleDb2DbChunkJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.flow(sampleDb2DbChunkStep)
			.end()
			.build();
	}


    @Bean
    public Step sampleDb2DbChunkStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
	JdbcCursorItemReader<Person> reader,JdbcBatchItemWriter<Person> writer) 
	{
        return new StepBuilder("sampleDb2DbChunkStep", jobRepository)
            .<Person, Person> chunk(10, transactionManager)
            .reader(reader)
            .processor(null)
            .writer(writer)
            .build();
    }

    
}
