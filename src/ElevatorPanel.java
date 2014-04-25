
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/** The ElevatorPanel is north on the GUI. It contains a list of sub panels that 
  * displays information of each elevator (floor requests and direction). These
  * sub panels and their images inside will be dynamically placed based on the 
  * number of elevators and the number of floors respectively. */
@SuppressWarnings("serial")
public class ElevatorPanel extends JPanel
{
	// Dimensions
	private final int WIDTH  = (int)(GUI.WIDTH*0.75);
	private final int HEIGHT = (int)(GUI.HEIGHT*0.2);
	
	// Elevator Sub Panels
	private ElevatorSubPanel [] panels;
	
	private final int PAD_H = 20;
	
	public ElevatorPanel(List<Elevator> elevators)
	{
		// Setting panel properties
		this.setBackground(new Color(35,50,65));
		this.setPreferredSize(new Dimension(this.WIDTH,this.HEIGHT));
		
		// Setting layout
		GridLayout lyt = new GridLayout(1, elevators.size());
		lyt.setHgap(PAD_H);
		this.setLayout(lyt);
		
		// Creating sub panels and adding them to layout
		panels = new ElevatorSubPanel[elevators.size()];
		for (int k = 0; k < panels.length; k++) {
			panels[k] = new ElevatorSubPanel(elevators.get(k));
			add(panels[k]);
		}
	}
	
	public void update(char c)
	{
		for (ElevatorSubPanel e : panels)
			e.update();
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
/*		// Space between panels
		int pad = 5;
		
		// Sub panel width, same for all panels
		int subW = panels[0].getWidth();
		
		// Background color
		Color col_bg = new Color(34,47,64);
		
		// Setting up background 
		g.setColor(col_bg);
		g.fillRect(0, 0, panels[0].getWidth(), HEIGHT);
		
		g.setColor(new Color(250, 190, 70));
		g.fillRoundRect(pad, pad, subW - (pad*2), HEIGHT - (pad*2), 25, 25);
		
		// Drawing up and down arrows
		
		
		// Invoking
		for (int k = 0; k < panels.length; k++)
			panels[k].repaint();
*/	}

	/** An elevator subpanel utilizes the grid layout to position images. Each
	 *  image is created as a button, their image updates upon repaint. This 
	 *  way formatting is most accurate and adding functionality in the future 
	 *  is already prepared. */
	private class ElevatorSubPanel extends JPanel
	{
		private Elevator elevator;
		
		// Buttons that represent elevator status
		private JToggleButton [] btn_floors;
		private JToggleButton btn_up, btn_down;
		
		public ElevatorSubPanel(Elevator elevator)
		{
			this.elevator = elevator;
			
			setBackground(GUI.col_orange_light);
			
			// Creating components
			JLabel lbl_elevNum = new JLabel("ELEVATOR #X");
			lbl_elevNum.setBounds(100, 0, (int)(lbl_elevNum.getHeight()*2), lbl_elevNum.getHeight());
			//lbl_elevNum.setPreferredSize(new Dimension(this.getWidth()-(PAD_H*3), 30));
			
			JPanel pnl_floorButtons = new JPanel();
			pnl_floorButtons.setLayout(calculateDimensions());
			
			btn_up = new JToggleButton("UP");
			btn_down = new JToggleButton("DOWN");
			
			// Filling array and adding buttons to layout 
			btn_floors = new JToggleButton[Runner.getMaxFloors()];
			for (int k = 0; k < btn_floors.length; k++) {
				
				// Creating button
				btn_floors[k] = new JToggleButton(""+k);
				
				// Assigning images
				//btn_floors[k].setSelectedIcon(new ImageIcon("res/btn_rect_on.png"));
				//btn_floors[k].setIcon(new ImageIcon("res/btn_rect_off.png"));
				
				// Adding to layout
				pnl_floorButtons.add(btn_floors[k]);
			}
			
			// Adding components
			add(lbl_elevNum, BorderLayout.PAGE_START);
			add(btn_up, BorderLayout.LINE_START);
			add(btn_down, BorderLayout.LINE_END);
			add(pnl_floorButtons, BorderLayout.PAGE_END);
			
		}
		
		/* Calculating the appropriate dimensions for laying out floor 
		 * buttons. Desired dimensions are rows of 6. After dimensions 
		 * have been found, sets grid to said dimensions. */
		private GridLayout calculateDimensions()
		{
			int numCol = 1;
			int numRow = 1; 
			
			while ((Runner.getMaxFloors() > numCol*numRow) || (numCol > 6)) {
				if (Runner.getMaxFloors() > numCol*numRow)
					numCol++;
				if (numCol > 6) {
					numRow++;
					numCol = 2;
				}
			}
			
			// Dimensions found, assigning them to grid layout.
			return new GridLayout(numRow, numCol);
		}
		
		// Updates status of elevator
		public void update()
		{
			btn_up.setSelected(elevator.getDirection() == 1);
			btn_down.setSelected(elevator.getDirection() == -1);
			
			for (int k = 0; k < Runner.getMaxFloors(); k++)
				btn_floors[k].setSelected(elevator.getFloorRequests().contains(k));
			//System.out.println(elevator.getFloorRequests());
		}
		
		public void paint(Graphics g)
		{
			super.paint(g);
		}
	}
}
