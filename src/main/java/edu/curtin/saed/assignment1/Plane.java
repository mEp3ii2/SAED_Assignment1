package edu.curtin.saed.assignment1;



public class Plane extends AirTrafficEntity{
    
    private static int nextId = 1;

    public Plane(String name, GridAreaIcon icon, int x, int y){
        super(name, nextId++,icon, x, y);
    }

    public int getX(){
        return super.getX();
    }

    public int getY(){
        return super.getY();
    }
}
    
