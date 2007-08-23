package webp2p.loadmeter;

import java.util.Comparator;

public class RankCompartor implements Comparator<FilesToResponseTime> {

	public int compare(FilesToResponseTime one, FilesToResponseTime another) {
		if (one.getResponseTime() < another.getResponseTime()) return 1;
		if (one.getResponseTime() > another.getResponseTime()) return -1;
		return 0;
	}

}
