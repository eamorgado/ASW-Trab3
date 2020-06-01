package wwwordz.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wwwordz.shared.Puzzle;
import wwwordz.shared.Puzzle.Solution;
import wwwordz.shared.Table;
import wwwordz.shared.Table.Cell;

/**
 * Class responsible by handling all game logic interaction
 * 	by providing it with the server returned puzzle it will
 * 	build a map with all its solutions where the keys are the
 * 	letters in the word and the respective row and col for each of the
 * 	cells in the sequence allowing it to distinguish between equal words
 * 	with different sequences.
 * 
 * 	Once a word is selected it removes it from the possible words
 * 
 * 	This class will keep a list of the letters in the current sequence
 * 	and the cell sequence.
 * 
 * 	When a user double clicks this class will validate the word as well as
 * 	the sequence and if they are valid it will calculate it's points and 
 * 	add them to the game session as well as removing all clicked data and 
 * 	the possible solution
 * 
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since May 2020
 */
public class GameMechanics {
	private Puzzle puzzle;
	private Map<String,Solution> valid_solutions;
	private List<String> clicked;
	private List<String> cells;
	private int points;
	
	private static GameMechanics instance;
	
	private GameMechanics() {
		valid_solutions = new HashMap<>();
		clicked = new ArrayList<>();
		cells = new ArrayList<>();
	}
	
	public static GameMechanics getInstance() {
		if(instance == null) instance = new GameMechanics();
		return instance;
	}
	
	/**
	 * This method takes a puzzle and build the map of keys and solutions
	 * @param p
	 * @return
	 */
	public boolean addPuzzle(Puzzle p) {
		instance.points = 0;
		instance.clearValid();
		instance.clearClicked();
		instance.puzzle = p;
		List<Solution> solutions = p.getSolutions();
		//generate solution id [word + [cell.r + cell.y] for all cells in solution]
		for(Solution solution : solutions) {
			String word = solution.getWord();
			String cells_ids = "";
			for(Cell cell : solution.getCells())
				cells_ids += cell.getRow() + "" + cell.getColumn();
			String id = word + cells_ids;
			valid_solutions.put(id,solution);
		}
		
		return instance.valid_solutions != null 
			&& instance.clicked != null
			&& instance.cells != null;
	}
	
	/**
	 * Resetes all possible solutions
	 */
	public void clearValid() {
		instance.valid_solutions.clear();
	}
	
	/**
	 * This method, given the selected letter and its row and column
	 * 	adds it to the sequence observers for the user chose the letter
	 * @param letter
	 * @param r
	 * @param c
	 */
	public void addClicked(String letter,int r, int c) {
		instance.clicked.add(letter);
		instance.cells.add(r+""+c);
	}
	
	/**
	 * Resets sequence observer
	 */
	public void clearClicked() {
		instance.clicked.clear();
		instance.cells.clear();
	}
	
	/**
	 * This method returns the current word in the sequence observers
	 * @return
	 */
	public String getWord() {
		String word = "";
		for(String letter : instance.clicked)
			word += letter;
		return word;
	}
	
	/**
	 * This method returns the sequential cells chosen so far
	 * @return
	 */
	public String getCellSeq() {
		String cell_seq = "";
		for(String cell : instance.cells)
			cell_seq += cell;
		return cell_seq;
	}
	
	/**
	 * Returns the id for the current selected word (word ++ [cells seq ids]
	 * @return
	 */
	public String getWordId() {
		return instance.getWord() + instance.getCellSeq();
	}

	public boolean isSolutionValid(String id) {
		return instance.valid_solutions.containsKey(id);
	}
	
	/**
	 * This method returns the points for the current selected word
	 * @param id
	 * @return
	 */
	public int getPoints(String id) {
		if(instance.isSolutionValid(id))
			return instance.valid_solutions.get(id).getPoints();
		return 0;
	}
	
	/**
	 * This method removes the solution with given id from all possible solutions
	 * @param id
	 */
	public void removeSolution(String id) {
		if(instance.isSolutionValid(id))
			instance.valid_solutions.remove(id);
	}
	
	/**
	 * This method handles the sequence submission
	 * 		-> retrieve stored word
	 * 		-> validate
	 * 			-> Calculate points
	 * 			-> clear sequence data and remove solution
	 * @return
	 */
	public int submitSolution() {
		//get Id
		String id = instance.getWordId();
		instance.clearClicked();
		if(id != null && instance.isSolutionValid(id)) {
			//get points
			int p = instance.getPoints(id);
			//clear input sequence
			//remove solution from valid/available
			instance.removeSolution(id);
			instance.points += p;
			return p;
		}
		return -1;
	}
	
	public Puzzle getPuzzle() {
		return instance.puzzle;
	}
	
	public Table getTable() {
		return instance.puzzle.getTable();
	}
	
	/**
	 * Returns total points for user in current game session
	 * @return
	 */
	public int getTotalPoints() {
		return instance.points;
	}
}
