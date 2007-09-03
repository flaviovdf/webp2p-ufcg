package webp2p_sim.browser;

import org.easymock.classextension.EasyMock;

import webp2p_sim.core.network.Host;
import webp2p_sim.core.network.Network;
import webp2p_sim.server.GetContentRequest;
import webp2p_sim.util.SmartTestCase;

public class BrowserTest extends SmartTestCase {

	public void testGeneratingRequest() {
		Host proxyHost = createRandomHost();
		Network mockNetwork = EasyMock.createMock(Network.class);
		
		Browser browser = new Browser(createRandomHost(), ZERO_DIST, mockNetwork, proxyHost, false);
		GetContentRequest getContentRequest = new GetContentRequest(browser.getRandomRequestGenerator().peekNextID(),"www.anything.com",browser.getHost());
		mockNetwork.sendMessage(browser.getHost(), proxyHost, getContentRequest);
		
		EasyMock.replay(mockNetwork);
		
		browser.generateRequest("www.anything.com");
		
		EasyMock.verify(mockNetwork);
	}
	
}
