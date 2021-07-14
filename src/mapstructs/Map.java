package mapstructs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import annexFunctions.arrayFunctions;
import annexFunctions.fileFunctions;

/**
 * 
 * @author Riordan Brennan
 *
 */
public class Map {
	
	final static int MARGIN = 7;
	final static int CHAR_WIDTH = 6;
	
	private int radius = 0;
	
	private ArrayList<Cluster> clusters;
	private ArrayList<ClusterConnection> clConnections;
	private ArrayList<Concept> concepts;
	private ArrayList<Connection> connections;
	
	private Concept topicCnc;
	
	/**
	 * 	Constructs a Map object given you already have all the elements built. 
	 * 	Almost never useful
	 */
	public Map(ArrayList<Cluster> clusters, 
			   ArrayList<ClusterConnection> clConnections, 
			   ArrayList<Concept> concepts,
			   ArrayList<Connection> connections) {
		super();
		this.clusters = clusters;
		this.clConnections = clConnections;
		this.concepts = concepts;
		this.connections = connections;
	}
	
	/**
	 * 	Constructs a Map object given a folder of .cxl files
	 * 
	 * 	@param	folder	the pathname of the folder of .cxl files
	 * 	@throws	IOException	if the pathname doesn't exist or isn't a folder
	 */
	public Map(File directoryPath) throws IOException {
		this.concepts = new ArrayList<Concept>();
		this.connections = new ArrayList<Connection>();
		this.clusters = new ArrayList<Cluster>();
		this.clConnections = new ArrayList<ClusterConnection>();
		
		Path fileName; String cxl; String cncString; String cnnString;
		
		String[] contents = directoryPath.list();
		
		for(String file : contents) {
			System.out.println(file);
			if(file.endsWith(".cxl")) {
				fileName = Path.of(directoryPath + "/" + file);
				cxl = Files.readString(fileName);
				
				cncString = fileFunctions.cncStrip(cxl);
				cnnString = fileFunctions.cnnStrip(cxl);
				
				this.cncReader(cncString);
				this.cnnReader(cnnString);
			}
		}
	}
		
	/**
	 * Execute the collective concept map process
	 * @param output	File to place the finished .cxl file
	 * @param pGamma	Inflation value for Markov Clustering
	 * @throws IOException
	 */
	public void execute(File output, int numClu) throws IOException {
		
		this.cluBuilder(numClu);
		
		this.clcBuilder();
		
		this.positionCalculator();
		
		String guts = this.mapCxl()+"\n"+
					  this.cluCxl()+"\n"+this.clcCxl()+"\n"+
					  this.cluCxlStyle()+"\n"+this.clcCxlStyle();
		
		File template = new File("Template.cxl");
		
		fileFunctions.writeCollectiveMap(template, output, guts);
	}
	
	/**
	 * Tests if a topic is in the map's concept list
	 * @param topic	topic to test
	 * @return		True if the topic is in the list, false otherwise
	 */
	public boolean inMap(String topic) {
		for(Concept cnc : concepts)
			if(cnc.getName().equalsIgnoreCase(topic)) {
				topicCnc = cnc;
				return true;
			}
		return false;
	}
	
	/**
	 * Turns string of .cxl concepts into Concept objects and adds them to concepts
	 * @param concepts	string of .cxl concepts, created by cncStrip()
	 */
	private void cncReader(String concepts) {
		String id; String name;
		
		while(concepts.indexOf("<concept") >= 0) {
			concepts = concepts.substring(concepts.indexOf("<concept")+8);
			id = concepts.substring(concepts.indexOf("id=")+4, concepts.indexOf("\" "));
			name = concepts.substring(concepts.indexOf("label=")+7, concepts.indexOf("\"/"));
			if(!Concept.includes(this.concepts, name, id))
				this.concepts.add(new Concept(name, id));
		}
	}
	
	/**
	 * Turns string of .cxl connections into Connection objects and adds them to connections.
	 * Must have already ran cncReader() or have a filled concepts object to work
	 * @param connections	string of .cxl connections, created by cnnStrip()
	 */
	private void cnnReader(String connections) {
		String id; String from; String to;
		Concept fromCnc; Concept toCnc;
		
		while(connections.indexOf("<connection") >= 0) {
			connections = connections.substring(connections.indexOf("<connection")+11);
			id = connections.substring(connections.indexOf("id=")+4, connections.indexOf("\" f"));
			from = connections.substring(connections.indexOf("from-id=")+9, 
										 connections.indexOf("\" t"));
			to = connections.substring(connections.indexOf("to-id=")+7, 
									   connections.indexOf("\"/"));
			fromCnc = this.concepts.get(Concept.indexOf(this.concepts, from));
			toCnc = this.concepts.get(Concept.indexOf(this.concepts, to));
			if(!Connection.includes(this.connections, fromCnc, toCnc))
				this.connections.add(new Connection(fromCnc, toCnc, id));
		}
	}

	/**
	 * Clusters concepts based on popularity algorithm
	 * Asks user for concept map topic and starting number of nodes
	 */
	private void cluBuilder(int numClu) {
		
		Concept[] initClu = new Concept[numClu];
		boolean gotTopic = false;
		
		for(Concept cnc : concepts) {
			if(cnc.equals(topicCnc)) {
				clusters.add(new Cluster(cnc));
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
		
		for(Concept cnc : initClu)
			clusters.add(new Cluster(cnc));
		
		//System.out.println(this.printClusters());
		
		int[][] cncGrid = this.cncGrid();
		
		int[][] cluGrid = this.cluGrid(cncGrid);
		
		int clu1, clu2, diff, tieCount;
		int[] winCnc = arrayFunctions.fill(new int[cluGrid.length], -1);
		int[] winClu = arrayFunctions.fill(new int[cluGrid.length], -1);
		while(true) {
			diff = -1;
			tieCount = 0;
			
			for(int i = 0; i<cluGrid.length; i++) {
				if(concepts.get(i).getCluster() != null)
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
				
				//System.out.println(arrayFunctions.print(winCnc));
				//System.out.println(arrayFunctions.print(winClu));
				
			}
			//System.out.println("---");
			if(cluGrid[winCnc[0]][winClu[0]] == 0 || winCnc[0] == -1)
				break;
			
			if(diff == 0) {
				winCnc = arrayFunctions.fill(winCnc, -1); winClu = arrayFunctions.fill(winClu, -1);
				winCnc[0] = mostConnected(cncGrid);
				winClu[0] = arrayFunctions.max(cluGrid[winCnc[0]]);
			}
			
			for(int i = 0; i<winCnc.length; i++){
				if(winCnc[i] < 0) 
					break;
				clusters.get(winClu[i]).addConcept(concepts.get(winCnc[i]));
				cluGrid = updateCluGrid(cluGrid, cncGrid, winCnc[i], winClu[i]);
			}
		}
		
		while(cncLeft()) {
			Concept cnc = mostPopular();
			Cluster clu = new Cluster(cnc);
			clusters.add(clu);
			for(Connection cnn : connections) {
				if(cnn.getFrom().equals(cnc) 
						&& cnn.getTo().getCluster() == null)
					clu.addConcept(cnn.getTo());
				else if(cnn.getTo().equals(cnc) 
						&& cnn.getFrom().getCluster() == null)
					clu.addConcept(cnn.getFrom());
			}
		}
	}
	
	/**
	 * Makes a grid of integers, each box records the count of connections 
	 * between the x and y elements
	 * @return	2-dimensional integer array
	 */
	private int[][] cncGrid() {
		int[][] grid = new int[concepts.size()][concepts.size()];
		for(int i = 0; i<grid.length; i++) {
			for(int j = 0; j<grid[i].length; j++) {
				for(Connection cnn : connections)
					if((cnn.getFrom().equals(concepts.get(i)) && cnn.getTo().equals(concepts.get(j))) ||
					   (cnn.getTo().equals(concepts.get(i)) && cnn.getFrom().equals(concepts.get(j)))) {
						grid[i][j] = cnn.getCount();
						break;
					}
			}
		}
		
		return grid;
	}
	
	/**
	 * Makes a grid of integers, each box records the connections between
	 * a concept and a cluster
	 * @param cncGrid	2-dim int array made by cncGrid()
	 * @return	2-dimensional integer array
	 */
	private int[][] cluGrid(int[][] cncGrid) {
		int[][] grid = new int[concepts.size()][clusters.size()];
		Cluster clu;
		
		for(int i = 0; i<grid.length; i++) {
			if(concepts.get(i).equals(topicCnc))
				continue;
			for(int j = 0; j<cncGrid[i].length; j++) {
				clu = concepts.get(j).getCluster();
				if(clu != null && !(clu.equals(topicCnc)))
					grid[i][clusters.indexOf(clu)] += cncGrid[i][j];
			}
		}
		
		return grid;
	}
	
	/**
	 * Updates the cluster grid made by cluGrid()
	 * @param cluGrid	2-dim int array made by cluGrid()
	 * @param cncGrid	2-dim int array made by cncGrid()
	 * @param cnc		Index of concept to update the grid off of
	 * @param clu		Index of cluster to update the grid for
	 * @return			The transformed 2-dim int array, cluGrid
	 */
	private int[][] updateCluGrid(int[][] cluGrid, int[][] cncGrid, int cnc, int clu) {
		
		for(int j = 0; j<cncGrid[cnc].length; j++) {
			cluGrid[j][clu] += cncGrid[cnc][j];
		}
		
		return cluGrid;
	}
	
	/**
	 * Finds the most connected concept not yet assigned to a cluster
	 * @param cncGrid	2-dim int array made by cncGrid()
	 * @return			Index of the most connected concept
	 */
	private int mostConnected(int[][] cncGrid) {
		int mostCon = 0; int mostCount = 0; int tempCount;
		for(int i = 0; i<cncGrid.length; i++) {
			if(concepts.get(i).getCluster() != null)
				continue;
			tempCount = arrayFunctions.sum(cncGrid[i]);
			if(tempCount > mostCount) {
				mostCon = i;
				mostCount = tempCount;
			}
		}
		
		return mostCon;
	}
	
	/**
	 * Finds the most popular concept not yet assigned to a cluster
	 * @return	The most popular concept with no cluster
	 */
	public Concept mostPopular() {
		Concept mostPop = null;
		for(Concept cnc : concepts) {
			if(cnc.getCluster() != null)
				continue;
			if(mostPop == null)
				mostPop = cnc;
			else if(cnc.getCount()>mostPop.getCount())
				mostPop = cnc;
		}
		
		return mostPop;
	}
	
	/**
	 * @return	Returns true if there is a concept that still needs
	 * to be assigned to a cluster, false otherwise
	 */
	private boolean cncLeft() {
		for(Concept cnc : concepts)
			if(cnc.getCluster() == null)
				return true;
		return false;
	}
	
	/**
	 * Fills clConnections and each Clusters' internalConnections.
	 * Must have filled clusters object to work
	 */
	private void clcBuilder() {
		Cluster a; Cluster b;
		for(Connection cnc : connections) {
			a = cnc.getFrom().getCluster(); b = cnc.getTo().getCluster();
			if(a.equals(b))
				a.addInternalConnection(cnc);
			else
				if(!ClusterConnection.includes(clConnections, cnc))
					this.clConnections.add(new ClusterConnection(cnc,"id"));
		}
		
		for(ClusterConnection clc : clConnections) {
			clc.getFrom().incrementEdgeCount(clc.getCount());
			clc.getTo().incrementEdgeCount(clc.getCount());
		}
	}
	
	/**
	 * Calculates the radius of the map for the .cxl file
	 */
	private void positionCalculator() {
		int longest = 0;
		for(Cluster clu : clusters)
			if(clu.getName().length() > longest)
				longest = clu.getName().length();
		
		radius = ((Map.MARGIN*2 + Map.CHAR_WIDTH*longest)*clusters.size())/4;
		
		if(radius<50)
			radius = 50;
	}
	
	/**
	 * Formats the .cxl map line
	 */
	private String mapCxl() {
		return "    <map width=\""+radius*3+"\" height=\""+radius*2.5+"\">";
	}
	
	/**
	 * Formats the .cxl concept block
	 */
	private String cluCxl() {
		String combine = "\t<concept-list>";
		for(Cluster clu : clusters)
			combine = combine.concat("\n\t    "+clu.toCxl());
		return combine.concat("\n\t</concept-list>");
	}
	
	/**
	 * Formats the .cxl concept appearance block
	 */
	private String cluCxlStyle() {
		String combine = "\t<concept-appearance-list>";
		for(int i = 0; i<clusters.size(); i++)
			combine = combine.concat("\n\t    "+clusters.get(i).toCxlStyle(radius,i,clusters.size()));
		return combine.concat("\n\t</concept-appearance-list>");
	}
	
	/**
	 * Formats the .cxl connection block
	 */
	private String clcCxl() {
		String combine = "\t<connection-list>";
		for(ClusterConnection clc : clConnections)
			combine = combine.concat("\n\t    "+clc.toCxl());
		return combine.concat("\n\t</connection-list>");
	}
	
	/**
	 * Formats the .cxl connection appearance block
	 */
	private String clcCxlStyle() {
		String combine = "\t<connection-appearance-list>";
		for(ClusterConnection clc : clConnections)
			combine = combine.concat("\n\t    "+clc.toCxlStyle());
		return combine.concat("\n\t</connection-appearance-list>");
	}

	/**
	 * @return	ArrayList of Cluster objects
	 */
	public ArrayList<Cluster> getClusters() {
		return clusters;
	}

	/**
	 * @return ArrayList of ClusterConnection objects
	 */
	public ArrayList<ClusterConnection> getClConnections() {
		return clConnections;
	}

	/**
	 * @return ArrayList of Concept objects
	 */
	public ArrayList<Concept> getConcepts() {
		return concepts;
	}

	/**
	 * @return ArrayList of Connection objects
	 */
	public ArrayList<Connection> getConnections() {
		return connections;
	}
	
	/**
	 * @return formated string of clusters
	 */
	public String printClusters() {
		String combined = "";
		for(Cluster clu : clusters)
			combined = combined.concat(clu.toString()).concat("\n");
		
		return combined.trim();
	}
	
	/**
	 * @return formated string of clConnections
	 */
	public String printClConections() {
		String combined = "";
		for(ClusterConnection clc : clConnections)
			combined = combined.concat(clc.getFrom().getName()+"\t"
									   +clc.getTo().getName()+"\t"
									   +clc.getCount()+"\n");
		
		return combined.trim();
	}
	
	/**
	 * @return formated string of concepts
	 */
	public String printConcepts() {
		String combined = "";
		for(Concept cnc : concepts)
			combined = combined.concat(cnc.getName()+"\t"+cnc.getCount()+"\n");
		
		return combined.trim();
	}
	
	/**
	 * @return formated string of connections
	 */
	public String printConnections() {
		String combined = "";
		for(Connection cnn : connections)
			combined = combined.concat(cnn.getFrom().getName()+"\t"
									   +cnn.getTo().getName()+"\t"
									   +cnn.getCount()+"\n");
		
		return combined.trim();
	}

}
