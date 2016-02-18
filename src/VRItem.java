
public class VRItem {
	
	//false means have-not-been-got
	private Boolean State = false;
	
	private int Id;
	private int Location;
	private int Direction;
	private double x;
	private double y;
	
	public VRItem(int Id, int Location, int Direction, double x, double y){
		this.setId(Id);
		this.setDirection(Direction);
		this.setLocation(Location);
		this.setX(x);
		this.setY(y);
	}	

	public Boolean getState() {
		return State;
	}
	
	public void setState(Boolean state) {
		State = state;
	}		

	public int getLocation() {
		return Location;
	}

	public void setLocation(int location) {
		Location = location;
	}

	public int getDirection() {
		return Direction;
	}

	public void setDirection(int direction) {
		Direction = direction;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
}
