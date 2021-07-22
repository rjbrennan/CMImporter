package mapstructs;
import java.util.ArrayList;

/**
 * 
 * @author Riordan Brennan
 *
 */
public class Connection {
	private Concept from;
	private Concept to;
	private String id;
	private int count;

	/**
	 * Creates a Connection object from two Concept objects and an id
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
	 * Checks if this Connection is already in the Connection ArrayList.
	 * Increments count by if it does
	 * @param cList	ArrayList of Connection objects to check against
	 * @param from	Left Concept Object (orientation doesn't matter)
	 * @param to	Right Concept Object (orientation doesn't matter)
	 * @return
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
	 * @return Left Concept object
	 */
	public Concept getFrom() {
		return from;
	}

	/**
	 * @return Right Connection object
	 */
	public Concept getTo() {
		return to;
	}
	
	public void setTo(Concept to) {
		this.to = to;
	}

	/**
	 * @return	Connection object's unique id String
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return	count of Connection object
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
