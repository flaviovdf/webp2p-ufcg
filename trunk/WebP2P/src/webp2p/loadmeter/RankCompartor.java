package webp2p.loadmeter;

import java.util.Comparator;

public class RankCompartor implements Comparator<FilesToDownloadRate> {
	public int compare(FilesToDownloadRate one, FilesToDownloadRate another) {
		if (one.getDownloadRate() > another.getDownloadRate()) return 1;
		if (one.getDownloadRate() < another.getDownloadRate()) return -1;
		return 0;
	}
}