package webp2p.loadmeter;

import java.util.List;

public class Metric {

	private List<String> files;
	private int timeout;
	
	/**
	 * 
	 * @param files The files to be monitored.
	 * @param timeout
	 */
	public Metric(List<String> files, int timeout) {
		this.files = files;
		this.timeout = timeout;
	}
	
	public List<String> getFiles() {
		return files;
	}
	public int getTimeOut() {
		return timeout;
	}
}