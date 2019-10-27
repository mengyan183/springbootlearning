package com.xuecheng.order.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 串行任务测试类
 */
//@Component
@Slf4j
public class TaskSeriallyTest {

//    @Scheduled(fixedRate = 5000L) // 上次任务执行后 5s 开始执行下一次任务
    @Scheduled(fixedDelay = 5000L) // 上次任务执行结束后 5s 开始执行下一次任务
//    @Scheduled(cron = "0 0 * * * *") // 使用cron表达式设置定时
    public void seriallyTask1(){
        log.info("task1 start ");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("task1 end  ");
    }

    //    @Scheduled(fixedRate = 5000L) // 上次任务执行后 5s 开始执行下一次任务
    @Scheduled(fixedDelay = 5000L) // 上次任务执行结束后 5s 开始执行下一次任务
//    @Scheduled(cron = "0 0 * * * *") // 使用cron表达式设置定时
    public void seriallyTask2(){
        log.info("task2 start ");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("task2 end  ");
    }

    /**
     * 2019-10-27 12:02:00.316 [pool-10-thread-1] INFO  c.x.order.task.TaskSeriallyTest - task1 start
     * 2019-10-27 12:02:01.318 [pool-10-thread-1] INFO  c.x.order.task.TaskSeriallyTest - task1 end
     * 2019-10-27 12:02:01.319 [pool-10-thread-1] INFO  c.x.order.task.TaskSeriallyTest - task2 start
     * 2019-10-27 12:02:02.321 [pool-10-thread-1] INFO  c.x.order.task.TaskSeriallyTest - task2 end
     * 2019-10-27 12:02:06.320 [pool-10-thread-1] INFO  c.x.order.task.TaskSeriallyTest - task1 start
     * 2019-10-27 12:02:07.321 [pool-10-thread-1] INFO  c.x.order.task.TaskSeriallyTest - task1 end
     * 2019-10-27 12:02:07.322 [pool-10-thread-1] INFO  c.x.order.task.TaskSeriallyTest - task2 start
     * 2019-10-27 12:02:08.323 [pool-10-thread-1] INFO  c.x.order.task.TaskSeriallyTest - task2 end
     *
     *  根据 任务打印日志中的 "pool-10-thread-1"和 task1 end task2 start  可以证明当前task1和task2是同一个线程串行执行,表示需要等待上一个任务结束后才会执行下一个任务 ;
     *  从两个task1 日志 时间间隔 可以看到 从第一个task1 end 到 第二个 task1 start 间隔时间为5秒,表示fixedDelay代表的为上一个任务结束后5秒才再次执行task1
     *
     */

    /**
     * 2019-10-27 12:44:49.314 [threadPoolTaskScheduler-1] INFO  c.x.order.task.TaskSeriallyTest - task2 start
     * 2019-10-27 12:44:49.314 [threadPoolTaskScheduler-2] INFO  c.x.order.task.TaskSeriallyTest - task1 start
     * 2019-10-27 12:44:50.318 [threadPoolTaskScheduler-1] INFO  c.x.order.task.TaskSeriallyTest - task2 end
     * 2019-10-27 12:44:50.318 [threadPoolTaskScheduler-2] INFO  c.x.order.task.TaskSeriallyTest - task1 end
     * 2019-10-27 12:44:55.321 [threadPoolTaskScheduler-2] INFO  c.x.order.task.TaskSeriallyTest - task2 start
     * 2019-10-27 12:44:55.321 [threadPoolTaskScheduler-4] INFO  c.x.order.task.TaskSeriallyTest - task1 start
     * 2019-10-27 12:44:56.324 [threadPoolTaskScheduler-2] INFO  c.x.order.task.TaskSeriallyTest - task2 end
     * 2019-10-27 12:44:56.324 [threadPoolTaskScheduler-4] INFO  c.x.order.task.TaskSeriallyTest - task1 end
     * 2019-10-27 12:45:01.329 [threadPoolTaskScheduler-2] INFO  c.x.order.task.TaskSeriallyTest - task2 start
     * 2019-10-27 12:45:01.329 [threadPoolTaskScheduler-1] INFO  c.x.order.task.TaskSeriallyTest - task1 start
     * 2019-10-27 12:45:02.333 [threadPoolTaskScheduler-2] INFO  c.x.order.task.TaskSeriallyTest - task2 end
     * 2019-10-27 12:45:02.333 [threadPoolTaskScheduler-1] INFO  c.x.order.task.TaskSeriallyTest - task1 end
     *
     * 和上面日志对比,可以很清楚的看到 "threadPoolTaskScheduler-1","threadPoolTaskScheduler-2"等 自定义线程池 @see org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler 中的线程
     * 说明如果我们配置的定时任务的自定义线程池,当前所有的定时任务都会冲线程池中获取线程 实现并行任务
     */
}
