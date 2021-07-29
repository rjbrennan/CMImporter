package mapstructs;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A concept is a single node to be added to a cluster
 * @author Riordan Brennan
 *
 */
public class Concept extends Node{
	private ArrayList<String> ids;
	private Cluster cluster;
	
	/**
	 * Creates Concept object given name and unique id
	 * @param name	Name of concept
	 * @param id	Unique id for concept object
	 */
	public Concept(String name, String id) {
		super(name, id);
		this.ids = new ArrayList<String>();
		this.ids.add(id);
		this.count = 1;
	}

	/**
	 * Get the index of a Concept in an ArrayList given an id
	 * @param cList	ArrayList to search through
	 * @param id	id to search for
	 * @return	index of Concept, -1 if it isn't there
	 */
	public static Concept[] indexOf(ArrayList<Concept> cList, String id) {
		Concept[] matches = new Concept[cList.size()];
		int i = -1;
		
		for(Concept cnc : cList) {
			for(String s : cnc.getIds())
				if(s.equals(id))
					matches[++i] = cnc;
		}
		if(i == -1)
			return null;
		
		return Arrays.copyOfRange(matches, 0, i+1);
	}
	
	/**
	 * Finds a concept from an ArrayList of concepts given a concept name
	 * @param cncList	ArrayList of concepts
	 * @param name		concept name
	 * @return	first instance of a concept with the specified name
	 */
	public static Concept get(ArrayList<Concept> cncList, String name) {
		for(Concept cnc : cncList)
			if(cnc.getName().equals(name))
				return cnc;
		return null;
	}
	
	/**
	 * Check a ArrayList for the name of a concept. Increments count if it is already there and adds id to id list
	 * @param cList	ArrayList to check against
	 * @param name	Name to check for, ignores case
	 * @param id	id to add to id list in case concept already exists
	 * @return	True if concept already exists, false otherwise
	 */
	public static boolean includes(ArrayList<Concept> cncList, String name, String id) {
		for(Concept cnc : cncList) {
			if(cnc.getId().equals(id))
				return true;
			else if(cnc.getName().equalsIgnoreCase(name)) {
				cnc.incrementCount();
				cnc.addIds(id);
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Finds a link from an ArrayList of link concepts given an id
	 * @param links		ArrayList of link concepts
	 * @param id		link id
	 * @return	first instance of a link concept with the specified name
	 */
	public static Concept getLink(ArrayList<Concept> links, String id) {
		for(Concept link : links)
			if(link.getId().equals(id))
				return link;
		return null;
	}

	/**
	 * @return	List of ids that had this same concept name
	 */
	public ArrayList<String> getIds() {
		return ids;
	}

	/**
	 * Add id to ids
	 * @param id	String of id to add
	 */
	public void addIds(String id) {
		for(String oldId : ids)
			if(id.equals(oldId))
				return;
				
		this.ids.add(id);
	}
	
	/**
	 * Increments count by 1
	 */
	public void incrementCount() {
		this.count++;
	}
	
	/**
	 * @return cluster this concept is a part of
	 */
	public Cluster getCluster() {
		return cluster;
	}

	/**
	 * Set the cluster this concept is a part of
	 * @param cluster	cluster to set
	 */
	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	@Override
	public String toString() {
		return "Concept [name=" + name + ", id=" + id + ", count=" + count + "]";
	}

}
