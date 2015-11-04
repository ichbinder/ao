package ue5;

import java.util.ArrayList;

public class Node {

	private Node left, right;
	private final long value;
	private final String Characters;
//	private short binaryPath;
	private ArrayList<Boolean> binaryPath;
	
	public Node(String s, long freq){

		value = freq;		
		Characters = s;
		binaryPath = new ArrayList<Boolean>();
	}
	
	public void setPath(ArrayList<Boolean> list){
		for(int i = 0; i < list.size(); i++){
			binaryPath.add(list.get(i));
		}
	}
	
	public void setPath(boolean b){
		
		binaryPath.add(b);
	}
	public ArrayList<Boolean> getPath(){		
		return binaryPath;
	}
	
	public String getCharacters(){
		
		return Characters;
	}
		
	public long getValue(){
		return value;
	}
	
	public void setLeftNode(Node leftN){
		
		left = leftN;
	}
	
	public Node getLeftNode(){
		
		return left;
	}

	public void setRightNode(Node rightN){
		
		right = rightN;
	}

	public Node getRightNode(){
		return right;
	}	
}
