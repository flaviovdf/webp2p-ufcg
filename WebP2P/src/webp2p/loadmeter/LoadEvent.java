package webp2p.loadmeter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LoadEvent {

	private String server;
	private int port;
	private List<FilesToResponseTime> files;
	
	public LoadEvent(String server, int port) {
		this.server = server;
		this.port = port;
		this.files = new LinkedList<FilesToResponseTime>();
	}
	
	public String getServer() {
		return server;
	}
	
	public int getPort() {
		return port;
	}
	
	/**
	 * Retorna uma lista de prioridade onde os primeiros elementos da listas são os de 
	 * maior responseTime.
	 */
	public List<FilesToResponseTime> getRankFilesList() {
		Collections.sort(this.files,new RankCompartor());
		return this.files;
	}

	public void addPopularFile(FilesToResponseTime fileToResponseTime) {
		assert fileToResponseTime.getFile() != null;
		assert fileToResponseTime.getResponseTime() > 0;
		this.files.add(fileToResponseTime);
	}
}
