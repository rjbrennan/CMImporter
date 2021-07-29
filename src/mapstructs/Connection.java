package mapstructs;
import java.util.ArrayList;

/**
 * A connection object connects two concepts
 * @author Riordan Brennan
 *
 */
public class Connection {
	private Concept from;
	private Concept to;
	private String id;
	private int count;

	/**
	 * Creates a connection from two concepts and an id
	 * @param from	Left Concept object
	 * @param to	Right Concept object
	 * @param id	String of unique id for Connection
	 */
	public Connection(Concept from, Concept to, String id) {
		super();
		this.from = from;
		this.to = to;
		this.id = id;
		this.count = 1;
	}
	
	/**
	 * Checks if this connection is already in a connection ArrayList.
	 * Increments count by 1 if it does
	 * @param cList	ArrayList of connection objects to check against
	 * @param from	left concept (orientation doesn't matter)
	 * @param to	right concept (orientation doesn't matter)
	 * @return	true if the connection is in the list, false otherwise
	 */
	public static boolean includes(ArrayList<Connection> cList, Concept from, Concept to) {
		
		for(Connection cnn : cList) {
			if(cnn.getFrom().equals(from) && cnn.getTo().equals(to) ||
			   cnn.getFrom().equals(to) && cnn.getTo().equals(from)) {
				cnn.incrementCount();
				return true;
			}
		}
		
		return false;
	}

	/**
	 * @return Left concept
	 */
	public Concept getFrom() {
		return from;
	}

	/**
	 * @return Right concept
	 */
	public Concept getTo() {
		return to;
	}
	
	public void setTo(Concept to) {
		this.to = to;
	}

	/**
	 * @return	Connection object's unique id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return	count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Increments count by 1
	 */
	public void incrementCount() {
		this.count++;
	}

	@Override
	public String toString() {
		return "Connection [from=" + from + ", to=" + to + ", id=" + id + ", count=" + count + "]";
	}

}
