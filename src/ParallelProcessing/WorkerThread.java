package ParallelProcessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

import UpdateDTO.Update;
import model.Player;

public class WorkerThread implements Runnable {
	private static Logger LOGGER = Logger.getLogger(WorkerThread.class.getName());
	private static List<Update> updateList = new ArrayList<>();
	private String fileName;
    private List<Player> players;
    
    public WorkerThread(String fileName, List<Player> players){
    	this.fileName = fileName;
        this.players = players;
    }

    @Override
    public void run() {
    	try {
			File file = new File(fileName);
			FileInputStream stream = new FileInputStream(file);
			InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader br = new BufferedReader(reader);
			
			Gson g = new Gson();
			String input;
			while ((input = br.readLine()) != null) {
				Update update = g.fromJson(input, Update.class);
				updateList.add(update);
			}
			br.close();			
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Did not find the file: " + e.getMessage());
		}
    	
    	processUpdates();
    }
    
    public void processUpdates() {
    	for (Update ud : updateList) {
    		processUpdate(ud);
    	}
    }
    
    public void processUpdate(Update update) {
    	Player player = getPlayerWithName(update.getUser());
    	int timestamp = update.getTimestamp();
    	Map<String,String> values = update.getValues();
    	String currentKey = values.entrySet().iterator().next().getKey();	
		
		boolean add = false;
		if (values.size() > 1) {
			LOGGER.log(Level.WARNING, "Update has too many value-entries.");
		}
		
		if (player.getUdList().size() == 0) {
			add = true;
		}
		else {
			for (Update prevUD : player.getUdList()) {
				if (prevUD.getValues().containsKey(currentKey) && timestamp <= prevUD.getTimestamp()) {
					continue;
				} else {
					add = true;
					break;
				}
			}
		}
		
		if (add) {
			player.addUpdateToList(update);
		}
    }
    
	public Player getPlayerWithName(String name) {
		for (Player player : players) {
			if (player.getName().equals(name)) {
				return player;
			}
		}
		return null;
	}
}
