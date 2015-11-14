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
	
	/**Gibt die Länge des Huffman-Baumes in Bits zurück, welche am Anfang der kodierten Textdatei steht.
	 * @param inputPath Pfad zur auszulesenen Datei
	 * @return Größe des Huffman-Baums in Bits*/
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
	
	/**Dekodiert Huffman-komprimierte Datei.
	 * 1. Es wird die Größe des Baumes ausgelesen
	 * 2. Es wird der Huffman-Baum rekonstuiert
	 * 3. Der Textinhalt als Bitstring wird Stück für Stück mit dem Huffman-Pfad durchgegangen und rekonstruiert
	 * @param inputPath Der Pfad zur auszuleseneen, komprimierten Datei*/
	public void decodeFile(String inputPath, String uncompressedPath) throws IOException{

		System.out.print("Get tree size: ");		
		treeLength = getTreeLength(inputPath);
		System.out.println(treeLength);		
		
		InputStream input = new FileInputStream(inputPath);
		bitInput = new BitInputStream(input);

		for(int i = 0; i < 32; i++){
			bitInput.readBit();
		}
		
		huffmanTree = new Node();
		inputContent ="";
		bitCounter = 0;		
		
		System.out.println("Decode tree:");
		buildHuffmanTree(huffmanTree, 0);
		System.out.println("converted:\n" +inputContent);
		System.out.println("DONE");		
		System.out.println("Decode File");
//		VisualizeTree visuTree = new VisualizeTree(huffmanTree, 2000, 400, 400, 40, "Input");	
		decodeData(uncompressedPath, huffmanTree);
		System.out.println("DONE");

	}
	
	/**Dekodiert eine Huffman-Komprimierte Datei.
	 * @param decompressFilePath Pfad für die neue dekomprimierte Datei
	 * @param node Der Startpunkt (Huffman-Baum)*/
	private void decodeData(String decompressFilePath, Node node) throws IOException{
		
		fileWriter = new FileWriter(decompressFilePath);
//		String decompressedLine = "";
		
		while(bitInput.available() > 0){

			String sChar = bitInput.readBit() == 0 ? getCharacter(node.getLeftNode()) : getCharacter(node.getRightNode());
			fileWriter.write(sChar);
		//	decompressedLine += sChar;
		}
		fileWriter.close();
//		System.out.println("decompressed:\n" + decompressedLine);
	}

	/**Gibt das Zeichen zurück, welches sich hinter der Node verbirt. Hier wird die Funktion so oft rekursiv aufgerufen,
	 * bis die aktuelle Node keine Kind-Nodes mehr besitzt. Sollte die Node Kinder haben wird der bitInputStream weiter 
	 * ausgelesen und bei 0 wird die linke Kind-Node untersucht und bie 1 die rechts. Ist das Ende des Pfades erreicht und das Zeichen 
	 * kann zurück gegeben werden.
	 * @param n aktuelle Node
	 * @return Das gesuchte Zeichen*/
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
	
	/**Wandelt ein Bytearray (Größe 4) in ein Integer um.
	 * @param b das umzuwandelne Byte-Array
	 * @return das konvertierte Integer*/
	private static int byteArrayToInt(byte[] b) 
	{
	    return   b[3] & 0xFF |
	            (b[2] & 0xFF) << 8 |
	            (b[1] & 0xFF) << 16 |
	            (b[0] & 0xFF) << 24;
	}
	
	/**Rekonstruiert den Huffman-Baum. Solange die Anzahl der Bits kleiner der ausgelesenen Baumgröße ist wird der Baum rekonstruiert.
	 * Solange der BitWert 0 auftaucht werden Kind-Nodes gebaut die wiederrum rekursiv diese Funktion wieder aufrufen. 
	 * Nach dem der Rekursionsvorgang abgeschloßen ist werden die Kind-Nodes der Eltern-Node hinzugefügt.
	 * Sollte der Bitwert 1 auftauchen wird eine Node erstellt die einen Character besitzt wird. 
	 * Dieser ist als Originaler Characterwert in 8 Bits abgelegt, wird ausgelesen in Byte (Dezimalwert) umgewandelt und kann als char 
	 * ausgelesen werden. Dieser wird der neuen Node hinzugefügt und zurückgegeben. 

	 * WICHTIG: Es durften beim komprimierten NUR ASCII-ZEICHEN verwendet werden.
	 * (Unsichtbare) Escape-Sequenzen oder z.B. Umlaute bringen den Algorithmus zum Scheitern. 
	 **/
	private Node buildHuffmanTree(Node inputNode, long gen) throws IOException{

		while(bitInput.available() > 0 && bitCounter < treeLength){

			int bit = bitInput.readBit();
			bitCounter++;
			if(bit == 1){
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
				inputNode.setLeftNode(leftChild);
				inputNode.setRightNode(rightChild);
				return inputNode;
			}
		}				
		return inputNode;
	}
	
	/**Liest den Inhalt der komprimierten Datei in Bits bis zu einem gegeben Maximum.
	 * @param bitStream der aktuelle Stream zum Ausleden der komprimierten Datei
	 * @param max Anzahl der maximal auszulesenen Bits.
	 * */
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
