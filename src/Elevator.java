import java.util.ArrayList;

//Elevator class provides the attributes and methods of elevators in a building simulation
public class Elevator
{
	private boolean ascending;//The direction that the elevator is going (true = up, false = down)
	
	private int currentFloor;//The current floor of the elevator
	private int maximumFloor;//The top floor of the building
	
	public static final int DEFAULT_MAX_OCCUPANCY = 10;//Default value for the number of passengers that can enter an elevator
	
	private final int maxOccupancy;//The maximum number of passengers that can ride in an elevator
	
	private ArrayList<Person> passengers;//A list of the passengers in the elevator
	
	private ArrayList<FloorRequest> floorRequests;//A list of the 'lit' floor buttons in the elevator
	
	//Creates a new elevator object with default parameters and a set maximum floor
	public Elevator(int maximumFloor)
	{
		this.maximumFloor = maximumFloor;
		
		ascending = true;
		
		currentFloor = 1;
		
		maxOccupancy = DEFAULT_MAX_OCCUPANCY;
		
		floorRequests = new ArrayList<FloorRequest>();
		
		passengers = new ArrayList<Person>();
	}
	
	//Creates a new elevator object with additional set parameters
	public Elevator(boolean ascending, int maximumFloor, int currentFloor, int maxOccupancy)
	{
		this.ascending = ascending;
		
		this.maximumFloor = maximumFloor;
		
		this.currentFloor = currentFloor;
		
		this.maxOccupancy = maxOccupancy;
		
		floorRequests = new ArrayList<FloorRequest>();
		
		passengers = new ArrayList<Person>();
	}
	
	//Adds a floor request to the jobs the elevator is tasked with
	public void addFloorRequest(FloorRequest toAdd)
	{
		boolean hasBeenAdded = false;//The request has been placed in the request list
		
		//Variables used to simulate the elevator's movement and the index in th elist of tasks
		//where the elevator whould be at, to find the correct spot for the new request
		boolean directionTracker = ascending;
		int iterator = currentFloor;
		int listIndex = 0;
		
		//Continue until the request has been added
		while(!hasBeenAdded)
		{
			//If the request would need to stop at the floor that has been reached, add it
			if(toAdd.stop(directionTracker, iterator))
			{
				floorRequests.add(listIndex, toAdd);
				
				hasBeenAdded = true;
			}
			
			//If another stop is being handled, increase the index of the current position in the list of tasks
			if(floorRequests.get(listIndex).stop(directionTracker, iterator))
			{
				listIndex++;
			}else if(iterator == 1 && !directionTracker)//If the bottom floor has been reached, change directions
			{
				directionTracker = true;
			}else if(iterator == maximumFloor && directionTracker)//If the top floor has been reached, change directions
			{
				directionTracker = false;
			}else if(directionTracker)//Theoretically move the elevator up a floor
			{
				iterator++;
			}else//Theoretically move the elevator down a floor
			{
				iterator--;
			}
		}
	}
	
	//Adds a new passenger to the elevator
	public void addPerson(Person toAdd)
	{
		passengers.add(toAdd);
		
		//See if their request needs to be added to the list
		if(requestAlreadyPresent(new FloorRequest(false, false, toAdd.getDestination())))
			addFloorRequest(new FloorRequest(false, false, toAdd.getDestination()));	
	}
	
	//Clear all jobs associated with the current stop
	public void removeFloorRequests()
	{
		FloorRequest thisStop = new FloorRequest(ascending, true, currentFloor);
		
		//Remove any floor requests that are handled at this stop
		for(int i = 0; i < floorRequests.size(); i++)
		{
			if(floorRequests.get(i).equals(thisStop))
			{
				floorRequests.remove(i);
			}
		}
	}
	
	//Set the elevator's direction based on the tasks that are left
	public void setDirection()
	{
		if(!stopping())
		{
			//If the maximum floor has been reached, change directions
			if(currentFloor == maximumFloor)
				ascending = false;
			
			//If the bottom floor has been reached, change directions
			if(currentFloor == 1)
				ascending = true;
			
			//If there are other requests
			if(floorRequests.size() > 0)
			{
				//If the next floor request is above the current tracked floor
				if(floorRequests.get(0).getFloor() > currentFloor)
					ascending = true;
				
				//If the next floor request is below the current tracked floor
				if(floorRequests.get(0).getFloor() < currentFloor)
					ascending = false;
			}
		}
	}
	
	//Continue ascending/descending if there are more stops to make, or let passengers on/off at this floor
	//returns true if there is a stop being made
	public boolean advance()
	{
		if(stopping())
			return true;
		
		setDirection();
		
		//Move the elevator up or down depending on its direction
		if(floorRequests.size() > 0)
		{
			if(ascending)
				currentFloor++;
			else
				currentFloor--;
		}
		
		return false;
	}
	
	//Returns if the elevator is going up
	public boolean getAscending(){return ascending;}
	
	//Returns of a request that is trying to be added is already being taken care of
	public boolean requestAlreadyPresent(FloorRequest request)
	{
		boolean toReturn = false;
		
		//Search to see if this type of request is already present
		for(int i = 0; i < floorRequests.size(); i++)
		{
			if(floorRequests.get(i).equals(request))
			{
				toReturn = true;
				i = floorRequests.size();
			}
		}
		
		return toReturn;
	}
	
	//If the elevator is stopping at this floor
	public boolean stopping()
	{
		//Search to see if any floor requests need to be serviced at this floor while the elevator
		//is moving in its current direction
		for(int i = 0; i < floorRequests.size(); i++)
		{
			if(floorRequests.get(i).equals(new FloorRequest(ascending, true, currentFloor)))
				return true;
		}
		
		return false;
	}
	
	//Return the number of time units adding this request will add to the overall completion time of the elevator's tasks.
	//The method takes into account the number of people that are on board that will be forced to wait additional time
	//While servicing the additional request
	public int getAddedRequestDifficulty(FloorRequest toAdd)
	{
		//If there is no room for additional passengers
		if(getAvailability() < 1)
			return -1;
		
		//If the request is already present
		if(requestAlreadyPresent(toAdd))		
			return 0;
			
		//If there are no other requests
		if(floorRequests.size() == 0)
			return Math.abs(currentFloor - toAdd.getFloor());
			
		//Make two copies of the current elevator
		Elevator toCompare = new Elevator(this.getAscending(), this.getMaximumFloor(), this.getCurrentFloor(), this.getMaxOccupancy());
		Elevator copy = new Elevator(this.getAscending(), this.getMaximumFloor(), this.getCurrentFloor(), this.getMaxOccupancy());
		
		for(int i = 0; i < this.getFloorRequests().size(); i++)
		{
			toCompare.getFloorRequests().add(this.getFloorRequests().get(i));
			copy.getFloorRequests().add(this.getFloorRequests().get(i));
		}
			
		//Add a new floor request to one of the copies
		toCompare.addFloorRequest(toAdd);
		
		//Return the difference in request completion time between the exact copy and copy with the additional request
		return toCompare.getRequestCompletionTime() - copy.getRequestCompletionTime();
	}
		
	//Returns the number of additional passengers the elevator can hold
	public int getAvailability(){return maxOccupancy - passengers.size();}
	
	//Retuns the current floor
	public int getCurrentFloor(){return currentFloor;}
		
	//Returns the maximum floor of the elevator
	public int getMaximumFloor(){return maximumFloor;}
	
	//Returns the maximum occupancy of the floor
	public int getMaxOccupancy(){return maxOccupancy;}
		
	//Return the amount of time (with consideration of the number of passengers) it will take to complete
	//all current tasks
	public int getRequestCompletionTime()
	{
		int time = 0;
		
		//While their are still passengers on the elevator waiting to get off
		while(this.passengers.size() > 0)
		{
			time++;
			
			//If the elevator is stopping, eject passengers and increment the amount of time waited by all passengers
			//still on the elevator
			if(stopping())
			{
				ejectPassengers();
				
				time += passengers.size();
				
				removeFloorRequests();
			}else//Increment the amount of time waited by all passengers and move the elevator
			{
				advance();
				
				time += passengers.size();
			}
		}
		
		return time;
	}
	
	//Returns a list of all the lit buttons in the elevator
	public ArrayList<Integer> getLitButtons()
	{
		ArrayList<Integer> toReturn = new ArrayList<Integer>();
		
		//Looks through all floor requests and returns the numbers floor numbers in the request
		for(int i = 0; i < floorRequests.size(); i++)
		{
			toReturn.add(floorRequests.get(i).getFloor());
		}
		
		return toReturn;
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
				toReturn.add(passengers.get(i));
				passengers.remove(i);
			}
		}
		
		return toReturn;
	}
	
	//Return a list of people that get off at this floor, and also add new passengers that board the elevator
	public ArrayList<Person> floorLanding(Floor floor)
	{
		//Eject passengers and obtain boarders from the floor
		ArrayList<Person> toReturn = ejectPassengers();
		
		ArrayList<Person> boarders = floor.removePassengers(ascending, getAvailability());
		
		//If the last entry is null, it means that more people are waiting to board another elevator going in the same direction,
		//and that another elevator needs to be sent
		if(boarders.size() > 0 && boarders.get(boarders.size() - 1) == null)
		{
			boarders.remove(null);
			
			toReturn.add(null);
		}
		
		//Add all boarders
		for(int i = 0; i < boarders.size(); i++)
		{
			addPerson(boarders.get(i));
		}
		
		//Remove floor requests of the elevator to this floor going in this direction
		removeFloorRequests();
		
		setDirection();
		
		return toReturn;
	}
	
	//Returns a list of the passengers on board
	public ArrayList<Person> getPassenger(){return passengers;}
	
	//Returns a list of the stop requests that this elevator is responsible for
	public ArrayList<FloorRequest> getFloorRequests(){return floorRequests;}
}