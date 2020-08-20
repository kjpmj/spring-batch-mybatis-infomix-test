package com.example.demo.job;

import com.example.demo.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MybatisReaderJob {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SqlSessionFactory sqlSessionFactory;

    private static final String JOB_NAME = "mybatisReader2Job";
    private static final String BEAN_PREFIX = JOB_NAME + "_";
    private static final int CHUNK_SIZE = 2;

    @Bean(JOB_NAME)
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step())
                .build();
    }

    @Bean(BEAN_PREFIX + "step")
    public Step step() {
        return stepBuilderFactory.get(BEAN_PREFIX + "step")
                .<Member, Member> chunk(CHUNK_SIZE)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean(BEAN_PREFIX + "reader")
    public MyBatisPagingItemReader<Member> reader() {
        return new MyBatisPagingItemReaderBuilder<Member>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("MemberMapper.getMembers")
                .pageSize(CHUNK_SIZE)
                .build();
    }

    private ItemWriter<Member> writer() {
        return items -> {
            log.info(">>>>> writer() 호출");
            for (Member member: items) {
                log.info(">>>>>> Member={}", member);
            }
        };
    }

}
