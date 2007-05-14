package proxy;

import java.io.File;

import junit.framework.TestCase;

import org.easymock.EasyMock;

public class RequestGeneratorTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void test() {
		Proxy proxyMock = EasyMock.createStrictMock(Proxy.class);
		
		RequestGenerator requestGenerator = new RequestGenerator(proxyMock);
		requestGenerator.loadFile(new File("tests" + File.separator + "requests" + File.separator + "input.txt"));
		
		// time 1
		proxyMock.makeRequest("http://site1.com/path/to/file");
		
		EasyMock.replay(proxyMock);
		requestGenerator.tickOcurred();
		EasyMock.verify(proxyMock);
		
		// time 2
		EasyMock.reset(proxyMock);
		
		proxyMock.makeRequest("http://192.168.1.1/robots.txt");
		proxyMock.makeRequest("http://192.168.1.1/one/two/three/four.bat");
		
		EasyMock.replay(proxyMock);
		requestGenerator.tickOcurred();
		EasyMock.verify(proxyMock);
		
		// time 5
		EasyMock.reset(proxyMock);
		
		proxyMock.makeRequest("http://site2.net/help.txt");
		
		EasyMock.replay(proxyMock);
		requestGenerator.tickOcurred();
		requestGenerator.tickOcurred();
		requestGenerator.tickOcurred();
		EasyMock.verify(proxyMock);
		
		// time 6
		EasyMock.reset(proxyMock);
		
		proxyMock.makeRequest("http://site2.net/test.jpg");
		
		EasyMock.replay(proxyMock);
		requestGenerator.tickOcurred();
		EasyMock.verify(proxyMock);
		
		// time 10
		EasyMock.reset(proxyMock);
		
		proxyMock.makeRequest("http://site2.net/robots.txt");
		
		EasyMock.replay(proxyMock);
		requestGenerator.tickOcurred();
		requestGenerator.tickOcurred();
		requestGenerator.tickOcurred();
		requestGenerator.tickOcurred();
		EasyMock.verify(proxyMock);
	}
}
