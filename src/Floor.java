import java.util.ArrayList;

//Floor class establishes the attributes and methods of floors in a building simulation
public class Floor
{
	
	private boolean downRequest;//The 'down' button has been pressed
	private boolean upRequest;//The 'up' button has been pressed
	
	private final int floorNumber;//The number of this level
	
	private ArrayList<Person> people;//List of people waiting on this level
	
	//Instantiates a new floor with a given level number and default attributes
	public Floor(int floorNumber)
	{
		this.floorNumber = floorNumber;
		
		upRequest = false;
		downRequest = false;
		
		people = new ArrayList<Person>();
	}
	
	//Adds a new set of waiting people to the floor
	public void addPeople(ArrayList<Person> people)
	{
		//Go through the list of people and add them
		for(int i = 0; i < people.size(); i++)
		{
			this.people.add(people.get(i));
			
			//Turn on the up/down button depending on the person's destination
			if(people.get(i).getDestination() > floorNumber)
				upRequest = true;
			else
				downRequest = true;
		}
	}
	
	//Adds a single person to the list of people waiting on the floor
	public void addPerson(Person toAdd)
	{
		//Add the person to the list
		people.add(toAdd);
		
		//Turn on the up/down request button depending on the person's destination
		if(toAdd.getDestination() > floorNumber)
			upRequest = true;
		else
			downRequest = true;
	}
	
	//Return's the status of the 'down' button
	public boolean getDownRequest(){return downRequest;}
	
	//Return's the status of the 'up' button
	public boolean getUpRequest(){return upRequest;}
	
	//Returns the people that enter an elevator given it's direction and availability, and removes them from the floor
	public ArrayList<Person> removePassengers(int dir, int availability, Elevator elev)
	{
		//List of people who exit the floor
		ArrayList<Person> toReturn = new ArrayList<Person>();
		
		//Doesnt have a real direction
		if (elev.getPassenger().size() == 0) {
			if (people.size() > 0) {
				elev.setDirection((int) Math.signum(people.get(0).getDestination() - floorNumber));
			}
		}

		//Loop through all of the people on the floor and find those that are exiting
		for(int i = 0; i < people.size(); i++)
		{
			//If the person is going in the direction of the elevator
			if(Math.signum(people.get(i).getDestination() - floorNumber) == dir && toReturn.size() < availability)
			{
				//Add the person to the list of people entering the elevator and remove them
				toReturn.add(people.remove(i));

				i--;
			}
		}

		//Turn off the up/down request button if all passengers moving in that direction boarded
		if(dir == 1)
		{
			boolean upRequest = false;

			for(int i = 0; i < people.size(); i++)
			{
				if(people.get(i).getDestination() > floorNumber)
					upRequest = true;
			}

			this.upRequest = upRequest;
		}else
		{
			boolean downRequest = false;

			for(int i = 0; i < people.size(); i++)
			{
				if(people.get(i).getDestination() < floorNumber)
					downRequest = true;
			}

			this.downRequest = downRequest;
		}

		return toReturn;
	}
	
	//Return's the floor number
	public int getFloorNumber(){return floorNumber;}
	
	//Return's a list of the people waiting on this floor
	public ArrayList<Person> getPeople(){return people;}
}