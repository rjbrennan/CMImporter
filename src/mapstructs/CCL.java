package mapstructs;

import javax.swing.JOptionPane;

import annexFunctions.arrayFunctions;

/**
 * Closest Cluster method. Uses the following steps to cluster the concepts:
 * <blockquote>
 * <ol>
 * <li>User-defined topic is the first cluster with only its own node in it.
 *     No other nodes can be added to this cluster</li>
 * <li>Take the n most popular nodes, assign as clusters</li>
 * <li>Take the unassigned node with the largest differential in distance between 
 * 	   the two clusters it is most connected to and assign it to the cluster it is 
 * 	   more connected to. All clusters tied in differential will be added to their 
 * 	   respective clusters simultaneously. If the differential is 0, the most
 * 	   connected node will be added to one of its clusters at random</li>
 * <li>Repeat step 3 until no nodes that are connected to an already existing 
 * 	   cluster (besides the topic cluster) are left</li>
 * <li>Of the remaining nodes, assign the most popular to be a cluster</li>
 * <li>Add all nodes connected to the new cluster to the cluster</li>
 * <li>Repeat steps 5-6 until no nodes are left</li>
 * </ol>
 * </blockquote>
 * @author rjbrennan
 *
 */
public class CCL {
	
	private Map map;
	private int numClu;
	
	/**
	 * Creates CCL object and asks user for the number of initial clusters they would like
	 * @param map	Concept map to run CCL on
	 */
	public CCL(Map map) {
		this.map = map;
		
		String numDialog = "Enter number of map.clusters (min. 2)";
		int numClu = 0;
		while(numClu<2) {
			try {
				numClu = Integer.parseInt(JOptionPane.showInputDialog(null, numDialog, "Clustering", JOptionPane.QUESTION_MESSAGE));
			}
			catch(NumberFormatException e) {
				numDialog = "Please enter a whole number";
			}
		}
		
		this.numClu = numClu;
	}
	
	/**
	 * runs the CCL process
	 */
	public void execute() {
		
		Concept[] initClu = new Concept[numClu];
		boolean gotTopic = false;
		
		//Initializes the topic cluster as well as the initial clusters
		//Adds concepts to the concept array, pushes out the smallest conecpt if the array is full
		for(Concept cnc : map.concepts) {
			if(cnc.equals(map.topicCnc)) {
				map.clusters.add(new Cluster(cnc));
				gotTopic = true;
				continue;
			}
			for(int i = 0; i < numClu; i++) {
				if(initClu[i] == null)
				{
					initClu[i] = cnc;
					break;
				}
				else if(cnc.getCount() > initClu[i].getCount())
				{
					initClu = arrayFunctions.move(initClu, cnc, i);
					break;
				}
			}
				
		}
		
		//Throws an error if the topic isn't found
		//Shouldn't happen with other error checking, but added just in case
		if(!gotTopic) {
			//TODO throw error
		}
		
		//Takes concept array created earlier and makes ever concept in it into a cluster
		for(Concept cnc : initClu) {
			map.clusters.add(new Cluster(cnc));
		}
		
		//Outer loop for added concepts to clusters, checks if there are any unassigned concepts left
		//If yes, then it keeps running
		while(map.cncLeft()) {
			
			//Initializing necessary variables
			double[][] cncGrid = map.cncGrid();
			int[][] cluGrid = map.cluGrid(cncGrid);
			int clu1, clu2, diff, tieCount;
			int[] winCnc = arrayFunctions.fill(new int[cluGrid.length], -1);
			int[] winClu = arrayFunctions.fill(new int[cluGrid.length], -1);
			
			//Inner loop for adding concepts, keeps going until no concepts are connected to any clusters
			while(true) {
				diff = -1;
				tieCount = 0;
				
				//Finding the highest differentials
				for(int i = 0; i<cluGrid.length; i++) {
					
					//If a concept is already in a cluster we ignore it
					if(map.concepts.get(i).getCluster() != null)
						continue;
					
					clu1 = clu2 = -1;
					
					//Finds the two closest clusters, stores their indices in clu1 and clu2
					for(int j = 1; j<cluGrid[i].length; j++) {
						if(clu1 < 0) {
							clu2 = clu1;
							clu1 = j;
						}
						else if(cluGrid[i][j] > cluGrid[i][clu1]) {
							clu2 = clu1;
							clu1 = j;
						}
						else if(clu2 < 0)
							clu2 = j;
						else if(cluGrid[i][j] > cluGrid[i][clu2])
							clu2 = j;
					}
					
					//If the differential is bigger than the largest differential found so far
					//Make this the new differential, clear the winning arrays, add this one, and reset the tie count
					if(cluGrid[i][clu1]-cluGrid[i][clu2] > diff) {
						tieCount = 0;
						diff = cluGrid[i][clu1]-cluGrid[i][clu2];
						winCnc = arrayFunctions.fill(winCnc, -1); winClu = arrayFunctions.fill(winClu, -1);
						winCnc[tieCount] = i; winClu[tieCount] = clu1;
					}
					
					//If the differential is the same as the largest differential so far
					//Add this one to the winning arrays and increment the tie count
					else if(cluGrid[i][clu1]-cluGrid[i][clu2] == diff) {
						tieCount++;
						winCnc[tieCount] = i; winClu[tieCount] = clu1;
					}
					
				}
				
				//Checks if all concepts left are not connected to any cluster
				//or if there are no concepts left. Breaks loop if true.
				if(cluGrid[winCnc[0]][winClu[0]] == 0 || winCnc[0] == -1)
					break;
				
				//If there is a tie, find the most connected cluster and add it to a random cluster it is closest to.
				//This works because if the highest diff is a tie, there shouldn't be any non-ties left
				if(diff == 0) {
					winCnc = arrayFunctions.fill(winCnc, -1); winClu = arrayFunctions.fill(winClu, -1);
					winCnc[0] = map.mostConnected(cncGrid);
					winClu[0] = arrayFunctions.max(cluGrid[winCnc[0]]);
				}
				
				//Takes all the concepts chosen and adds them to their corresponding clusters
				for(int i = 0; i<winCnc.length; i++){
					
					//Error checking, should never happen
					if(winCnc[i] < 0) 
						break;
					map.clusters.get(winClu[i]).addConcept(map.concepts.get(winCnc[i]));
					
					//Updates the cluster grid to add the connections between concepts and 
					//concepts recently added to a cluster
					cluGrid = map.updateCluGrid(cluGrid, cncGrid, winCnc[i], winClu[i]);
				}
			}
			
			//If there are no concepts left, break out of the loop
			if(!map.cncLeft())
				break;
			
			//Take the most popular concept left and make it a cluster
			Concept cnc = map.mostPopular();
			Cluster clu = new Cluster(cnc);
			
			//Marks the cluster as an 'extra', this will affect how it is displayed
			clu.setExtra(true);
			map.clusters.add(clu);
			
			//Adds all concepts connected to the new cluster to the cluster
			//TODO make this faster by separating the extra cluster part from the main part
			for(Connection cnn : map.connections) {
				if(cnn.getFrom().equals(cnc)
						&& cnn.getTo().getCluster() == null)
					clu.addConcept(cnn.getTo());
				else if(cnn.getTo().equals(cnc) 
						&& cnn.getFrom().getCluster() == null)
					clu.addConcept(cnn.getFrom());
			}
			
		}
		
	}

}
