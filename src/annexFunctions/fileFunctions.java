package annexFunctions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class fileFunctions {
	
	/* TODO write in metadata 
	 * Title, fName, email, orgname
	 * Repeat for contributor and rights holder
	 * Created and modified timestamps*/
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
	 * @param cxl	String containing the contents of a .cxl file
	 * @return	String of .cxl list of concepts
	 */
	public static String cncStrip(String cxl) {
		
		int cncStart = cxl.indexOf("<concept-list>");
		int cncEnd = cxl.indexOf("</concept-list>");
		
		String concepts = cxl.substring(cncStart+16, cncEnd);
		
		return concepts.trim();
	}
	
	/**
	 * Strips .cxl string down to just the list of connections
	 * @param cxl	String containing the contents of a .cxl file
	 * @return	String of .cxl list of connections
	 */
	public static String cnnStrip(String cxl) {
		
		int cnnStart = cxl.indexOf("<connection-list>");
		int cnnEnd = cxl.indexOf("</connection-list>");
		
		String connections = cxl.substring(cnnStart+19, cnnEnd);
		
		return connections.trim();
	}

}
