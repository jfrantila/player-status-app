package UpdateDTO;

import java.util.LinkedHashMap;
import java.util.Map;

public class Update {
	private String type;
	private String user;
	private int timestamp;
	private Map<String,String> values = new LinkedHashMap<>();
	
	public Update(String type, String user, int timestamp) {
		this.type = type;
		this.user = user;
		this.timestamp = timestamp;
	}
	
	public Update(String type, String user, int timestamp, Map<String,String> values ) {
		this.type = type;
		this.user = user;
		this.timestamp = timestamp;
		this.values = values;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	public Map<String, String> getValues() {
		return values;
	}
	public void setValues(Map<String, String> values) {
		this.values = values;
	}
}
