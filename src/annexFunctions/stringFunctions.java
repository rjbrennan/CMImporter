package annexFunctions;

/**
 * Functions used in the collective concept map program that involve strings
 * @author Riordan Brennan
 *
 */
public class stringFunctions {
	
	/**
	 * Splits a string into multiple if it is a list, such that:
	 * <blockquote>
	 * "apples, oranges, and bananas" --> {"apples", "oranges", "bananas"}
	 * </blockquote>
	 * Currently assumes Oxford Comma is used
	 * @param name	phrase to split up
	 * @return	array of parts of the phrase
	 */
	public static String[] splitCnc(String name) {
		int numTerms = numChar(name, ',');
		String[] names = new String[numTerms+1];
		
		//Case if only two terms ("apples and oranges")
		if(numTerms==0 && name.indexOf(" and ")>=0) {
			names = new String[2]; 
			names[0] = name.substring(0, name.indexOf(" and "));
			names[1] = name.substring(name.indexOf(" and ")+5);
			return names;
		}
		
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
	
	/**
	 * Counts the number of occurrences of a character in a string
	 * @param name
	 * @param key
	 * @return	number of occurrences
	 */
	public static int numChar(String name, char key) {
		int numTerms = 0;
		for(char x : name.toCharArray())
			if(x==key)
				numTerms++;
		return numTerms;
	}
	
	/**
	 * Counts how many lines a string takes up if a line has a max character count
	 * @param width	max character count
	 * @param term	string
	 * @return	number of lines
	 */
	public static int numLines(int width, String term) {
		int numLines = 1;
		int curLine = 0;
		int wordLength = 0;
		String separators = "!%)-}]:;\",.? ";
		
		for(char x : term.toCharArray()) {
			
			//If a character is in separators we consider anything after a new word
			if(separators.indexOf(x)>=0) {
				curLine += wordLength+1;
				wordLength = 0;
				if(curLine>width) {
					numLines++;
					curLine = 0;
				}
			}
			
			/*
			 * Continues current word, if current word + the rest of the line is 
			 * greater than the max width we move the current word to the next line
			 * */
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
