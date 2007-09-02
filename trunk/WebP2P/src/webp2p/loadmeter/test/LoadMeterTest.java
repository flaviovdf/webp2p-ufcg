package webp2p.loadmeter.test;

import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;
import webp2p.loadmeter.LoadMeter;
import webp2p.loadmeter.Metric;

public class LoadMeterTest extends TestCase {
	
	public void testExtractserver() throws MalformedURLException {
		String file = "http://www.java.com/getjava/";
		LoadMeter meter = new LoadMeter("whatever",10);
		assertEquals("www.java.com",meter.extractServerName(file));
		file = "http://www.apl.jhu.edu/~hall/java/";
		assertEquals("www.apl.jhu.edu",meter.extractServerName(file));
		
		try {
			meter.extractServerName("x");
			fail("this line can not be executed.");
		} catch (MalformedURLException e) {
		}
	}
	
	public void testVerifyConsistence() {
		List<String> files = new LinkedList<String>();
		Metric metric = new Metric(files,10);
		LoadMeter meter = new LoadMeter("whatever",10);
		assertTrue(meter.verifyConsistencyAmongFiles(metric));
		
		files.add("http://www.apl.jhu.edu/~hall/java/");
		assertTrue(meter.verifyConsistencyAmongFiles(metric));
		files.add("http://www.apl.jhu.edu/~hall/java/test/one/another");
		assertTrue(meter.verifyConsistencyAmongFiles(metric));
		
		files.add("http://www.dsc.jhu.edu/~hall/java/test/one/another");
		assertFalse(meter.verifyConsistencyAmongFiles(metric));
	}
}
