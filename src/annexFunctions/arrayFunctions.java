package annexFunctions;

import mapstructs.Concept;

public class arrayFunctions {
	
	/**
	 * @param array	int array to find the max of
	 * @return	The index of the int array with the highest value 
	 */
	public static int max(int[] array) {
		int max = 0;
		for(int i = 0; i<array.length; i++)
			if(array[i] > array[max])
				max = i;
		return max;
	}
	
	/**
	 * Fills an int array with an int
	 * @param array	Array to fill
	 * @param fill	Int to fill array with
	 * @return	The filled array
	 */
	public static int[] fill(int[] array, int fill) {
		for(int i = 0; i<array.length; i++)
			array[i] = fill;
		return array;
	}
	
	/**
	 * Formats an int array to print
	 * @param array	Array to format
	 * @return	Formated string to print
	 */
	public static String print(int[] array) {
		String combine = "{";
		for(int i : array)
			combine = combine + i + ",";
		
		return combine+"}";
	}
	
	/**
	 * @param array	int array to sum
	 * @return	The sum of the int array
	 */
	public static int sum(int[] array) {
		int sum = 0;
		for(int i : array)
			sum += i;
		return sum;
	}
	
	/**
	 * Adds an element to an array by moving everything to the right 
	 * and removing the last element
	 * @param array	Array to add the new element to
	 * @param cnc	Concept to add
	 * @param i		Index to place the new element in
	 * @return		The transformed array
	 */
	public static Concept[] move(Concept[] array, Concept cnc, int i) {
		array[array.length-1] = null;
		for(int j = array.length-2; j>=i; j--) {
			array[j+1] = array[j];
		}
		array[i] = cnc;
		
		return array;
	}

}
