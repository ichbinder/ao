package ue5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
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
	
	/**Konstrukor für die Huffman-Kodierung.
	 * frequencies: Zeichen die im Text vorkommen und deren Anzahl
	 * nodeList: Eine Liste worin alle Nodes abgelegt werden
	 * lookupTable: Dient zum späteren kodieren des eigentlichen Textes
	 * */
	public HuffmanEncoding(){
		
		frequencies = new HashMap<Character, Long>();
		nodeList = new ArrayList<Node>();
		lookupTable = new HashMap<Character, Node>();
	}
	
	/**Enkodiert eine ASCII-Textdatei mit Huffman-Kodierung. 
	 * 1. Es wird der Huffman-Baum gebildet.
	 * 2. Eine neue Datei wird erzeugt worin zuerst der Baum und danach der Textinhalt komprimiert gespeichert wird.
	 * 3. Die Größe des Baumes (welche beim Schreiben mitgezählt wurde) wird an den Dateianfang gesetzt, der frühere Dateiinhalt wird dann hinzugefügt.
	 */ 
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
		
		OutputStream output = new FileOutputStream(outputPath);
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
	
	/**Da Vorkalkulationen zur Baumgröße fehlschlugen wird erst der Baum in die Ausgangsdatei geschrieben, sowie der kodierte Textinhalt.
	 * Durch das Mitzählen der Bits beim Schreiben des Baumes ist die richtige Bitanzahl nun vorhanden. Diese wird am Anfang der Datei 
	 * als Integerwert mit GENAU 4 Bytes gespeichert, um diesen wieder korrekt rekonstruieren zu können. Der eigentliche Dateninhalt wird
	 * an die Datei wieder angehängt.
	 * @param outputPath Pfad zur komprimierten Ausgangsdatei*/
	private void addTreeSize(String outputPath) throws IOException{
		
		FileInputStream fis = new FileInputStream(new File(outputPath));				
		
		File file = new File(outputPath);
		
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
		
		FileOutputStream fos = new FileOutputStream(new File(outputPath));		
		fos.write(content);
		fos.close();		
		
/*		File fileToDelete = new File(outputPath + ".txt");
		fileToDelete.delete();
		
		
		
		File fileToRename = new File(outputPath + "tree.txt");
		boolean b = fileToRename.renameTo(new File(outputPath));
		System.out.println(fileToRename.getAbsolutePath());
*/		
	}

	
	/**Gibt die Code-Tabelle des Huffman-Baumes an die Konsole aus.*/
	private void printLookupTable(){
		
		for (Entry<Character, Node> entry : lookupTable.entrySet()) {  // Itrate through hashmap

			if(entry.getKey() == '\r') System.out.print("\\r");
			else if(entry.getKey() == '\n') System.out.print("\\n");
			else System.out.print(entry.getKey());
			System.out.println(":" + entry.getValue().getBitString());
        }
	}
	
	/**Exportiert die eingelesene Datei mit Huffman-Kompriemierung.
	 * @path inputPath Pfad zur zu komprimierenden Datei*/
	private void exportCompression(String inputPath) throws IOException{		

		//Ermittlung der Dateigröße in Byte
		FileInputStream fr = new FileInputStream(inputPath);
		long size = fr.getChannel().size();
		fr.close();
		
		//BufferedReader zum Lesen der Datei mit selbstfestgelegtem Buffer
		BufferedReader brInput = new BufferedReader(new FileReader(inputPath));
		int length = 1024;
		char chars []  = new char [1024];
		
		
		while(brInput.ready()){

			brInput.read(chars, 0, length);
			for(int i = 0; i < length; i++){

				//Wenn keine Escapesequenz (-1) vorhanden ist dann Schreibe Buchstaben als Bitstring von der Huffman-Codetabelle
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

	/**Erstellt den LookupTable, worin die Eingabe eines Zeichens einen Binärpfad (Bitstring) vom Huffman-Baum zurückgibt.*/
	private void buildLookupTable() throws IOException{
		findLowestLayer(huffmanTree);		
	}

	/**Gibt den Bitwert an einer bestimmten Stelle im Byte-Array zurück.
	 * @param data aktuelles Byte-Array
	 * @param pos aktuell untersuchte Position
	 * @return den Bitwert an der gesuchten Stelle*/
	private static int getBit(byte[] data, int pos) {
	      int posByte = pos/8; 
	      int posBit = pos%8;
	      byte valByte = data[posByte];
	      int valInt = valByte>>(8-(posBit+1)) & 0x0001;
	      return valInt;
	   }
	
	/**Sucht den Pfad zur niedrigsten Node um den Huffmann-Baum in die Ausgangsdatei schreiben zu können.
	 * Es wird die Länge des Huffmann-Baums erhöht.
	 * Immer wenn der Baum noch Kinder hat wird eine "0" als Bit geschrieben. Sollte der 
	 * Sollte die Node keine Kinder haben wird eine "1" geschrieben. 
	 * Hier wurde nun die unterste Schicht erreicht und der Buchstaben wird als BitString geschrieben.
	 * @param n = aktuell untersuchte Node*/
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
			
			treeContent +="1";			
			treeContent += n.getCharacters();			
			treeContent += "       ";
			
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
	
	/**Baut den Huffmann-Baum aus dem gezählten Vorkommen der ausgelesenen Buchstaben.
	 * 1. Buchstaben werden zu Nodes umgewandelt
	 * 2. Es werden Nodepaare gebildet und Verbindugen gezogen (von unten nach oben)
	 * 3. Binärpfade werden an den Nodes gebildet (von oben nach unten)*/
	private void buildHuffmanTree(){
	
		charactersToNodes();		
		huffmanTree = buildConnections();

		labelPaths(huffmanTree.getLeftNode(), (short ) 0);
		labelPaths(huffmanTree.getRightNode(), (short) 1);		
	}

	/**Kennzeichnet die linken und rechten Nodes für den Binärpfad. Anfangspunkt.
	 * @param node der Startpunkt (Huffmann-Node)
	 * @param binary Binär-Ziffer für die Huffmannode selbst
	 * */
	private void labelPaths(Node node, short binary){

		node.setPath(binary);
		
		if(node.getLeftNode() != null){
			
			labelPaths(node.getLeftNode(), node.getPath(), (short) 0 );			
		}
		if(node.getRightNode() != null){			
			labelPaths(node.getRightNode(), node.getPath(), (short) 1);			
		}
	}
	
	/**Kennzeichnet die linken und rechten Nodes für den Binärpfad. Hier wird auch der schon entstandene Binärpfad des Elternteils mitgegeben.
	 * Diese Funktion ruft sich selbst rekursiv so lange auf, bis die unterste Schicht des Baumes erreicht wurde 
	 * (die Node die einen einzelnen Buchstaben abbildet).
	 * @param node die Elternnode
	 * @param arrayList der Binärpfad der Elternnode
	 * @param binary Binär-Ziffer für die eigene Node (0 = links, 1 = rechts)
	 * */
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
	
	
	/**Baut die Paar-Verbindungen unter den existieredenen Nodes auf.
	 * Es wird immer nach den zwei niedrigsten Nodes gesucht, welche zu Kind-Nodes für eine neue Eltern-Node werden.
	 *@return Den fertigen Huffmann-Baum, welcher die oberste Eltern-Node abbildet.*/
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
	
	/**Sucht die Node mit dem kleinsten Character-Vorkommen.
	 * Wurde diese erreicht wird die Node aus der zusammengefassten Node-Liste gelöscht und zurückgegeben.
	 * @Return Node mit dem kleinsten Character-Vorkommen*/
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
	
	/**Erstellt aus allen gefundenen Buchstaben Nodes mit entsprechenden Werten, wie seinem Vorkommen im Text.
	 * Danach wird der Eintrag aus der Map für das Vorkommen des Buchstabens gelöscht.*/
	private void charactersToNodes(){

		//Gehe Alle Buchstaben durch
		while(!frequencies.isEmpty()){
			Entry<Character, Long> entry = getMinimumEntry();
			Node node = new Node(entry.getKey().toString(), entry.getValue());
			nodeList.add(node);			
			frequencies.remove(entry.getKey());
		}
	}
	
	/**Sucht den kleinsten Value Eintrag in einer Map. Wurde dieser gefunden wird er aus der Map genommen.
	 **/
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
	
	/**Liest aus einem BufferedReader den Textinhalt. In jedem ausgelesenen Stück wird das Auftauchen von Characters gezählt.
	 * @param br lesender BufferedReader über die Datei
	 * @param size Dateigrößte in Byte*/
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
	
	public void EncodeAndDecodeFile(String fileName) throws IOException{
		
		String inputPath = fileName;
		String outputPath = "compressed_"+fileName; 
		String uncompressPath = "uncompressed_" + fileName; 
		String visualPath = "huffmanTree_" + fileName;		
		
		HuffmanEncoding encode = new HuffmanEncoding();
		
		long sysTimeStart = System.nanoTime();
		System.out.println("Compressing");
		encode.EncodeFile("src/ue5/META-INF/"+ inputPath + ".txt", "src/ue5/META-INF/" +outputPath + ".txt");
		long sysTimeEnd = System.nanoTime();
		
		System.out.println("Done in:"+ (sysTimeEnd - sysTimeStart)/ 1000000);
		System.out.println(encode.treeBits);
		System.out.println(encode.treeContent);
		VisualizeTree visuTree = new VisualizeTree(encode.huffmanTree, 5250, 700, 1250, 40, "src/ue5/META-INF/" + visualPath);			
		
		HuffmanDecoding decode = new HuffmanDecoding();	
		sysTimeStart = System.nanoTime();
		decode.decodeFile("src/ue5/META-INF/" + outputPath +".txt", "src/ue5/META-INF/" + uncompressPath + ".txt");
		sysTimeEnd = System.nanoTime();
		
		System.out.println("Done in:"+ (sysTimeEnd - sysTimeStart)/ 1000000);

	}
	
	
	public static void main(String[] args) throws IOException {
		
		HuffmanEncoding huffmanEncode = new HuffmanEncoding();
		huffmanEncode.EncodeAndDecodeFile("ascii_peter_pan");
		huffmanEncode.EncodeAndDecodeFile("ascii_little_woman");
		huffmanEncode.EncodeAndDecodeFile("ASCII_great_expectations");
		huffmanEncode.EncodeAndDecodeFile("ascii_Treasure_island");
		huffmanEncode.EncodeAndDecodeFile("ascii_war_and_peace");

	}
}
