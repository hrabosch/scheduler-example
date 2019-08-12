package org.hrabosch.example.task;

import org.hrabosch.example.model.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;


public class RequestTask implements Runnable {

    private Logger logger = LoggerFactory.getLogger(RequestTask.class);

    private Request request;

    public RequestTask(Request request) {
        this.request = request;
    }

    @Override
    public void run() {
        logger.debug("Starting processing request task for request with id {}.", request.getUid());
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(500, 2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Finished processing request with id {}.", request.getUid());
    }
}
