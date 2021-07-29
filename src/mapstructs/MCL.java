package mapstructs;

import java.awt.Dimension;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;

import annexFunctions.arrayFunctions;
import net.sf.javaml.clustering.mcl.MarkovClustering;
import net.sf.javaml.clustering.mcl.SparseMatrix;

/**
 * Markov Clustering method, adapts the {@link MarkovClustering} 
 * to be used for collective concept map clustering.
 * @author Riordan Brennan
 *
 */
public class MCL {
	
	private Map map;
	private double pGamma;
	
	/**
	 * Creates MCL object and asks user for a pGamma input, framed as "how many cluster would you like"
	 * @param map	concept map to run MCL on
	 */
	public MCL(Map map) {
		this.map = map;
		
		pGamma = 0;
		while(pGamma<=0) {
			JFrame frame = new JFrame();
			JSlider slider = createSlider(10, 20);
			JPanel sliderPanel = createSliderPanel(slider, "How many clusters would you like?");
			String title = "Clustering";
			int dialogResponse = JOptionPane.showOptionDialog
		            (frame,                  // I'm within a JFrame here
		             sliderPanel,
		             title,
		             JOptionPane.OK_CANCEL_OPTION,
		             JOptionPane.QUESTION_MESSAGE,
		             null, null, null
		            );
			if (JOptionPane.OK_OPTION == dialogResponse) {
				pGamma = slider.getValue();
			}
			else
				pGamma = 0;
			
			frame.setVisible(false);
			frame.dispose();
			
		}
		
		//Slider only works in integers, so I do 10-20 and reduce to 1.0-2.0
		pGamma = pGamma/10;
	}
	
	public void execute() {
		double[][] cncGrid = map.cncGrid();
		
		//Creates MarkovCLustering object
		MarkovClustering mcl = new MarkovClustering();
		//Creates SparseMatrix from the cncGrid, which I want to run MCL on
		SparseMatrix matrix = new SparseMatrix(cncGrid);
		//Runs MCL, mins set to 0.01, loopGain set to 1 (pretty sure this is normal), and pGamma to user input
		matrix = mcl.run(matrix, 0.01, pGamma, 1, 0.01);
		
		//Converts output matrix to double matrix
		cncGrid = matrix.getDense();
		
		//Matrix is wrong way for what I need, so I transpose it
		cncGrid = arrayFunctions.transposeMatrix(cncGrid);
		
		Cluster temp = null;
		
		//Creates clusters if there are values above 0 in its row
		//Adds the column of every value above 0 to that cluster
		for(int i = 0; i<cncGrid.length; i++)
		{
			temp = null;
			for(int j = 0; j<cncGrid[i].length; j++)
			{
				//If a concept already has a cluster keep going
				if(map.concepts.get(j).getCluster() != null)
					continue;
				
				//If value is above threshold, add to row's cluster
				if(cncGrid[i][j] > 0.01) {
					
					//If the row isn't a cluster yet, make it one and add itself
					if(temp == null) {
						temp = new Cluster(map.concepts.get(i));
						map.clusters.add(temp);
						if(i!=j)
							temp.addConcept(map.concepts.get(j));
					}
					else if(i!=j)
						temp.addConcept(map.concepts.get(j));		
				}
			}
		}
					
		
	}
	
	/**
	 * Creates JSlider object
	 * @param min	Minimum input value
	 * @param max	Maximum input value
	 * @return	The resulting JSlider object
	 */
	private static JSlider createSlider(int min, int max) {
		JSlider slider = new JSlider(min, max);
		slider.setMajorTickSpacing(5);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setValue(min);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(min, new JLabel("A Couple"));
		labelTable.put(max, new JLabel("A Lot"));
		slider.setLabelTable(labelTable);
		
		return slider;
	}
	
	/**
	 * Creates JPanel with JSlider inside
	 * @param slider	Slider to place
	 * @param label		Title of panel
	 * @return	The resulting JPanel object
	 */
	public static JPanel createSliderPanel(final JSlider slider, String label) {
        final JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new TitledBorder(label));
        p.setPreferredSize(new Dimension(300, 60));
        p.add(slider);
        return p;
    }

}
