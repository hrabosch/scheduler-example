package org.hrabosch.example.task;

import org.hrabosch.example.TestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class ProcessQueueTask {
    private volatile static BlockingQueue<Runnable> queue = new LinkedBlockingQueue();

    private static Logger logger = LoggerFactory.getLogger(ProcessQueueTask.class);


    // When all tasks are passed to executor, wait this time to start it again. Passed task to executor != finished task!!!
    @Scheduled(fixedDelayString = "${requestprocessor.scheduler.fixedDelay:2000}")
    @Async
    public void processQueue() throws InterruptedException {
        logger.debug("Start scheduled request processing.");

        //Simulate DB Call
        logger.info("Getting requests from DB");
        for (int i = 0; i < 200; i++) {
            queue.put(TestUtil.generateRequestTask());
        }

        logger.debug("Starting processing queue in thread " + Thread.currentThread().getName());

        // Prepare executor. It is starting with 5 threads (workers) and can be up to 10.
        // Also it can holds 100 tasks in queue to be processed. Everything else is gonna be rejected.
        ThreadPoolExecutor e = new ThreadPoolExecutor(5, 10, Long.MAX_VALUE, TimeUnit.NANOSECONDS,
                new ArrayBlockingQueue<Runnable>(100));

        try {
            // Add new tasks for executor over and over till there are no lefts in queue.
            // If this queue will be bigger than executor queue and we will try to add next task, Rejected exception will be returned.
            while (!queue.isEmpty())
                e.execute(queue.poll());
        } catch (RejectedExecutionException ex) {
            logger.error("Rejected task exection.");
        } finally {
            logger.info("Unprocessed request which lefts in queue: {}", queue.size() );
        }
    }

}
