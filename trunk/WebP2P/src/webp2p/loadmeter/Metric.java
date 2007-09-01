package webp2p.loadmeter;

import java.util.List;

public class Metric {

	private List<String> files;
	private int download_rate;
	
	/**
	 * 
	 * @param files The files to be monitored.
	 * @param the minimum download rate (KB/s).
	 */
	public Metric(List<String> files, int download_rate) {
		this.files = files;
		this.download_rate = download_rate;
	}
	
	public List<String> getFiles() {
		return files;
	}
	public int getTimeOut() {
		return download_rate;
	}
}