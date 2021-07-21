package mapstructs;

import javax.swing.JOptionPane;

import annexFunctions.arrayFunctions;

public class CCL {
	
	private Map map;
	private int numClu;
	
	public CCL(Map map) {
		this.map = map;
		
		String numDialog = "Enter number of map.clusters (min. 2)";
		int numClu = 0;
		while(numClu<2) {
			try {
				numClu = Integer.parseInt(JOptionPane.showInputDialog(numDialog));
			}
			catch(NumberFormatException e) {
				numDialog = "Please enter a whole number";
			}
		}
		
		this.numClu = numClu;
	}
	
	public void execute() {
		
		Concept[] initClu = new Concept[numClu];
		boolean gotTopic = false;
		
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
		
		if(!gotTopic) {
			//throw error
		}
		
		for(Concept cnc : initClu) {
			System.out.println("Init Cluster: "+cnc.getName());
			map.clusters.add(new Cluster(cnc));
		}
		
		//System.out.println(this.printmap.clusters());
		while(map.cncLeft()) {
			double[][] cncGrid = map.cncGrid();
			
			int[][] cluGrid = map.cluGrid(cncGrid);
			
			int clu1, clu2, diff, tieCount;
			int[] winCnc = arrayFunctions.fill(new int[cluGrid.length], -1);
			int[] winClu = arrayFunctions.fill(new int[cluGrid.length], -1);
		
			while(true) {
				diff = -1;
				tieCount = 0;
				
				for(int i = 0; i<cluGrid.length; i++) {
					if(map.concepts.get(i).getCluster() != null)
						continue;
					clu1 = clu2 = -1;
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
					if(cluGrid[i][clu1]-cluGrid[i][clu2] > diff) {
						tieCount = 0;
						diff = cluGrid[i][clu1]-cluGrid[i][clu2];
						//System.out.println(diff);
						winCnc = arrayFunctions.fill(winCnc, -1); winClu = arrayFunctions.fill(winClu, -1);
						winCnc[tieCount] = i; winClu[tieCount] = clu1;
					}
					else if(cluGrid[i][clu1]-cluGrid[i][clu2] == diff) {
						tieCount++;
						winCnc[tieCount] = i; winClu[tieCount] = clu1;
					}
					
				}
				//System.out.println("---");
				if(cluGrid[winCnc[0]][winClu[0]] == 0 || winCnc[0] == -1)
					break;
				
				if(diff == 0) {
					winCnc = arrayFunctions.fill(winCnc, -1); winClu = arrayFunctions.fill(winClu, -1);
					winCnc[0] = map.mostConnected(cncGrid);
					winClu[0] = arrayFunctions.max(cluGrid[winCnc[0]]);
				}
				
				for(int i = 0; i<winCnc.length; i++){
					if(winCnc[i] < 0) 
						break;
					map.clusters.get(winClu[i]).addConcept(map.concepts.get(winCnc[i]));
					cluGrid = map.updateCluGrid(cluGrid, cncGrid, winCnc[i], winClu[i]);
				}
			}
			
			
			Concept cnc = map.mostPopular();
			System.out.println("Extra Cluster: "+cnc.getName());
			Cluster clu = new Cluster(cnc);
			clu.setExtra(true);
			map.clusters.add(clu);
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
