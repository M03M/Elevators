
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI extends JFrame implements Runnable
{
	// Dimensions
	public static final int HEIGHT = 600;
	public static final int WIDTH = 800;
	
	// Fonts
	public static Font font_bold_montserrat;
	public static Font font_reg_montserrat;
	
	// Handles the logic and data of application
	public Runner runner;
	
	// JPanels that compose the GUI
	private JPanel pnl_elevators, pnl_display, pnl_stats;
	
	// Colors
	public static final Color col_blue_dark = new Color(30,40,50);
	public static final Color col_blue_med = new Color(35,50,65);
	public static final Color col_blue_light = new Color(66,88,115);
	public static final Color col_orange_dark = new Color(210,130,10);
	public static final Color col_orange_light = new Color(250,190,70);
	
	public GUI(Runner runner)
	{
		this.runner = runner;
		
		// Setting JFrame properties
		this.setSize(WIDTH, HEIGHT);
		this.setName("Elevators");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		// Loading fonts
		loadFonts();
		
		// Creating panels
		JPanel pnl_main = new JPanel();
		pnl_main.setSize(WIDTH, HEIGHT);
		
		pnl_elevators = new ElevatorPanel(runner.getElevatorList());
		
		pnl_display = new DisplayPanel();
		
		pnl_stats = new StatsPanel();
		
		// JPanel pnl_ui = new InputPanel();
		
		// Creating layout
		pnl_main.setLayout(new BorderLayout());
		
		/* Adding panels to frame */
		
		//pnl_main.add(pnl_ui, BorderLayout.LINE_END);
		
		// Temporary sub panel for holding other components
		JPanel pnl_left = new JPanel();
		pnl_left.setLayout(new BorderLayout());
		pnl_left.add(pnl_elevators, BorderLayout.PAGE_START);
		pnl_left.add(pnl_display, BorderLayout.CENTER);
		pnl_left.add(pnl_stats, BorderLayout.PAGE_END);
		
		// Add sub panel
		pnl_main.add(pnl_left);
		
		// Displaying JFrame
		this.getContentPane().add(pnl_main);
		this.pack();
		this.setVisible(true);
	}
	
	/** Loads fonts that will be used in the program, accessible by all. */
	private void loadFonts()
	{
		try {
			
			font_bold_montserrat = (Font.createFont(Font.TRUETYPE_FONT, 
					new File("res/Montserrat-Bold.ttf")))
					.deriveFont(16f);
			
			font_reg_montserrat = (Font.createFont(Font.TRUETYPE_FONT, 
					new File("res/Montserrat-Regular.ttf")))
					.deriveFont(16f);
			
		} catch (FontFormatException e) {	e.printStackTrace();	} 
		  catch (IOException e) 		{	e.printStackTrace();	}
	}

	@Override
	public void run()
	{
		while (true) {
			runner.tick();
			Statistics.tick();
			((ElevatorPanel)pnl_elevators).update('t');
			
			pnl_elevators.repaint();
			pnl_display.repaint();
			pnl_stats.repaint();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	/** Creates a dialog box prompting the user for parameters (# of elevators 
	 * and/or # of floors). */
	public static void main(String[]args)
	{
		// Initializing dialog box
		final JDialog jdbox = new JDialog();
		
		// Setting attributes
		jdbox.setName("Elevators!");
		jdbox.setSize(200, 100);
		jdbox.setLayout(new GridLayout(3,2));
		
		// Creating components
		final JTextField jtxtfld_elevators = new JTextField(2);
		final JTextField jtxtfld_floors = new JTextField(2);
		
		final JLabel lbl_elevators = new JLabel("# Elevators: ");
		final JLabel lbl_floors = new JLabel("# Floors: ");
		
		JButton btn_start = new JButton("Start!");
		JButton btn_random = new JButton("Random");
		
		// Attaching action listeners to buttons.
		btn_start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					Runner runner = new Runner();
					runner.setNumFloors(Integer.parseInt(jtxtfld_floors.getText()));
					runner.setNumElevators(Integer.parseInt(jtxtfld_elevators.getText()));
					
					GUI test = new GUI(runner);
					
					// Hiding input box
					jdbox.setVisible(false);
					
					(new Thread(test)).start();
				} catch (NumberFormatException ex) {}
			}
		});
		
		btn_random.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					int randNumFloors = (int)(Math.random()*7) + 3;
					int randNumElevators = (int)(Math.random()*3) + 1;
					
					Runner runner = new Runner();
					runner.setNumElevators(randNumElevators);
					runner.setNumFloors(randNumFloors);
					
					GUI test = new GUI(runner);
					
					// Hiding input box
					jdbox.setVisible(false);
					
					(new Thread(test)).start();
				} catch (NumberFormatException ex) {}
			}
		});
		
		// Adding components
		jdbox.getContentPane().add(lbl_elevators);
		jdbox.getContentPane().add(jtxtfld_elevators);
		jdbox.getContentPane().add(lbl_floors);
		jdbox.getContentPane().add(jtxtfld_floors);
		jdbox.getContentPane().add(btn_start);		
		jdbox.getContentPane().add(btn_random);
		
		// Now that everything has been added, displaying dialog box
		jdbox.setVisible(true);
	}
}