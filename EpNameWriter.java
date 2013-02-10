/**
 * A class designed to write out the names of the episodes into a text file.
 * This works with the string we get from the EpNameLister class.
 * 
 * @author Marcus Heine
 *
 */

import java.io.*;
import java.util.*;

public class EpNameWriter {
	public static void main(String [] args) throws IOException {
		
		System.out.println("This is the nameLister main class printing the names.\n");
		EpNameLister lister = new EpNameLister();
		lister.run("Game of Thrones", 2);
		
		ArrayList<String> episodeNames = new ArrayList<String>();
		episodeNames = lister.getEpisodeNames();
		
		System.out.println("This is the nameWriter main class printing the names.\n");
		EpNameWriter nameWriter = new EpNameWriter();
		nameWriter.writeText("Game of Thrones", "G:\\Serier\\", episodeNames);
		
	}
	
	/**
	 * The main method for this class.
	 * It takes takes 3 parameters, the name of the TV Series, the Directory in which you keep your files and the list of names, created by the EpNameLister class.
	 * @param series
	 * @param directory
	 * @param epNames
	 */
	public void writeText(String series, String directory, ArrayList <String> epNames) {
		
		//Get the list of the names of the episodes.
		ArrayList<String> episodes = epNames;
		
		//Create a new BufferedWriter.
		BufferedWriter writer = null;
		
		try
		{
			
			/*
			 * Create a writer, in a new text file, defined by the directory and the name of the TV series.
			 * Ex: directory = "G:\Serier";   series = "Game Of Thrones";   will become "G:\Serier\Game of Thrones\Game of Thrones.txt
			 * 
			 * The reason this works is because when we call this method, the directory is not the place where we keep all TV Series.
			 * Rather, the String we insert has the name f the show added on to it.
			 * 
			 * SEE: String directoryForTextFile  in  RenamerForGUI
			 * 
			 */
			writer = new BufferedWriter( new FileWriter(directory + "\\" + series + ".txt"));
			
			for(String ep : episodes) {
				
				if(ep == null)
					break;
				
				//If the name contains an illegal character ()meaning Windows doesn't allow this character in file names), we remove that character.
				if(ep.contains("?") || ep.contains(":") || ep.contains("/") || ep.contains("\\") || ep.contains("|") || ep.contains("*") || ep.contains("\"") || ep.contains(">") || ep.contains("<")) {
					
					ep = ep.replace("?","");
					ep = ep.replace(":","");
					ep = ep.replace("/","");
					ep = ep.replace("\\","");
					ep = ep.replace("|","");
					ep = ep.replace("*","");
					ep = ep.replace("\"","");
					ep = ep.replace("<","");
					ep = ep.replace(">","");
				}
					
				
				writer.write(ep);
				writer.newLine();
			}
			
				

		}
		//If we for some reason cannot write to the file.
		catch ( IOException e)
		{
			System.out.println("Unable to write to file.");
		}
		finally
		{
			try
			{
				if ( writer != null)
					writer.close( );
			}
			catch ( IOException e)
			{
				System.out.println("Unable to close writer.");
			}
	     }
		
		
	}
}
