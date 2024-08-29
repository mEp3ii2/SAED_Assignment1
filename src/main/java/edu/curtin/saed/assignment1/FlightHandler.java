package edu.curtin.saed.assignment1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FlightHandler implements Runnable{
    private final BlockingQueue<Runnable> flightQueue;
    private final ThreadPoolExecutor executor;
    private A a;

    public FlightHandler(int threadCount, A a){
        flightQueue = new LinkedBlockingQueue<>();
        executor = new ThreadPoolExecutor(threadCount, threadCount, 0L,TimeUnit.MILLISECONDS, flightQueue);
        this.a = a;
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

    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }
}
