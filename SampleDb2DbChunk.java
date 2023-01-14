package com.example.batch;
import javax.sql.DataSource;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Configuration  
public class SampleDb2DbChunk {
    //@Autowired
    //private DataSource dataSource;
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
}
