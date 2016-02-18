
public class User {
	
	private int Direction;
	private int Location;	
	
	public User(int direction,int location){
		this.setDirection(direction);
		this.setLocation(location);
	}
	
	public int getDirection() {
		return Direction;
	}
	public void setDirection(int direction) {
		Direction = direction;
	}
	public int getLocation() {
		return Location;
	}
	public void setLocation(int location) {
		Location = location;
	}
}
