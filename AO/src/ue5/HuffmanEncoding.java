package ue5;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HuffmanEncoding {

	private Map<Character, Long> frequencies;
	
	private Node huffmanTree; 
	private List<Node> nodeList;
	private Map<Character, ArrayList<Boolean>> lookupTable;

	public HuffmanEncoding(){
		
		frequencies = new HashMap<Character, Long>();
		nodeList = new ArrayList<Node>();
		lookupTable = new HashMap<Character, ArrayList<Boolean>>();
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
		buildLookupTable();
		exportCompression(inputPath, outputPath);
	}
	
	private void exportCompression(String inputPath, String outputPath) throws IOException{		
				
		BufferedReader br = new BufferedReader(new FileReader(inputPath));
		OutputStream output = new FileOutputStream(outputPath);
		BitOutputStream bos = new BitOutputStream(output);

		char [] singleChar = new char[1];		

		//Write LookUptable		
		for (Map.Entry<Character, ArrayList<Boolean>> entry : lookupTable.entrySet())
		{
			ArrayList<Boolean> path = new ArrayList<Boolean>();
			for(int i = 0; i < path.size(); i++){

				boolean bool = path.get(i); 
				int bit = (bool) ? 1 : 0;
				bos.writeBit(bit);
			}
			bos.write(entry.getKey());
		}
		bos.close();
		/*
		//Write ConvertedFile		
		while(br.ready()){
			br.read(singleChar);
			ArrayList<Boolean> path = lookupTable.get(singleChar[0]);

			for(int i = 0; i < path.size(); i++){
			
				boolean bool = path.get(i); 
				int bit = (bool) ? 1 : 0;
				bos.writeBit(bit);
			}
		}
		*/
	}
		
	private void buildLookupTable(){
		findLowestLayer(huffmanTree);
	}
	
	private void findLowestLayer(Node n){
		
		if(n.getLeftNode() == null && n.getRightNode() == null) {			
			lookupTable.put((char) n.getCharacters().charAt(0),(ArrayList<Boolean>) n.getPath());
		}
		else{
			findLowestLayer(n.getLeftNode());
			findLowestLayer(n.getRightNode());
		}
	}
	
	private void buildHuffmanTree(){
	
		charactersToNodes();		
		huffmanTree = buildConnections();

		labelPaths(huffmanTree.getLeftNode(), false);
		labelPaths(huffmanTree.getRightNode(), true);		
	}

	private void labelPaths(Node node, boolean binary){

		node.setPath(binary);
		
		if(node.getLeftNode() != null){
			
			labelPaths(node.getLeftNode(), node.getPath(), false );			
		}
		if(node.getRightNode() != null){			
			labelPaths(node.getRightNode(), node.getPath(), true);			
		}

	}
		
	private void labelPaths(Node node, ArrayList<Boolean> parentPath, boolean binary){
		
		node.setPath(parentPath);
		node.setPath(binary);
		
		if(node.getLeftNode() != null){
			
			labelPaths(node.getLeftNode(), node.getPath(), false );			
		}
		if(node.getRightNode() != null){			
			labelPaths(node.getRightNode(), node.getPath(), true);			
		}
	}
		
	private Node buildConnections(){
		
		while(nodeList.size() > 2){

			Node firstNode  = findSmallestNode();
			Node secondNode = findSmallestNode();
			
//			firstNode.setPath(false);
//			secondNode.setPath(true);
			
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
//		nFirst.setPath(false); 
		Node nSecond = findSmallestNode();
//		nSecond.setPath(true);		
		
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
			
//				char [] array = new char [1024];
//				br.read(array, 0, 1024);
//				countOccurences(array);	
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
		encode.EncodeFile("src/ue5/META-INF/TestTextFile.txt", "src/ue5/META-INF/CompressedTextFile.txt");
	}
}
