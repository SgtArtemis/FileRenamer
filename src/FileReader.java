
import java.io.*;

/**
 * A class designed to 'read' an episode to determine the series, the season and the episode number.
 * 
 * @author Marcus Heine
 *
 */
public class FileReader {

	String episode;
	String season;
	String series;
	String directory;
	EpNameFinder epNameFinder;

	public static void main(String[] args) throws IOException {

		FileReader er = new FileReader();

		er.readName("C:\\Users\\Marcus\\Downloads");


	}

	public FileReader() throws IOException {
		epNameFinder = new EpNameFinder();
	}

	/**
	 * Main run method.
	 * 
	 * @param fullEpisodeName - The name of the file.
	 */
	private void runReader(String fullEpisodeName) {

		this.directory = fullEpisodeName;

		boolean irregularName = false;

		//Will match  (S or s)(1 or two digits)(E or e)(one or two digits)
		String seriesRegex = "[S|s]\\d{1,2}[E|e]\\d{1,2}";
		String reverseSeriesRegex = "[E|e]\\d{1,2}[S|s]\\d{1,2}";

		//Will match ([)(1 or 2 digits)(x)(1 or 2 digits)(])
		String seriesBracketsRegex = "\\[\\d{1,2}[x|X]\\d{1,2}\\]";

		//TODO - Beware (Might match more than we want?)
		//This regex will match (.)(1 or 2 digits)(x)(1 or 2 digits)(.)
		String seriesOtherFormatRegex = "\\.\\d{1,2}[x]\\d{1,2}\\.";

		//These are the 'wrong way around' because we want to split at these points.
		String seasonRegex = "[E|e]\\d{1,2}";

		String [] seriesArray = fullEpisodeName.split(seriesRegex);

		//Basically, if the split "failed", it means the format is something other than the usual

		//TODO - Split at the x

		if(seriesArray.length == 1) {

			if(fullEpisodeName.matches(".*" +  reverseSeriesRegex + ".*"))
				seriesArray = fullEpisodeName.split(reverseSeriesRegex);

			else if(fullEpisodeName.matches(".*" +  seriesBracketsRegex + ".*")) {
				seriesArray = fullEpisodeName.split(seriesBracketsRegex);
				irregularName = true;
				System.out.println("Gets in here |SCRUBS|");
			}

			else if(fullEpisodeName.matches(".*" +  seriesOtherFormatRegex + ".*")) {
				seriesArray = fullEpisodeName.split(seriesOtherFormatRegex);

				irregularName = true;

				System.out.println("got in here");
				System.out.println(seriesArray[0] + "     " + seriesArray[1]);
			}


		}

		//We have to assume the name of the show is in the first spot
		series = seriesArray[0];


		String episodeAndSeason;
		//What this does is that it removes the "other" parts of the string, leaving us with the 'regex' the we defined.
		episodeAndSeason = fullEpisodeName.replace(seriesArray[0], "");
		episodeAndSeason = episodeAndSeason.replace(seriesArray[1], "");
		
		episodeAndSeason = episodeAndSeason.replaceAll("[\\.|\\]|\\[|(|)]", "").trim();

		series = series.replaceAll("[\\.|\\]|\\[|(|)|-]", " ").trim();

		//TODO - Fix this shit
		//Possible idea: Use the "x" as a split, and get the season and episode number from that
		if(irregularName) { //This assumes the filename is something like [3x04]
			
			//Regex that will match either of the following: . [ ] ( )
			//episodeAndSeason.replaceAll("[\\.|\\]|\\[|(|)]", "");

			//Split the String (which should look like "4x05") at the x
			String epAndSeasonArray [] = episodeAndSeason.split("[x|X]");

			//Check if we need to add a 0 to make it S04
			if(epAndSeasonArray[0].length() < 2)
				season = "S0" + epAndSeasonArray[0];
			else
				season = "S" + epAndSeasonArray[0];

			if(epAndSeasonArray[1].length() < 2)
				episode = "E0" + epAndSeasonArray[1];
			else
				episode = "E" + epAndSeasonArray[1];

		}
		else {

		season = episodeAndSeason.replaceAll(seasonRegex, "");

		episode = episodeAndSeason.replace(season, "");

		season = season.replace("s", "S");
		episode = episode.replace("e", "E");
		}

		System.out.println("Series: " + series + "\nEpisode: " + episode + "\nSeason: " + season);
	}



	private String getSeriesString() {

		if(series == null)
			return "";

		return series;
	}

	private String getEpisodeString() {

		if(episode == null)
			return "";

		if(episode.length() == 2)
			return "" + 0 + episode.charAt(1);

		return episode;
	}


	private int getEpisodeInteger() {
		if(episode == null)
			return 0;

		episode = episode.replace("E", "");

		return Integer.parseInt(episode);

	}

	private int getSeasonInteger() {
		if(season == null)
			return 0;

		season = season.replace("S", "");

		return Integer.parseInt(season);

	}

	public void readName(String seriesDirectory) throws IOException {

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

		if(!seriesDirectory.endsWith("\\"))
			seriesDirectory = seriesDirectory.concat("\\");

		File directory = new File(seriesDirectory);

		//Create a string array of all the files, only including the ones specified by the filter
		String [] filenames = directory.list(filefilter);


		for(String episode : filenames) {

			FileReader fileReader = new FileReader();
			fileReader.runReader(episode);

			String series = fileReader.getSeriesString();
			int season = fileReader.getSeasonInteger();
			String episodeNumber = fileReader.getEpisodeString();


			epNameFinder.run(series, season);


			//The "-1" is needed because Episode 1 will be at index 0, and so on.
			String episodeName = epNameFinder.getEpisodeName(fileReader.getEpisodeInteger() - 1);

			//Perform the renaming	
			rename(seriesDirectory, series, "" + season + "", episodeNumber, episodeName, episode);


		}
		//End of main for-loop.

	}

	/**
	 * Main renaming method - to be executed for each file.
	 * 
	 * @param directory - The directory in which your files are found
	 * @param series - The name of the TV Series
	 * @param season - The season (in string from)
	 * @param episode - The episode (in string for)
	 * @param episodeName - The name of the episode
	 * @param oldFileName - The old file name
	 * 
	 * @throws IOException
	 */
	private void rename(String directory, String series, String season, String episode, String episodeName, String oldFileName) throws IOException{

		String fileEnding = "";

		File oldName = new File(directory +  oldFileName);

		if((directory + oldFileName).endsWith(".avi"))
			fileEnding = ".avi";

		else if((directory + oldFileName).endsWith(".mp4"))
			fileEnding = ".mp4";

		else if((directory + oldFileName).endsWith(".mov"))
			fileEnding = ".mov";

		else if((directory + oldFileName).endsWith(".mkv"))
			fileEnding = ".mkv";

		else if(oldName.isDirectory())
			fileEnding = "";

		else
			System.out.println("File ending was not recognized.");


		if(season.length() == 1)
			season = "S0" + season;
		else
			season = "S" + season;

		if(episode.length() == 1)
			episode = "" + episode;
		else
			episode = "" + episode;



		episodeName = fixIllegalCharacters(episodeName);

		//Define the new name of the file
		File newName = new File(directory + series + " - " + season + episode + " - " + episodeName + fileEnding);


		//If the file name already exists, throw an exception.
		if(newName.exists())
			System.out.println("Another file with this name already exists.");


		//Rename the file, and check whether or not it succeeded. 
		if (!oldName.renameTo(newName)) {
			System.out.println("Failed.");
		}
		else
			System.out.println("Succeeded.");

	}

	/**
	 * Since Windows filenames doesn't allow certain characters, we either remove or replace them.
	 * @param ep - The name of the episode
	 * @return The "fixed" name of the episode
	 */
	public String fixIllegalCharacters(String ep) {

		if(ep.contains("?") || ep.contains(":") || ep.contains("/") || ep.contains("\\") || ep.contains("|") || ep.contains("*") || ep.contains("\"") || ep.contains(">") || ep.contains("<")) {

			ep = ep.replace("?","");
			ep = ep.replace(":","");
			ep = ep.replace("/","-");
			ep = ep.replace("\\","-");
			ep = ep.replace("|","-");
			ep = ep.replace("*","");
			ep = ep.replace("\"","-");
			ep = ep.replace("<","");
			ep = ep.replace(">","");
		}

		return ep;

	}


}
