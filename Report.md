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

For such as system there is a few important non-functional requirements that come to mind to ensure a well working system.

Issues such as performance in regards to the transmission of data to ensure that the system runs smoothly in real time. NFRs for this would be ensuring that the system maintains low mantency to ensure real time flight data is being displayed. Challanges from this is that flight data that originates from the aircraft will have to be transported through a lot of intermediate services/devices to reach the user, and considering that there are alot of important services in airline systems requiring this data they will be priorisited over non-critical user services such as ours making maintinaing low latency challenging. Another requirement might be that the system should be able to simulate/estimate flight data to ensure that the system appears to be continoulsy tracking inbetween recieving data transmissions from aircraft this might require functional requirements such as using timestamps and tracking speed to calculate positions. 

Scalabilty also becomes an issue, as a application that is avaliable to users all around the world the system should be able to track and display flights all around the world.
By 2024, the number of flights is forecasted to reach a new high of 40.1 million (Statista Research Department, 2024), this comes to just shy of 110 thousand flights a day, to be able to track and display this data to users proves to be significant challenge with the infastructre needed being immense unless using a infastructure as a service solution both proving to be expensive solutions to the problem. Solutions to this can be that only flights that are present on the users screen are displayed and as the scroll across the globe/screen flights are brought into view and others are removed. You could also restrict the users view so that they can't zoom out to far to reduce the amounts of flights being displayed or set a limit and make it so the user has to select what they wish to view instead of just showing everything such as the users can only be tracking at most x numbers of flights at a time.


Having a mobile app will singificantly increase total user and also the average concurrent users as well as compared to just being a desktop application. The same issues mentioned earlier will be present but also further exacerbrated since the scale will increase dramatically making is significantly harder to ensure a seemless experience for all users. Issues that stem from mobile as well is that maintaining persistent connections is taxing on the mobile devices battery. while on the topic of connection unlike desktop devices that are usually connected to there home network mobile devices will be relying on the aviavle connection in there area this can lead to periods where the user experiences limited connection or no connection for moments of time and the app should be able to handle this to ensure the app works smoothly, therefore the app should be able to handle transmission between online and offline states.Of course you can't talk about mobile with out mentioning the standard qestion of apple or andriod or both as it is now most developers choose both and therefor to be successfull the app must support both operating systems.

To be able to allow the user to be able to visualise past flights, the system must be able to store all flights in a distrubuted system to then allow for any user to download the view past flights in a timely manner subject to there network connection. Storing of flight data must comply with the various regulations as they apply such as regulations for the country of departure, the destination country and also the which country the airline is registred in as well must be complied with.


## Architectural approach



**References:**

Statista Research Department. (2024, June 28). Global air traffic - number of flights 2004-2024. Statista. https://www.statista.com/statistics/564769/airline-industry-number-of-flights/



