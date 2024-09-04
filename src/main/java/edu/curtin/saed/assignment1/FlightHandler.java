package edu.curtin.saed.assignment1;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class FlightHandler implements Runnable{
    
    private final ExecutorService executor;
    private FlightLog log;
    private List<AirPort> airports;
    private volatile boolean running = true;
    private GridArea grid;
    private JTextArea textArea;
    private JLabel status;
    private int currFlights =0;
    private int completedFlights =0;
    private int currService =0;
    private final Object mutex = new Object();
    private volatile boolean flag = false;
    

    public FlightHandler(FlightLog log, List<AirPort> airports,GridArea grid, JTextArea textArea, JLabel status){
        this.log = log;
        this.executor = Executors.newFixedThreadPool(10);
        this.airports = airports;
        this.grid = grid;
        this.textArea = textArea;
        this.status = status;
    }

    @Override
    public void run() {
        System.out.println("In flightHandler run");
        try{
            while(running){
                FlightRequest request = log.getFlight();//does this not block when nothing inside it?
                if(!executor.isShutdown()){
                    executor.submit(()-> processFlightRequest(request));
                }else{
                    System.out.println("Executor Shutdown, not taking any more requests");
                }
                
            }
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
            System.out.println("FlightHandler interrupted");
        }   
    }

    private void processFlightRequest(FlightRequest request){
        try {
            AirPort origin = getAirPort(request.getOrigin());
            AirPort dest = getAirPort(request.getDest());
            
            if (origin != null && dest != null) {
                Plane plane = origin.getPlane();   
                double rotation = PlaneMovement.calculateRotationAngle(dest.getX(),dest.getY(),plane.getX(),plane.getY());
                plane.getIcon().setRotation(rotation);             

                SwingUtilities.invokeLater(() -> textArea.append("Flight started from " + origin.getId() + " to destination " + dest.getId() + "\n"));
                // Move plane using the SwingWorker
                
                movePlane(dest.getX(), dest.getY(), plane,dest,origin.getId());                          
            }
        } catch (InterruptedException e) {
            System.out.println("processFlightRequest interrupted");
        }
    
    }

    private void movePlane(double destX, double destY, Plane plane, AirPort dest, int origin) {
        try{
            boolean run = true;
            plane.getIcon().setShown(true);
            synchronized(mutex){
                currFlights++;
                updateStatus();
            }
            while (run) {
                //double check
                if(flag){
                    return;
                }
                if (Thread.currentThread().isInterrupted()) {//can never be too safe
                    return; // Exit the method if interrupted
                }
                double[] newCords = PlaneMovement.calcNextPosition(destX, destY, plane);
                plane.setX(newCords[0]);
                plane.setY(newCords[1]);
                plane.getIcon().setPosition(newCords[0], newCords[1]);

                SwingUtilities.invokeLater(()->grid.repaint());

                if (plane.getX() == destX && plane.getY() == destY) {
                    run = false;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Plane movement interrupted");
                }
            }

            plane.getIcon().setShown(false);
            SwingUtilities.invokeLater(()->grid.repaint());
            synchronized(mutex){
                completedFlights++; 
                currService++;
                currFlights--;
                updateStatus();
            }
            SwingUtilities.invokeLater(() -> textArea.append("Flight from " + origin + " to destination " + dest.getId() + " has finished\n"));
            PlaneService planeService = new PlaneService();
            String serviceMsg = planeService.service(plane.getId(), dest.getId());
            if(!serviceMsg.isBlank()){
                SwingUtilities.invokeLater(() -> textArea.append(serviceMsg+"\n"));
            }
            synchronized(mutex){
                currService--;
                updateStatus();
            }
            dest.setPlane(plane); 
        }catch(InterruptedException e){
            System.out.println("Flight Interrupted");
        }        
    }

    private AirPort getAirPort(int id){
        for (AirPort airPort : airports) {
            if (airPort.getId() == id) {
                return airPort;
            }
        }

        return null;
    }

    public void updateStatus(){
        SwingUtilities.invokeLater(() -> {
            // Synchronize only the block that accesses shared variables
            String statusText;
            synchronized (mutex) {
                statusText = "Birds in the air: " + currFlights + 
                             " Planes being serviced: " + currService + 
                             " Total Trips: " + completedFlights;
            }
            status.setText(statusText);
        });
    }

    public void shutdown(){
        flag = true;
        running = false;
        executor.shutdownNow(); 
        try {
        // Wait for existing tasks to finish
        if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
            // If the executor did not terminate within the timeout, force shutdown
            executor.shutdownNow();
            // Wait for tasks to respond to being cancelled
            if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                System.err.println("Executor did not terminate");
            }
        }
    } catch (InterruptedException ex) {
        // (Re-)Cancel if current thread also interrupted
        executor.shutdownNow();
        // Preserve interrupt status
        Thread.currentThread().interrupt();
    }
        
        SwingUtilities.invokeLater(() -> {
            textArea.append("All operations stopped.\n");
            updateStatus(); // Update the status to reflect the current state
        });
        PlaneService.closeResource();
    }
}
