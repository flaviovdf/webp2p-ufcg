package webp2p.loadmeter.test;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;

import webp2p.loadmeter.FilesToResponseTime;
import webp2p.loadmeter.LoadEvent;
import webp2p.loadmeter.LoadListener;
import webp2p.loadmeter.LoadMeter;
import webp2p.loadmeter.Metric;

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
	
	public void testAddingEvent() {
		LoadListener mockListener = EasyMock.createMock(LoadListener.class);
		
		LoadMeter meter = new LoadMeter("server",100);
		
		List<String> list = new LinkedList<String>();
		list.add("one");
		list.add("two");
		
		
		Metric metric = new Metric(list,10);
		meter.addListener(mockListener, metric);
		
		LoadEvent event = new LoadEvent("server",100);
		event.addPopularFile(new FilesToResponseTime("one",200));
		event.addPopularFile(new FilesToResponseTime("two",200));
		
		// expected behavior
		mockListener.overheadedServerDetected(event);
		
		EasyMock.replay(mockListener);
		meter.ping();
		EasyMock.verify(mockListener);
		
		
		/*List<FilesToResponseTime> files = event.getRankFilesList();
		asserTrue(files.contains(arg0))*/
	}
	
}