import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class StatsPanel extends JPanel
{
	
	public StatsPanel()
	{
		// Setting panel properties
		this.setBackground(GUI.col_blue_dark);
		this.setPreferredSize(new Dimension(600,150));
		
		JTextArea jtxtarea = new JTextArea();
		jtxtarea.setFont(GUI.font_reg_montserrat.deriveFont(12f));
		jtxtarea.setBackground(GUI.col_blue_med);
		jtxtarea.setForeground(GUI.col_orange_light);
		
		jtxtarea.append("Statistics Area");
		
		this.add(jtxtarea, BorderLayout.CENTER);
	}
}
