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
	
	public static int numLines(int width, String term) {
		int numLines = 1;
		int curLine = 0;
		int wordLength = 0;
		String seperators = "!%)-}]:;\",.? ";
		
		for(char x : term.toCharArray()) {
			if(seperators.indexOf(x)>=0) {
				curLine += wordLength+1;
				wordLength = 0;
				if(curLine>width) {
					numLines++;
					curLine = 0;
				}
			}
			else {
				wordLength++;
				if(curLine+wordLength>width) {
					numLines++;
					curLine = 0;
				}
			}
				
		}
		
		return numLines;
	}

}
