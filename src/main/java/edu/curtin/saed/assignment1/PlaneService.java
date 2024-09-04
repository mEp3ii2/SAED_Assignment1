package edu.curtin.saed.assignment1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PlaneService {
    private static BufferedReader br;

    public String service(int planeId, int airportId){
        try{
            Process proc = Runtime.getRuntime().exec(
                new String[]{"comms/bin/saed_plane_service",
                             String.valueOf(airportId),
                             String.valueOf(planeId)});
            br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String output = br.readLine();
            proc.waitFor();
            br.close();
            return output;
            
        }catch(InterruptedException e){
            System.out.println("Servicing Interrupted");
        }catch(IOException e){
            System.out.println("I/O Exception during plane servicing");
        }
        return null;
    }

    public static void closeResource(){
        try {
            if(br != null){
                br.close();
            }
        } catch (IOException e) {
            System.out.println("Error on closing plane service buffered reader");
        }
    }
}
