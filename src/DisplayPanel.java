
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;


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
		
		// Painting backgrounds
		g.setColor(GUI.col_blue_med);
		g.fillRect(0, 0, this.WIDTH, this.HEIGHT);
		
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
		
		/** Painting office background on remaining space. */
		g.setFont(GUI.font_bold_montserrat.deriveFont(20f));
		
		// Walls
		g.setColor(new Color(230,230,230));
		g.fillRect(0, 0, separation, this.HEIGHT);
		
		List<Floor> floorList = Runner.getFloorList();
		for (int k = 0; k < floorY.length; k++) {
			
			// Floors
			g.setColor(new Color(200,200,200));
			g.fillRect(0, floorY[k], separation, 10);
			g.drawString("FLOOR " + k, this.WIDTH/10, floorY[k]-5);
			
			// People
			g.drawString("PEOPLE: " + floorList.get(k).getPeople().size(), this.WIDTH/10 + 100, floorY[k] - 5);
			g.setColor(GUI.col_orange_light);
			
			for (int r = 0; r < floorList.get(k).getPeople().size(); r++)
				g.fillOval(separation-12*(r+1), floorY[k]-12, 10, 10);
		}
	}
}