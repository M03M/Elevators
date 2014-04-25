
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class DisplayPanel extends JPanel
{
	// Dimensions
	private final int WIDTH  = (int)(GUI.WIDTH*1);
	private final int HEIGHT = (int)(GUI.HEIGHT*0.6);
	
	// Painting variables
	private int elevH;		// Height of an elevator
	private int elevW;		// Width of an elevator
	private int floorH;		// Height of floor	
	private int [] floorY;	// Y coordinates of each floor level.
	
	/* A background image is drawn from left to right until it meets 
	 * the elevators. The separation variable determines when the image
	 * and the elevators meet */
	private int separation;
	
	public DisplayPanel()
	{
		// Modifying panel attributes
		this.setBackground(GUI.col_blue_med);
		this.setPreferredSize(new Dimension(this.WIDTH,this.HEIGHT));
		
		// Calculating floor y coordinates
		floorY = new int[Runner.getMaxFloors()];
		floorH = this.HEIGHT/floorY.length;
		for (int i = Runner.getMaxFloors()-1; i >= 0; i--)
			floorY[i] = this.HEIGHT - floorH*i;
		
		// Determining elevator dimensions
		if (Runner.getElevatorFloors().length <= 6)
			elevW = 50;
		else
			elevW = 25;
		
		elevH = (int)(floorH * 0.75);
		
		// Finding separation
		separation = this.WIDTH - (int)(1.5*elevW*Runner.getElevatorFloors().length) - (int)(0.5*elevW);
		
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		// Painting variables
		float fontSize = 20;
		
		// Painting backgrounds: right side then left
		g.setColor(GUI.col_blue_med);
		g.fillRect(0, 0, this.WIDTH, this.HEIGHT);

		g.setColor(new Color(230,230,230));
		g.fillRect(0, 0, separation, this.HEIGHT);
		
		// Painting elevator display
		paintElevatorSide(g,fontSize);
		
		// Painting floor display
		paintFloorSide(g);
	}
	
	/** Paints the right side of the display area: the elevator view area. This
	  * includes the shafts, floor levels, elevators, and minor aesthetic details.
	 */
	private void paintElevatorSide(Graphics g, float fontSize)
	{
		// Painting floors
		for (int k = 0; k < floorY.length; k++) {
			g.setColor(GUI.col_blue_light);
			g.fillRect(0, floorY[k], this.WIDTH, 10);
		}
		
		/** Painting elevator shafts */
		for (int k = Runner.getElevatorFloors().length-1; k >= 0; k--) {
			// The starting x coordinate of the elevators - left side
			int startX = this.WIDTH - (Runner.getElevatorFloors().length - k)*(int)(elevW*1.5);
			
			// Shaft itself
			g.setColor(GUI.col_blue_dark);
			g.fill3DRect(startX, 0, elevW, this.HEIGHT, false);
			
			// Shaft details
			g.setColor(GUI.col_blue_dark);
			g.setFont((GUI.font_reg_montserrat).deriveFont(fontSize));
			for (int i = 0; i < this.HEIGHT; i += 60) {
				
				// Tick marks
				g.drawLine(startX, i, (int)(startX + (elevW*0.25)), i);
				g.drawLine((int)(startX + (elevW*0.75)), i, startX+elevW, i);
				
				// Number
				g.drawString(""+k, startX + (elevW/2) - (int)(fontSize/4), i + (int)(fontSize/2));
			}
			
			// Elevators
			g.setColor(GUI.col_orange_light);
			g.fill3DRect(startX, floorY[Runner.getElevatorFloors()[k]]-elevH, elevW, elevH, true);	
		}		
	}
	
	/** Paints the left side of the display area: the floor view area. This 
	  * includes people on the floor, floor number, and arrows showing which 
	  * direction people on that floor want to go to. */
	private void paintFloorSide(Graphics g)
	{
		// Variables
		int pad = floorH/20;
		List<Floor> floorList = Runner.getFloorList();
		
		for (int k = 0; k < floorY.length; k++) {
			
			// Text on the floors
			g.setColor(new Color(200,200,200));
			
			g.setFont(GUI.font_bold_montserrat.deriveFont(20f));
			g.drawString("FLOOR " + k, pad, floorY[k] - 2*pad - 16);
			
			g.setFont(GUI.font_reg_montserrat.deriveFont(16f));
			g.drawString("PEOPLE: " + floorList.get(k).getPeople().size(), 10*pad, floorY[k] - pad);
			
			// Floors
			g.fillRect(0, floorY[k], separation, 10);
			
			// Button arrows on current floor
			paintFloorArrows(g,k);
			
			// People on the floors
			paintPeopleOnFloor(g,k,floorList.get(k).getPeople().size());
		}
	}
	
	/** On each floor, people press a button to request to go up or down. Arrows
	  * are painted to represented this. Orange when a request is placed and gray 
	  * otherwise.*/
	private void paintFloorArrows(Graphics g, int floorNum)
	{
		Color gray = new Color(200,200,200);
		Color orange = GUI.col_orange_light;
		
		// Coordinates for triangle shapes
		int [] x = {	separation - (int)(elevW*0.8), 
						separation - (int)(elevW*0.5), 
						separation - (int)(elevW*0.2)};
		int [] y_dwn = {floorY[floorNum]  - (int)(floorH*0.45),
						floorY[floorNum]  - (int)(floorH*0.2),
						floorY[floorNum]  - (int)(floorH*0.45)};
		int [] y_up =  {floorY[floorNum]  - (int)(floorH*0.55),
						floorY[floorNum]  - (int)(floorH*0.8),
						floorY[floorNum]  - (int)(floorH*0.55)};

		// Changing color depending on request, then draws arrow.
		g.setColor((Runner.getFloorRequests()[floorNum][0]) ? orange : gray);
		g.fillPolygon(x, y_up, 3);
		
		g.setColor((Runner.getFloorRequests()[floorNum][1]) ? orange : gray);
		g.fillPolygon(x, y_dwn, 3);		
	}
	
	// Draws circles representing the people waiting on a given floor.
	private void paintPeopleOnFloor(Graphics g, int floorNum, int pplNum)
	{
		g.setColor(GUI.col_orange_light);
		
		// Variables
		int rad = (int)(floorH*0.25);
		int pad = (int)(floorH*0.05);
		
		for (int k = 0; k < pplNum; k++)
			g.fillOval(separation - (k+4)*rad, 
						floorY[floorNum] - pad - ((k%2 == 0) ? rad : 2*rad), rad, rad);
		
	}
}