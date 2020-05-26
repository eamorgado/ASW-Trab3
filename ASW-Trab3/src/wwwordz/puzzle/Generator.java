package wwwordz.puzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import wwwordz.shared.Puzzle.Solution;
import wwwordz.puzzle.Trie.Search;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Table;
import wwwordz.shared.Table.Cell;

/**
 * A puzzle generator
 * Creates a puzzle with many srambled words contained in a dictionary
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since April 2020
 */
public class Generator {
	private final Dictionary dictionary = Dictionary.getInstance();
	private Random rand = new Random();;
	public Generator() {}
	
	/**
	 * Generate a high quality puzzle with many words in it
	 * 
	 * Iterate over all free cells in a table
	 * Randomly select cells and assign to them a random word on the dictionary
	 * Calculate solutions for table
	 * Return the puzzle
	 * @return Puzzle
	 */
	public Puzzle generate() {
		rand = new Random();
		Table table = new Table();
		Puzzle puzzle = new Puzzle();
		HashMap<String,Cell> free = new HashMap<>();
		for(Cell cell : table.getEmptyCells()) {
			String key = cell.getRow() + "" + cell.getColumn();
			free.put(key,cell);
		}
		//For all available cells, get a random word and fill them
		while(!free.isEmpty()) {
			String word = dictionary.getRandomLargeWord();
			ArrayList<Cell> cells = new ArrayList<>(free.values());
			Cell c = cells.get(rand.nextInt(cells.size()));
			fillCellNeighbors(table,free,c,word,0);
		}
		puzzle.setTable(table);
		//Calculate solutions for this table
		List<Solution> solutions = getSolutions(table);
		//Update puzzle solutions
		puzzle.setSolutions(solutions);
		return puzzle;
	}
	
	/**
	 * Method to recursively save a word to neighboring cells
	 * 	Save current unsaved letter to cell
	 * 	Randomly pick valid neighbors to save next letters
	 * 	Neighbors have to be free or have consumed desired letter
	 * 
	 * @param Table table
	 * @param Cell to fill
	 * @param String word being saved in puzzle
	 * @param int index for letter to be saved
	 */
	private void fillCellNeighbors(Table table,HashMap<String,Cell> free, Cell cell, String word, int index) {
		int r, c;
		r = cell.getRow(); c = cell.getColumn();
		cell.setLetter(word.charAt(index));
		if(free.containsKey(r+""+c))
			free.remove(r+""+c);
		//word has next char?
		if(++index < word.length()) {
			//Randomize order of neighbors
			List<Cell> neighbors = new ArrayList<>(table.getNeighbors(cell));
			Collections.shuffle(neighbors);
			
			//Iterate over neighbors, if free or have same letter as current in index, fill
			//Use for in case all cells fail to prevent loop
			for(Cell neighbor : neighbors) {
				if(neighbor.isEmpty() || neighbor.getLetter() == word.charAt(index)) {
					fillCellNeighbors(table,free,neighbor,word,index);
					break;
				}
			}			
		}		
	}
	
	/**
	 * Return a list of solutions for this table
	 * 
	 * Solutions have at least 2 letters
	 * Different solutions for same word are discarded
	 * 
	 * Iterate over cells, start dictionary search checking if letter exists
	 * Get neighbors, for each check if search can continue
	 * 
	 * If word if found, add to list of solutions
	 * Keeps a list of visited cells 
	 * @param Table to find solutions
	 * @return list of solutions for table
	 */
	List<Solution> getSolutions(Table table){
		List<Solution> solutions = new ArrayList<>();
		for(Cell cell : table) {
			//Mark beginning of search
			Search search = dictionary.startSearch();
			//List of used cells
			List<Cell> used_cells = new ArrayList<>();
			getSolutions(table,search,used_cells,cell,"",solutions);
		}
		return solutions;
	}
	
	/**
	 * Check if given list of solutions already contains a word
	 * @param solutions
	 * @param word
	 * @return
	 */
	private boolean contains(List<Solution> solutions, String word) {
		for(Solution solution:solutions)
			if(solution.getWord().equals(word))
				return true;
		return false;
	}
	
	private void getSolutions(Table t,Search sh,List<Cell> used,Cell cell,String seq,List<Solution> sols) {
		//Check is cell was used
		if(used.contains(cell)) return;
		//Mark used
		used.add(cell);
		
		//Check if we can continue search
		if(!sh.continueWith(cell.getLetter())) return;
		//Add to word
		seq += cell.getLetter();
		
		//Consider solution => unique words with more than 2 letters 
		if(!contains(sols,seq) && sh.isWord() && seq.length() > 2)
			sols.add(new Solution(seq,used));
		
		for(Cell neighbor : t.getNeighbors(cell)) {
			List<Cell> used_extend = new ArrayList<>(used);
			String seq_ext = seq;
			Search sh_ext = new Search(sh);
			getSolutions(t,sh_ext,used_extend,neighbor,seq_ext,sols);			
		}
	}
	
	
	Puzzle random() {
		Puzzle puzzle = new Puzzle();
		int size = puzzle.getTable().getSize();
		String words[] = new String[size];
		
		for(int i = 0; i < size; i++) {
			String word = "";
			for(int j = 0; j < size; j++)
				word += "" + (char) (rand.nextInt(26) + 'A');
			words[i] = word;
		}
		
		//Gen table based on words and update puzzle
		Table table = new Table(words);
		puzzle.setTable(table);
		puzzle.setSolutions(getSolutions(table));
		return puzzle;
	}
	
}
