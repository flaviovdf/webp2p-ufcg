package webp2p.loadmeter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LoadEvent {

	private String server;
	private List<FilesToDownloadRate> files;
	
	public LoadEvent(String server) {
		this.server = server;
		this.files = new LinkedList<FilesToDownloadRate>();
	}
	
	public String getServer() {
		return server;
	}
	
	/**
	 * Retorna uma lista de prioridade onde os primeiros elementos da listas s�o os de 
	 * maior responseTime.
	 */
	public List<FilesToDownloadRate> getRankFilesList() {
		Collections.sort(this.files,new RankCompartor());
		return this.files;
	}

	public void addPopularFile(FilesToDownloadRate fileToResponseTime) {
		assert fileToResponseTime.getFile() != null;
		assert fileToResponseTime.getDownloadRate() > 0;
		this.files.add(fileToResponseTime);
	}

	
	
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((files == null) ? 0 : files.hashCode());
		result = PRIME * result + ((server == null) ? 0 : server.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final LoadEvent other = (LoadEvent) obj;
		if (files == null) {
			if (other.files != null)
				return false;
		} else if (!files.equals(other.files))
			return false;
		if (server == null) {
			if (other.server != null)
				return false;
		} else if (!server.equals(other.server))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.server + " " + this.files;
	}
}
