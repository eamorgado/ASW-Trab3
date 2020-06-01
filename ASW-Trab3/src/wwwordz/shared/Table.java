package wwwordz.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import wwwordz.shared.Table.Cell;
import wwwordz.shared.Positions;

/**
 * Class representing the a puzzle table.
 * 	A table is a collection of cells each indexed by row and column.
 * 
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since April 2020
 */
public class Table implements Iterable<Cell>, Serializable{
	private static final long serialVersionUID = 1L;
	private static final int SIZE = 4;
	private Cell[][] table;
	
	/**
	 * Nested class representing enclosing table
	 */
	public static class Cell implements Serializable{
		private static final long serialVersionUID = 1L;
		private int row, column;
		private char letter;
		
		/**
		 * Empty Constructor
		 * @param void
		 */
		Cell(){}
		
		/**
		 * Create empty cell given row and column
		 * @param int row
		 * @param int column
		 */
		Cell(int row, int column){
			this.row = row;
			this.column = column;
			this.letter = ' ';
		}
		
		/**
		 * Create cell given its letter and row and column
		 * @param int row
		 * @param int column
		 * @param char letter
		 */
		Cell(int row, int column, char letter){
			this(row,column);
			this.letter = letter;
		}
		
		/**
		 * This method returns the letter in the cell
		 * @param void
		 * @return char
		 */
		public char getLetter() {return letter;}
		
		/**
		 * Method to return row of cell
		 * @param void
		 * @return int
		 */
		public int getRow() {return row;}
		
		/**
		 * Method to return column of cell
		 * @param void
		 * @return int
		 */
		public int getColumn() {return column;}
				
		/**
		 * Method that checks if cell is empty/blank
		 * @param void
		 * @return boolean
		 */
		public boolean isEmpty() {return letter == ' ';}
		
		/**
		 * Method to set letter of cell
		 * @param char letter
		 * @return void
		 */
		public void setLetter(char lett) {this.letter = lett;}
		
		@Override
		public int hashCode() {
			return Objects.hash(column, letter, row);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof Cell)) {
				return false;
			}
			Cell other = (Cell) obj;
			return column == other.getColumn() && letter == other.getLetter() && row == other.getRow();
		}
		
		@Override
		public String toString() {
			return "[(" + row + "," + column + ")," + letter +"]";
		}		
	}
	
	/**
	 * Iterator over cells in the table
	 */
	private class CellIterator implements Iterator<Table.Cell>{
		private int row;
		private int column;
		
		CellIterator(){
			row = column = 1;
		}
		
		@Override
		public boolean hasNext() {
			return row <= SIZE;
		}
		
		@Override
		public Cell next() {
			Cell cell = table[row][column++];
			if(column > SIZE) {
				//Margin reached => reset column, move to new line
				column = 1;
				row++;
			}
			return cell;
		}
		
		@Override
		public void remove() {
			Cell cell = table[row][column];
			cell.setLetter(' ');
			if(column == 1) {
				column = SIZE - 1;
				row = row > 1? --row : 1;
			}else column--;
		}
	}
	
	
	/**
	 * Default constructor, initializes table with empty cells
	 */
	public Table(){
		table = new Cell[SIZE+2][SIZE+2]; //+2 for safe search
		for(int r = 1; r <= SIZE; r++)
			for(int c = 1; c <= SIZE; c++)
				table[r][c] = new Cell(r,c);
	}
	
	/**
	 * Constructor that initializes table given a sequence of words
	 * @param data
	 */
	public Table(String[] data) {
		table = new Cell[SIZE+2][SIZE+2]; //+2 for safe search
		for(int r = 1; r <= SIZE; r++)
			for(int c = 1; c <= SIZE; c++)
				table[r][c] = new Cell(r,c,data[r-1].charAt(c-1));
	}
	
	public Iterator<Cell> iterator(){
		return new CellIterator();
	}
	
	/**
	 * Method that returns the letter of a cell at a given row and column
	 * @param row
	 * @param column
	 * @return char
	 */
	public char getLetter(int row, int column) {
		return table[row][column].getLetter();
	}
	
	/**
	 * Method to set a letter at a given row and column
	 * @param row
	 * @param column
	 * @param letter
	 */
	public void setLetter(int row, int column,char letter) {
		table[row][column].setLetter(letter);
	}
	
	/**
	 * Method that returns a list with the empty cells in the table
	 * @return List of empty cells
	 */
	public List<Cell> getEmptyCells(){
		List<Cell> empty = new ArrayList<>();
		for(int r = 1; r <= SIZE; r++)
			for(int c = 1; c <= SIZE; c++)
				if(table[r][c].isEmpty()) empty.add(table[r][c]);
		return empty;
	}
	
	/**
	 * Method that returns up to 8 valid neighboring cells
	 * 	of the given cell
	 * This method relies on the support structure wwwordz.shared.Positions
	 * 	a structure that stores the coordinates for each neighboring cell
	 * 	according to the current cell coordinates
	 * @param Cell cell - the cell to which we want to find the neighboring cells
	 * @return List<Cell> - list of the valid/existing neighboring cells
	 */
	public List<Cell> getNeighbors(Cell cell){
		int r, c;
		List<Cell> neighbors = new ArrayList<>();
		for(Positions pos : Positions.values())
			if(table[(r = pos.calRow(cell))][(c = pos.calCol(cell))] != null)
				neighbors.add(table[r][c]);
		return neighbors;
	}
	
	/**
	 * Method that returns the cell at a given row and column
	 * @param row
	 * @param column
	 * @return Cell 
	 */
	public Cell getCell(int row, int column){
		return table[row][column];
	}
	
	public int getSize() {
		return SIZE;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(table);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Table other = (Table) obj;		
		Iterator<Cell> it_this = this.iterator();
		Iterator<Cell> it_other = other.iterator();
		
		while(it_this.hasNext() && it_other.hasNext()) {
			if(!(it_this.next().equals(it_other.next())))
				return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(int r = 1; r <= SIZE; r++) {
			for(int c = 1; c <= SIZE; c++) 
				s += Character.toString(this.getCell(r,c).getLetter());
			s += '\n';
		}
		return s;
	}
}
