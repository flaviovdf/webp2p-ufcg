package webp2p_sim.proxy;

import org.easymock.classextension.EasyMock;

import webp2p_sim.server.GetContentRequest;
import webp2p_sim.util.SmartTestCase;

public class BrowserTest extends SmartTestCase {

	public void testGeneratingRequest() {
		Proxy proxyMock = EasyMock.createMock(Proxy.class);
		Browser browser = new Browser("webserver",ZERO_DIST,proxyMock);
		
		proxyMock.sendMessage(new GetContentRequest(browser.getRandomRequestGenerator().peekNextID(),"www.anything.com",browser));
		
		EasyMock.replay(proxyMock);
		
		browser.generateRequest("www.anything.com");
		EasyMock.verify(proxyMock);
	}
	
}
