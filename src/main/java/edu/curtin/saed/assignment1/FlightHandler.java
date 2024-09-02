package edu.curtin.saed.assignment1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

public class FlightHandler implements Runnable{
    
    private final ExecutorService executor;
    private FlightLog log;
    private List<AirPort> airports;
    private volatile boolean running = true;
    private List<gridUpdateObsv> gridUpdateObsvs = new ArrayList<>();

    public FlightHandler(int threadCount, FlightLog log, List<AirPort> airports){
        this.log = log;
        this.executor = Executors.newFixedThreadPool(threadCount);
        this.airports = airports;
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
        //check int aiport and grab plane
        System.out.println("Hellloooooooooooooo");
        boolean tripComplete = false;
        Plane plane = null;
        try {
            AirPort origin = getAirPort(request.getOrigin());
            AirPort dest = getAirPort(request.getDest());
            if(origin != null && dest != null){
                while(!tripComplete){
                    
                    plane = origin.getPlane();
                    plane.getIcon().setShown(true);                    
                    notifyGridOsbv();
                    double[] newCords = planeMovement.calcNextPosition(dest.getX(),dest.getY(),plane);
                    plane.setX(newCords[0]);
                    plane.setY(newCords[1]);
                    if(plane.getX() == dest.getX() && plane.getY() == dest.getY()){
                        tripComplete = true;
                    }
                    notifyGridOsbv();
                    Thread.sleep(100);
                }
                System.out.println("Howdy");
                plane.getIcon().setShown(false);
                dest.setPlane(plane);
                //need to service plane as well but for now can go straight into new airport
                notifyGridOsbv();
                
            }                   
        } catch (InterruptedException e) {
            System.out.println("Flight request processor interupted");
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
}
