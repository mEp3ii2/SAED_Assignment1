package edu.curtin.saed.assignment1;


public class AirPort extends AirTrafficEntity {

    private static int nextId = 1;

    public AirPort(String name,GridAreaIcon icon,int y, int x){
        super(name, nextId++,icon, x,y);
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
}
