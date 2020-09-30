package com.quarts.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2020/9/28 14:28
 */
@Component
public class MyJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //jobExecutionContext.getJobDetail().getJobDataMap().get
        System.out.println("hello world");
    }
}
