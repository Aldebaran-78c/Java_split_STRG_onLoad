package loga_packaage_1;

import java.util.ArrayList;

public class SplitJournal {
	/**
	 * This class splits each journal file loaded into chunks; these chunks then will be
	 * cut into one String for each journal line of the formerly loaded Journal.
	 * This is necessary to prevent an overflow of the String variable(s)
	 * used during the content searches ai Analysis package.
	 */
	
	private static ArrayList<String> journalLines = new ArrayList<>();
	private static ArrayList<String> listOfChunks = new ArrayList<>();
	
	private static int chunkLineNum = 0;
	
	//---------------------------------------------
	// Getter and Setter
	//---------------------------------------------
	public static String get_listOfChunks (int index) {
		return listOfChunks.get(index);
	}
	
	public static void set_ListOfChunks(String chunk) {
		listOfChunks.add(chunk);
	}
	
	public static int seize_listOfChunks() {
		return listOfChunks.size();
	}	
	//---------------------------------------------
	// Further Methods
	//---------------------------------------------
	public static String read_JournalLine(int index) {
		if(journalLines.size() != 0) {
			if(journalLines.size() == index) { index = index -1; if(index <= 0) { index = 0; }}
			return journalLines.get(index);
		}
		else {
			return "";
		}
	}
	
	public static void add_Line(String line) {
		journalLines.add(line);
	}
	
	public static int numJournalLines() {
		return journalLines.size();
	}
	//---------------------------------------------
	public static void clearing_preprocessLists() {
		listOfChunks.clear(); 
		journalLines.clear();
		System.out.println(">>>> clearing tmp field data <<<");
	}
	//---------------------------------------------
	public static int get_chunkLineNum() {
		return chunkLineNum;
	}
	
	public static void set_chunkLineNum (int line) {
		chunkLineNum = line;
	}
	//---------------------------------------------
	//	RAW string pre-processing
	//---------------------------------------------
	public static void cutRaw() {
		
		//clearing_preprocessLists();
		
		int rawLength = Log_Raw_Data.rawContent.length(); //System.out.println("Raw data str length is: " + rawLength);
		int numOfChunks = 0;
		int rest = 0;
		int maxChunkSize = 100000;
		int lastLineInChunk_startIndex = 0;
		int lastLineInChunk_endIndex = 0;
		int lastCunkLineEnd = 0;
		int startIndex = 0;
		StringBuilder chunk; // = new StringBuilder();
		
		//----------------------------------------------------------------------------------------------------------------------------------
		// LONG JOURNAL FILES (( short means string.length() is smaller than 100000 ))
		//----------------------------------------------------------------------------------------------------------------------------------
		if(rawLength > maxChunkSize) {
			
			rest =  rawLength % maxChunkSize;
			if(rest > 0) {
				numOfChunks = rawLength / maxChunkSize +1;
			}
			else {
				numOfChunks = rawLength / maxChunkSize;
			}
			//System.out.println(">>> Number of chunks needed: " + numOfChunks); 
			
			
			for(int count=0; count <= numOfChunks; count++) {
				chunk = new StringBuilder();
				String regularExp = "{ \"timestamp\":"; //regEx for lineStart;
				String regularExp_2 = "\" }"; //regEx for lineEnd;
				int chnunkEndIndex = maxChunkSize * count + maxChunkSize;
				
				startIndex = lastLineInChunk_endIndex; //maxChunkSize * count; // First Index of chunk;
								
				lastLineInChunk_startIndex = Log_Raw_Data.rawContent.lastIndexOf(regularExp, chnunkEndIndex); 
					//Index of last line of the recent chunk;
				lastLineInChunk_endIndex = Log_Raw_Data.rawContent.indexOf(regularExp_2, chnunkEndIndex); 
					// the end index of the last line of the recent chunk may reach into the following chunk, unless line end equals index of 200k;
				if(lastLineInChunk_endIndex != -1) {
					//System.out.println("Last chunk line index remains unchanged.");
				}
				else {
					lastLineInChunk_endIndex = rawLength;
				}
				
				//System.out.println("Last Line-START-Index (index of regEx in dataChunk No " + count + ") is " + lastLineInChunk_startIndex);
				//System.out.println("Last Line-END-Index (index of regEx_2 in dataChunk No " + count + ") is " + lastLineInChunk_endIndex);
				
				//System.out.println("Start of chunk at: " + startIndex + " - End of chunk at index: " + lastLineInChunk_endIndex);
				
				for(int indexCount = startIndex; indexCount < lastLineInChunk_endIndex; indexCount++) {
					char glyphAtIndex = Log_Raw_Data.rawContent.charAt(indexCount);
					chunk.append(glyphAtIndex); //(Log_Raw_Data.rawContent.charAt(indexCount));
				}
				String chunkPart = chunk.toString();
				set_ListOfChunks(chunkPart);
			}
			//seize_listOfChunks();
			//System.out.println(get_listOfChunks(2));
			//System.out.println("ArrayList of chunks now contains: " + seize_listOfChunks() + " elements.");
		}
		
		//----------------------------------------------------------------------------------------------------------------------------------
		// SHORT JOURNAL FILES (( short means string.length() is smaller than 100000 ))
		//----------------------------------------------------------------------------------------------------------------------------------
		//The alternate Procedure inside the following else-block covers journal files, which are
		//shorter than the max chunk size (variable maxChunkSize, which is set to 100000)
		//------------------------------------------------------------------------------------------
		else {
			chunk = new StringBuilder();
			String regularExp = "{ \"timestamp\":"; //regEx for lineStart;
			String regularExp_2 = "\" }"; //regEx for lineEnd;
			int chnunkEndIndex = rawLength;
			
			startIndex = lastLineInChunk_endIndex; //maxChunkSize * count; // First Index of chunk;
							
			lastLineInChunk_startIndex = Log_Raw_Data.rawContent.lastIndexOf(regularExp, chnunkEndIndex); 
				//Index of last line of the recent chunk;
			lastLineInChunk_endIndex = Log_Raw_Data.rawContent.indexOf(regularExp_2, chnunkEndIndex); 
				// the end index of the last line of the recent chunk may reach into the following chunk, unless line end equals index of 200k;
			if(lastLineInChunk_endIndex != -1) {
				//System.out.println("Last chunk line index remains unchanged.");
			}
			else {
				lastLineInChunk_endIndex = rawLength;
			}
			
			//System.out.println("Last Line-START-Index (index of regEx in dataChunk No " + count + ") is " + lastLineInChunk_startIndex);
			//System.out.println("Last Line-END-Index (index of regEx_2 in dataChunk No " + count + ") is " + lastLineInChunk_endIndex);
			
			//System.out.println("Start of chunk at: " + startIndex + " - End of chunk at index: " + lastLineInChunk_endIndex);
			
			for(int indexCount = startIndex; indexCount < lastLineInChunk_endIndex; indexCount++) {
				char glyphAtIndex = Log_Raw_Data.rawContent.charAt(indexCount);
				chunk.append(glyphAtIndex); //(Log_Raw_Data.rawContent.charAt(indexCount));
			}
			String chunkPart = chunk.toString();
			set_ListOfChunks(chunkPart);
		}
		//------------------------------------------------------------------------------------------
		splitJournal();
	}
	//---------------------------------------------
	public static void splitJournal() {
		int totalNumberChunck = seize_listOfChunks();
		String chunk = "";
		String regularExp = "{ \"timestamp\":";
		//String regularExp_2 = "\" }";
		int regEx_START_Index = 0;
		int regEx_End_Index = 1;
		int startLine= 0;
		int nextStartIndex = regularExp.length() +1;
		int iteration = -1;
		
		for(int count = 0; count < /*3*/totalNumberChunck; count++) {
			chunk = get_listOfChunks (count);
			
			startLine = chunk.indexOf(regularExp, regEx_START_Index);
			
			while (startLine != -1) {
				iteration = iteration +1;
				startLine = chunk.indexOf(regularExp, startLine); //System.out.println("Value of indexOf startLine at loop (re-) entry: " + startLine);
				regEx_End_Index = chunk.indexOf(regularExp, startLine + nextStartIndex);
				
				String lineContent = "";
				for(int numOfGlyphs = startLine; numOfGlyphs < regEx_End_Index; numOfGlyphs++) {
					lineContent += chunk.charAt(numOfGlyphs);
				}
				add_Line(lineContent);
				if(startLine != regEx_End_Index) {
					startLine = regEx_End_Index;
				}
				else {
					for(int numOfGlyphs = regEx_End_Index; numOfGlyphs < chunk.length(); numOfGlyphs++) {
						lineContent += chunk.charAt(numOfGlyphs);
					}
				}
				//set_chunkLineNum(iteration);
				//System.out.println(iteration + "line-number...");
				//System.out.println(iteration + ".\n" + lineContent);
				//System.out.println(iteration + lineContent);
				//System.out.println(read_JournalLine(iteration));
			}
			//System.out.println("   CHUNK NUMBER: " + count);
		}
		Log_Raw_Data.set_loadingfinished (true);
		//System.out.println("Splitting journal done.\n");
	}
	//---------------------------------------------
	//
	//---------------------------------------------
	/*public static void showJournalLines() {
		if(journalLines.length > 0) {
			System.out.println("JournalLines:\n------------\n");
			for(int count = 0; count < journalLines.length; count++) {
				System.out.println(journalLines + "\n");
			}
			System.out.println("JournalLines:\n------------\n");
			System.out.println("Number of found lines: : " + journalLines.length  + "\n------------\n");	
		}
		else {
			System.out.println("No Journal lines found.");
		}
	}*/
//----------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------
}
