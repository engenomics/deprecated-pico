import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SeqGraphVCF {


	private String readFilePath = "C:/Adithya/PRIMES/Data/VCF Data/chr9.txt";
	private String outputFilePath = "C:/Adithya/PRIMES/Data/outputVCF.txt";
	
	private String fileHeader = "WinStart\tWinEnd\tVar";
	
	private BufferedWriter writer;

	
	public static void main(String [] args) {
		SeqGraphVCF graph = new SeqGraphVCF();
		graph.createSequenceGraphVCF(10);
	}
	
	public void openWriter() {
		
		try {
		    writer = new BufferedWriter(new FileWriter(outputFilePath));
		}
		
		catch(Exception e) {
			   System.out.println("unsuccessful open");
		}
	}
	
	public void closeWriter() {
		
		try {
			writer.close();
		}
		
		catch(Exception e) {
			System.out.println("unsucessful close");
		}
	}
	
	public void writeToFile(String ln) {
		
		try {
		    writer.write(ln);
		    writer.newLine();    
		} 
		
		catch(Exception e) {
		    System.out.println("unsuccessful write");
		}
	}

	public void createSequenceGraphVCF(int windowSize) {
		
		try {
			BufferedReader buf = new BufferedReader(new FileReader(readFilePath));
			ArrayList<String> words = new ArrayList<String>();
			String lineFetched = "";
			String [] wordsArray = null;
			openWriter();
			writeToFile(fileHeader);
			
			while(true) {
				
				String line  = "";
				lineFetched = buf.readLine();
				if(lineFetched == null) break;
				else {
					wordsArray = lineFetched.split("\t");
					for(String each : wordsArray) {
						if(!"".equals(each)) words.add(each);
					}
				}
			
				if(wordsArray.length < 2 || lineFetched.contains("#")) continue;
				
				else {
					int startKMerPosition = Integer.parseInt(wordsArray[1]) - Integer.parseInt(wordsArray[1]) % windowSize;
					int endKMerPosition = startKMerPosition + windowSize;
					
					if(!wordsArray[2].contains(">")) {
						if(wordsArray[4].contains(",")) wordsArray[4] = wordsArray[4].replace(",", "");	
						wordsArray[2] = wordsArray[0] + ":" + wordsArray[1] + wordsArray[3] + ">" + wordsArray[4];
					}
					line += startKMerPosition + "\t" + "\t" + endKMerPosition + "\t" + wordsArray[2];
					
					writeToFile(line);
				
				}
			}

			buf.close();
			closeWriter();
		}
		
		catch(Exception e) {
			e.printStackTrace();
		}

	}
}
