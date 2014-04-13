//Class creates floor requests on a floor to be used by the elevator for 
//computational reasons
public class FloorRequest
{
	private boolean direction;//Used by requests made from outside the elevator (a person waiting for an elevator going down/up)
	private boolean directionOriented;//If the request was made outside the elevator (false = button pushed inside the elevator)
	
	private int floor;//The floor the elevator must stop at to pick up/unload passengers
	
	//Creates a new floor request based on the direction (if needed), if the request was made from outside or inside the elevator,
	//and the floor number
	public FloorRequest (boolean direction, boolean directionOriented, int floor)
	{
		this.floor = floor;
		this.direction = direction;
		this.directionOriented = directionOriented;
	}
		
	//Returns if a floor request will be served at the same time as another
	public boolean equals(FloorRequest toCompare)
	{
		//If one of them is not direction oriented, compare their floor numbers
		if(directionOriented == false || toCompare.directionOriented == false)
			return floor == toCompare.floor;
			
		//Both are direction oriented, compare their floor numbers and directions
		return floor == toCompare.floor && direction == toCompare.direction;
	}
	
	//The direction of the request (if applicable)
	public boolean getDirection()
	{
		return direction;
	}
	
	//If the request was made from outside or inside the elevator, i.e. if the elevator needs to be going in a certain direction
	//when it stops
	public boolean getDirectionOriented()
	{
		return directionOriented;
	}
	
	//Methods returns true if the elevator should stop for this floor request
	public boolean stop(boolean ascending, int floor)
	{
		//If the correct floor has been obtained
		if(this.floor == floor)
		{
			//If it doesn't matter what direction it is going in
			if(!this.directionOriented)
			{
				return true;
			}
			
			//If the elevator is going in the right direction
			if(ascending == this.direction)
				return true;
		}
		
		return false;
	}
	
	//The floor number of the request
	public int getFloor()
	{
		return floor;
	}
}