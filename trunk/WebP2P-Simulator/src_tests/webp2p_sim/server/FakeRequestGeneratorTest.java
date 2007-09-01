package webp2p_sim.server;

import webp2p_sim.util.SmartTestCase;


public class FakeRequestGeneratorTest extends SmartTestCase {

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
