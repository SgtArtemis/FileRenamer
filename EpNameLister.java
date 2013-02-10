/**
 * A class that lists the names of the episodes of a certain Season of a certain TV series.
 * It works by reading the source from a website containing a list of all the episodes.
 * It then splits the file at certain points using Regular Expressions.
 * 
 * The main point of this class is to return a list of the names of the episodes.
 * This is done by the getEpisodeNames() method.
 * 
 * The run() method has to be called first.
 * 
 * Most other methods are there for troubleshooting purposes only.
 * 
 * @author Marcus Heine
 *
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class EpNameLister {
	
	String mainString;
	String mainHTMLCopy;
	String [] textArray;
	//String [] mainArray;
	ArrayList<String> episodeNames = new ArrayList<String>();
	
	/**
	 * The main class, which isn't really supposed to be run, but can be used for testing purposes.
	 */
	public static void main(String [] args) throws IOException, ArrayIndexOutOfBoundsException
	{
		
		EpNameLister lister = new EpNameLister();
		
		lister.run("Game of Thrones", 2);
		
		lister.printNames();
		
	}
	
	
	/**
	 * Constructor for the EpNameLister class. Pretty much useless, but hey, you need one.
	 * @throws IOException
	 */
	public EpNameLister() throws IOException {
		mainString = "";
	}
	
	
	/**  
	 * OBSOLETE - We dpn't use the mainString anyway. this is here for testing purposes.
	 * @return mainString, the string which holds the episode names.
	 */
	public String getMainString() {
		return mainString;
	}
	
	
	/**
	 * This class returns the enitre source code of the webpage of the series you are trying to access.
	 * This method is only here for testing purposes.
	 * //TODO It could potentially be used to check if a certain season does not exist.
	 * @return
	 */
	public String getEntireHTML() {
		return mainHTMLCopy;
	}
	
	
	/**
	 * Returns the main list of episodes.
	 * @return A List containing the names of the episodes of the specified show and season.
	 */
	public ArrayList<String> getEpisodeNames() {
		return episodeNames;
	}
	
	
	/**
	 * Prints the names of the episodes.
	 * This method is only used in the main method of this class.
	 */
	private void printNames() {
		
		for(String episode : episodeNames) {
			System.out.println(episode);
		}
			
	}
	
	
	/**
	 * The main run method.
	 * It takes two parameters, the name of the TV Series, and the number of the season.
	 * 
	 * This method has to be called before getEpisodeNames() is called, otherwise episodeNames will be null.
	 * 
	 * @param series - The name of the TV Series.
	 * @param seriesNumber - The number of te season.
	 * @throws IOException - If for some reason we can't read the HTML file.
	 */
 	public void run(String series, int seriesNumber) throws IOException {
 		
 		/*
 		 * Because the website needs the string to not have any spaces.
 		 * Capitalization is apparently not important; it will find the site as long as the spelling is right.
 		 */
 		String seriesNoSpaces = series.replaceAll(" ", "");
 		URL url = new URL("http://epguides.com/" + seriesNoSpaces + "/");
		
 		//Open a connection to the website. If there's any trouble, consult:  http://stackoverflow.com/questions/5867975/reading-websites-contents-into-string
		URLConnection con = url.openConnection();
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[8192];
		int len = 0;
		while ((len = in.read(buf)) != -1) {
		    baos.write(buf, 0, len);
		}
		
		//The main string, containing the sites entire HTML code.
		String mainHTML = new String(baos.toByteArray(), encoding);
		mainHTMLCopy = mainHTML;
			
		//Regex that "deletes" most parts of the page, making the site less of a pain in the ass to work with.
		//This works because the names of the episodes are 'encapsuled' in <pre> tags. No idea why, but that makes it work.
		String openingRegex = "<pre>|</pre>";
		
		textArray = mainHTML.split(openingRegex);
		
		//The source is now divided into 3 parts. We want the middle one, and we trim it down.
		mainHTML = textArray[1].trim();
		
		//Before the name of the episode, the site writes "season xx episode xx'>"
		//We set up a regex to split the string just after this occurence.
		String startRegex = "season\\s\\d{1,2}\\sepisode\\s\\d{1,2}'>";
		
		//After the name of the episode, we encounter a </a> in some form, so we split there as well.
		String endRegex = "</a>\\s+";
		String altEndRegex = "</a>\\s+\\d+\\s+";
		
		String mainRegex = startRegex + "|" + endRegex + "|" + altEndRegex;
		
		//Create the main array, split at the points we defined.
		textArray = mainHTML.split(mainRegex);
		
		boolean correctSeries = false;
		
		//TODO
		/*
		 * If you have a certain series that does not work, make a note of it here.
		 * (This is probably because there is a Pilot not in the season, or something like that.)
		 */
		boolean GameOfThrones = false;
		
		//Because we only want the episode names, and not the stuff in between, we make the iteration jump 2 steps.
		for(int i = 1; i<textArray.length; i=i+2) {
			
			//If the series is "Game of Thrones", which has a pilot (not in a season), it will start at [2] instead of [1]
			if(!GameOfThrones && (series.equals("Game Of Thrones") || series.equals("Game of Thrones"))) {
				GameOfThrones = true;
				i++;
			}
			
			//If we encounter a special or an episode that hasn't been named, then you do best in leaving it.
			if(textArray[i].trim().startsWith("Season ") || textArray[i].trim().startsWith("Special ") || textArray[i].trim().startsWith("\\W"))
				break;
			
			/*
			 * Code snippet to remove the </a> that will sometimes occur.
			 * This can't be done (easily) with a regex, since it will match pretty much everything.
			 */
			if(textArray[i].trim().endsWith("</a>")) {
				String ss = textArray[i].substring(0, textArray[i].length() - 4); //Create a new string with the last 4 characters removed.
				textArray[i] = ss;
			}
			
			//If the episode is unaired, skip it until we find a proper episode name.
			while(textArray[i].trim().startsWith("Unaired "))
				i = i+2;
			
			
			/*
			 * The two main if-clauses.
			 * We input a certain Season number. Because we only want to list certain episode names,
			 * we check if we're in the correct season.
			 */
			if(textArray[i-1].contains("Season " + seriesNumber)) {
				correctSeries = true;
			}
			
			if(textArray[i-1].contains("Season " + (seriesNumber+1))) {
				correctSeries = false;
			}
				
			if(correctSeries) {
				episodeNames.add(textArray[i]);
			}				
			
			
			//Obsolete, only here for testing purposes.
			if(textArray.length == i+1)
				mainString = mainString.concat(textArray[i]);
			else
				mainString = mainString.concat(textArray[i] + "\n");
				
		}
	}

}
