package webp2p.loadmeter;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import webp2p.loadmeter.exception.InconsistenWebServerFilesListException;

public class LoadMeter {

	private static final Logger LOG = Logger.getLogger(LoadMeter.class);
	private Map<LoadListener,Metric> listeners;
	private int bufferSize;
	
	/**
	 * Creates a new LoadMeter. This object measures the load
	 * of a webserver trying to download a file using the bufferSize.
	 * @param bufferSize the size of the buffer.
	 */
	public LoadMeter(int bufferSize) {
		listeners = new HashMap<LoadListener, Metric>();
		this.bufferSize = bufferSize;
	}
	
	public void addListener(LoadListener listener, Metric metric) throws InconsistenWebServerFilesListException {
		if (!verifyConsistencyAmongFiles(metric)) throw new InconsistenWebServerFilesListException();
		this.listeners.put(listener, metric);
	} 
	
	/**
	 * Verifies if exists different servers in the list. Servers are indentified by method getHost() from <code>URL</code>.
	 * @param metric
	 * @return
	 */
	public boolean verifyConsistencyAmongFiles(Metric metric) {
		String server = null;
		
		if (!metric.getFiles().isEmpty())
		try {
			server = extractServerName(metric.getFiles().get(0));
		} catch (MalformedURLException e1) {
			return false;
		}
		
		for (String file : metric.getFiles()) {
			try {
				if (!server.equals(extractServerName(file))) return false;
			} catch (MalformedURLException e) {
				return false;
			}
		}
		return true;
	}

	public String extractServerName(String file) throws MalformedURLException {
		URL url = new URL(file);
		return url.getHost();
	}

	public void removeListener(LoadListener listener) {
		this.listeners.remove(listener);
	} 
	
	public void ping() {
		Map<String,Double> cache = new HashMap<String,Double>();
		LoadEvent loadEvent = null;
		boolean mustThrowEvent = false;
		
		double download_rate = -1;
		for (Entry<LoadListener, Metric> entry : listeners.entrySet()) {
			loadEvent = new LoadEvent();
			
			for (String file : entry.getValue().getFiles()) {
				
				if (!cache.containsKey(file)) {
					download_rate = extractDownloadRate(file);
					cache.put(file, download_rate);
					
					if (download_rate <= entry.getValue().getMinimumDowloadRate()) {
						LOG.info("Overhead detected while trying to download " + file);
						LOG.debug("Creating overhead event for " + file + " with download rate: "+ download_rate);
						loadEvent.addPopularFile(new FilesToDownloadRate(file, download_rate));
						mustThrowEvent = true;
					}
				}
			}
			if (mustThrowEvent) {
				entry.getKey().overheadDetected(loadEvent);
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
		int bytes = this.download(file, buffer);
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