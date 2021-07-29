package mapstructs;

/**
 * Superclass to concept and cluster classes
 * @author Riordan Brennan
 *
 */
public class Node {
	
	String name;
	String id;
	int count;
	
	/**
	 * @param name	name of node
	 * @param id	unique id of node
	 */
	public Node(String name, String id) {
		super();
		this.name = name;
		this.id = id;
		this.count = 0;
	}

	/**
	 * @return	name of node
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the name of the node
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return unique id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return count
	 */
	public int getCount() {
		return count;
	}

	@Override
	public String toString() {
		return "Node [name=" + name + ", id=" + id + "]";
	}

}
