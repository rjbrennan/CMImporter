package annexFunctions;

public class stringFunctions {
	
	public static String[] splitCnc(String name) {
		int numTerms = numChar(name, ',');
		String[] names = new String[numTerms+1];
		int i = 0;
		
		while(name.indexOf(',')>=0) {
			names[i++] = name.substring(0, name.indexOf(',')).strip();
			name = name.substring(name.indexOf(',')+1).strip();
		}
			
		name = name.strip();
		if(name.startsWith("and "))
			name = name.substring(4);
		names[i] = name;
		return names;
	}
	
	public static int numChar(String name, char key) {
		int numTerms = 0;
		for(char x : name.toCharArray())
			if(x==key)
				numTerms++;
		return numTerms;
	}

}
