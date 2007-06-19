package webp2p_sim.server;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;

import webp2p_sim.proxy.Proxy;


public class FakeRequestGeneratorTest extends TestCase {

	public void testWebServerReceiving() {
/*		WebServer serverMock = EasyMock.createStrictMock(WebServer.class);
		Proxy proxyMock = EasyMock.createStrictMock(Proxy.class);
		
		List<WebServer> list = new LinkedList<WebServer>();
		list.add(serverMock);
		
		TrafficGenerator fake = new TrafficGenerator(list);
		
		// expectations
		GetContentRequest getContent = new GetContentRequest(FakeRequestGenerator.id,"www.does.not.matter", proxyMock);
		serverMock.sendMessage(getContent);
		
		EasyMock.replay(serverMock);
		
		fake.generateRequests(1,"www.does.not.matter");
		
		EasyMock.verify(serverMock);
		EasyMock.reset(serverMock);
		
		//expectations
		serverMock.sendMessage(getContent);
		serverMock.sendMessage(getContent);
		
		EasyMock.replay(serverMock);
		//two calls
		fake.generateRequests(2,"www.does.not.matter");
		
		EasyMock.verify(serverMock);*/
	}
}
