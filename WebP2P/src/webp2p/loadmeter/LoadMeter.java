package webp2p.loadmeter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LoadMeter {

	private Map<LoadListener,Metric> listeners;
	private String server;
	private int port;
	
	public LoadMeter(String server, int port) {
		this.server = server;
		this.port = port;
	}
	
	public void addListener(LoadListener listener, Metric metric) {
		assert listener != null;
		assert metric != null;
		this.listeners.put(listener, metric);
	} 
	
	public void removeListener(LoadListener listener) {
		this.listeners.remove(listener);
	} 
	
	
	public String getServer() {
		return server;
	}


	public int getPort() {
		return port;
	}

	public void ping() {
		Map<String,Integer> cache = new HashMap<String,Integer>();
		LoadEvent loadEvent = new LoadEvent(this.server,this.port);
		boolean mustThrowEvent = false;
		
		int responseTime = -1;
		for (Entry<LoadListener, Metric> entry : listeners.entrySet()) {
			
			
			for (String file : entry.getValue().getFiles()) {
				if (!cache.containsKey(file)) {
					responseTime = extractTimeResponse(file);
					cache.put(file, responseTime);
					
					if (responseTime >= entry.getValue().getTrash_hold()) {
						loadEvent.addPopularFile(new FilesToResponseTime(file, responseTime));
					}
				}

			}				
		}
		
		if (mustThrowEvent) this.fireOverheadedWebServer(loadEvent);
	}

	private void fireOverheadedWebServer(LoadEvent loadEvent) {
		for (LoadListener listener : listeners.keySet()) {
			listener.overheadedServerDetected(loadEvent);
		}
	}

	private int extractTimeResponse(String file) {
		return -1;
	}
}