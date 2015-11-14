package Ue4_sortieren;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Graph {

	private final int width=500, height=400, margin_top=60, margin_right=300,
			margin_bottom=20, margin_left=40;
	private final int y=2, x=3;
	private Vector<float[][]> graphs = new Vector<float[][]>();
	private Vector<String> titles = new Vector<String>();
	private Color[] colors = {Color.BLACK, Color.BLUE, Color.GREEN, Color.ORANGE, Color.CYAN};
	private JFrame frame = new JFrame();
	
	public Graph() {
		frame.setContentPane(new GraphPanel());
		
		frame.setTitle("Sorting-Evaluation-Graph");
		frame.setSize(width+margin_left+margin_right, height+margin_top+margin_bottom);
		frame.setLocation(500, 0);
		frame.setVisible(false);
	}
	
	public void addGraph(float[][] pUeberR, String title) {
		graphs.addElement(pUeberR);
		titles.add(title);
		frame.setVisible(true);
		frame.toFront();
		frame.requestFocus();
		frame.repaint();
	}
	
	public boolean isClosed() {
		return frame.isVisible();
	}
	
	class GraphPanel extends JPanel {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void paint (Graphics graphics) {
			paintCoordinates(graphics);
			
			for (int i=0; i<graphs.size(); i++) {
				Color c;
				if (i>= 5) {
					int r = (i*200) % 256;
					int g = (i*95) % 256;
					int b = (i*35) % 256;
					c = new Color(r, g, b);
				}
				else 
					c = colors[i];
				paintGraphs(graphs.elementAt(i), c, graphics, i, titles.elementAt(i));
			}
		}
		
		public void paintGraphs(float[][] pUeberR, Color c, Graphics g, int z, String titel){
			int x1, x2, y1, y2;
			g.setColor(c);
			g.drawString(titel, margin_left+width+10, margin_top+z*15);
			for (int i = 1; i < pUeberR[x].length; i++) {

				x1 = margin_left + (int)(pUeberR[x][i-1]*width+0.5);
				x2 = margin_left + (int)(pUeberR[x][i]*width+0.5);
				
				y1 = height + margin_top - (int)(pUeberR[y][i-1]*height+0.5);
				y2 = height + margin_top - (int)(pUeberR[y][i]*height+0.5);

				g.drawLine(x1, y1, x2, y2);
				g.setColor(Color.RED);
				g.fillRect(x1-1, y1-1, 3, 3);
				g.setColor(c);
			}

		}

		private void paintCoordinates(Graphics g) {			
			g.setColor(Color.white);
			g.fillRect(0, 0, width+margin_left+margin_right, height+margin_bottom+margin_top);
			g.setColor(Color.black);
			g.drawLine(margin_left, margin_top, margin_left, height+margin_top);
			g.drawLine(margin_left, height+margin_top, width+margin_left, height+margin_top);
			g.drawString("Zeit", 10, margin_top+10);
			g.drawString("Anzahl", margin_left+width-15, margin_top+height+12);
			
			int hl = 10; // 4 -> Hilfslinie alle viertel
			for(int z=0; z<=hl; z++) {
				//Hilfslinien zeichnen
				//vertikal
				g.drawLine(margin_left+z*width/hl, margin_top, margin_left+z*width/hl, height+margin_top);
				//horizontal
				g.drawLine(margin_left, margin_top+z*height/hl, width+margin_left, margin_top+z*height/hl);
			}
		}
	}
}

