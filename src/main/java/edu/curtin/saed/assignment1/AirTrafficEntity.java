package edu.curtin.saed.assignment1;

public class AirTrafficEntity {
    
    private String name;
    private int id;
    private GridAreaIcon icon;
    private int x;
    private int y;

    public AirTrafficEntity(String name,int id,GridAreaIcon icon,int x,int y){
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

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
