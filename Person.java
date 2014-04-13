//Person class establishes attributes and methods for passengers on floors and elevators

public class Person
{
	private int destination;//The floor that they are going too
	
	private long birth;//The time that they were instantiated (for statistics purposes)
	
	//Instantiates a person with a set destination and sets their birth time to the system's current time
	//in nano seconds
	public Person(int destination)
	{
		this.destination = destination;
		
		birth = System.nanoTime();
	}
	
	//Returns the person's destination
	public int getDestination()
	{
		return destination;
	}
	
	//Return's the person's time of birth in nano seconds
	public long getBirth()
	{
		return birth;
	}
}