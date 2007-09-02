package webp2p.loadmeter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LoadEvent {

	private List<FilesToDownloadRate> files;
	
	public LoadEvent() {
		this.files = new LinkedList<FilesToDownloadRate>();
	}
	
	/**
	 * Retorna uma lista de prioridade onde os primeiros elementos da listas s�o os de 
	 * maior responseTime.
	 */
	public List<FilesToDownloadRate> getRankFilesList() {
		Collections.sort(this.files,new RankCompartor());
		return this.files;
	}

	/**
	 * 
	 * @param fileToResponseTime
	 */
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
		return true;
	}
}
