package tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import UpdateDTO.Update;
import model.Player;

class PlayerTests {
	private static Map<String,String> testUpdates = new LinkedHashMap<>();
	private static List<Update> testUdList = new ArrayList<>();
	private static Player player;
	private static final String PLAYER_NAME = "testPlayer";
	
	@BeforeAll
	static void setup() {
		player = new Player(PLAYER_NAME, testUdList, testUpdates);
	}

	@Test
	void testFilterFinalState() {
		Map<String, String> sameValues = Map.of("foo", "bar");
		Update ud1 = new Update("update", PLAYER_NAME, 100, sameValues);
		Update ud2 = new Update("update", PLAYER_NAME, 200, sameValues);
		Update ud3 = new Update("update", PLAYER_NAME, 2, Map.of("foo", "notBar"));
		Update ud4 = new Update("update", PLAYER_NAME, 300, Map.of("boo", "far"));
		Update ud5 = new Update("update", PLAYER_NAME, 1000, Map.of("boo", "hoo"));
		
		testUdList.add(ud1); testUdList.add(ud2); testUdList.add(ud3);
		testUdList.add(ud4); testUdList.add(ud5);
		
		player.filterFinalState();
		//Should add only the latest with the same key
		assertEquals(2, player.getUpdates().size());
		assertEquals("bar", player.getUpdates().get("foo"));
		assertEquals("hoo", player.getUpdates().get("boo"));
	}
	
	@Test
	void testGetPlayerKeys() {
		testUdList.add(new Update("update", PLAYER_NAME, 2, Map.of("foo", "doo")));
		testUdList.add(new Update("update", PLAYER_NAME, 10, Map.of("foo", "boo")));
		testUdList.add(new Update("update", PLAYER_NAME, 2, Map.of("boo", "hoo")));
		
		assertEquals(2, player.getPlayersKeys().length);
		//Checking alphabetical order
		assertEquals("boo", player.getPlayersKeys()[0]);
	}
}
