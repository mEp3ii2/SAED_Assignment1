package edu.curtin.saed.assignment1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FlightHandler implements Runnable{
    
    private final ExecutorService executor;
    private FlightLog log;

    public FlightHandler(int threadCount, FlightLog log){
        this.log = log;
        this.executor = Executors.newFixedThreadPool(threadCount);
        

    }

    
    public void shutdown(){
        executor.shutdown();
        try{
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        
        }
    }

    @Override
    public void run() {
        
    }
}
