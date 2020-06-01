package wwwordz.shared;

import java.io.Serializable;

import wwwordz.shared.Table.Cell;

/**
 * Enum representing neighbor position coordinates, relative to a cell
 * 
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since April 2020
 */
enum Positions implements Serializable{
	UP(1,0), UP_RIGHT(1,1),
	RIGHT(0,1),
	DOWN_RIGHT(-1,1), DOWN(-1,0), DOWN_LEFT(-1,-1),
	LEFT(0,-1),
	UP_LEFT(1,-1);
	
	private int row, column;
	Positions() {}
	private Positions(int row,int column) {
		this.row = row;
		this.column = column;
	}
	
	public int getRow() {return this.row;}
	public int getColumn() {return this.column;}
	
	/**
	 * Method to calculate the true coordinate for the row 
	 * 	of this neighbor relative to cell
	 * @param Cell cell
	 * @return int row true coordinate
	 */
	public int calRow(Cell cell) {
		return row + cell.getRow();
	}
	
	/**
	 * Method to calculate the true coordinate for the column 
	 * 	of this neighbor relative to cell
	 * @param Cell cell
	 * @return int column true coordinate
	 */
	public int calCol(Cell cell) {
		return column + cell.getColumn();
	}
}
