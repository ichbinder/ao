package ue5;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class HuffmanDecoding {

	
	private BitInputStream bitInput;
	private InputStream inputStream;
	private Node huffmanTree;
	private String inputContent;
	private int treeLength;	
	private int bitCounter;	
	private FileWriter fileWriter; 
	
	public HuffmanDecoding(){}
	
	
	public int getTreeLength(String inputPath) throws IOException{
				
		InputStream inputSize = new FileInputStream(inputPath);
		inputStream = new BufferedInputStream(inputSize);
				
		byte [] treeSize = new byte[4];
		inputStream.read(treeSize);
		int length = byteArrayToInt(treeSize);
		
		inputStream.close();
		inputSize.close();
		return length;
	}
	
	public void decodeFile(String inputPath) throws IOException{
		
		treeLength = getTreeLength(inputPath);
		
		InputStream input = new FileInputStream(inputPath);
		bitInput = new BitInputStream(input);

//		System.out.println("BITS");
		for(int i = 0; i < 32; i++){
			int a = bitInput.readBit();
//			System.out.print(a);
		}
//		System.out.println();
		
		huffmanTree = new Node();
		inputContent ="";
		bitCounter = 0;
		buildHuffmanTree(huffmanTree, 0);
		System.out.println("bitCounter: " +bitCounter);		
		
		System.out.println("converted:\n" +inputContent);
//		VisualizeTree visuTree = new VisualizeTree(huffmanTree, 2000, 400, 400, 40, "Input");	
		decodeData("src/ue5/META-INF/DecompressedTextFile.txt", huffmanTree);
	}
			
	private void decodeData(String decompressFilePath, Node node) throws IOException{
		
		fileWriter = new FileWriter(decompressFilePath);
		String decompressedLine = "";
		
		while(bitInput.available() > 0){

			String sChar = bitInput.readBit() == 0 ? getCharacter(node.getLeftNode()) : getCharacter(node.getRightNode());
			fileWriter.write(sChar);
			decompressedLine += sChar;
		}
		fileWriter.close();
		System.out.println("decompressed:\n" + decompressedLine);
	}
	
	private String getCharacter(Node n){

		String chars = "";
		
		if(n.getLeftNode() == null || n.getRightNode() == null) chars = n.getCharacters();
		else{
			int bit = bitInput.readBit();
			if(bit == 0) chars = getCharacter(n.getLeftNode());
			else chars = getCharacter(n.getRightNode());
			//chars = bitInput.readBit() == 0 ? getCharacter(n.getLeftNode()) : getCharacter(n.getRightNode());			
		}
		return chars;
	}
	
	
	private static int byteArrayToInt(byte[] b) 
	{
	    return   b[3] & 0xFF |
	            (b[2] & 0xFF) << 8 |
	            (b[1] & 0xFF) << 16 |
	            (b[0] & 0xFF) << 24;
	}
	
	private Node buildHuffmanTree(Node inputNode, long gen) throws IOException{

		String sBuildOutput = "";		
		
		while(bitInput.available() > 0 && bitCounter < treeLength){

			int bit = bitInput.readBit();
			bitCounter++;
			if(bit == 1){
				sBuildOutput += "1";
				System.out.print("1");
				Node lowestNode = new Node();
				String bitString = "";
				for(int i = 0; i < 8; i++){					
					bitString += bitInput.readBit();
				}
				bitCounter +=8;
				System.out.print(bitString);

				byte readByte = 0;
				try{
					readByte = Byte.parseByte(bitString, 2);
				}
				catch(NumberFormatException nfe){
					System.out.println();
					System.out.println(nfe.toString());
					System.out.println(bitString);
					System.exit(-1);
				}
				
				char c = (char) readByte;
//				System.out.print(c);
				lowestNode.setCharacters("" + c);
				
				inputContent += c;
				lowestNode.setGeneration(gen);
				return lowestNode;
			}
			else{
				System.out.print("0");
				inputNode.setGeneration(gen);
				Node leftChild = buildHuffmanTree(new Node(), gen+1);
				Node rightChild = buildHuffmanTree(new Node(), gen+1);
				//Node parent = new Node();
				inputNode.setLeftNode(leftChild);
				inputNode.setRightNode(rightChild);
				return inputNode;
			}
		}				
		return inputNode;
	}
	
	
	public void ReadFileContent(BitInputStream bitStream, int max) throws IOException{

		int dataSize = 0;
		System.out.println("WHOLE FILE");
		while(bitStream.available() > 0){
			System.out.print(bitStream.readBit());		
			dataSize++;
			if(dataSize == max) break;
		}
		System.out.println();
		System.out.println("dataSize:"+ dataSize);
	}
}
