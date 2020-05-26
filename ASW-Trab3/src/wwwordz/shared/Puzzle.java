package wwwordz.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import wwwordz.shared.Table;
import wwwordz.shared.Table.Cell;

/**
 * Class representing the puzzle
 *  This class contains the puzzle table and its list of solutions.
 * 
 * A table is a square grid of letters.
 * A solution is a word contained in the grid, where consecutive letters
 * 	are in neighboring cells on the grid and the letter in each cell is used only once
 * 
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since April 2020
 */
public class Puzzle implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<Solution> solutions;
	private Table table;
	
	/**
	 * Class that stores one solution for puzzle
	 * 	saves the solution word as well as the list
	 * 	of cells that form the word
	 */
	public static class Solution implements Serializable{
		private static final long serialVersionUID = 1L;
		private List<Cell> cells;
		private String word;
		
		public Solution(){
			 cells = new ArrayList<>();;
		}
		
		public Solution(String word, List<Cell> cells) {
			this.word = word;
			this.cells = cells;
		}
		
		/**
		 * Getter for solution word
		 */
		public String getWord() {
			return word;
		}
		
		/**
		 * Getter for list of cells forming the solution word
		 */
		public List<Cell> getCells(){
			return cells;
		}
		
		/**
		 * Method that recursively calculates points for word
		 * 
		 * Consider recursion:
		 * 	-> base: size less than 3 -> 0 points
		 * 	-> base: size == 3 -> 1 point
		 * 	-> points_n = 2 * points_(n-1) + 1
		 * 	
		 * @param int length of word
		 * @return int points
		 */
		private int getPoints(int length) {
			return length < 3? 0
					: length == 3? 1
					: 2 * getPoints(--length) + 1;
		}
		
		/**
		 * Calculate the points for the word
		 * 
		 * Call recursive getPoints for word
		 * @return int points
		 */
		public int getPoints() {
			return getPoints(word.length());
		}
	}
	
	public Puzzle() {
		table = new Table();
	}
	
	/**
	 * Getter for the table that has this solutions
	 */
	public Table getTable() {
		return table;
	}
	
	/**
	 * Set tables for solutions
	 */
	public void setTable(Table table) {
		this.table = table;
	}
	
	/**
	 * Getter for the list of all solutions to table
	 */
	public List<Solution> getSolutions(){
		return solutions;
	}
	
	/**
	 * Set solutions for table
	 * @param List<Solution> solutions
	 */
	public void setSolutions(List<Solution> solutions) {
		this.solutions = solutions;
	}
}
