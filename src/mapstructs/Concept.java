package mapstructs;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author Riordan Brennan
 *
 */
public class Concept extends Node{
	private ArrayList<String> ids;
	private Cluster cluster;
	
	/**
	 * Creates Concept object given name and unique id
	 * @param name	Name of Concept
	 * @param id	Unique id for Concept object
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
		
		return Arrays.copyOfRange(matches, 0, i+1);
	}
	
	/**
	 * Check a ArrayList for the name of a Concept. Increments count if it is already there and adds id to id list
	 * @param cList	ArrayList to check against
	 * @param name	Name to check for, ignores case
	 * @param id	id to add to id list in case Concept already exists
	 * @return	True if Concept already exists, false otherwise
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
	 * @return	List of ids that had this same Concept name
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
	 * @return Cluster this Concept is a part of
	 */
	public Cluster getCluster() {
		return cluster;
	}

	/**
	 * Set the Cluster this Concept is a part of
	 * @param cluster	Cluster to set
	 */
	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	@Override
	public String toString() {
		return "Concept [name=" + name + ", id=" + id + ", cluster=" + cluster.getName() + ", count=" + count + "]";
	}

}
