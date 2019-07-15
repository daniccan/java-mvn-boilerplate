package com.daniccan.javamvnboilerplate.threads;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.log4j.Logger;

/**
 * @author daniccan
 */
public class Thread2 implements Callable {
    
    private static final Logger LOGGER = Logger.getLogger(Thread2.class);
    
    private static final String THREAD_NAME = "THREAD 2::";

    private final AtomicBoolean closed = new AtomicBoolean(false);
    
    public Thread2() {
        LOGGER.info(THREAD_NAME + "Constructor Called!!!");
    }
    
    @Override
    public Object call() throws Exception {
        while (!closed.get()) {
            
            LOGGER.info(THREAD_NAME + "TODO!!!");
            
            LOGGER.info(THREAD_NAME + "Sleeping for 2 mins!!!");
            
            Thread.sleep(2 * 60 * 1000);
        }
        
        return null;
    }
    
    public void shutdown() {
        LOGGER.info(THREAD_NAME + "Shutting Down!!!");
        closed.set(true);
    }
}
