package edu.curtin.saed.assignment1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AirPort extends AirTrafficEntity {

    private static int nextId = 1;
    private BlockingQueue<Plane> planes;

    public AirPort(String name,GridAreaIcon icon,int y, int x){
        super(name, nextId++,icon, x,y);
        planes = new ArrayBlockingQueue<>(10);
    }

    public int getId(){
        return super.getId();
    }

    public int getX(){
        return super.getX();
    }

    public int getY(){
        return super.getY();
    }

    public Plane getPlane() throws InterruptedException{
        
        return planes.take();

    }

    public void setPlane(Plane plane) throws InterruptedException{
        planes.put(plane);
    }
}
