package mapstructs;

import java.util.ArrayList;

import annexFunctions.stringFunctions;

/**
 * 
 * @author Riordan Brennan
 *
 */
public class Cluster extends Node {
	
	static int idCount = 0;
	static final String ID_PREFIX = "CLU";
	
	private ArrayList<Concept> concepts;
	private ArrayList<Connection> internalConnections;
	private int edgeCount;
	private boolean extra = false;
	int y = 0;

	/**
	 * Create new cluster object
	 * @param concept	The initial concept to be added. 
	 * The cluster will be named after it.
	 */
	public Cluster(Concept concept) {
		super(concept.getName(), Cluster.ID_PREFIX+String.format("%08d", idCount++));
		this.concepts = new ArrayList<Concept>();
		this.addConcept(concept);
		this.internalConnections = new ArrayList<Connection>();
		this.edgeCount = 0;
	}

	/**
	 * @return	ArrayList of Concepts in this Cluster
	 */
	public ArrayList<Concept> getConcepts() {
		return concepts;
	}
	
	/**
	 * Refreshes the count of this Cluster 
	 */
	public void setCount() {
		this.count = 0;
		for(Concept c : this.getConcepts())
			this.count += c.getCount();
	}
	
	/**
	 * Adds edges to the cluster
	 * @param edge number of edges to add
	 */
	public void incrementEdgeCount(int edge) {
		edgeCount += edge;
	}
	
	/**
	 * @return the edgeCount variable
	 */
	public int getEdgeCount() {
		return edgeCount;
	}
	
	public void setExtra(boolean b) {
		this.extra = b;
	}
	
	public boolean getExtra() {
		return extra;
	}
	
	/**
	 * Adds a Concept object to the Cluster and updates the Concept object's cluster parameter.
	 * Also updates this Cluster's count
	 * @param c	Concept to be added
	 */
	public void addConcept(Concept c) {
		concepts.add(c);
		c.setCluster(this);
		this.count += c.getCount();
	}
	
	/**
	 * Adds a connection between to Concept objects in this cluster
	 * to the internalConnections ArrayList
	 * @param co	Connection to be added
	 */
	public void addInternalConnection(Connection co) {
		this.internalConnections.add(co);
	}
	
	@Override
	public String toString() {
		String combined = name+"\t"+count;
		for(Concept cnc : concepts)
			combined = combined.concat("\n\t"+cnc.getName()+"\t"+cnc.getCount());
		
		return combined;
	}
	
	public String toStringCxl() {
		String combined = "Topics:";
		for(Concept cnc : concepts)
			combined = combined.concat("&#xa;"+cnc.getName()+" ["+cnc.getCount()+"]");
		
		return combined;
	}
	
	/**
	 * Formats the concept .cxl line
	 */
	public String toCxl() {
		return "<concept id=\""+this.getId()+"\" label=\""+this.getName()+
			   "\" short-comment=\""+this.getCount()+"&#xa;e:"+this.getEdgeCount()+
			   "&#xa;"+this.toStringCxl()+"\" />";
	}
	
	/**
	 * Formats the cluster style .cxl line
	 * @param radius	the radius, in pixels, of the circle concept map
	 * @param i			the clusters ordinal value in the map
	 * @param n			the total number of clusters in the map		
	 */
	public String toCxlStyle(int radius, int i, int n) {
		int x = (int) (radius * Math.cos(((2*Math.PI*i)/n)+(3*Math.PI/2))+(1.5*radius));
		int y = (int) (radius * Math.sin(((2*Math.PI*i)/n)+(3*Math.PI/2))+(1.25*radius));
		return "<concept-appearance id=\""+this.getId()+
				"\" x=\""+x+"\" y=\""+y+"\" width=\"33\" height=\"24\""+
				" background-color=\"255,200,0,255\" border-color=\"0,0,0,255\""+
				" border-shape=\"oval\" shadow-color=\"none\"/>";
	}
	
	public String toCxlStyleExtra(Map map, int yLast, int hLast) {
		int x = (int) (map.radius*2.5 + map.longestPix*1.5);
		this.y = (int) (yLast+
					   	(0.5*hLast*Map.HEIGHT)+
					   	Map.HEIGHT+
					   	(0.5*stringFunctions.numLines(map.longestWidth, this.name)*Map.HEIGHT));
		
		int y = (int) (this.y+(map.radius*0.25));
		
		
		return "<concept-appearance id=\""+this.getId()+
				"\" x=\""+x+"\" y=\""+y+"\" width=\""+map.longestPix+
				"\" background-color=\"255,200,0,255\" border-color=\"0,0,0,255\""+
				" border-shape=\"rectangle\" shadow-color=\"none\" min-width=\""+map.longestPix+
				"\" min-height=\""+Map.HEIGHT+"\" max-width=\""+map.longestPix+"\"/>";
		
				
	}

}
