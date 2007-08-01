package webp2p.webserver;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A <code>DataManager</code> instance manages the <code>WebServer</code> dataset.
 * It might contains both local and remote data and stores these in memory.
 * When the data has expired, the manager refreshes it from the <code>WebServer</code>
 * so that the returned data is always up-to-date.
 */
public class DataManager {
	
	private Map<String, Pair<URL, byte[]>> localData;

	/**
	 * Creates a new <code>DataManager</code>.
	 */
	public DataManager() {
		this.localData = new HashMap<String, Pair<URL, byte[]>>();
	}

	/**
	 * Reads a file containing the url of the local shared files.
	 * The file content has to take one url per line.
	 * The lines which start with a '#' will be ignored.
	 * 
	 * @param sharedFiles The filename.
	 */
	public void loadLocalSharedFiles(String sharedFiles) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(sharedFiles));
			String url = null;
			
			while ((url = reader.readLine()) != null) {
				if (url.startsWith("#")) continue; // ignoring commented lines
				this.storeLocalData(url);
			}
			
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("File " + sharedFiles + " not found", e);
		} catch (IOException e) {
			new RuntimeException("Could not read the file " + sharedFiles, e);
		}
	}
	
	/**
	 * Stores a local data given a url.
	 * 
	 * @param url The data url.
	 */
	public void storeLocalData(String url) {
		ByteArrayOutputStream out = null;
		InputStream in = null;
		try {
			URL urlObj = new URL(url);
			out = new ByteArrayOutputStream(256);
			
			in = urlObj.openStream();
			int nextByte = -1;
			while ((nextByte = in.read()) != -1) {
				out.write(nextByte);
			}
			
			this.localData.put(url, new Pair<URL, byte[]>(urlObj, out.toByteArray()));
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
	 * Stores a remote data given a url.
	 * 
	 * @param url The data url.
	 * @return <code>true</code> if the data can be stored, and <code>false</code> otherwise.
	 */
	public boolean storeData(String url) {
		return false;
	}
	
	/**
	 * Returns the data as an array of bytes.
	 * 
	 * @param url The url of the data.
	 * @return The data as an array of bytes.
	 */
	public byte[] getData(String url) {
		Pair<URL, byte[]> pair = this.localData.get(url);
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
