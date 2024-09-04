package edu.curtin.saed.assignment1;


import java.util.concurrent.BlockingQueue;

public class AirPort extends AirTrafficEntity {

    private static int nextId = 1;
    private BlockingQueue<Plane> planes;

    public AirPort(String name,GridAreaIcon icon,int y, int x,BlockingQueue<Plane> planes){
        super(name, nextId++,icon, x,y);
        this.planes = planes;
    }

    @Override //pmd told me too
    public int getId(){
        return super.getId();
    }
    
    @Override
    public double getX(){
        return super.getX();
    }

    @Override
    public double getY(){
        return super.getY();
    }

    public Plane getPlane() throws InterruptedException{
        
        return planes.take();

    }

    public void setPlane(Plane plane) throws InterruptedException{
        planes.put(plane);
    }
}
