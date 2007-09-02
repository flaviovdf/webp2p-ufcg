package webp2p.webserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import webp2p.util.LineReader;

/**
 * A <code>DataManager</code> instance manages the <code>WebServer</code> dataset.
 * It might contains both local and remote data and stores these in memory.
 * When the data has expired, the manager refreshes it from the <code>WebServer</code>
 * so that the returned data is always up-to-date.
 */
public class DataManager {
	
	private static final Logger LOG = Logger.getLogger(DataManager.class);
	
	private Map<String, Pair<URL, byte[]>> localData, remoteData;

	/**
	 * Creates a new <code>DataManager</code>.
	 */
	public DataManager() {
		this.localData = new HashMap<String, Pair<URL, byte[]>>();
		this.remoteData = new HashMap<String, Pair<URL, byte[]>>();
	}

	/**
	 * Reads a file containing the url of the local shared files.
	 * The file content has to take one url per line.
	 * The lines which start with a '#' will be ignored.
	 * 
	 * @param sharedFiles The filename.
	 */
	public void loadLocalSharedFiles(String sharedFiles) {
		LOG.info("Loading local files: " + sharedFiles);
		try {
			List<String> filesToLoad = LineReader.readFile(new File(sharedFiles), "#");
			
			for (String url : filesToLoad) {
				this.storeLocalData(url);
			}
		} catch (FileNotFoundException e) {
			LOG.error("File " + sharedFiles + " not found", e);
			throw new IllegalArgumentException("File " + sharedFiles + " not found", e);
		}
	}
	
	/**
	 * Stores a local data given a url.
	 * 
	 * @param url The data url.
	 */
	public void storeLocalData(String url) {
		this.localData.put(url, this.getDataPair(url));
		LOG.debug("Local file loaded: " + url);
	}
	
	/**
	 * Stores a remote data given a url.
	 * 
	 * @param url The data url.
	 * @return <code>true</code> if the data can be stored, and <code>false</code> otherwise.
	 */
	public boolean storeRemoteData(String url) {
		this.remoteData.put(url, this.getDataPair(url));
		LOG.debug("Remote file loaded: " + url);
		return true;
	}
	
	private Pair<URL, byte[]> getDataPair(String url) {
		ByteArrayOutputStream out = null;
		InputStream in = null;
		try {
			out = new ByteArrayOutputStream(256);
			URL urlObj = new URL(url);
			
			in = urlObj.openStream();
			int nextByte;
			while ((nextByte = in.read()) != -1) {
				out.write(nextByte);
			}
			
			return new Pair<URL, byte[]>(urlObj, out.toByteArray());
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("The URL " + url + " is not valid", e);
		} catch (IOException e) {
			throw new RuntimeException("Could not read the content of URL " + url, e);
		} finally {
			if (out != null) try { out.close(); } catch (IOException e) {} // ignore
			if (in != null) try { in.close(); } catch (IOException e) {} // ignore
		}
	}
	
	/**
	 * Returns the data as an array of bytes.
	 * It first looks in the local data repository
	 * and then, if it is not found, in the remote repository.
	 * 
	 * @param url The url of the data.
	 * @return The data as an array of bytes.
	 */
	public byte[] getData(String url) {
		Pair<URL, byte[]> pair = this.localData.get(url);
		if (pair == null) pair = this.remoteData.get(url);
		return pair != null ? pair.second : null;
	}

	/**
	 * A class that represents a tuple.
	 *
	 * @param <K> The first value.
	 * @param <V> The second value.
	 */
	private class Pair<F, S> {
		public F first;
		public S second;
		
		public Pair(F f, S s) {
			this.first = f;
			this.second = s;
		}
	}

}
