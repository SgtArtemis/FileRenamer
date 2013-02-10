/**
 * A program designed (primarily for me) to rename files, in this case the names of certain TV series.
 * 
 * A class designed to be used by the RenamerGUI class.
 * 
 * This class (more specifically, the runRenamer() method) renames all the episodes of a TV Series to have their proper name.
 * 
 * @author Marcus Heine
 *
 */

import java.io.*;
import java.util.*;

public class RenamerForGUI {
	
	/**
	 * 
	 * Main method. This method isn't really supposed to be called, it's main use is testing.
	 * 
	 */
	public static void main(String [] args) throws IOException {
		RenamerForGUI renamer = new RenamerForGUI();
		renamer.runRenamer("G:\\Serier\\", "Game of Thrones", 1);
	}
	
	
	/**
	 * The main method that's going to be used.
	 * 
	 * This method takes 3 parameters.
	 * 		* The directory in which all of your TV Series are placed.
	 * 		* The name of the TV Series
	 * 		* The number of the season of that show.
	 * 
	 * @param directoryString - The name of the directory in which you have placed your TV Series.
	 * @param series - The name of the Series.
	 * @param showNumber - The season number.
	 */
	public void runRenamer(String directoryString, String series, int showNumber) throws IOException {
		
		//Create the lister and the writer.
		EpNameLister lister = new EpNameLister();
		EpNameWriter writer = new EpNameWriter();
		
		//Create a new list, which will hold the names of the episodes.
		ArrayList<String> episodeNames = new ArrayList<String>();	
		
		int seriesNumber = showNumber;
		
		String dir = directoryString;
		
		String directoryForTextFile = directoryString + series;
		
		dir = dir.concat(series + "\\Season " + seriesNumber + "\\");
		
		System.out.println("The directory in which the episodes are found is: " + dir);
		
		String season = "";
		String episode = "";
		String episodeName = "";
		String fileEnding = "";
		
		
		File directory = new File(dir);
		
		//we want the Season number to be S00 (as in S01 and S12) regardless of number
		if(seriesNumber < 10)
			season = " - S0" + seriesNumber;
		else
			season = " - S" + seriesNumber;
		
		
		//Run the lister - Essentially meaning create a list of the episodes.
		lister.run(series, seriesNumber);
		
		//Retrieve the list of episodes.
		episodeNames = lister.getEpisodeNames();
		
		//If the list is empty, we assume that there are no episodes.
		try {
			System.out.println(episodeNames.get(0));
		}
		catch(IndexOutOfBoundsException error) {
				System.out.println("Error: There are no episodes.");
			}
		
		//Create the text file. See the documentation for writeText();
		writer.writeText(series, directoryForTextFile, episodeNames);
		
		//Scan the text file we created.
		Scanner sc = new Scanner(new File(directoryForTextFile + "\\" + series + ".txt"));

		ArrayList<String> names = new ArrayList<String>();
		
		/*
		 * Read the names of the episodes off the text file we create with EpNameWriter
		 */
		while(sc.hasNextLine()) {
			names.add(sc.nextLine());
			//System.out.println("Added some files.");
		}
		
		sc.close();
		
		/*
		 * Create a "filter" for our files, overriding the class' accept method.
		 */
		FilenameFilter filefilter = new FilenameFilter()
   		{
			public boolean accept(File dir, String name)
     		{
     			//In case the filename ends with a (recognized) suffix, we return true.
				if(name.endsWith(".avi") || name.endsWith(".mp4") || name.endsWith(".mkv") || name.endsWith(".mkv"))
					return true;
				
				else
					return false;

      		}
    	};
    	
    	//Create a string array of all the files, only including the ones specified by the filter
    	String [] filenames = directory.list(filefilter);
    	
    	if(filenames != null) {
    	
    	for(int i = 0; i < filenames.length; i++){
    		
    		//Since we want the format to be Exx (as in E01 or E14) regardless of episode number, we set up an if-else check.
    		if(i<9)
    			episode = "E0" + (i+1) + " - ";
    		else
    			episode = "E" + (i+1) + " - ";
    		
    		episodeName = names.get(i);
    		
    		// Old name of file
    	    File oldName = new File(dir + filenames[i]);
    	    
    	    if((dir + filenames[i]).endsWith(".avi"))
    	    	fileEnding = ".avi";
    	    
    	    else if((dir + filenames[i]).endsWith(".mp4"))
    	    	fileEnding = ".mp4";
    	    
    	    else if((dir + filenames[i]).endsWith(".mov"))
    	    	fileEnding = ".mov";
    	    
    	    else if((dir + filenames[i]).endsWith(".mkv"))
    	    	fileEnding = ".mkv";
    	    
    	    else
    	    	throw new IllegalArgumentException("File ending was not recognized.");
    	    
    	    //Define the new name of the file
    	    File newName = new File(dir + series + season + episode + episodeName + fileEnding);
    	    
    	    //If the file name already exists, throw an exception.
    	    if(newName.exists())
    	    	throw new IOException("Another file with this name already exists.");
    	    
    	    
    	    //Rename the file, and check whether or not it succeeded. 
    	    if (!oldName.renameTo(newName)) {
    	        System.out.println("Failed.");
    	    }
    	    else
    	    	System.out.println("Succeeded.");
    	} //End of for-loop
    	
	}	//End of if-statement
	
	}
}
