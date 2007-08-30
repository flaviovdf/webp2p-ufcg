package webp2p.loadmeter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
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
		listeners = new HashMap<LoadListener, Metric>();
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
		Map<String,Long> cache = new HashMap<String,Long>();
		LoadEvent loadEvent = null;
		boolean mustThrowEvent = false;
		
		long responseTime = -1;
		for (Entry<LoadListener, Metric> entry : listeners.entrySet()) {
			loadEvent = new LoadEvent(this.server, this.port);
			
			for (String file : entry.getValue().getFiles()) {
				
				if (!cache.containsKey(file)) {
					responseTime = extractResponseTime(file);
					cache.put(file, responseTime);
					
					if (responseTime >= entry.getValue().getTimeOut()) {
						loadEvent.addPopularFile(new FilesToResponseTime(file, responseTime));
						mustThrowEvent = true;
					}
				}
			}
			if (mustThrowEvent) {
				entry.getKey().overheadedServerDetected(loadEvent);
				mustThrowEvent = false;
			}
		}
	}
	
	/**
	 * 
	 * @param file
	 * @return the response time in milliseconds.
	 */
	private long extractResponseTime(String file) {
		long startTime = System.currentTimeMillis();
		this.download(this.server + File.separator +file, 1024);
		long endTime = System.currentTimeMillis();
		return 1024/(endTime - startTime)*1000;
	}
	
	private void download(String file, int numOfBytesToDownload) {
		URLConnection conn = null;
		InputStream  in = null;
		try {
			URL url = new URL(file);
			conn = url.openConnection();
			in = conn.getInputStream();
			byte[] buffer = new byte[numOfBytesToDownload];
			in.read(buffer);
//			while ((in.read(buffer)) != -1) {}
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ioe) {
			}
		}
	}
}