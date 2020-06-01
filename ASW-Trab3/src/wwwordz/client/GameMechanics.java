package wwwordz.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wwwordz.shared.Puzzle;
import wwwordz.shared.Puzzle.Solution;
import wwwordz.shared.Table;
import wwwordz.shared.Table.Cell;

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
	
	public void clearValid() {
		instance.valid_solutions.clear();
	}
	
	public void addClicked(String letter,int r, int c) {
		instance.clicked.add(letter);
		instance.cells.add(r+""+c);
	}
	
	public void clearClicked() {
		instance.clicked.clear();
		instance.cells.clear();
	}
	
	public String getWord() {
		String word = "";
		for(String letter : instance.clicked)
			word += letter;
		return word;
	}
	
	public String getCellSeq() {
		String cell_seq = "";
		for(String cell : instance.cells)
			cell_seq += cell;
		return cell_seq;
	}
	
	public String getWordId() {
		return instance.getWord() + instance.getCellSeq();
	}

	public boolean isSolutionValid(String id) {
		return instance.valid_solutions.containsKey(id);
	}
	
	public int getPoints(String id) {
		if(instance.isSolutionValid(id))
			return instance.valid_solutions.get(id).getPoints();
		return 0;
	}
	
	public void removeSolution(String id) {
		if(instance.isSolutionValid(id))
			instance.valid_solutions.remove(id);
	}
	
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
	
	public int getTotalPoints() {
		return instance.points;
	}
}
