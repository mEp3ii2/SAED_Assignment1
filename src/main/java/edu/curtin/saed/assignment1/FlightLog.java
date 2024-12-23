package edu.curtin.saed.assignment1;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FlightLog {

    private BlockingQueue<FlightRequest> flights; 
    
    
    public FlightLog() {
        flights = new ArrayBlockingQueue<>(10);
    }

    public void addFlight(FlightRequest flight) throws InterruptedException {
        flights.put(flight);
    }

    
    public FlightRequest getFlight() throws InterruptedException{
        return flights.take();
    }
}