package com.quarts.job;

import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2020/9/28 14:28
 */
@Component
public class SendFaxJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        if(null == jobDetail){
            return;
        }
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        if(null == jobDataMap){
            return;
        }
        String faxLogId = jobDataMap.getString("faxLogId");
        if(!StringUtils.isEmpty(faxLogId)){
            return ;
        }

        System.out.println("hello world");
    }
}
