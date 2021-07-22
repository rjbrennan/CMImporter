package mapstructs;

import java.util.ArrayList;

public class Link extends Node{
	
	public Link(String name, String id) {
		super(name, id);
	}

	public static Concept get(ArrayList<Concept> links, String id) {
		for(Concept link : links)
			if(link.getId().equals(id))
				return link;
		return null;
	}

}
