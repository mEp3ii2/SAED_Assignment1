package edu.curtin.saed.assignment1;

public class FlightRequest {
    private int originAirport;
    private int destinationAirport;

    public FlightRequest(int originAirport, int destinationAirport){
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
    }

    public int getOrigin(){
        return originAirport;
    }

    public int getDest(){
        return destinationAirport;
    }
}
