package webp2p_sim.proxy;

import java.io.File;

import org.easymock.classextension.EasyMock;

import webp2p_sim.util.SmartTestCase;

public class RequestGeneratorTest extends SmartTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void test() {
		GeneratorInterested interestedMock = EasyMock.createStrictMock(GeneratorInterested.class);
		
		RequestGenerator requestGenerator = new RequestGenerator(interestedMock);
		requestGenerator.loadFile(new File("tests" + File.separator + "requests" + File.separator + "input.txt"));
		
		// time 1
		interestedMock.generateRequest("http://site1.com/path/to/file");
		
		EasyMock.replay(interestedMock);
		requestGenerator.tickOcurred();
		EasyMock.verify(interestedMock);
		
		// time 2
		EasyMock.reset(interestedMock);
		
		interestedMock.generateRequest("http://192.168.1.1/robots.txt");
		interestedMock.generateRequest("http://192.168.1.1/one/two/three/four.bat");
		
		EasyMock.replay(interestedMock);
		requestGenerator.tickOcurred();
		EasyMock.verify(interestedMock);
		
		// time 5
		EasyMock.reset(interestedMock);
		
		interestedMock.generateRequest("http://site2.net/help.txt");
		
		EasyMock.replay(interestedMock);
		requestGenerator.tickOcurred();
		requestGenerator.tickOcurred();
		requestGenerator.tickOcurred();
		EasyMock.verify(interestedMock);
		
		// time 6
		EasyMock.reset(interestedMock);
		
		interestedMock.generateRequest("http://site2.net/test.jpg");
		
		EasyMock.replay(interestedMock);
		requestGenerator.tickOcurred();
		EasyMock.verify(interestedMock);
		
		// time 10
		EasyMock.reset(interestedMock);
		
		interestedMock.generateRequest("http://site2.net/robots.txt");
		
		EasyMock.replay(interestedMock);
		requestGenerator.tickOcurred();
		requestGenerator.tickOcurred();
		requestGenerator.tickOcurred();
		requestGenerator.tickOcurred();
		EasyMock.verify(interestedMock);
	}
}
