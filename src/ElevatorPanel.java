
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
			panels[k] = new ElevatorSubPanel(k);
			add(panels[k]);
		}
	}
	
	public void update(char c)
	{
		for (ElevatorSubPanel e : panels)
			e.update();
	}

	/** An elevator subpanel utilizes the grid layout to position images. Each
	 *  image is created as a button, their image updates upon repaint. This 
	 *  way formatting is most accurate and adding functionality in the future 
	 *  is already prepared. */
	private class ElevatorSubPanel extends JPanel
	{		
		// Buttons that represent elevator status
		private JToggleButton [] btn_floors;
		
		// Elevator number
		int elevNum;
		
		public ElevatorSubPanel(int elevNum)
		{
			// Setting attributes
			setBackground(GUI.col_orange_light);
			
			// Setting layout
			setLayout(new GridBagLayout());
			
			this.elevNum = elevNum;
			
			// Creating components
			JLabel lbl_elevNum = new JLabel("ELEVATOR #" + elevNum);
			lbl_elevNum.setFont(GUI.font_bold_montserrat);
			lbl_elevNum.setBackground(GUI.col_blue_dark);
			
			JPanel pnl_floorButtons = new JPanel();
			GridLayout lyt_buttons = calculateDimensions();
			pnl_floorButtons.setLayout(lyt_buttons);
			
			
			// Filling array and adding buttons to layout 
			btn_floors = new JToggleButton[Runner.getMaxFloors()];
			for (int k = 0; k < btn_floors.length; k++) {
				
				// Creating button
				btn_floors[k] = new JToggleButton(""+k);
				btn_floors[k].setBackground(GUI.col_orange_dark);
				btn_floors[k].setForeground(GUI.col_orange_light);
				btn_floors[k].setFont(GUI.font_reg_montserrat.deriveFont(12f));
				btn_floors[k].setEnabled(false);
				
				// Adding to layout
				pnl_floorButtons.add(btn_floors[k]);
			}
			// Adding components
			add(lbl_elevNum, new GridBagConstraints( 0, 0, lyt_buttons.getColumns(), 1, 
								0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
			add(pnl_floorButtons,  new GridBagConstraints( 0, 1, lyt_buttons.getColumns(), lyt_buttons.getRows(), 
					0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
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
			// Resets buttons
			for (int btnNum = 0; btnNum < btn_floors.length; btnNum++)
				btn_floors[btnNum].setSelected(false);
			
			List<Integer> currentLit = Runner.getLitButtons()[elevNum];
				
			for (int btnNum = 0; btnNum < btn_floors.length; btnNum++)
				btn_floors[btnNum].setSelected(currentLit.contains(btnNum));
		}
		
		public void paint(Graphics g)
		{
			super.paint(g);
		}
	}
}
