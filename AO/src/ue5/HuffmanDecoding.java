package ue5;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class HuffmanDecoding {

	
	private BitInputStream bitInput;
	private Node huffmanTree;
	private String inputContent;
	
	public HuffmanDecoding(){}
	
	
	public void decodeFile(String inputPath) throws FileNotFoundException{
		
		InputStream input = new FileInputStream(inputPath);
		bitInput = new BitInputStream(input);
		
		huffmanTree = new Node();
		inputContent ="";
		buildHuffmanTree(huffmanTree);
		System.out.println("converted:\n" +inputContent);
		int a = 0;
		int c = a;	
	}
			
	private Node buildHuffmanTree(Node inputNode){
		
		while(bitInput.available() > 0){
						
			int bit = bitInput.readBit();
			inputContent +=""+bit;
			
			if(bit == 1){
				Node lowestNode = new Node();				
				String character = "";
	
				String bitString = "";
				for(int i = 0; i < 8; i++){
					
					bitString += bitInput.readBit();
				}
				byte readByte = Byte.parseByte(bitString, 2);
				
				char c = (char) readByte;
				
	//			bitInput.read();
	//			char c = (char) bitInput.read();
//				char c = (char) bitInput.digits;
//				character = character.valueOf(c);
				lowestNode.setCharacters("" + c);
				inputContent += c;
				return lowestNode;
			}
			else{
				Node leftChild = buildHuffmanTree(inputNode);
				Node rightChild = buildHuffmanTree(inputNode);
				//Node parent = new Node();
				
				inputNode.setLeftNode(leftChild);
				inputNode.setRightNode(rightChild);
				return inputNode;
			}			
		}				
		return inputNode;
	}
}
