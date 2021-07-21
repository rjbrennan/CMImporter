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

public class MCL {
	
	private Map map;
	private double pGamma;
	
	public MCL(Map map) {
		this.map = map;
		
		pGamma = 0;
		while(pGamma<=0) {
			JFrame frame = new JFrame();
			JSlider slider = createSlider(10, 40);
			JPanel sliderPanel = createSliderPanel(slider, "myMessage");
			String title = "myTitle";
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
		
		pGamma = pGamma/10;
	}
	
	public void execute() {
		double[][] cncGrid = map.cncGrid();
		
		MarkovClustering mcl = new MarkovClustering();
		SparseMatrix matrix = new SparseMatrix(cncGrid);
		matrix = mcl.run(matrix, 0.01, pGamma, 1, 0.01);
		
		cncGrid = matrix.getDense();
		
		cncGrid = arrayFunctions.transposeMatrix(cncGrid);
		
		Cluster temp = null;
		for(int i = 0; i<cncGrid.length; i++)
		{
			temp = null;
			for(int j = 0; j<cncGrid[i].length; j++)
				if(cncGrid[i][j] > 0.1) {
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
	
	/**
	 * Creates JSlider object
	 * @param min	Minimum input value
	 * @param max	Maximum input value
	 * @return	The resulting JSlider object
	 */
	private static JSlider createSlider(int min, int max) {
		JSlider slider = new JSlider(min, max);
		slider.setMajorTickSpacing(10);
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
