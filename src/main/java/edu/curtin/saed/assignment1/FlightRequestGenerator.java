package edu.curtin.saed.assignment1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class FlightRequestGenerator  implements Runnable  {
    private int nAirports;
    private int originAirport;
    private FlightLog log;
    private BufferedReader br;

    public FlightRequestGenerator(int nAirports, int originAirport, FlightLog log){
        this.nAirports = nAirports;
        this.originAirport = originAirport;
        this.log = log;

    }

    @Override
    public void run() {
        try {
            Process proc = Runtime.getRuntime().exec(
                new String[]{"comms/bin/saed_flight_requests",
                             String.valueOf(nAirports),
                             String.valueOf(originAirport)});

            // Create a BufferedReader to read the process output
            br = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
                try {
                    int destinationAirport = Integer.parseInt(line);
                    FlightRequest flightRequest = new FlightRequest(originAirport, destinationAirport);
                    log.addFlight(flightRequest);
                } catch (NumberFormatException e) {
                    // Handle invalid data (e.g., log or ignore)
                    System.err.println("Invalid data received: " + line);
                }
            }
            br.close();
            // Optionally, wait for the process to complete or destroy it later
            proc.waitFor(); // Wait for the process to complete

            // When you need to end the simulation
            proc.destroy();

        } catch (InterruptedException e) {
            System.out.println("Request Generator interrupted");
        }catch(IOException e ){
            System.out.println("IO exception occured with saed_flight_requests");
        }
    }

    public void closeResource(){
        try{
            br.close();
        }catch(IOException e){
            System.out.println("Error on closing request gen reader");
        }
    }
}