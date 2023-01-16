package com.example.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SampleJobListener extends JobExecutionListenerSupport{
    private static final Logger log = LoggerFactory.getLogger(SampleJobListener.class);
 
    private final JdbcTemplate jdbcTemplate;
 
    @Autowired
    public SampleJobListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void beforeJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.STARTED) {
            log.info("chunkJob start! ");
        }
    }
    
    public void afterJob(JobExecution jobExecution) {
        
        
        jdbcTemplate.query("SELECT * FROM person",
                (rs, row) -> new Person(
                    rs.getString(1), rs.getString(2), rs.getString(3)
                    )
            ).forEach(person -> log.info("###" + person.getFirstName() + " " + person.getLastName() + " in postgresql database."));
        
        
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("chunkJob successed! ");
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.info("chunkJob failed! ");
        }
    }
}
