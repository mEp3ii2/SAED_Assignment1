package edu.curtin.saed.assignment1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class planeService {

    public static String service(int planeId, int airportId){
        Process proc = null;
        try{
            proc = Runtime.getRuntime().exec(
                new String[]{"comms/bin/saed_plane_service",
                             String.valueOf(airportId),
                             String.valueOf(planeId)});
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String output = br.readLine();
            proc.waitFor();
            return output;

            
        }catch(InterruptedException e){
            System.out.println("gjdfbg");
        }catch(IOException e){
            System.out.println("jifdsnbgjd");
        }
        return null;
    }
}
