
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

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
	
	// Applicatoin logic
	private Runner runner;
	
	public DisplayPanel(Runner runner)
	{
		// Modifying panel attributes
		this.setBackground(new Color(255,0,0));
		this.setPreferredSize(new Dimension(this.WIDTH,this.HEIGHT));
		
		// Calculating floor y coordinates
		floorY = new int[Runner.getMaxFloors()];
		floorH = this.HEIGHT/floorY.length;
		for (int i = Runner.getMaxFloors()-1; i >= 0; i--)
			floorY[i] = this.HEIGHT - floorH*i;
		
		// Determining elevator dimensions
		if (runner.getElevatorList().size() <= 6)
			elevW = 50;
		else
			elevW = 25;
		
		elevH = (int)(floorH * 0.75);
		
		// Finding separation
		separation = this.WIDTH - (int)(1.5*elevW*runner.getElevatorList().size()) - (int)(0.5*elevW);
		
		this.runner = runner;
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		// Painting variables
		float fontSize = 20;
		
		// Painting backgrounds
		g.setColor(new Color(35,50,65));
		g.fillRect(0, 0, this.WIDTH, this.HEIGHT);
		
		// Painting floors
		for (int k = 0; k < floorY.length; k++) {
			g.setColor(new Color(66,88,115));
			g.fillRect(0, floorY[k], this.WIDTH, 10);
		}
		
		/** Painting elevator shafts */
		for (int k = runner.getElevatorList().size()-1; k >= 0; k--) {
			// The starting x coordinate of the elevators - left side
			int startX = this.WIDTH - (runner.getElevatorList().size() - k)*(int)(elevW*1.5);
			
			// Shaft itself
			g.setColor(new Color(30,40,50));
			g.fill3DRect(startX, 0, elevW, this.HEIGHT, false);
			
			// Shaft details
			g.setColor(new Color(30,40,50));
			g.setFont((GUI.font_reg_montserrat).deriveFont(fontSize));
			for (int i = 0; i < this.HEIGHT; i += 60) {
				
				// Tick marks
				g.drawLine(startX, i, (int)(startX + (elevW*0.25)), i);
				g.drawLine((int)(startX + (elevW*0.75)), i, startX+elevW, i);
				
				// Number
				g.drawString(""+k, startX + (elevW/2) - (int)(fontSize/4), i + (int)(fontSize/2));
			}
			
			// Elevators
			g.setColor(new Color(250,190,70));
			g.fill3DRect(startX, floorY[Runner.getElevatorFloors()[k]]-elevH, elevW, elevH, true);	
		}
		
		/** Painting office background on remaining space. */
		
		// Walls
		g.setColor(new Color(230,230,230));
		g.fillRect(0, 0, separation, this.HEIGHT);
		
		// Floors
		g.setColor(new Color(200,200,200));
		g.setFont(GUI.font_bold_montserrat.deriveFont(20f));
		for (int k = 0; k < floorY.length; k++) {
			g.fillRect(0, floorY[k], separation, 10);
			g.drawString("FLOOR " + k, this.WIDTH/10, floorY[k]-5);
		}
	}
}