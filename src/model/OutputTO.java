package model;

import java.util.LinkedHashMap;
import java.util.Map;

public class OutputTO {
	private Map<String, Map<String,String>> output = new LinkedHashMap<>();

	public Map<String, Map<String, String>> getOutput() {
		return output;
	}

	public void addOutput(String name, Map<String,String> values) {
		output.put(name, values);
	}
}
