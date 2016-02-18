import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;

public class MyController {

	@FXML
	private ImageView MainImage;
	@FXML
	private ImageView ItemImage1;	
	@FXML
	private ImageView ItemImage2;
	@FXML
	private ImageView ItemImage3;	
	@FXML
	private ImageView ItemImage4;	
	@FXML
	private ImageView MapImage;	
	@FXML
	private Label Mark1;	
	@FXML
	private Label Mark2;
	@FXML
	private Label Mark3;
	@FXML
	private Label Mark4;
	@FXML
	private Label LocMark;
	@FXML
	private TextArea Message;	
	@FXML
	private AnchorPane TopPane;
	
	public static final int room1=0;
	public static final int room2=1;
	public static final int room3=2;
	public static final int none=0;
	
	public static final int WEST=0;
	public static final int NORTH=1;
	public static final int EAST=2;
	public static final int SOUTH=3;
	
	public static final int item1=0;
	public static final int item2=1;
	public static final int item3=2;
	public static final int item4=3;
	
	User user;
	Room []rooms = new Room[3];
	VRItem []items = new VRItem[4];
	ImageView []ItemImages = new ImageView[4];
	Label []Marks = new Label[4];
		
	String Messages = "";
	
	int CurrentRoom;
	int CurrentDirection;
	VRItem CurrentItem;
	boolean MapFlag;
	
	Timeline timeline;
	
	double [][]ItemTabs = {{512,37},{512,114},{512,192},{512,273}};
	
	String []LocMarks = {"¡û","¡ü","¡ú","¡ý"};
	double [][]RoomLocMarks = {{341,389},{341,440},{414,440}};
	
	double [][][]ItemMarkLoc = {
			//room1,W,N,E,S
			{{290,389},{373,366},{454,381},{379,397}},
			//room2,W,N,E,S
			{{290,448},{324,412},{367,452},{330,483}},
			//room3,W,N,E,S
			{{379,456},{420,412},{454,448},{420,483}}};
	
	double [][][]RoomImageMinXY = {
			//room1,W,N,E,S
			{{265,0},{1065,0},{1865,0},{2700,0}},
			//room2,W,N,E,S
			{{300,0},{900,0},{1500,0},{2100,0}},
			//room3,W,N,E,S
			{{2400,0},{450,0},{1100,0},{1800,0}}};
	
	double draggedx;
	double draggedy;
	
	double CurrentImageMinX;
	
	public void Initialise() {
		user = new User(SOUTH, room1);
		
		rooms[room1] = new Room(room1, 				 
				new Door(WEST, false, none),
				new Door(NORTH, false, none), 
				new Door(EAST, false, none),
				new Door(SOUTH, true, room2));

		rooms[room2] = new Room(room2, 				 
				new Door(WEST, false, none),
				new Door(NORTH, true, room1),
				new Door(EAST, true, room3),
				new Door(SOUTH, false, none));

		rooms[room3] = new Room(room3, 				 
				new Door(WEST, true, room2),
				new Door(NORTH, false, none),
				new Door(EAST, false, none),
				new Door(SOUTH, false, none));
		
		items[item1] =  new VRItem(item1, room1, EAST, 
				Math.random()*MainImage.getFitWidth()*0.6 + 50,Math.random()*MainImage.getFitHeight()*0.6 + 50);
		items[item2] =  new VRItem(item2, room2, NORTH, 
				Math.random()*MainImage.getFitWidth()*0.6 + 50,Math.random()*MainImage.getFitHeight()*0.6 + 50);
		items[item3] =  new VRItem(item3, room3, NORTH, 
				Math.random()*MainImage.getFitWidth()*0.6 + 50,Math.random()*MainImage.getFitHeight()*0.6 + 50);
		items[item4] =  new VRItem(item4, room3, SOUTH, 
				Math.random()*MainImage.getFitWidth()*0.6 + 50,Math.random()*MainImage.getFitHeight()*0.6 + 50);
		
		ItemImages[item1] = ItemImage1;
		ItemImages[item2] = ItemImage2;
		ItemImages[item3] = ItemImage3;
		ItemImages[item4] = ItemImage4;
		
		Marks[item1] = Mark1;
		Marks[item2] = Mark2;
		Marks[item3] = Mark3;
		Marks[item4] = Mark4;
				
		LoadItemImage();
				
		CurrentRoom = user.getLocation();
		CurrentDirection = user.getDirection();
		CurrentItem = GetItemHere(CurrentRoom, CurrentDirection);
		CurrentImageMinX = RoomImageMinXY[CurrentRoom][CurrentDirection][0];
		MapFlag = false;
		
		
		Messages = "";
		Message.setText(Messages);
		
		String MainImageName = "resource/rooms/" + String.valueOf(CurrentRoom) + ".jpg";
    	Image mainimage = new Image(MainImageName);
        MainImage.setImage(mainimage);
        Rectangle2D viewportRect = new Rectangle2D( CurrentImageMinX, 0, 500, 380);
        MainImage.setViewport(viewportRect);
        
        update();
	}
	
    public void moveFoward(ActionEvent event) {
    	if(rooms[CurrentRoom].getDoorState(CurrentDirection)){
    		int NextRoom = rooms[CurrentRoom].getNextRoom(CurrentDirection);    	
        	user.setLocation(NextRoom);
        	CurrentRoom = NextRoom;
        	Messages = "You moved foward!\n";
        	String MainImageName = "resource/rooms/" + String.valueOf(CurrentRoom) + ".jpg";
        	Image mainimage = new Image(MainImageName);
            MainImage.setImage(mainimage);
            CurrentImageMinX = RoomImageMinXY[CurrentRoom][CurrentDirection][0];
            Rectangle2D viewportRect = new Rectangle2D( CurrentImageMinX, 0, 500, 380);
            MainImage.setViewport(viewportRect);       	
        	
    	}
    	else
    	{
    		Messages = "Here you can't move foward!\n";
    	}
    	
    	if(CurrentItem != null){
    		ItemImages[CurrentItem.getId()].setVisible(false);
    	}
    	
    	CurrentItem = GetItemHere(CurrentRoom, CurrentDirection);
    	
    	TopPane.setCursor(Cursor.DEFAULT);
    	update();
    }
    
    public void moveLeft(ActionEvent event) {
    	int NextDirection = (CurrentDirection + 3) % 4;
    	user.setDirection(NextDirection);
    	CurrentDirection = NextDirection;
    	Messages = "You turned left!\n";
    	
    	if(CurrentItem != null){
    		ItemImages[CurrentItem.getId()].setVisible(false);
    	}
    	
		CurrentItem = GetItemHere(CurrentRoom, CurrentDirection);

		timeline = new Timeline(new KeyFrame(Duration.millis(10), ev -> {

			System.out.println(CurrentImageMinX);

			if (CurrentImageMinX >= RoomImageMinXY[CurrentRoom][CurrentDirection][0]) {
				CurrentImageMinX -= (RoomImageMinXY[CurrentRoom][2][0] - RoomImageMinXY[CurrentRoom][1][0]) / 100 + 5;
				if (CurrentImageMinX < RoomImageMinXY[CurrentRoom][CurrentDirection][0]) {
					CurrentImageMinX = RoomImageMinXY[CurrentRoom][CurrentDirection][0];
					this.timeline.stop();
				}
			} 
			else {
				CurrentImageMinX -= (RoomImageMinXY[CurrentRoom][2][0] - RoomImageMinXY[CurrentRoom][1][0]) / 100 + 5;
				if (CurrentImageMinX <= 0)
					CurrentImageMinX = MainImage.getImage().getWidth() - 500;
			}

			Rectangle2D viewportRect = new Rectangle2D(CurrentImageMinX, 0, 500, 380);
			MainImage.setViewport(viewportRect);

		}));
		timeline.setCycleCount(100);
		timeline.play();
            
    	
    	
    	update();
     }
    
    public void moveRight(ActionEvent event) throws InterruptedException {
    	TopPane.setCursor(Cursor.DEFAULT);
    	Messages = "You turned right!\n";
    	
    	int NextDirection = (CurrentDirection + 1) % 4;
    	user.setDirection(NextDirection);   
    	CurrentDirection = NextDirection;
    	//System.out.println(CurrentDirection);
    	//System.out.println(NextDirection);
    	if(CurrentItem != null){
    		ItemImages[CurrentItem.getId()].setVisible(false);
    	}
    	
    	CurrentItem = GetItemHere(CurrentRoom, CurrentDirection);	
    	
    	//CurrentImageMinX = RoomImageMinXY[CurrentRoom][CurrentDirection][0];
    	
    	//System.out.println(CurrentImageMinX);

            
    		timeline = new Timeline(new KeyFrame(Duration.millis(10), ev -> {

    			System.out.println(CurrentImageMinX);

    			if (CurrentImageMinX <= RoomImageMinXY[CurrentRoom][CurrentDirection][0]) {
    				CurrentImageMinX += (RoomImageMinXY[CurrentRoom][2][0] - RoomImageMinXY[CurrentRoom][1][0]) / 100 + 5;
    				if (CurrentImageMinX > RoomImageMinXY[CurrentRoom][CurrentDirection][0]) {
    					CurrentImageMinX = RoomImageMinXY[CurrentRoom][CurrentDirection][0];
    					this.timeline.stop();
    				}
    			} else {
    				CurrentImageMinX += (RoomImageMinXY[CurrentRoom][2][0] - RoomImageMinXY[CurrentRoom][1][0]) / 100 + 5;
    				if (CurrentImageMinX >= MainImage.getImage().getWidth() - 500)
    					CurrentImageMinX = 0;
    			}

    			Rectangle2D viewportRect = new Rectangle2D(CurrentImageMinX, 0, 500, 380);
    			MainImage.setViewport(viewportRect);

    		}));
    		timeline.setCycleCount(100);
    		timeline.play();
    	
        
    	update();
        
    	
    	
     }
    
    
    


	public void restart(ActionEvent event) {
        this.Initialise();
        
        TopPane.setCursor(Cursor.DEFAULT);
        update();
     }
    
    public void close(ActionEvent event) {
        System.exit(0);
     }
    
    public void openmap(ActionEvent event) {
        MapFlag = true;
        
        TopPane.setCursor(Cursor.DEFAULT);
        update(); 	    	
    	/*Image map = new Image("map.png");
        MapImage.setImage(map);
        MapImage.setVisible(true);*/
     }
    
    public void closemap(ActionEvent event) {
    	MapFlag = false;
    	
    	TopPane.setCursor(Cursor.DEFAULT);
    	update();
     }
    
    public void pickItem(ActionEvent event) {
    	
    	Image cusimage = new Image("resource/cus_s.png");
    	TopPane.setCursor(new ImageCursor(cusimage,50, 0));
    	
    	if(CurrentItem != null){
    		ItemImages[CurrentItem.getId()].setOnMouseClicked(new EventHandler<MouseEvent>() 
            {             
            	public void handle(MouseEvent me) {                                   
            		CurrentItem.setX(ItemTabs[CurrentItem.getId()][0]);
            		CurrentItem.setY(ItemTabs[CurrentItem.getId()][1]);            		
            		CurrentItem.setState(true);
            		
            		CurrentItem = null;
            		TopPane.setCursor(Cursor.DEFAULT);
            		Messages = "";
            		update();
            		
            	} 
            }
            );    		
    	}
    	
    	
    	
    	Messages = "Let's Pick!\n";
    	update();
     }
    
    public void putItem(ActionEvent event) {
    	
    	TopPane.setCursor(Cursor.DEFAULT);
    	if(CurrentItem ==null){
    		Image cusimage = new Image("resource/cus_p.png");
        	TopPane.setCursor(new ImageCursor(cusimage,50, 0));
        	BeginPut();
    	}
    	else{
    		Messages = "Here already has an item !\n";
    		update();
    	}
    	
    	
    	
     }
    
    public void update(){    	
        String MapImageName = "resource/map.png";
        Image mapimage = new Image(MapImageName);
        MapImage.setImage(mapimage);
        MapImage.setVisible(MapFlag);
        for(int i=0; i < 4; i++){
        	Marks[i].setLayoutX(ItemMarkLoc[items[i].getLocation()][items[i].getDirection()][0]);
        	Marks[i].setLayoutY(ItemMarkLoc[items[i].getLocation()][items[i].getDirection()][1]);
        	Marks[i].setVisible(MapFlag && (!items[i].getState()));
        }
        LocMark.setText(LocMarks[CurrentDirection]);
        LocMark.setLayoutX(RoomLocMarks[CurrentRoom][0]);
        LocMark.setLayoutY(RoomLocMarks[CurrentRoom][1]);
        LocMark.setVisible(MapFlag);
        //ItemImages[CurrentItem.getId()].setVisible(false);
        //CurrentItem = GetItemHere(CurrentRoom, CurrentDirection);
        if(CurrentItem != null){
        	ItemImages[CurrentItem.getId()].setLayoutX(CurrentItem.getX());
        	ItemImages[CurrentItem.getId()].setLayoutY(CurrentItem.getY());
        	ItemImages[CurrentItem.getId()].setVisible(true);
        }
        
        
        ShowTabs();        

        Message.appendText(Messages);
        
    	
    }
    
    public void ShowTabs(){
    	for( VRItem x : items){
    		if(x.getState()){
    			ItemImages[x.getId()].setLayoutX(x.getX());
            	ItemImages[x.getId()].setLayoutY(x.getY());
    			ItemImages[x.getId()].setVisible(true);
    			ItemImages[x.getId()].setOnMouseClicked(null);
    			ItemImages[x.getId()].setOnMousePressed(null);
    			ItemImages[x.getId()].setOnMouseDragged(null);
    			ItemImages[x.getId()].setOnMouseReleased(null);
    		}
    	}		    
    }
    
    public void BeginPut(){
    	for( VRItem x : items){
    		if(x.getState()){
    			//ItemImages[x.getId()].setLayoutX(x.getX());
            	//ItemImages[x.getId()].setLayoutY(x.getY());
    			//ItemImages[x.getId()].setVisible(true);
    			ItemImages[x.getId()].setOnMousePressed(new EventHandler<MouseEvent>() {
    				 public void handle(MouseEvent mouseEvent) {
    					 // record a delta distance for the drag and drop operation.
    					 draggedx = ItemImages[x.getId()].getLayoutX() - mouseEvent.getSceneX();
    					 draggedy = ItemImages[x.getId()].getLayoutY() - mouseEvent.getSceneY();
    					 }
    					});
    			ItemImages[x.getId()].setOnMouseDragged(new EventHandler<MouseEvent>() {
    				 public void handle(MouseEvent mouseEvent) {
    					 ItemImages[x.getId()].setLayoutX(mouseEvent.getSceneX() + draggedx);
    					 ItemImages[x.getId()].setLayoutY(mouseEvent.getSceneY() + draggedy);
    				 }
    				});
    			ItemImages[x.getId()].setOnMouseReleased(new EventHandler<MouseEvent>() {
    				 public void handle(MouseEvent mouseEvent) {
    					 x.setState(false);
    					 x.setX(ItemImages[x.getId()].getLayoutX());
    					 x.setY(ItemImages[x.getId()].getLayoutY());
    					 x.setLocation(CurrentRoom);
    					 x.setDirection(CurrentDirection);    					
    					 
    					 TopPane.setCursor(Cursor.DEFAULT);
    					 CurrentItem = x;
    					 
    	            	 Messages = "";
    	            	 update();
    				 }
    				});
    		}
    	}		    
    }
    
    public VRItem GetItemHere(int location, int direction){
    	for( VRItem x : items){
    		if(!x.getState()){
    			if(x.getLocation() == location){
    				if(x.getDirection() == direction)
    					return x;
    			}
    		}
    	}
    	return null;
    }
    
    public void LoadItemImage(){    	
    	for(int i=1; i <= 4; i++){
    		String ItemImageName = "resource/items/i" + String.valueOf(i) + ".png";
    		Image item = new Image(ItemImageName);
    		ItemImages[i-1].setImage(item);
    		ItemImages[i-1].setVisible(false);
    	}    	
    }
    
}
