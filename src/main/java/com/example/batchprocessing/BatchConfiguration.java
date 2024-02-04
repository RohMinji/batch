package com.example.batchprocessing;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfiguration {

	// tag::readerwriterprocessor[]
	// @Bean
	// public FlatFileItemReader<SourcePerson> reader() {
	// 	return new FlatFileItemReaderBuilder<SourcePerson>()
	// 		.name("personItemReader")
	// 		.resource(new ClassPathResource("sample-data.csv"))
	// 		.delimited()
	// 		.names(new String[]{"firstName", "lastName"})
	// 		.fieldSetMapper(new BeanWrapperFieldSetMapper<SourcePerson>() {{
	// 			setTargetType(SourcePerson.class);
	// 		}})
	// 		.build();
	// }

	@Bean
    public JdbcCursorItemReader<SourcePerson> jdbcCursorItemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<SourcePerson>()
                .name("jdbcCursorItemReader")   
                .fetchSize(100) 
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(SourcePerson.class))
                .sql("SELECT id, firstname, lastname, birthdate FROM source_person")
                .build();
    }
	
	@Bean
	public PersonItemProcessor processor() {
		return new PersonItemProcessor();
	}

//	@Bean
//	public ItemWriter<TargetPerson> jdbcBatchItemWriter(DataSource dataSource) {
//		JdbcBatchItemWriter<TargetPerson> writer = new JdbcBatchItemWriter<>();
//		writer.setDataSource(dataSource);
//		// 다른 설정들을 여기에 추가하세요
//		return writer;
//	}

	@Bean
	public JdbcBatchItemWriter<TargetPerson> writer(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<TargetPerson>()
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.sql("INSERT INTO target_person (firstname, lastname, birthdate) VALUES (:firstName, :lastName, :birthDate)")
			.dataSource(dataSource)
			.build();
	}
	// end::readerwriterprocessor[]

	// tag::jobstep[]
	@Bean
	public Job importUserJob(JobRepository jobRepository,
			JobCompletionNotificationListener listener, Step step1) {
		return new JobBuilder("importUserJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.listener(listener)
			.flow(step1)
			.end()
			.build();
	}

//	@Bean
//	public Job importUserJob(JobRepository jobRepository,
//							 JobCompletionNotificationListener listener, Step step1) {
//		return new JobBuilder("importUserJob", jobRepository)
//				.incrementer(new RunIdIncrementer())
//				.listener(listener)
//				.flow(step1)
//				.end()
//				.build();
//	}

	@Bean
//	@JobScope
	public Step step1(JobRepository jobRepository,
			PlatformTransactionManager transactionManager, JdbcBatchItemWriter<TargetPerson> writer) {
		return new StepBuilder("step1", jobRepository)
			.<SourcePerson, TargetPerson> chunk(10, transactionManager)
			.reader(jdbcCursorItemReader(null))
			.processor(processor())
			.writer(writer)
			.build();
	}
	// end::jobstep[]
}
