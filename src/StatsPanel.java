import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class StatsPanel extends JPanel
{
	
	public StatsPanel()
	{
		// Setting panel properties
		this.setBackground(GUI.col_blue_dark);
		this.setPreferredSize(new Dimension(800,150));
		
		JTextArea jtxtarea = new JTextArea();
		jtxtarea.setFont(GUI.font_reg_montserrat.deriveFont(12f));
		jtxtarea.setBackground(GUI.col_blue_med);
		jtxtarea.setForeground(GUI.col_orange_light);
		
		jtxtarea.append("Statistics Area");
		
		//this.add(jtxtarea, BorderLayout.LINE_END);
		
		/*
		
		ImageIcon img = new ImageIcon(Statistics.getServiceGraph());

		this.add(img.getImage(), BorderLayout.LINE_START);
		*/
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(GUI.col_blue_dark);
		g.fillRect(0, 0, 800, 150);
		g.drawImage(Statistics.getServiceGraph(), 550, 0, null);
	}
}
