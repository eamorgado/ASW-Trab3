package wwwordz.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wwwordz.shared.Positions;
import wwwordz.shared.Table.Cell;


/**
 * Test class for Positions enum
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since April 2020
 */
public class PositionsTest {
	private static final int NUM_POS = 8;
	private static final int[] LINES = {1,1,0,-1,-1,-1,0,1};
	private static final int[] COLS  = {0,1,1,1,0,-1,-1,-1};
	private static final String[] NAMES = {
				"UP","UP_RIGHT","RIGHT","DOWN_RIGHT",
				"DOWN","DOWN_LEFT","LEFT","UP_LEFT"
				};
	
	@Test
	@DisplayName("Integrity test")
	void testIntegrity() {
		assertEquals(NUM_POS,Positions.values().length,"Positions expected 8 positions");
		for(String name : NAMES)
			assertNotNull(Positions.valueOf(name),"Expected valid position");
		int i = 0;
		for(Positions p : Positions.values())
			assertEquals(NAMES[i++],p.name(),"Expected position in order");
	}
	
	@Test
	@DisplayName("getRow test")
	void testGetRow() {
		int i = 0;
		for(Positions p : Positions.values())
			assertEquals(LINES[i++],p.getRow(),"Rows are not equal");
	}
	
	@Test
	@DisplayName("getColumn test")
	void testGetColumn() {
		int i = 0;
		for(Positions p : Positions.values())
			assertEquals(COLS[i++],p.getColumn(),"Columns are not equal");
	}
	
	@Test
	@DisplayName("calRow test")
	void testCalRow() {
		int r, c;
		r = c = 2;
		Cell cell = new Cell(r,c);
		int[] expected = new int[NUM_POS];
		for(int i = 0; i < NUM_POS; i++) 
			expected[i] = LINES[i] + r;
		
		int i = 0;
		for(Positions p : Positions.values())
			assertEquals(expected[i++],p.calRow(cell),"Expected correct row calculation");		
	}
	
	@Test
	@DisplayName("calCol test")
	void testCalCol() {
		int r, c;
		r = c = 2;
		Cell cell = new Cell(r,c);
		int[] expected = new int[NUM_POS];
		for(int i = 0; i < NUM_POS; i++) 
			expected[i] = COLS[i] + r;
		
		int i = 0;
		for(Positions p : Positions.values())
			assertEquals(expected[i++],p.calCol(cell),"Expected correct col calculation");		
	}

}
