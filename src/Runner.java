import java.util.ArrayList;
import java.util.List;


public class Runner {
	private static List<Elevator> elevatorList = new ArrayList<Elevator>();
	
	private static List<Floor> floorList = new ArrayList<Floor>();
	
	private static List<Person> finishedPeople = new ArrayList<Person>();
	
	/*
	 * @param n sets the number of elevators
	 */
	public void setNumElevators(int n) {
		elevatorList.clear();
		
		for (int i = 0; i < n; i++) {
			addElevator();
		}
	}
	
	/*
	 * adds a new elevator object
	 */
	public void addElevator() {
		elevatorList.add(new Elevator());
	}
	
	/*
	 * removes the right most elevator
	 */
	public void removeElevator() {
		elevatorList.remove(elevatorList.size() - 1);
	}
	
	/*
	 * @param n sets the number of elevators
	 */
	public void setNumFloors(int n) {
		floorList.clear();
		
		for (int i = 0; i < n; i++) {
			addFloor();
		}
	}
	
	/*
	 * adds a new elevator object
	 */
	public void addFloor() {
		floorList.add(new Floor(floorList.size()));
	}
	
	/*
	 * removes the highest floor
	 */
	public void removeFloor() {
		floorList.remove(floorList.size() - 1);
	}
	
	/*
	 * run this method after each draw
	 */
	public void tick() {
		for (int i = 0; i < floorList.size(); i++) {
			Floor curFloor = floorList.get(i);
			
			int numPeopleToAdd = (int) Math.ceil(Math.random() * 10);
			
			for (int a = 0; a < numPeopleToAdd; a++) {
				int floorGoingTo = (int) (Math.random() * (floorList.size() - 1));
				if (floorGoingTo >= i) {
					floorGoingTo++;
				}
				
				curFloor.addPerson(new Person(floorGoingTo));
			}
		}
		
		for (int i = 0; i < elevatorList.size(); i++) {
			Elevator curElevator = elevatorList.get(i);
			
			if (curElevator.justStopped()) {//just picked up people, has to move to next floor
				curElevator.tick();
			} else {//can pick up/drop off more people
				List<Person> exitPassengers = curElevator.ejectPassengers();
				boolean elevatorHasGoal = curElevator.getPassenger().size() != 0;
				List<Person> floorPeople = floorList.get(curElevator.getCurrentFloor()).removePassengers(curElevator.getDirection(), curElevator.getAvailability(), curElevator);
				
				if (exitPassengers.size() != 0 || floorPeople.size() != 0) {//TODO could be better if another elevator had to pick up and drop off, check for that
					curElevator.stop();
					finishedPeople.addAll(curElevator.ejectPassengers());
					
					for (Person p : floorPeople) {
						curElevator.addPerson(p);
					}
				} else {
					curElevator.tick();
				}
			}
		}
		
		for (int i = 0; i < floorList.size(); i++) {
			Floor curFloor = floorList.get(i);
			
			if (curFloor.getUpRequest()) {
				Elevator closest = getClosestElevator(1, i);
				if (closest.getDirection() == 0) {
					closest.setDirection((int) Math.signum(i - closest.getCurrentFloor()));
				}
			}
			
			if (curFloor.getDownRequest()) {
				Elevator closest = getClosestElevator(-1, i);
				if (closest.getDirection() == 0) {
					closest.setDirection((int) Math.signum(i - closest.getCurrentFloor()));
				}
			}
		}
	}
	
	/*
	 * returns the closest elevator thats going or can go in the direction of the button pressed on the floor
	 * @param dir the direction of the button pressed
	 * @param floor the floor number on which the button was pressed 
	 */
	public Elevator getClosestElevator(int dir, int floor) {
		int distance = Integer.MAX_VALUE;
		Elevator closest = null;
		
		for (int i = 0; i < elevatorList.size(); i++) {
			int curDistance = Integer.MAX_VALUE;
			Elevator curElevator = elevatorList.get(i);
			int elevatorDir = curElevator.getDirection();
			int elevatorFloor = curElevator.getCurrentFloor();
			
			if (Math.signum(elevatorFloor - floor) == elevatorDir) {//its going farther away
				if (elevatorDir == 1) {//going to go to the top first at worst case
					int distanceToTop = (floorList.size() - 1) - elevatorFloor;
					int distanceFromTop = (floorList.size() - 1) - floor;
					curDistance = distanceToTop + distanceFromTop;
				} else if (elevatorDir == -1) {//going to go to the bottom first at worst case
					int distanceToBottom = elevatorFloor;
					int distanceFromBottom = floor;
					curDistance = distanceToBottom + distanceFromBottom;
				}
			} else if (Math.signum(floor - elevatorFloor) == elevatorDir) {//its moving towards it
				curDistance = Math.abs(floor - elevatorFloor);
			} else if (elevatorDir == 0) {//elevators not moving
				curDistance = Math.abs(floor - elevatorFloor);
			}
			
			if (curDistance < distance) {
				distance = curDistance;
				closest = curElevator;
			}
		}
		
		return closest;
	}
	
	/*
	 * @return an array of the floors each elevator is on. array[0] is the first elevator's floor.
	 */
	public static int[] getElevatorFloors() {
		int[] floors = new int[elevatorList.size()];
		
		for (int i = 0; i < floors.length; i++) {
			floors[i] = elevatorList.get(i).getCurrentFloor();
		}
		
		return floors;
	}
	
	/*
	 * @return an array of an integer arraylist of all the floors lit
	 */
	public static List<Integer>[] getLitButtons() {
		List[] litButtons = new ArrayList[elevatorList.size()];
		
		for (int i = 0; i < litButtons.length; i++) {
			litButtons[i] = elevatorList.get(i).getLitButtons();
		}
		
		return litButtons;
	}
	
	/*
	 * @return returns the number of floors
	 */
	public static int getMaxFloors() {
		return floorList.size();
	}
	
	/*
	 * @return returns a 2d array of boolean values where the first index is the floor number. the second array is whether up or down are pressed. aka: floor[2][0] is if the 3rd floors up button is pressed. floor[2][1] is if the 3rd floors down buttons is pressed.
	 */
	public static boolean[][] getFloorRequests() {
		boolean[][] floorRequests = new boolean[floorList.size()][2];
		
		for (int i = 0; i < floorRequests.length; i++) {
			floorRequests[i][0] = floorList.get(i).getUpRequest();
			floorRequests[i][1] = floorList.get(i).getDownRequest();
		}
		
		return floorRequests;
	}
}
