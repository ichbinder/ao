package ue5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
	private Map<Character, Node> lookupTable;
	private BitOutputStream bitOutput;
	private String outputContent = "";
	private int treeLength = 0;
	
	String treeSize = "";
	String treeContent = "";
	String fileContent = "";
	String treeBits = "";
	
	public HuffmanEncoding(){
		
		frequencies = new HashMap<Character, Long>();
		nodeList = new ArrayList<Node>();
		lookupTable = new HashMap<Character, Node>();
	}
	
	public void EncodeFile(String inputPath, String outputPath) throws IOException{

		//Lies Datei und Zähle Aufkommen der Characters						
		FileInputStream fr = new FileInputStream(inputPath);
		long size = fr.getChannel().size();
		fr.close();
		
		BufferedReader br = new BufferedReader(new FileReader(inputPath));
		readFile(br, size);
		//Baue Huffmann-Tree
		System.out.println("Build HuffmanTree");

		buildHuffmanTree();
		//Baue Lookup-Tabelle

		System.out.println("DONE");
	//	VisualizeTree visuTree = new VisualizeTree(huffmanTree, 2000, 400, 400, 40, "Output");			
//		VisualizeTree visuTree = new VisualizeTree(huffmanTree, 200000, 1000, 10000, 40, "Output");			

//		System.exit(0);
		
		OutputStream output = new FileOutputStream(outputPath +".txt");
		bitOutput = new BitOutputStream(output);

		//Init LookupTable
		System.out.println("Build LookupTable");

		buildLookupTable();	
		
		
		
		printLookupTable();
		
		System.out.println("DONE");
		
		treeBits = outputContent;
		treeContent = treeContent.replace("\r ", "\\r");
		treeContent = treeContent.replace("\n ", "\\n");
		
		
		outputContent = "";
		System.out.println("Write Compressionfile");

		exportCompression(inputPath);
		System.out.println("DONE");
		
		fileContent = outputContent;
		output.close();
		bitOutput.close();

		System.out.println("Add TreeSize");
		addTreeSize(outputPath);		
		System.out.println("DONE");			
	}
	
	private void addTreeSize(String outputPath) throws IOException{
		
		FileInputStream fis = new FileInputStream(new File(outputPath+ ".txt"));				
		
		File file = new File(outputPath + ".txt");
		
		String tempSize = Integer.toBinaryString(treeLength);	

		int size = 32-tempSize.length();
		for(int i = 0; i < size; i++) tempSize = 0+ tempSize;
		
		byte[] bTreeSize = new BigInteger(tempSize, 2).toByteArray();	

		byte[] writtenBytes = new byte[4];
		for(int i = 0; i < 4; i++){
			
			if(i >= bTreeSize.length) writtenBytes[i] = bTreeSize[i - bTreeSize.length];
		}
		treeSize = tempSize;

		byte [] content = new byte[(int) file.length() +4];

		content[0] = writtenBytes[0];
		content[1] = writtenBytes[1];
		content[2] = writtenBytes[2];
		content[3] = writtenBytes[3];
		
		fis.read(content, 4, content.length -4);
		fis.close();
		file.delete();
		
		FileOutputStream fos = new FileOutputStream(new File(outputPath+ ".txt"));		
		fos.write(content);
		fos.close();		
		
	}

	int dataSize = 0;
	int iLines = 0;
	
	private void printLookupTable(){
		
		for (Entry<Character, Node> entry : lookupTable.entrySet()) {  // Itrate through hashmap

			if(entry.getKey() == '\r') System.out.print("\\r");
			else if(entry.getKey() == '\n') System.out.print("\\n");
			else System.out.print(entry.getKey());
			System.out.println(":" + entry.getValue().getBitString());
        }
	}
	private void exportCompression(String inputPath) throws IOException{		

		FileInputStream fr = new FileInputStream(inputPath);
		long size = fr.getChannel().size();
		fr.close();
		
		BufferedReader brInput = new BufferedReader(new FileReader(inputPath));
		int length = 1024;
		char chars []  = new char [1024];
		
		while(brInput.ready()){

			brInput.read(chars, 0, length);
			for(int i = 0; i < length; i++){

				byte charInByte = (byte) chars[i];
				if(charInByte != -1){
					String bits = lookupTable.get(chars[i]).getBitString();
					bitOutput.writeBits(bits);
				}
			}
			size -= length;
			if(length > size){
				length = (int) size;
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
		if(n.getLeftNode() == null || n.getRightNode() == null) {
			
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
			if(n.getCharacters().charAt(0) == '@'){
				int a = 0;
			}
			
			treeContent +="1";
			
			treeContent += n.getCharacters();			
			treeContent += "       ";
			
//			outputContent += n.getCharacters();
//			outputContent += "|"+bitString + "|";
			outputContent += bitString;
			lookupTable.put((char) n.getCharacters().charAt(0),n);			
		}
		else{
			bitOutput.writeBit(0);
			outputContent += "0";
			treeContent +="0";
			
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
			byte charInByte = (byte) cLine[i];
			if(!frequencies.containsKey(cLine[i]) && charInByte != -1){
				frequencies.put(cLine[i], Long.valueOf(0));
				frequencies.put(cLine[i], frequencies.get(cLine[i]) + 1);
			}
		}
	}
	
	/**Liest aus einem BufferedReader die Textzeilen. In jeder Zeile wird das Auftauchen von Characters gezählt*/
	private void readFile(BufferedReader br, long size){

		try{
			int length = 1024;
			char chars []  = new char [1024];
			
			while(br.ready()){

				br.read(chars, 0, length);
				countOccurences(chars);
				
				size -= length;
				if(length > size){
					length = (int) size;
				}				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	
	public static void main(String[] args) throws IOException {
		
		HuffmanEncoding encode = new HuffmanEncoding();
		
		long sysTimeStart = System.nanoTime();
		System.out.println("Compressing");
		encode.EncodeFile("src/ue5/META-INF/Random.txt", "src/ue5/META-INF/Random_compressed");
//		System.out.println("TreeSize:" + encode.treeLength);
//		encode.EncodeFile("src/ue5/META-INF/TestTextFile.txt", "src/ue5/META-INF/CompressedTextFile");
		long sysTimeEnd = System.nanoTime();
		
		System.out.println("Done in:"+ (sysTimeEnd - sysTimeStart)/ 1000000);
		System.out.println(encode.treeBits);
		System.out.println(encode.treeContent);
		
		HuffmanDecoding decode = new HuffmanDecoding();
		
//		InputStream is = new FileInputStream("src/ue5/META-INF/CompressedTextFile.txt");
	//	InputStream is = new FileInputStream("src/ue5/META-INF/Compressed_Great_Expectations.txt");

//		BitInputStream bis = new BitInputStream(is);
		
		/*
		System.out.println("TreeSize:" + encode.treeSize);
		System.out.println("TreeContent:" + encode.treeContent);
		System.out.println("FileContent:" + encode.fileContent);
		*/
		
//		decode.ReadFileContent(bis);		
//		decode.ReadFile("src/ue5/META-INF/CompressedTextFile.txt");		
//		decode.decodeFile("src/ue5/META-INF/CompressedTextFile.txt");		
//		sysTimeStart = System.nanoTime();
		decode.decodeFile("src/ue5/META-INF/Random_compressed.txt");		
//		sysTimeEnd = System.nanoTime();
		
//		System.out.println("Done in:"+ (sysTimeEnd - sysTimeStart)/ 1000000);


	}
}
