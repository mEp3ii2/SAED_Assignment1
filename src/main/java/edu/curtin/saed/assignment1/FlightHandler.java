package edu.curtin.saed.assignment1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FlightHandler {
    private final BlockingQueue<Runnable> flightQueue;
    private final ThreadPoolExecutor executor;

    public FlightHandler(int threadCount){
        flightQueue = new LinkedBlockingQueue<>();
        executor = new ThreadPoolExecutor(threadCount, threadCount, 0L,TimeUnit.MILLISECONDS, flightQueue);
    }

    public void addFlightRequest(FlightRequest flightRequest){
        executor.execute(() -> processFlight(flightRequest));
    }

    private void processFlight(FlightRequest flightRequest){
        System.out.println("Processing flight from Airport " + flightRequest.getOrigin()
                + " to Airport " + flightRequest.getDest());
        try {
            Thread.sleep(3000); // Simulate time to handle the flight
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
}
