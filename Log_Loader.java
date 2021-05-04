package loga_packaage_1;

import java.io.File;
import java.util.ArrayList;
//import java.util.HashMap;

import javax.swing.JOptionPane;

public class Log_Loader {
		
	private static File logFileDir = new File("");
	private static ArrayList<String> foundDirectory = new ArrayList<>();
	static ArrayList<String> fileList = new ArrayList<>();
	private static ArrayList<File> ListOfFileObj = new ArrayList<>();
	private static int [] dirList_startIndeces = new int[16];
	/**
	 * ArrayList foundLogs stores the path(s) and the files contained inside the
	 * given directory.
	 */
	//private HashMap<LogDiscoveries, Log_Raw_Data> allLogMatches;
	//this HashMap is needed for the log analysis hand will have to
	//be placed in the related class methods.
	static boolean  param_concat = false;
	static private String saveDir = "undefined";
	static private String[] directoryFiles;
	static private int numOf_files = 0;
	static private int newFoundFiles = 0;
	static private int filesInDir = 0;
	static private int indexer = 0;
	//---------------------------------------------------------------
	// Getter and Setter
	//---------------------------------------------------------------

	public static void set_filesInDirectory(int number) {
		filesInDir = number;
	}
	
	public static int get_filesInDirectory() {
		return filesInDir;
	}
	
	static public void setNum_newFoundfFiles(int aNum) {
		newFoundFiles = aNum;
	}
	
	static public int getNum_newFoundFiles () {
		int num = newFoundFiles;
		return num;
	}

	static public void setParam_concat(boolean bit) {
		param_concat = bit;
	}
	
	static public boolean getParam_concat() {
		boolean bit = param_concat;
		return bit;
	}
	
	static public void setNum_ofFiles(int number) {
		numOf_files = number;
	}
	
	static public int getNum_ofFiles() {
		int number = numOf_files;
		return number;
	}
	
	static public void addFileObjTo_ListOfFileObj (File targetDir, String fileName) {
		File fileObj_toAdd = new File(targetDir, fileName);
		ListOfFileObj.add(fileObj_toAdd);
	}
	
	static public File getFileObj(int index) {
		if (index < 0) { index = 0; }
		File fileObject = ListOfFileObj.get(index);
		return fileObject;
	}
	
	static public int getFileOBJ_list_Size() {
		int	size = ListOfFileObj.size();
		return size;
	}
	
	/*static public void remove_ElemFromListOfFilOBJ (int index) {
		if( getFileOBJ_list_Size() > 0) {
			ListOfFileObj.remove(index);
		}
	}*/
	
	static public int getFoundDirectory_Size() {
		int size = foundDirectory.size();
		return size;
	}
	
	static public String getFoundDirectoryEntry (int index) {
		String indexSTR = foundDirectory.get(index) ;
		return indexSTR;
	}
	
	static public void addSTR_toListOfFiles(String fileNameStr) {
		String listElement = fileNameStr;
		fileList.add(listElement);
		System.out.println("JournalFile " + fileNameStr + " added to fileList.");
	}
	
	static public String getFileName_AtIndex (int index) {
		String fileName = "";
		if(getFileList_Size() > 0) {
			fileName = fileList.get(index);
			return fileName;
		}
		else {
			return "";
		}
	}
	
	static public int getFileList_Size() {
		int size = fileList.size();
		//System.out.println("\nNumber of files found: " + size + "\n");
		return size;
	}
	
	/*static public void remove_ElemFromFileList (int index) {
		if(getFileList_Size() > 0) {
			fileList.remove(index);
		}
	}*/
	
	static public void removeAllFilefromSelection() {
		
		fileList.clear();
		ListOfFileObj.clear();
		foundDirectory.clear();
		
		Event_Collections.clear_discoveries ();
		//Event_Collections.fuelGauges.clear();
		Event_Collections.clear_cmdrNames();
		Event_Collections.clear_unconfirmedDiscovery ();
		Event_Collections.clear_SystemsWithUnconfirmedDiscoveries ();
		Event_Collections.clear_confirmedSystems ();
		Event_Collections.clear_visitedSystems();
		Event_Collections.clear_universalCartoAppointment_Date ();
		
		Log_Raw_Data.clear_alreadyLoadedFiles();
		Log_Raw_Data.set_loadingfinished(false);
		Log_Raw_Data.set_completeScanZeroIndexFile(false);
		
		clearFoundDir();
		
		Path_operations_2.refresh_AddedFileNames();
		Path_operations_2.refreshFileExcluded();
		Path_operations_2.set_dirscardNum(0);
		Path_operations_2.refresh_discardNum();
		Log_Loader.set_filesInDirectory(0);
		ed_LogaStage.set_HaveBeenFilesLoaded(false);
		ed_LogaStage.set_numOfConcats(0);
		ed_LogaStage.set_limitReached(false);
		//resetting the index for the 'simple' array containing numbers of files for each found valid directory
		set_indexer(0); 
		ed_LogaStage.set_activeFlag(true);
		ed_LogaStage.lblProgramInfo.setText("Program Info");
		ed_LogaStage.internalReadNumber = 0;
		ed_LogaStage.clear_infoContent ();
		Analysis.fileZeroSecondScan = false;
		ed_LogaStage.testLoadNum = 0;
		
		System.gc();
	}
	
	static public void clearFoundDir() {
		foundDirectory.clear();
	}
	
	static public String getLogFileDirAsSTR() {
		String recentDir = "";
		if (logFileDir!=null) {
			recentDir = logFileDir.toString();
		}
		else recentDir = "no directory defined.";
		return recentDir;
	}
	
	static public int sizeArrayFoundDirs() {
		int size = foundDirectory.size();
		return size;
	}
	
	static public void remove_ElemFromFoundDir (int index) {
		if(sizeArrayFoundDirs() > 0) {
			foundDirectory.remove(index);
		}
	}
	
	static public void setArray_directoryFiles(File targetDir) {
		directoryFiles = targetDir.list();
	}
	
	static public int get_directoryFilesSize() {
		if(directoryFiles!=null) {
			return directoryFiles.length;
		}
		else {
			return 0;
		}
	}
	//-------------------------------------------------------------------------------------------
	//number array storing the number of valid directories
	//-------------------------------------------------------------------------------------------
	// Getter for receiving the number of directories containing Journal Files in total
	//
	public static int get_dirList_startIndece(int index) {
		int element = dirList_startIndeces[index];
		//This writes/sets a single start position of FileNames and Objects in the related ArrayLists belonging 
		//to one directory;
		//the stored integer contains the start position of a SEQUENCE of Filenames or File-Objects in the related 
		//ArrayLists, which belongs to one directory and is expressing a sequence (refer above) of stored filenames or File-Objects 
		//inside the ArrayLists of **fileList** and **ListOfFileObj**.
		//These sequences of Journal names or File-Objects relating to one directory may vary in lengths which are referenced by the 
		//integer numbers stored in this 'simple' Array. Each of these numbers expressing the number of file names (objects) of 
		//a directory derives from NewAdded ArrayList. These numbers have to be stored in the 'simple' Array 
		//called **dirList_startIndeces** used for iteration through the ArrayList fileList becoming possible by using the 
		//numbers stored in this array.
		//
		//Since there are only valid filenames and paths (in the ArrayLists filenNames and fileNameOBJ),
		//which are synchronously stored, the
		//
		//START POSITION OF A NAME/OBJECT SEQUENCE IN THE ARRAYLIST is:
		//*** the stored integer at an index of this array holds the start index of a sequence or set of 
		//files expressed/described by their names contained in the ArrayList. All members of this set or sequence do 
		//belong to one directory. ***
		//ONLY DIRECTORIES CONTAINING AT LEAST ONE JOURNAL FILE ARE VALID AND SHOULD BE REPRESENTED BY A START-INDEX
		//AT ONE INDEX OF THIS 'SIMPLE' ARRAY. Of course there are no files of empty directories since only files were accessed and
		//listed inside the ArrayLists.
		//
		//***The index of a file (filename) in the ArrayList fileList (ListOfFileOBJ) being the last one of the referenced directory 
		//(as a sequence of names in the ArrayList(s)) equals the following: (value of the following index of this 'simple array -1).
		//***
		//
		//Direct access of Journal Files should be possible via the related File-Object stored inside
		//the ArrayList ListOfFileObj. But if a single Journal File String is needed, the index of this
		//array allows the quick access without constraining a FileObject.toString().
		//
		//THE INTEGER AT EACH INDEX EQUALS THE START INDEX OF THE FIRST FILE NAME OF A DIRECTORY. THE FOLLOWING
		//INTEGER VALUE OF THIS ARRAY MINUS ONE EQUALS THE END OF THE FILE-NAME-SEQUENCE BELONGING TO THE REFERRED DIRECTORY.
		
		return element;
	}
	//-------------------------------------------------------------------------------------------
	// Setter for investigating the number of directories containing Journal Files
	//dirList_startIndece is a common Array (!) size is predefined (!) at 16 (!)
	//
	/*public static int size_dirList_startIndece() {
		int numOfJournalDirectories = dirList_startIndeces.length;
		//Evaluating the whole size of the directory List containing the start indices
		//of valid Journal directories - each index contains the start index of the entries
		//of a directory containing at least one Journal File (refer to the method above
		// - get_dirList_startIndece - which returns the position as integer number.
		
		return numOfJournalDirectories;
	}*/
	//-------------------------------------------------------------------------------------------
	//Setter for storing the number of directories containing Journal Files
	//Counting directories containing more than zero Journals entries
	//
	public static void set_dirList_startIndece(int validDirectories) {
		//int DirIndex = size_dirList_startIndece(); //always returns 16 (!)
		int DirIndex = Path_operations_2.getSize_List("foundDirectory");
		
		//this is the length and thus the first free index of the array since the length/size starts 
		//and the indeces are numbered starting from index zero;
		//checking for validity and entry should be done at the end of the fillList method at
		//Class LogLoader (this class) after the iteration of all file objects.
		
		if(Path_operations_2.getSize_List("foundDirectory") > 16) {
			DirIndex = 16; //common Array does have a fixed number of indexes (!)
		}
		//an index should never become greater than 16 due to the limitation of concat option at
		//ed_LoagStage.requestFramesCONCAT
		dirList_startIndeces[DirIndex] = validDirectories;
	}
	
	
	//---------------------------------------------------------------
	public static void setSavePath(boolean concat, File savePath) {
		/**
		 * the boolean parameter checks whether to clear or leave previously 
		 * found log-files being saved in the ArrayList foundLogs. 
		 * If found log-files of the new directory shall be concatenated newly
		 * found entries (path, fileName) will be added to the ArrayList; if
		 * a new set is to be created, foundLogs is to be cleared.
		 */
		
		setParam_concat(concat);
		saveDir = savePath.toString(); 
		
		//System.out.println("Log-Loader.setSavePath entry: result = " + concat + "; saveDir = " + saveDir);
		
		if(concat!=true) {
			//clearFoundDir(); 
			//no concatenation. The ArraList should have been cleared before; this conditional test and the param concat for the method
			// may be deleted in the future
			//
			foundDirectory.add(saveDir); //stored directory as String-Variable inside ArrayList of found dirs;
			logFileDir = savePath;		//stored directory as File-Object
			//System.out.println("== Log_Loader.setSaveDir saved variable of type File with a value of " + logFileDir.toString() + " ==");
		}
		else {
			foundDirectory.add(saveDir);//stored directory as String-Variable inside ArrayList of found dirs;
			logFileDir = savePath;		//stored directory as File-Object
			//System.out.println("== Log_Loader.setSaveDir saved variable of type File with a value of " + logFileDir.toString() + " ==");
		}
		 pathIsDir (logFileDir); 	//testing if path is a directory and no single file;
		 allFilesInDir(savePath);	//Filling the ArrayList containing the filenames as fileobjects (value pairs (dir, file);
		 //fillFileList();			//REMOVED and integrated in method allFilesInDir(savePath)
		 							//Filling the ArrayList containing the filenames as Strings;
	}
	//---------------------------------------------------------------
	
	//---------------------------------------------------------------	
	public static String getFileDir() {
		System.out.println("== Log_Loader.getFileDir returns " + saveDir + " ==");
		return saveDir;
	}
	//---------------------------------------------------------------
	
	//---------------------------------------------------------------
	// Testing Validity of the Given Target Directory
	//---------------------------------------------------------------
	public static boolean pathIsDir (File targetPath) {
		/**
		 * This method tests if the Journal directory-path element is a directory
		 * and not a file; the given parameter is the File variable
		 * named logFileDir.
		 * This method as well tests if the directory content is readable.
		 */
		boolean result = false;
		String messageOut = "not tested, yet...";
		if(targetPath.exists() && targetPath.isDirectory()) {
			if(targetPath.canRead()) {
				messageOut = "Save path of ED logs was selected. It is a valid and accessable directory.";
				System.out.println(messageOut);
				result = true;
			}
			else {
				messageOut = "No reading right to this path.\n Please select a valid directory or start this application with higher privileges.";
				System.out.println(messageOut);
				result = false;
			}
		
		}
		else {
			messageOut = "Either path is nor directory or doesn't exist.\n Please select a valid directory.\n\n";
			System.out.println(messageOut);
			result = false;
		}
		ed_LogaStage.get_OutputContent();
		return result;
	}
	//---------------------------------------------------------------
	public static void set_DirList_Sizes(int index, int numOfFilesAdded) {
		if (index > 16) {
			JOptionPane.showMessageDialog(null, "Attention! More than 16 different directories were calculated.\nMaximum number of concatenation exceeded.");
		}
		else {
			System.out.println("\n StartIndexNextSet: " + index);
			dirList_startIndeces[index] = numOfFilesAdded;
		}
	}
	//---------------------------------------------------------------
	// Indexing the 'simple' array containing number of files of each valid directory
	//---------------------------------------------------------------
	static public void set_indexer(int filenum) {
		indexer = filenum;
	}
	
	static public int get_indexer() {
		return indexer;
	}

	//---------------------------------------------------------------
	// Reading all File-Names at Target Directory
	//---------------------------------------------------------------
	public static void allFilesInDir(File targetDir) {
		//System.out.println(" ");
		//System.out.println("Recent Target Directory is: " + targetDir.toString());
		ed_LogaStage.set_carryOn(true);
		Path_operations_2.refresh_AddedFileNames();
		Path_operations_2.refreshFileExcluded();
		Path_operations_2.refresh_discardNum();
		//setNum_newFoundfFiles(0);
		
		setArray_directoryFiles(targetDir); //targetDir is a File-Object
		int size = get_directoryFilesSize(); //size of ArrayList containing
		set_filesInDirectory(size);
		int skippedFiles = Path_operations_2.get_discardNum();
		
		
		for(int count = 0; count < size; count++) {
			String pathExpression = directoryFiles[count]; //System.out.println(count + " " + pathExpression);
			//Array directoryFiles contains all filenames of the selected directory as strings of filenames
			//=> pathExpression iterates through the filenames of the selected directory.
			
			File path = new File(targetDir, pathExpression); 
			//path is a File-Object expressed as 
			//path(File-Object Directory, String filename), which either can be a dir or
			//a file;
			//System.out.println("Variable path of Type File is: " + path.toString());

			
			if(path.isFile()==true) {
				String fileName = pathExpression.toString();
			//This condition will test the File-Object path if it is a file:
			//Verify filename and suffix of the file name;
			//if not matching, do not add to the File Object List and the File Name List
			//create a boolean variable to feed into the condition by a method;
			//method will be places in Class Path_operations_2.
			//Implementing it here no array iterations are needed to do so later and
			//the resulting arrays take less storage space.
			//Count of elements should be done, if data is added in the respective fields,
			//thus retrieving the number of elements and the field length as number of
			//elements minus one.								
				boolean duplicated = Path_operations_2.duplicateTest(fileName); 
				boolean validName = Path_operations_2.list_verify_name(fileName);
				if(duplicated == true) {System.out.println(">>>>>>>>>>>>>>>>> dunplicatetd state: " + duplicated + "<<<<<<<<<<<<<<<<<<<<<<<<<");}
				if (validName == false) {System.out.println(">>>>>>>>>>>>>>>>> Name Verification " + validName + "<<<<<<<<<<<<<<<<<<<<<<<<<\n\n");}
				
				if(validName != false && duplicated != true)
				{
					addFileObjTo_ListOfFileObj (targetDir, pathExpression);
					addSTR_toListOfFiles(pathExpression.toString());
					Path_operations_2.set_NewAddedFileNames(pathExpression.toString());
					num_of_dirs_storage();
				}

				else {
					Path_operations_2.setFilesExcluded(pathExpression.toString());
					skippedFiles = skippedFiles + 1;
					Path_operations_2.set_dirscardNum(skippedFiles);
				}
			}
		}
	}
	//--------------------------------------------------------------
	//---------------------------------------------------------------
	// STORING NUMBER OF VALID DIRECTORIES
	//---------------------------------------------------------------
	public static void num_of_dirs_storage() {
	
		//Retrieving the value of valid JournalFileNames, to be stored;
						
		int num_Dir = getFoundDirectory_Size();
		int recentIndex = get_indexer() +1; //variable recentIndex to be reset at each clearing (adding files with concat-option NO);
			 
		//if there were more than 16 valid directories the setter re-sets the last index to 16
		if (recentIndex > 0 && recentIndex < 16) {
			
		set_dirList_startIndece(recentIndex);	
			
			System.out.println("\n" + num_Dir + " were stored in the Array dirList_startIndeces.\n");
			System.out.println(num_Dir + " valid directories of Journals stored in total.\n");
			System.out.println("============\n\n");
		}
	}
	//--------------------------------------------------------------
}
