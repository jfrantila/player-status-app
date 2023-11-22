package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import UpdateDTO.Update;

public class Player {
	private String name;
	private Map<String,String> updates = new LinkedHashMap<>();
	private List<Update> udList = new ArrayList<>();
	
	
	public List<Update> getUdList() {
		return udList;
	}

	public void setUdList(List<Update> udList) {
		this.udList = udList;
	}

	public Player(String name, List<Update> udList, Map<String,String> updates) {
		this.name = name;
		this.udList = udList;
		this.updates = updates;
	}
	
	public Player(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addUpdateToList(Update ud) {
		this.udList.add(ud);
	}

	public Map<String, String> getUpdates() {
		return updates;
	}

	public void setUpdates(Map<String, String> updates) {
		this.updates = updates;
	}
	
	/**
	 * Retrieves the status keys which player has updated in alphabetical order 
	 * @return the keys player has updated
	 */
	public String[] getPlayersKeys() {
		Set<String> keySet = new HashSet<>();
		for (Update ud : udList) {
			ud.getValues().keySet().forEach(key -> keySet.add(key));
		}
		String[] keys = keySet.toArray(new String[0]);
		Arrays.sort(keys);
		return keys;
	}
	
	public void filterFinalState() {
		String[] keys = getPlayersKeys();

		for (String key : keys) {
			List<Update> eachKey = new ArrayList<>();
			Update latest = null;
			
			for (Update ud : udList) {
				if (ud.getValues().containsKey(key)) {
					eachKey.add(ud);
				}
			}
			
			for (Update udWithKey : eachKey) {
				if (latest == null) {
					latest = udWithKey;
				} else {
					if (udWithKey.getTimestamp() >= latest.getTimestamp()) {
						latest = udWithKey;
					}
				}
			}
			String latestKey = latest.getValues().entrySet().iterator().next().getKey();
			String latestValue = latest.getValues().get(latestKey);
			this.updates.put(latestKey, latestValue);
		}
	}
}
