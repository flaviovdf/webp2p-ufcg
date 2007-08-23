package webp2p.loadmeter.test;

import java.util.List;

import webp2p.loadmeter.FilesToResponseTime;
import webp2p.loadmeter.LoadEvent;
import junit.framework.TestCase;

public class LoadEventTest extends TestCase {

	public void testPriorityList() {
			
		LoadEvent event = new LoadEvent("whatherver", 9);
		event.addPopularFile(new FilesToResponseTime("one", 10));
		event.addPopularFile(new FilesToResponseTime("two", 2));
		event.addPopularFile(new FilesToResponseTime("three", 100));
		event.addPopularFile(new FilesToResponseTime("four", 10000));
		event.addPopularFile(new FilesToResponseTime("five", 1));
		event.addPopularFile(new FilesToResponseTime("six", 1));
		event.addPopularFile(new FilesToResponseTime("seven", 100));
		
		
		List<FilesToResponseTime> list = event.getRankFilesList();
		assertEquals(list.get(0), new FilesToResponseTime("four",10000));
		assertEquals(list.get(1), new FilesToResponseTime("three",100));
		assertEquals(list.get(2), new FilesToResponseTime("seven",100));
		assertEquals(list.get(3), new FilesToResponseTime("one",10));
		assertEquals(list.get(4), new FilesToResponseTime("two",2));
		assertEquals(list.get(5), new FilesToResponseTime("five",1));
		assertEquals(list.get(6), new FilesToResponseTime("six",1));
	}
}
