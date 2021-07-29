package annexFunctions;

import java.util.ArrayList;

import mapstructs.Concept;

/**
 * Functions used in the collective concept map program that involve arrays
 * @author Riordan Brennan
 *
 */
public class arrayFunctions {
	
	/**
	 * Finds the index of the highest number in an int array. 
	 * If multiple indices have the same value and are the highest number in the array, choose a random one
	 * @param array	int array
	 * @return	The index of the highest number
	 */
	public static int max(int[] array) {
		ArrayList<Integer> max = new ArrayList<Integer>();
		max.add(0);
		for(int i = 1; i<array.length; i++) {
			if(array[i] > array[max.get(0)]) {
				max.clear();
				max.add(i);
			}
			else if(array[i] == array[max.get(0)]) {
				max.add(i);
			}
		}
		int r = (int) Math.floor(Math.random()*max.size());
		System.out.println(r);
		return max.get(r);
	}
	
	/**
	 * Fills an int array with a specified value
	 * @param array	array
	 * @param fill	specified value
	 * @return	filled array
	 */
	public static int[] fill(int[] array, int fill) {
		for(int i = 0; i<array.length; i++)
			array[i] = fill;
		return array;
	}
	
	/**
	 * Formats an int array in a printable format
	 * @param array array
	 * @return	Formated string
	 */
	public static String print(int[] array) {
		String combine = "{";
		for(int i : array)
			combine = combine + i + ",";
		
		return combine+"}";
	}
	
	/**
	 * Formats a double matrix in a printable format
	 * @param matrix	matrix
	 * @return	Formated string
	 */
	public static String print(double[][] matrix) {
		String combine = "";
		for(double[] row : matrix) {
			for(double j : row)
				combine = combine.concat(String.format("%1.2f,", j));
			combine = combine.concat("\n");
		}
		
		return combine;
	}
	
	/**
	 * Find the sum of a double array
	 * @param cncGrid	double array
	 * @return	sum
	 */
	public static int sum(double[] cncGrid) {
		int sum = 0;
		for(double i : cncGrid)
			sum += i;
		return sum;
	}
	
	/**
	 * Transposes a double matrix
	 * @param m	matrix
	 * @return	transposed matrix
	 */
	public static double[][] transposeMatrix(double [][] m) {
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }
	
	/**
	 * Adds an element to an array by moving everything to the right 
	 * and removing the last element
	 * @param array	array
	 * @param cnc	concept to add
	 * @param i		index to place the new element in
	 * @return		transformed array
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
