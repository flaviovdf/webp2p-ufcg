package webp2p.loadmeter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

public class LoadMeter {

	private Map<LoadListener,Metric> listeners;
	private String server;
	private int port, bufferSize;
	
	public LoadMeter(String server, int port) {
		this.server = server;
		this.port = port;
		listeners = new HashMap<LoadListener, Metric>();
		
		//default
		this.bufferSize = 1024;
		try {
			Properties props = new Properties();
			props.load(this.getClass().getResourceAsStream("/loadmeter.properties"));
			this.bufferSize = Integer.parseInt(props.getProperty("buffer"));
		} catch (IOException e) {
		}
	}
	
	public void addListener(LoadListener listener, Metric metric) {
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
		Map<String,Double> cache = new HashMap<String,Double>();
		LoadEvent loadEvent = null;
		boolean mustThrowEvent = false;
		
		double download_rate = -1;
		for (Entry<LoadListener, Metric> entry : listeners.entrySet()) {
			loadEvent = new LoadEvent(this.server, this.port);
			
			for (String file : entry.getValue().getFiles()) {
				
				if (!cache.containsKey(file)) {
					download_rate = extractDownloadRate(file);
					cache.put(file, download_rate);
					
					if (download_rate >= entry.getValue().getTimeOut()) {
						loadEvent.addPopularFile(new FilesToResponseTime(file, download_rate));
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
	private double extractDownloadRate(String file) {
		byte[] buffer = new byte[this.bufferSize];
		long startTime = System.currentTimeMillis();
		int bytes = this.download(this.server + File.separator +file, buffer);
		long endTime = System.currentTimeMillis();
		
		float f = this.bufferSize;
		double readedInKb = bytes/f;
		double timeInSec = (endTime - startTime)/1000f;
		return readedInKb/timeInSec;
	}
	
	/**
	 * Download numBytesToBeReaded from file.
	 * @param file the file to be downloaded.
	 * @param numBytesToBeReaded the number of bytes to download from the file.
	 * @return the number of bytes readed.
	 */
	private int download(String file, byte[] buffer) {
		InputStream  in = null;
		int readed = 0;
		try {
			URL url = new URL(file);
			in = url.openStream();
			
			int cont = 0;
			while ( (cont = in.read(buffer)) != -1 ) {
				readed+=cont;
			}
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
		return readed;
	}
}