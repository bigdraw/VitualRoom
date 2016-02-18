
public class Room {

	private int Id;
	private Door []doors = new Door[4];
	
	public Room(int Id, Door west, Door north, Door east, Door south) {
		this.Id = Id;
		doors[0] = west;
		doors[1] = north;
		doors[2] = east;
		doors[3] = south;
	}
	
	public int getId(){
		return Id;
	}
	
	public boolean getDoorState(int direction){
		return doors[direction].getState();
	}
	
	public int getNextRoom(int direction){
		return doors[direction].getNextRoom();
	}
}

class Door{
	private int Direction;
	private int NextRoom;
	private boolean State;
	
	public Door(int direction, boolean state, int nextRoom) {
		State = state;
		Direction = direction;
		NextRoom = nextRoom;
	}
 
	public int getDirection() {
		return Direction;
	}

	public void setDirection(int direction) {
		Direction = direction;
	}

	public int getNextRoom() {
		return NextRoom;
	}

	public void setNextRoom(int nextRoom) {
		NextRoom = nextRoom;
	}

	public boolean getState() {
		return State;
	}
	
}