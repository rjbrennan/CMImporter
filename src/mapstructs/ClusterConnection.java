package mapstructs;

import java.util.ArrayList;

/**
 * A cluster connection is a connection between two clusters
 * @author Riordan Brennan
 *
 */
public class ClusterConnection {
	
	private static int idCount = 0;
	final static String ID_PREFIX = "CLC";
	
	private Cluster from;
	private Cluster to;
	private String id;
	private int count;
	
	/**
	 * Creates a cluster connection with a connection between two clusters and an id
	 * @param co	Connection object between two concepts in separate clusters
	 * @param id	Unique id for this cluster connection
	 */
	public ClusterConnection(Connection co, String id) {
		super();
		this.from = co.getFrom().getCluster();
		this.to = co.getTo().getCluster();
		this.id = ClusterConnection.ID_PREFIX + String.format("%08d", idCount++);
		this.count = co.getCount();
	}
	
	/**
	 * Checks if a cluster connection already exists in a ArrayList,
	 * if it does, add the count of the tested connection to the existing cluster connection
	 * @param clList	cluster connection ArrayList to be checked against
	 * @param co	connection object to be tested
	 * @return	true if cluster connection already exists, false otherwise
	 */
	public static boolean includes(ArrayList<ClusterConnection> clList, Connection co) {
		for(ClusterConnection clc : clList) {
			if(clc.getFrom().equals(co.getFrom().getCluster()) && clc.getTo().equals(co.getTo().getCluster()) ||
			   clc.getFrom().equals(co.getTo().getCluster()) && clc.getTo().equals(co.getFrom().getCluster())) {
				clc.incrementCount(co.getCount());
				return true;
			}
		}
		
		return false;
	}

	/**
	 * @return	cluster on the left side of the cluster connection
	 */
	public Cluster getFrom() {
		return from;
	}
	
	/**
	 * @return	cluster on the right side of the cluster connection
	 */
	public Cluster getTo() {
		return to;
	}

	/**
	 * @return	id of cluster connection
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return	Get the current count of connections between the two clusters
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Increments the count of connections between the two clusters
	 * @param count	int of amount to increment by
	 */
	public void incrementCount(int count) {
		this.count += count;
	}

	@Override
	public String toString() {
		return "ClusterConnection [from=" + from + ", to=" + to + ", id=" + id + ", count=" + count + "]";
	}
	
	/**
	 * Formats the connection .cxl line
	 */
	public String toCxl() {
		return "<connection id=\""+this.getId()+
						"\" from-id=\""+this.getFrom().getId()+
						"\" to-id=\""+this.getTo().getId()+"\"/>";
	}
	
	/**
	 * Formats the connection appearance .cxl line
	 */
	public String toCxlStyle() {
		if(this.getCount() == 1)
			return "<connection-appearance id=\""+this.getId()+
				   "\" from-pos=\"center\" to-pos=\"center\""+
				   " style=\"dashed\" arrowhead=\"no\"/>";
		return "<connection-appearance id=\""+this.getId()+
				   "\" from-pos=\"center\" to-pos=\"center\""+
				   " style=\"solid\" thickness=\""+this.getCount()/2+"\" arrowhead=\"no\"/>";
	}

	
	
	
	
	
}
