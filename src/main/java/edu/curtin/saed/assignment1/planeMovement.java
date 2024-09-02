package edu.curtin.saed.assignment1;

public class planeMovement {

    public static double[] calcNextPosition(double destX, double destY, Plane plane){
        
        double currentX = plane.getX();
        double currentY = plane.getY();

        double deltaX = destX - currentX;
        double deltaY = destY - currentY;

        double distance = Math.sqrt(deltaX *deltaX + deltaY * deltaY);

        double unitX = deltaX / distance;
        double unitY = deltaY / distance;

        // Calculate the next position
        double nextX = currentX + unitX * 0.1;
        double nextY = currentY + unitY * 0.1;

        if(Math.abs(nextX - destX)<0.1 && Math.abs(nextY - destY)<0.1){//rounding
            nextX = destX;
            nextY = destY;
        }
        return new double[]{nextX,nextY};

    }
}
