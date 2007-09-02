package webp2p.loadmeter.test;

import java.util.List;

import junit.framework.TestCase;
import webp2p.loadmeter.FilesToDownloadRate;
import webp2p.loadmeter.LoadEvent;

public class LoadEventTest extends TestCase {

	public void testPriorityList() {
			
		LoadEvent event = new LoadEvent("whatherver", 9);
		event.addPopularFile(new FilesToDownloadRate("one", 10));
		event.addPopularFile(new FilesToDownloadRate("two", 2));
		event.addPopularFile(new FilesToDownloadRate("three", 100));
		event.addPopularFile(new FilesToDownloadRate("four", 10000));
		event.addPopularFile(new FilesToDownloadRate("five", 1));
		event.addPopularFile(new FilesToDownloadRate("six", 1));
		event.addPopularFile(new FilesToDownloadRate("seven", 100));
		
		
		List<FilesToDownloadRate> list = event.getRankFilesList();
		assertEquals(list.get(0), new FilesToDownloadRate("five",1));
		assertEquals(list.get(1), new FilesToDownloadRate("six",1));
		assertEquals(list.get(2), new FilesToDownloadRate("two",2));
		assertEquals(list.get(3), new FilesToDownloadRate("one",10));
		assertEquals(list.get(4), new FilesToDownloadRate("three",100));
		assertEquals(list.get(5), new FilesToDownloadRate("seven",100));
		assertEquals(list.get(6), new FilesToDownloadRate("four",10000));
	}
	
	/*public void testAddingEvent() {
		LoadListener mockListener = EasyMock.createMock(LoadListener.class);
		
		LoadMeter meter = new LoadMeter("http://www.dsc.ufcg.edu.br/~jarthur/upload",80);
		
		List<String> list = new LinkedList<String>();
		list.add("delay-10.png");
		list.add("delay-20.png");
		
		
		Metric metric = new Metric(list,1000000000);
		meter.addListener(mockListener, metric);
		
		LoadEvent event = new LoadEvent("http://www.dsc.ufcg.edu.br/~jarthur/upload",80);
		event.addPopularFile(new FilesToDownloadRate("delay-10.png",200));
		event.addPopularFile(new FilesToDownloadRate("delay-20.png",200));
		
		// expected behavior
		mockListener.overheadedServerDetected(event);
		
		EasyMock.replay(mockListener);
		meter.ping();
		EasyMock.verify(mockListener);
	}*/
}