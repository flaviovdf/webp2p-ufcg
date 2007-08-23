package webp2p.loadmeter;

import java.util.List;

public class Metric {

	private List<String> files;
	private int timout;
	
	/**
	 * 
	 * @param files The files to be monitored.
	 * @param timeout
	 */
	public Metric(List<String> files, int timeout) {
		this.files = files;
		this.timout = timeout;
	}
	
	public List<String> getFiles() {
		return files;
	}
	public int getTrash_hold() {
		return timout;
	}
}