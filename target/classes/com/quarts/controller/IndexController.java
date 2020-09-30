package com.quarts.controller;

import com.quarts.job.SendFaxJob;
import com.quarts.service.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2020/9/28 15:05
 */
@RestController
public class IndexController {

    @Autowired
    private QuartzService quartzService;
    @GetMapping("/index")
    public void index(){

    }

    @PostMapping("/addJob")
    public void addJob() throws  Exception{
        quartzService.addJob(SendFaxJob.class,"job1"+System.currentTimeMillis(),"job-group-1",30,3,null);

    }

    @PostMapping("/addSchduler")
    public void addSchduler(@RequestBody ScheduleRequest request) throws  Exception{

        long current = System.currentTimeMillis()+1000*60;
        Date now = new Date(current);
        quartzService.addJob(SendFaxJob.class,"job1"+System.currentTimeMillis(),"job-group-1",now,null);
    }


}
