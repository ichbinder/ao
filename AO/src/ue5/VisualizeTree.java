package ue5;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class VisualizeTree {

//private JFrame frame = new JFrame();
	
	public VisualizeTree(Node startNode, int width, int height, int spaceX, int spaceY, String fileName) throws IOException {
		
		// TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
		// into integer pixels
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D ig2 = bi.createGraphics();		
//		paintNode(ig2, startNode, (int) (width * 0.7), spaceY, spaceX, spaceY);
		paintNode(ig2, startNode, width / 2, spaceY, spaceX, spaceY);
		
		ImageIO.write(bi, "PNG", new File(fileName +".PNG"));		
	}
	
	/***/
	private void paintNode(Graphics g, Node n, int x, int y, int spaceX, int spaceY){

		g.setColor(Color.black);
		if(n.getLeftNode() != null || n.getRightNode() != null){
			g.drawLine(x, y,x , y+spaceY);//Vertikal
			y = y+spaceY;	
			y = y+spaceY / 2;
			String sValue = Long.toString(n.getValue());
			g.drawString(sValue, x, y);
			y = y+spaceY / 2;		
			g.drawString(n.getCharacters(), x - n.getCharacters().length() / 2, y);
			g.drawString("0", x-spaceX, y);
			g.drawString("1", x+spaceX, y);		
			
			g.drawLine(x-spaceX, y, x+spaceX, y); //Horizontal				
			paintNode(g, n.getLeftNode(), x-spaceX, y, spaceX/2, spaceY ); //Links
			paintNode(g, n.getRightNode(), x+spaceX, y, spaceX /2, spaceY); //Rechts
//			paintNode(g, n.getLeftNode(), x-spaceX, y, (int)(spaceX * 0.7), spaceY ); //Links
//			paintNode(g, n.getRightNode(), x+spaceX, y, (int) (spaceX * 0.7), spaceY); //Rechts			
		}
		else{				
			
			int ic = (int) n.getCharacters().charAt(0);
			
			if(ic == 10){					
				g.drawString("\\n", x, y+spaceY);
			}
			else if(n.getCharacters().charAt(0) == ' '){
				g.drawString("⎵", x, y+ spaceY);
			}
			else{
				g.drawString(n.getCharacters(), x, y+spaceY);
			}
		}
	}
	
	class TreePanel extends JPanel {

		private  Node treeNodes;
		private int width, height;
		private int spaceX, spaceY;		
		private int longestChain = 0;
		
		public int getWidth(){
			
			return width;
		}
		
		public int getHeight(){
			
			return height;
		}
		
		
		
		public TreePanel(Node nodes,  int w, int h, int sX, int sY){
			
			treeNodes = nodes;
			findLongestChain(nodes);
			int n = permute(longestChain);

			width = w;
			height = h;
			
			spaceX = sX;
			spaceY = sY;
		}
		
		private int permute(int n){
			
			int output = 1;
			for(int i = 1; i <= n;i++){
				output *= i;
			}
			return output;
		}
		
		private void findLongestChain(Node n){
			
			if(n.getLeftNode() != null || n.getRightNode() != null){
				findLongestChain(n.getLeftNode());
				findLongestChain(n.getRightNode());
			}
			else{				
				if(longestChain < n.getPathAsString().length()) longestChain = n.getPathAsString().length();
			}
		}
				
		@Override
		public void paint (Graphics graphics) {
			
//			findLongetChain(treeNodes);
			paintNode(graphics, treeNodes, width / 2, spaceY, spaceX, spaceY);
		}		
		
		private void paintNode(Graphics g, Node n, int x, int y, int spaceX, int spaceY){

			if(n.getLeftNode() != null || n.getRightNode() != null){
				g.drawLine(x, y,x , y+spaceY);//Vertikal
				y = y+spaceY;
				g.drawString("0", x-spaceX, y);
				g.drawString("1", x+spaceX, y);
				g.drawLine(x-spaceX, y, x+spaceX, y); //Horizontal				
				paintNode(g, n.getLeftNode(), x-spaceX, y, spaceX/2, spaceY ); //Links
				paintNode(g, n.getRightNode(), x+spaceX, y, spaceX /2, spaceY); //Rechts
				
			}
			else{
				
				int ic = (int) n.getCharacters().charAt(0);
				
				if(n.getCharacters().charAt(0) == '\n'){					
					g.drawString("\n", x, y+spaceY);
				}
				else if(n.getCharacters().charAt(0) == ' '){
					g.drawString("⎵", x, y+ spaceY);
				}
				else{
					g.drawString(n.getCharacters(), x, y+spaceY);
				}
//				g.drawString(n.getPathAsString(), x - spaceX, y+ 2* spaceY);
			}
		}
	}	
}
