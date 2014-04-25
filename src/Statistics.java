import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.LinkedList;


public class Statistics {
	private static int width = 250;
	private static int height = 150;
	private static BufferedImage serviceTime = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	private static LinkedList<Integer> avgTimes = new LinkedList<Integer>();
	private static Font f = GUI.font_reg_montserrat.deriveFont(5);
	
	public static void tick() {
		updateServiceTime();
	}
	
	public static void updateServiceTime() {
		serviceTime = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) serviceTime.getGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		int newTime = (int) Math.round((double) Runner.getTotalTime() / Runner.getTotalPeople());
		System.out.println(newTime);
		if (Runner.getTotalPeople() == 0) {
			avgTimes.add(-1);
		} else {
			avgTimes.add(newTime);
		}
		
		if (avgTimes.size() > width)
			avgTimes.removeFirst();
		
		g.setColor(GUI.col_orange_light);
		
		g.setFont(f);
		g.drawString("Average Service Time", 45, 12);
		
		double scale = (double) (height - 15) / longestTime();
		int count = 0;
		int shift = width - avgTimes.size();
		for (Integer i : avgTimes) {
			if (count < avgTimes.size() - 1) {
				int nextI = avgTimes.get(count + 1);
				if (i != -1 && nextI != -1) {
					double point = scale * i;
					double nextPoint = scale * nextI;
					g.drawLine(count + shift, height - (int) Math.floor(point), count + 1 + shift, height - (int) Math.floor(nextPoint));
					//g.fillRect(count + shift, (int) Math.floor(point), 1, 1);
				}
			}
			count++;
		}
		
		g.drawLine(0, 0, 0, 150);
	}
	
	public static int longestTime() {
		int maxTime = 0;
		for (Integer i : avgTimes) {
			maxTime = Math.max(maxTime, i);
		}
		
		return maxTime + (int) Math.pow(maxTime, 0.75);
	}
	
	public static Image getServiceGraph() {
		return serviceTime;
	}
}
