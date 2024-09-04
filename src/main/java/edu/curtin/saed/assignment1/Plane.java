package edu.curtin.saed.assignment1;



public class Plane extends AirTrafficEntity{
    
    private static int nextId = 1;

    public Plane(String name, GridAreaIcon icon, double x, double y){
        super(name, nextId++,icon, x, y);
    }

    @Override
    public double getX(){
        return super.getX();
    }

    @Override
    public double getY(){
        return super.getY();
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    
}
    
