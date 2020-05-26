package wwwordz.game;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wwwordz.TestData;
import wwwordz.puzzle.Generator;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

@DisplayName("Manager")
public class ManagerTest extends TestData {
	private static final int REPEAT = 100;
	static final long STAGE_DURATION = 100;
	static final long SLACK = 20;
	Manager manager;
	
	/**
	 * Set stage durations in round before any tests
	 */
	@BeforeAll
	public static void prepare() {
		Round.setJoinStageDuration(STAGE_DURATION);
		Round.setPlayStageDuration(STAGE_DURATION);
		Round.setReportStageDuration(STAGE_DURATION);
		Round.setRankingStageSuration(STAGE_DURATION);		
	}
	
	
	/**
	 * Get an instance for testing and wait for beginning of round
	 */
	@BeforeEach
	public void before()  throws  InterruptedException {
		manager = Manager.getInstance();
		manager.round = new Round();
	}
	
	
	/**
	 * Test values to start a next play stage
	 * @throws InterruptedException 
	 */
	@Test
	@DisplayName("Time to next play")
	public void testGetTimeToNextPlay() throws InterruptedException {
		long time = manager.timeToNextPlay();
		assertTrue(time <= STAGE_DURATION,"Less them stage duration");
		Thread.sleep(time-SLACK);
		
		time = manager.timeToNextPlay();
		assertTrue(time <= SLACK , "Just slack time before play");
	}
	

	/**
	 * Test a sequence of rounds, and for each one
	 * 1) register user and check time to start playing
	 * 3) get puzzle and check its a new one
	 * 4) add random points
	 * 5) get ranking and check expected accumulated points 
	 * @throws WWWordzException 
	 * @throws InterruptedException
	 */
	@Test
	@DisplayName("Rounds")
	public void TestRounds() throws InterruptedException, WWWordzException {
		
		long time = manager.register(NICK, PASSWORD);
		assertTrue(time>0,"Positive value expected");
		time = manager.timeToNextPlay();
		Thread.sleep(time);
		
		Puzzle p = new Generator().generate();
		Set<Puzzle> puzzles = new HashSet<>();
		puzzles.add(p);
		assertTrue(puzzles.add(manager.getPuzzle()),"This puzzle was already generated");
		
		Thread.sleep(STAGE_DURATION);
		
		int acum = 0;
		Random rand = new Random();
		for(int i = 0; i < REPEAT; i++) {
			int points = rand.nextInt((REPEAT - 1) + 1) - 1;
			acum += points;
			manager.setPoints(NICK,points);
		}
		
		Thread.sleep(STAGE_DURATION);
		
		List<Rank> ranks = manager.getRanking();
		assertNotNull(ranks,"Expected list of players");
		assertEquals(acum,ranks.get(0).getAccumulated(),"Invalid accumulation");
	}
	
}
