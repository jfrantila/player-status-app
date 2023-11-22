import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ParallelProcessing.WorkerThread;
import model.OutputTO;
import model.Player;
import model.Vakiot;


public class PlayerStateApp {
	private static Logger LOGGER = Logger.getLogger(PlayerStateApp.class.getCanonicalName());
	private static Object monitor = new Object();
	private static List<Player> players = new ArrayList<>();
	private static boolean playersInit = false;
	private static boolean allUpdated = false;
	private static String fileName;

	public static void main(String[] args){
		if (args.length > 0) {
			fileName = args[0];
			LOGGER.info((String.format("Starting the app with the inputfile: %s", fileName)));
			new InitPlayers().start();
			new ReadAndUpdate().start();
			new WriteFinalStates().start();
		}
		else LOGGER.log(Level.WARNING, "Necessary filename is missing.");
	}
	
	public static class WriteFinalStates extends Thread {
		private OutputTO allPlayersFinalStates = new OutputTO();
		public void run() {
			while(!allUpdated) {
				synchronized(monitor) {
			          try {
			            monitor.wait();
			          } catch(InterruptedException e) {
			          }
				}			
			}

			for (Player player : players) {
				player.filterFinalState();
				allPlayersFinalStates.addOutput(player.getName(), player.getUpdates());
			}
			
			Gson g = new GsonBuilder().setPrettyPrinting().create();
			System.out.println(g.toJson(allPlayersFinalStates.getOutput()));
		}
	}
	
	public static class ReadAndUpdate extends Thread {
		public void run() {
			while(!playersInit) {
				synchronized(monitor) {
			          try {
			            monitor.wait();
			          } catch(InterruptedException e) {
			          }
				}
			}			
			LOGGER.info("Players initialized. Starting the app now!");
			
			
			ExecutorService executor = Executors.newFixedThreadPool(1);
	        for (int i = 0; i < 1; i++) {
	            Runnable worker = new WorkerThread(fileName, players);
	            executor.execute(worker);
	         }
	        executor.shutdown();
	        while (!executor.isTerminated()) {
	        }
	        LOGGER.info("Finished all threads");
	        
	        allUpdated = true;
	        
	        synchronized(monitor) {
				monitor.notifyAll();
			}
		}
	}
	
	public static class InitPlayers extends Thread {
		
		public void run() {
			for (String name : Vakiot.PLAYER_NAMES) {
				Player player = new Player(name);
				players.add(player);
			}
			playersInit = true;
				
			synchronized(monitor) {
				monitor.notifyAll();
			}	
		}
	}
}
