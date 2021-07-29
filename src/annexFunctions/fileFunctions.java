package annexFunctions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Functions used in the collective concept map program that involve files
 * @author Riordan Brennan
 *
 */
public class fileFunctions {
	
	/* TODO write in metadata 
	 * Title, fName, email, orgname
	 * Repeat for contributor and rights holder
	 * Created and modified timestamps*/
	/**
	 * Writes collective concept map to a .cxl file
	 * @param template	.cxl file to base new file on
	 * @param output	file to write to
	 * @param guts		map specifics to place inside the template file
	 * @throws IOException
	 */
	public static void writeCollectiveMap(File template, File output, String guts) throws IOException {
		String contents = Files.readString(Path.of(template.toString()));
		int cut = contents.indexOf("\n\t<style-sheet-list>");
		String outputString = contents.substring(0, cut)+guts+contents.substring(cut);
		FileWriter w = new FileWriter(output);
		w.write(outputString);
		w.close();
	}
	
	/**
	 * Strips .cxl string down to just the list of concepts
	 * @param cxl	contents of a .cxl file
	 * @return	list of concepts in .cxl format
	 */
	public static String cncStrip(String cxl) {
		
		int cncStart = cxl.indexOf("<concept-list>");
		int cncEnd = cxl.indexOf("</concept-list>");
		
		String concepts = cxl.substring(cncStart+16, cncEnd);
		
		return concepts.trim();
	}
	
	/**
	 * Strips .cxl string down to just the list of connections
	 * @param cxl	contents of a .cxl file
	 * @return	list of connections in .cxl format
	 */
	public static String cnnStrip(String cxl) {
		
		int cnnStart = cxl.indexOf("<connection-list>");
		if(cnnStart==-1)
			return "";
		int cnnEnd = cxl.indexOf("</connection-list>");
		
		String connections = cxl.substring(cnnStart+19, cnnEnd);
		
		return connections.trim();
	}

	/**
	 * Strips .cxl string down to just the list of linking phrases
	 * @param cxl	contents of a .cxl file
	 * @return	list of linking phrases in .cxl format
	 */
	public static String linkStrip(String cxl) {
		int linkStart = cxl.indexOf("<linking-phrase-list>");
		if(linkStart==-1)
			return "";
		int linkEnd = cxl.indexOf("</linking-phrase-list>");
		
		String links = cxl.substring(linkStart+23,linkEnd);
		return links.trim();
	}

}
