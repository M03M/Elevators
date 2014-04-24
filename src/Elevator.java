import java.util.ArrayList;
import java.util.HashSet;

//Elevator class provides the attributes and methods of elevators in a building simulation
public class Elevator
{
	private int dir;//The direction that the elevator is going (1 = up, 0 = still, -1 = down)
	private int floorDir;//The floor that the elevator is going to
	
	private boolean justStopped;
	
	private int currentFloor;//The current floor of the elevator
	
	public static final int DEFAULT_MAX_OCCUPANCY = Integer.MAX_VALUE;//Default value for the number of passengers that can enter an elevator
	
	private final int maxOccupancy;//The maximum number of passengers that can ride in an elevator
	
	private ArrayList<Person> passengers;//A list of the passengers in the elevator
	
	//private ArrayList<FloorRequest> floorRequests;//A list of the 'lit' floor buttons in the elevator
	private HashSet<Integer> floorRequests;
	
	//Creates a new elevator object with default parameters and a set maximum floor
	public Elevator()
	{
		currentFloor = (int) (Math.random() * Runner.getMaxFloors());
		
		maxOccupancy = DEFAULT_MAX_OCCUPANCY;
		
		floorRequests = new HashSet<Integer>();
		
		passengers = new ArrayList<Person>();
	}
	
	public void tick() {
		justStopped = false;
		
		currentFloor += dir;
		if (currentFloor == floorDir) {
			dir = 0;
		}
	}
	
	//Adds a new passenger to the elevator
	public void addPerson(Person toAdd)
	{
		passengers.add(toAdd);
		floorRequests.add(toAdd.getDestination());
		if (dir < 0) {
			if (toAdd.getDestination() < floorDir) {//wants to go down more than its already going down
				setFloor(toAdd.getDestination());
			}
		} else if (dir > 0) {
			if (toAdd.getDestination() > floorDir) {//wants to go down more than its already going down
				setFloor(toAdd.getDestination());
			}
		}
	}	
	
	//Sets the direction of the elevator
	public void setFloor(int floorDir) {
		this.floorDir = floorDir;
		this.dir = (int) Math.signum(floorDir - currentFloor);
	}
	
	//Returns the direction of the elevator
	public int getDirection() {return dir;}
	
	//Returns the floor youre the elevator is going to
	public int getFloorDirection() {return floorDir;}
	
	//Returns the number of additional passengers the elevator can hold
	public int getAvailability(){return maxOccupancy - passengers.size();}
	
	//Retuns the current floor
	public int getCurrentFloor(){return currentFloor;}
	
	//Returns the maximum occupancy of the floor
	public int getMaxOccupancy(){return maxOccupancy;}
	
	//Returns a list of all the lit buttons in the elevator
	public HashSet<Integer> getFloorRequests()
	{
		return floorRequests;
	}
	
	//Return a list of passengers that exit the elevator at this stop
	public ArrayList<Person> ejectPassengers()
	{
		ArrayList<Person> toReturn = new ArrayList<Person>();
		
		//Loops through all passengers and if the person is getting off at that floor remove them from the elevator
		for(int i = 0; i < passengers.size(); i++)
		{
			if(passengers.get(i).getDestination() == currentFloor)
			{
				toReturn.add(passengers.remove(i));
				i--;
			}
		}
		
		return toReturn;
	}
	
	//Returns a list of the passengers on board
	public ArrayList<Person> getPassenger(){return passengers;}
	
	public void stop() {
		justStopped = true;
	}
	
	public boolean justStopped() {
		return justStopped;
		
	}
}