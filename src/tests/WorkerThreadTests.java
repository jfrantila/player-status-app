package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ParallelProcessing.WorkerThread;
import UpdateDTO.Update;
import model.Player;

public class WorkerThreadTests {
	private static WorkerThread worker;
	private static Player player1;
	private static Player player2;
	private static final String PLAYER_NAME1 = "testPlayer1";
	private static final String PLAYER_NAME2 = "testPlayer2";
	private static List<Player> testPlayers = new ArrayList<>();
	private static List<Update> player1PrevUpdates = new ArrayList<>();
	
	@BeforeAll
	static void setup() {
		player1 = new Player(PLAYER_NAME1, player1PrevUpdates, new HashMap<String,String>());
		player2 = new Player(PLAYER_NAME2);
		testPlayers.add(player1); testPlayers.add(player2);
		worker = new WorkerThread("noFile", testPlayers);
	}
	
	@Test
	void testprocessUpdate() {
		Update firstUpdate = new Update("update", PLAYER_NAME2, 1, Map.of("foo", "foo"));
		worker.processUpdate(firstUpdate);
		
		//Should add if player has no previous updates
		assertEquals(1, player2.getUdList().size());
		//Update goes to the right player
		assertEquals(0, player1.getUdList().size());
		assertEquals(1, player2.getUdList().size());

		Update ud1 = new Update("update", PLAYER_NAME1, 100, Map.of("foo", "bar"));
		player1PrevUpdates.add(ud1);

		Update ud3 = new Update("update", PLAYER_NAME1, 100, Map.of("foo", "bar"));
		worker.processUpdate(ud3);
		
		//Skip duplicates
		assertEquals(1, player1.getUdList().size());
		
		Update ud4 = new Update("update", PLAYER_NAME1, 1, Map.of("foo", "notBar"));
		worker.processUpdate(ud4);
		
		//Skip older state updates
		assertEquals(1, player1.getUdList().size());
		assertEquals("bar", player1.getUdList().get(0).getValues().get("foo"));
		
		Update ud5 = new Update("update", PLAYER_NAME1, 22, Map.of("boo", "far"));
		worker.processUpdate(ud5);
		
		//Add with another key
		assertEquals(2, player1.getPlayersKeys().length);
		assertEquals("far", player1.getUdList().get(1).getValues().get("boo"));
	}
	
	@Test
	void testProcessUpdate_withIllegalValues() {
		Update illegalUpdate = new Update("update", PLAYER_NAME2, 1, Map.of("foo", "foo", "boo", "hoo"));
		worker.processUpdate(illegalUpdate);
		
		//Won't take into consideration the second entry
		assertTrue(player2.getUdList().get(0).getValues().containsKey("foo"));
		assertFalse(player2.getUdList().get(0).getValues().containsKey("boo"));
		assertEquals(1, player2.getPlayersKeys().length);
	}
	
	@Test
	void testGetPlayerWithName() {
		assertNull(worker.getPlayerWithName("abc"));
		assertEquals(PLAYER_NAME1, worker.getPlayerWithName(PLAYER_NAME1).getName());
		assertEquals(PLAYER_NAME2, worker.getPlayerWithName(PLAYER_NAME2).getName());
	}
}
