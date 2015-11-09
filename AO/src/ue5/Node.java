package ue5;

import java.util.ArrayList;

public class Node {

	private Node left, right;
	private long value;
	private String Characters;
//	private short binaryPath;
	private ArrayList<Short> binaryPath;
	
	private long generation;
	
	public Node(){
		
		Characters = "";
	}
	
	public Node(String s, long freq){

		value = freq;		
		Characters = s;
		binaryPath = new ArrayList<Short>();
	}
	
	public void setPath(ArrayList<Short> list){
		for(int i = 0; i < list.size(); i++){
			binaryPath.add(list.get(i));
		}
	}
	
	public void setPath(short i){
		
		binaryPath.add(i);
	}
	public ArrayList<Short> getPath(){		
		return binaryPath;
	}
	
	public String getCharacters(){
		
		return Characters;
	}

	public void setCharacters(String chars){
		
		Characters += chars;
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
	
	public String getPathAsString(){
		String output = "";
		for(int i = 0; i < binaryPath.size(); i++){
			
			output += binaryPath.get(i).toString();
		}
		return output;
	}
	
	public long getGeneration(){
		
		return generation;
	}
	public void setGeneration(long l){
		generation = l;
	}
	
	public int getLength(){
		
		int length = 0; 
		if(left != null || right != null){
			length = this.left.getLength() + this.right.getLength();
		}
		else{
			length = this.getPathAsString().length() +8;
		}
		return length;
	}
}
