package com.example.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;


import lombok.RequiredArgsConstructor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Bean
    public JdbcCursorItemReader<Person> jdbcCursorItemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Person>() 
                .name("jdbcCursorItemReader")   
                .fetchSize(100) 
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Person.class))  
                .sql("SELECT id, firstname, lastname, birthdate FROM source_person")
                .build();
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }
    

    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("INSERT INTO target_person (firstname, lastname, birthdate " + 
                    ") VALUES (:firstname, :lastname, :birthdate"
                    + ")")
            .dataSource(dataSource)
            .build();
    }
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
