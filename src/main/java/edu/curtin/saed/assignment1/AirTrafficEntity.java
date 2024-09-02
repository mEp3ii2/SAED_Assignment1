package edu.curtin.saed.assignment1;

public class AirTrafficEntity {
    
    private String name;
    private int id;
    private GridAreaIcon icon;
    protected double x;
    protected double y;

    public AirTrafficEntity(String name,int id,GridAreaIcon icon,double x,double y){
        this.name = name;
        this.id = id;
        this.icon = icon;
        this.x = x;
        this.y = y;
    }

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }

    public GridAreaIcon getIcon(){
        return icon;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

   
}
