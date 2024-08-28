package edu.curtin.saed.assignment1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

import javax.swing.SwingUtilities;

public class FlightRequestGenerator  implements Runnable  {
    private int nAirports;
    private int originAirport;
    private BlockingQueue<FlightRequest> flightQueue;

    public FlightRequestGenerator(int nAirports, int originAirport, BlockingQueue<FlightRequest> flightQueue){
        this.nAirports = nAirports;
        this.originAirport = originAirport;
        this.flightQueue = flightQueue;

    }

    @Override
    public void run() {
        try {
            Process proc = Runtime.getRuntime().exec(
                new String[]{"comms/bin/saed_flight_requests",
                             String.valueOf(nAirports),
                             String.valueOf(originAirport)});

            // Create a BufferedReader to read the process output
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
                try {
                    int destinationAirport = Integer.parseInt(line);
                    FlightRequest flightRequest = new FlightRequest(originAirport, destinationAirport);
                    flightQueue.put(flightRequest);
                    SwingUtilities.invokeLater(()->{
                        // Handle the destination airport (e.g., add to a list or process it)
                        System.out.println("Origin Airport: "+originAirport+" Destination Airport: " + destinationAirport);    
                    });
                } catch (NumberFormatException e) {
                    // Handle invalid data (e.g., log or ignore)
                    System.err.println("Invalid data received: " + line);
                }
            }

            // Optionally, wait for the process to complete or destroy it later
            proc.waitFor(); // Wait for the process to complete

            // When you need to end the simulation
            proc.destroy();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
