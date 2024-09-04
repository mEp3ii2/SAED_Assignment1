# SAED Assignment One <br> Flight Simulation using Multithreading



## A) Solutions to the Problem Description using multithreading

### Classes responsible

In the main we start 10 threads to run the FlightRequestGenerator class, these instances take a int for the total number of airports and a int for the origin airport using this it will run a bash script using these values and capture the outputs with this is will create a FlightRequest and attempt to put it in the blocking queue that is help by FlightLog

The main also creates a thread and starts a instance FlightHandler, flighthandler will take flightrequest from the Flightlog and assign them to a thread from the pool to process the flight. A SwingWorker is used to complete the animation of the plane across the grid.

### Thread Communication
To facilitate the passing of FlightRequests from the FlightRequestGenerators to the FlightHandler, FlightLog was established. FlightLog holds the blocking queue that the generators add their requests too and that the FlightHandler takes from. 

### Shared Resources
Ints for tracking the current amount of flights in progress, planes being serviced and total completed flights were used in the FlightHandler.
In order to prevent reace conditions access to these variables was throught the use of the syncronised statement using a Mutex object
as shown here 

```Java
synchronized(mutex){
    currFlights++;
    updateStatus();
}
```
similiar methods are in place for modifying the other shared resources
and updateStatus which reads from all three

### Ending threads

when the endBtn is clicked the threads for the request generators are interrupted and then the Buffered Readers are closed, then the flightManger which is our instance of the FlightHandler has it shutdown method called. In this we set the running status to false so the no new tasks are retrieved from the BlockingQueue we then call shutdownNow() on our threadpool and then for each SwingWorker we set its cancel status to true so that they stop work after this we send a message to the gui and closed the BufferedReader for the Service requests.

## B) Modified Version Challenges

### Application being a real time visulisation

Challenges from this will be the recieving of data, as these flights could be across a country, continent or globe this proposes the issue of how you would recieve these transmissions of data it would invole either satellites or having multiple ground stations to cover the scope your working with. With this as well based on the time taken to transmit the data the aircraft would no longer be in that position so you would need to be getting additional data such as velocity and say a timestamp so that based on that our system could roughtly calculate the position of the plane. The aircraft would also not be constantly transmitting this data but would rather be on a set time scale so with that in mind you would need to be able to calculate the journey of the plane based on the given data to calculate the positions until the next lot of data. Data could also be lost as well in transmit which leads to the system either having to suspend the flight tracking or continue to estimate until the next successful data transmission, suspending the display of a flight is also not advised as it may cause stress to viewers.

As the scale increases as well would need to look at the resources required to process the amount of flights being tracked.

### Mobile App



### Past Flight Visulisation

## Architectural approach