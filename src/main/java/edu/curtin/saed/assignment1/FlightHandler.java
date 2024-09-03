package edu.curtin.saed.assignment1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class FlightHandler implements Runnable{
    
    private final ExecutorService executor;
    private FlightLog log;
    private List<AirPort> airports;
    private volatile boolean running = true;
    private List<gridUpdateObsv> gridUpdateObsvs = new ArrayList<>();
    private GridArea grid;
    private JTextArea textArea;
    private JLabel status;
    private int currFlights =0;
    private int completedFlights =0;
    private int currService =0;
    private final Object mutex = new Object();

    public FlightHandler(FlightLog log, List<AirPort> airports,GridArea grid, JTextArea textArea, JLabel status){
        this.log = log;
        this.executor = Executors.newCachedThreadPool();
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
                executor.submit(()-> processFlightRequest(request));
            }
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
            System.out.println("FlightHandler interrupted");
        }finally{
            shutdownThreadPool();
        }
        
    }

    private void processFlightRequest(FlightRequest request){
        try {
            AirPort origin = getAirPort(request.getOrigin());
            AirPort dest = getAirPort(request.getDest());
            
            if (origin != null && dest != null) {
                Plane plane = origin.getPlane();
                plane.getIcon().setShown(true);
                SwingUtilities.invokeLater(() -> textArea.append("Flight started from " + origin.getId() + " to destination " + dest.getId() + "\n"));
                // Move plane using the SwingWorker
                
                movePlane(dest.getX(), dest.getY(), plane,dest);
                
                SwingUtilities.invokeLater(() -> textArea.append("Flight from " + origin.getId() + " to destination " + dest.getId() + " has finished\n"));
                dest.setPlane(plane);                              
            }
        } catch (InterruptedException e) {
            System.out.println("processFlightRequest interrupted");
        }
    
    }

    private String movePlane(double destX, double destY, Plane plane, AirPort dest) {
        synchronized(mutex){
            currFlights++;
            updateStatus();
        }
        //idk we had to use swing worker but just trying to call sqingutils didn't cut it so idk
        SwingWorker<String, Void> worker = new SwingWorker<String,Void>() {
            //process plane movement
            @Override
            protected String doInBackground() throws Exception {
                boolean run = true;
                while (run) {
                    double[] newCords = planeMovement.calcNextPosition(destX, destY, plane);
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

                synchronized(mutex){
                    currService++;
                    updateStatus();
                }
                return planeService.service(plane.getId(), dest.getId());
            }

            //final update once background eg plane movement fin
            @Override
            protected void done() {
                try{
                    
                    plane.getIcon().setShown(false);
                    grid.repaint();

                    String serviceMsg = get();
                
                    //remove plane from grid and repaint 
                    
                    synchronized(mutex){
                        currFlights--;
                        completedFlights++; 
                        updateStatus();
                    }

                    SwingUtilities.invokeLater(()-> textArea.append(serviceMsg+"\n"));
                    synchronized(mutex){
                        currService--;
                        updateStatus();
                    }
                }catch(InterruptedException | ExecutionException e){
                    System.out.println("Plane Trip interrupted");
                }                            
            }
        };

        // Execute the SwingWorker
        worker.execute();
        return "";
    }


    private AirPort getAirPort(int id){
        for (AirPort airPort : airports) {
            if (airPort.getId() == id) {
                return airPort;
            }
        }

        return null;
    }

    public void shutdown(){
        running = false;
        Thread.currentThread().interrupt();
    }

    private void shutdownThreadPool(){
        executor.shutdown();
        try{
            if(!executor.awaitTermination(5, TimeUnit.SECONDS)){
                executor.shutdownNow();
            }
        }catch(InterruptedException e){
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

    }

    public void addGridObsv(gridUpdateObsv obs){
        gridUpdateObsvs.add(obs);
    }

    public void removeGridObsv(gridUpdateObsv obs){
        gridUpdateObsvs.remove(obs);
    }

    public void notifyGridOsbv(){
        for(var obs: gridUpdateObsvs){
            SwingUtilities.invokeLater(()->obs.gridUpdateEvent());
            
        }
    }

    public void updateStatus(){
        SwingUtilities.invokeLater(()->{
            status.setText("Birds in the air: "+currFlights+" Planes being serviced: "+currService+" Total Trips: "+completedFlights);
        });
    }
}
