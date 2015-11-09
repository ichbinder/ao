package ue5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HuffmanEncoding {

	private Map<Character, Long> frequencies;
	
	private Node huffmanTree; 
	private List<Node> nodeList;
	private Map<Character, ArrayList<Short>> lookupTable;
	private BitOutputStream bitOutput;
	private String outputContent = "";
	private int treeLength = 0;
	
	public HuffmanEncoding(){
		
		frequencies = new HashMap<Character, Long>();
		nodeList = new ArrayList<Node>();
		lookupTable = new HashMap<Character, ArrayList<Short>>();
	}
	
	public void EncodeFile(String inputPath, String outputPath) throws IOException{

		//Lies Datei und Zähle Aufkommen der Characters						
		BufferedReader br = new BufferedReader(new FileReader(inputPath));
		readFile(br);
		//Init LookupTable
//		initLookupTable();
		//Baue Huffmann-Tree
		buildHuffmanTree();
		//Baue Lookup-Tabelle

		VisualizeTree visuTree = new VisualizeTree(huffmanTree, 2000, 400, 400, 40, "Output");			
		
		OutputStream output = new FileOutputStream(outputPath +".txt");
		bitOutput = new BitOutputStream(output);
		
		buildLookupTable();	
		
		System.out.println("size:" + treeLength);
		System.out.println(outputContent);
		outputContent = "";
		exportCompression(inputPath);
		
		System.out.println(outputContent);
		output.close();
		bitOutput.close();

		RandomAccessFile raf = new RandomAccessFile(new File(outputPath + ".txt"), "rw");
		String tempSize = Integer.toBinaryString(289);	
		
		byte[] bTreeSize = new BigInteger(tempSize, 2).toByteArray();	

		byte[] writtenBytes = new byte[4];
		for(int i = 0; i < 4; i++){
			
			if(i >= bTreeSize.length) writtenBytes[i] = bTreeSize[i - bTreeSize.length];
		}

		byte [] content = new byte[(int) raf.length() +4];
		content[0] = writtenBytes[0];
		content[1] = writtenBytes[1];
		content[2] = writtenBytes[2];
		content[3] = writtenBytes[3];
				
		raf.readFully(content, 4, content.length -4);
		
//		raf.write(writtenBytes, 0, 4);
//		raf.getChannel().position(4);
		raf.write(content, 0, content.length);
	
		raf.close();		
	}
			
	private void exportCompression(String inputPath) throws IOException{		
				
		BufferedReader brInput = new BufferedReader(new FileReader(inputPath));

		while(brInput.ready()){
			
			char c = (char) brInput.read();
			ArrayList<Short> huffmanPath = lookupTable.get(c);
			for(int i = 0; i < huffmanPath.size(); i++){
				bitOutput.write(huffmanPath.get(i));
				outputContent+= huffmanPath.get(i);
			}
		}
		
		bitOutput.close();
		brInput.close();
	}
		
	private static byte[] intToByteArray(int a)
	{
	    return new byte[] {
	        (byte) ((a >> 24) & 0xFF),
	        (byte) ((a >> 16) & 0xFF),   
	        (byte) ((a >> 8) & 0xFF),   
	        (byte) (a & 0xFF)
	    };
	}	
	
	private void buildLookupTable() throws IOException{
	//	treeLength = huffmanTree.getLength();		
		byte [] treeSize = intToByteArray(treeLength);		
		
		System.out.println("TreeSize: "+treeLength);
//		bitOutput.write(treeSize);
		findLowestLayer(huffmanTree);		
	}
	
	private boolean isBitSet(byte b, int bit)
	{
		return (b & (1 << bit)) != 0;
	}
	
	private static int getBit(byte[] data, int pos) {
	      int posByte = pos/8; 
	      int posBit = pos%8;
	      byte valByte = data[posByte];
	      int valInt = valByte>>(8-(posBit+1)) & 0x0001;
	      return valInt;
	   }
	
	private void findLowestLayer(Node n){
		
		treeLength++;
		if(n.getLeftNode() == null && n.getRightNode() == null) {
			
			bitOutput.writeBit(1);
			
			byte [] byt = new byte [1];
			byt[0] = (byte) n.getCharacters().charAt(0);
			
			String bitString = "";
			for(int i = 0; i < 8; i++){		
				bitString += getBit(byt, i);
			}
			
			treeLength+=8;
			
			bitOutput.writeBits(bitString);
			outputContent += "1";
			outputContent += bitString;
			lookupTable.put((char) n.getCharacters().charAt(0),(ArrayList<Short>) n.getPath());			
		}
		else{
			bitOutput.writeBit(0);
			outputContent += "0";
			
			findLowestLayer(n.getLeftNode());
			findLowestLayer(n.getRightNode());
		}
	}
	
	private void buildHuffmanTree(){
	
		charactersToNodes();		
		huffmanTree = buildConnections();

		labelPaths(huffmanTree.getLeftNode(), (short ) 0);
		labelPaths(huffmanTree.getRightNode(), (short) 1);		
	}

	private void labelPaths(Node node, short binary){

		node.setPath(binary);
		
		if(node.getLeftNode() != null){
			
			labelPaths(node.getLeftNode(), node.getPath(), (short) 0 );			
		}
		if(node.getRightNode() != null){			
			labelPaths(node.getRightNode(), node.getPath(), (short) 1);			
		}
	}
		
	private void labelPaths(Node node, ArrayList<Short> arrayList, short binary){
		
		node.setPath(arrayList);
		node.setPath(binary);
		
		if(node.getLeftNode() != null){
			
			labelPaths(node.getLeftNode(), node.getPath(), (short) 0 );			
		}
		if(node.getRightNode() != null){			
			labelPaths(node.getRightNode(), node.getPath(), (short) 1);			
		}
	}
		
	private Node buildConnections(){
		
		while(nodeList.size() > 2){

			Node firstNode  = findSmallestNode();
			Node secondNode = findSmallestNode();
						
			String parentCharacters = "";
			long parentValue =  0;
			parentCharacters += secondNode.getCharacters();
			parentValue += secondNode.getValue();
			parentCharacters += firstNode.getCharacters();
			parentValue += firstNode.getValue();
			
			Node parentNode = new Node(parentCharacters, parentValue );
			parentNode.setRightNode(firstNode);
			if(secondNode != null){
				parentNode.setLeftNode(secondNode);
			}
			nodeList.add(parentNode);
		}

		Node nFirst = findSmallestNode();
		Node nSecond = findSmallestNode();
		
		String parentCharacters = "";
		long parentValue =  0;
		parentCharacters += nSecond.getCharacters();
		parentValue += nSecond.getValue();
		parentCharacters += nFirst.getCharacters();
		parentValue += nFirst.getValue();
		
		Node huffmanNode = new Node(parentCharacters, parentValue );
		huffmanNode.setRightNode(nFirst);
		huffmanNode.setLeftNode(nSecond);		
		
		return huffmanNode;
	}
	
	private Node findSmallestNode(){

		Node minimumNode;
		long minimum = 0;
		int index = 0;

		for(int j = index; j < nodeList.size(); j++){
			Node temp = nodeList.get(j);
			if(minimum == 0) minimum = temp.getValue();
			if(minimum > temp.getValue()){
				minimum = temp.getValue();
				index = j;				
			}
		}
		minimumNode = nodeList.get(index);
		nodeList.remove(index);
		return minimumNode;
	}
	
	private void charactersToNodes(){

		//Gehe Alle Buchstaben durch
		while(!frequencies.isEmpty()){
			Entry<Character, Long> entry = getMinimumEntry();
			Node node = new Node(entry.getKey().toString(), entry.getValue());
			nodeList.add(node);			
			frequencies.remove(entry.getKey());
		}
	}
	
	private Entry<Character, Long> getMinimumEntry(){
		
		long minValueInMap=(Collections.min(frequencies.values())); 
		for (Entry<Character, Long> entry : frequencies.entrySet()) {  // Itrate through hashmap
            if (entry.getValue() == minValueInMap) {  

            	frequencies.remove(entry.getKey(), entry.getValue());
            	return entry;
            }
        }
		return null;
	}
	
	/**Zählt das Vorkommen der Characters in einem String und fügt sie einer Map hinzu.
	 * @param cLine ein Character-Array (String)*/
	private void countOccurences(char [] cLine){
		
		for(int i = 0; i < cLine.length; i++){	
			if(!frequencies.containsKey(cLine[i])) frequencies.put(cLine[i], Long.valueOf(0));
			frequencies.put(cLine[i], frequencies.get(cLine[i]) + 1);
		}
	}
	
	/**Liest aus einem BufferedReader die Textzeilen. In jeder Zeile wird das Auftauchen von Characters gezählt*/
	private void readFile(BufferedReader br){

		try{
			long lineCount = 0;
			while(br.ready()){
			
				String sLine = br.readLine();
				countOccurences(sLine.toCharArray());
				if(br.ready()){
					lineCount++;
				}
			}
			if(lineCount > 0){
				frequencies.put('\n', lineCount);				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public static void main(String[] args) throws IOException {
		
		HuffmanEncoding encode = new HuffmanEncoding();
		encode.EncodeFile("src/ue5/META-INF/TestTextFile.txt", "src/ue5/META-INF/CompressedTextFile");
		
		HuffmanDecoding decode = new HuffmanDecoding();
		decode.ReadFile("src/ue5/META-INF/CompressedTextFile.txt");		
		decode.decodeFile("src/ue5/META-INF/CompressedTextFile.txt");		
	}
}
