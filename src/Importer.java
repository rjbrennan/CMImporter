import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFileChooser;

import fileChooser.CustomFileChooser;
import mapstructs.Map;

/**
 * Front end of program and calls to back end
 * @author rjbrennan
 *
 */
public class Importer {
	
	private static File input;
	private static File output;

	public static void main(String[] args) throws IOException, InvocationTargetException, InterruptedException {
		
		//Prompts user to choose a folder of .cxl files they would like to combine
		//EventQueue used to fix error where UI wouldn't appear
		EventQueue.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				JFileChooser folder = new JFileChooser();
				folder.setDialogTitle("Select a folder of concept maps");
				folder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				folder.setAcceptAllFileFilterUsed(false);
				System.out.println("Trying to keep the code running");
				folder.showOpenDialog(null);
				input = folder.getSelectedFile();
			}
		});
		
		
		//Creates Map object
		Map map = new Map(input);
		
		// FIXME if you write the name of another file, it just does filename..cxl
		//Prompts user to save output file
		//EventQueue used to fix error where UI wouldn't show up
		System.out.println("Got this far");
		EventQueue.invokeAndWait(new Runnable() {
            @Override
            public void run() {
            	CustomFileChooser j = new CustomFileChooser(".cxl");
        		j.setAcceptAllFileFilterUsed(false);
                j.showSaveDialog(null);
                output = j.getSelectedFile();
            }
        });

		//Execute map clustering and file building
		map.execute(output);
		
		System.out.println(map.printConcepts());
		System.out.println(map.printConnections());
		System.out.println(map.printClusters());
		System.out.println(map.printClConnections());

	}

}
