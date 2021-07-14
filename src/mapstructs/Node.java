package mapstructs;

/**
 * 
 * @author Riordan Brennan
 *
 */
public class Node {
	
	String name;
	String id;
	int count;
	
	/**
	 * @param name	Name of Node
	 * @param id	Unique id of Node
	 */
	public Node(String name, String id) {
		super();
		this.name = name;
		this.id = id;
		this.count = 0;
	}

	/**
	 * @return	Name of node
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the Node object
	 * @param name	Name String to set to
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Unique id of Node
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return Current count of Node
	 */
	public int getCount() {
		return count;
	}

	@Override
	public String toString() {
		return "Node [name=" + name + ", id=" + id + "]";
	}

}
