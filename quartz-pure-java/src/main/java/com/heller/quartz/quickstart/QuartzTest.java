package com.heller.quartz.quickstart;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.heller.quartz.quickstart.job.HelloJob;

/**
 * Quartz 是可以用来做定时任务的框架，它的核心是 Scheduler，Job，Trigger。
 * Scheduler 负责调度 Job，Trigger 用来触发 Job 的执行。
 * Job 是一个接口，只有一个方法 execute，用来执行具体的任务。
 *
 * quartz 可以用来做定时任务，也可以用来做分布式任务调度。
 * 它可以不依赖服务器，单独运行，只作为一个 jar 包集成到服务中。
 */
public class QuartzTest {

    public static void main(String[] args) {
        try {
            // Grab the Scheduler instance from the Factory
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // and start it off
            scheduler.start();


            // define the job and tie it to our HelloJob class
            JobDetail job = JobBuilder.newJob(HelloJob.class)
                    .withIdentity("job1", "group1")
                    .build();

            // 简单的定时任务：每 2 秒执行一次任务
            // Trigger the job to run now, and then repeat every 2 seconds
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(2)
                            .repeatForever())
                    .build();

            // Tell quartz to schedule the job using our trigger
            scheduler.scheduleJob(job, trigger);

            // 阻塞下主线程，否则程序会立即结束
            Thread.sleep(10000);

            scheduler.shutdown();
        } catch (SchedulerException se) {
            se.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
