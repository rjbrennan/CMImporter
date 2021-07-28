import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import fileChooser.CustomFileChooser;
import mapstructs.Cluster;
import mapstructs.Concept;
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
		
		map.buildClusters();
		
		String rearrangeOption = "";
		Cluster clu; Concept cnc;
		String option;
		String[] cluNames; String[] cncNames;
		String[] choices = {"Move Element", "Rename Cluster", "Done"};
		while(!rearrangeOption.equals("Done")) {
			rearrangeOption = (String) JOptionPane.showInputDialog(null, "What do you want to do",
																	"Line 2", JOptionPane.QUESTION_MESSAGE,
																	null, choices, choices[0]);
			if(rearrangeOption.equals("Move Element")) {
				cluNames = map.getClusterNames(false);
				
				option = (String) JOptionPane.showInputDialog(null, "Choose a cluster to remove a concept from",
															  "Line 2", JOptionPane.QUESTION_MESSAGE,
															  null, cluNames, cluNames[0]);
				clu = Cluster.get(map.getClusters(), option);
				cncNames = clu.getConceptNames();
				option = (String) JOptionPane.showInputDialog(null, "Choose a concept to move",
															  "Line 2", JOptionPane.QUESTION_MESSAGE,
															  null, cncNames, cncNames[0]);
				cnc = Concept.get(clu.getConcepts(), option);
				clu.removeConcept(cnc);
				option = (String) JOptionPane.showInputDialog(null, "Choose a cluster to move the concept to",
															  "Line 2", JOptionPane.QUESTION_MESSAGE,
															  null, cluNames, cluNames[0]);
				clu = Cluster.get(map.getClusters(), option);
				clu.addConcept(cnc);
			}
			else if(rearrangeOption.equals("Rename Cluster")) {
				cluNames = map.getClusterNames(false);
				
				option = (String) JOptionPane.showInputDialog(null, "Choose a cluster to rename",
															  "Line 2", JOptionPane.QUESTION_MESSAGE,
															  null, cluNames, cluNames[0]);
				clu = Cluster.get(map.getClusters(), option);
				cncNames = clu.getConceptNames();
				option = (String) JOptionPane.showInputDialog(null, "Choose a new name",
															  "Line 2", JOptionPane.QUESTION_MESSAGE,
															  null, cncNames, cncNames[0]);
				clu.setName(option);
			}
		}
		
		map.buildClConnections();
		
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
		map.export(output);

	}

}
