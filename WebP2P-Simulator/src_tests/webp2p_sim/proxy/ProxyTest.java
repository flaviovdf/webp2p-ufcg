package webp2p_sim.proxy;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.easymock.classextension.EasyMock;

import webp2p_sim.core.entity.NetworkEntity;
import webp2p_sim.ds.DiscoveryService;
import webp2p_sim.ds.GetServersForURLRequest;
import webp2p_sim.server.GetContentRequest;
import webp2p_sim.server.WebServer;
import webp2p_sim.util.RandomLongGenerator;
import webp2p_sim.util.SmartTestCase;

public class ProxyTest extends SmartTestCase {

	private static final String URL = "url1";
	private Proxy proxy;
	private RandomLongGenerator generator;
	private DiscoveryService dsMock;
	
	
	@Override
	protected void setUp() throws Exception {
		this.generator = new RandomLongGenerator();
		this.dsMock = EasyMock.createMock(DiscoveryService.class);
		this.proxy = new Proxy("proxy1",ZERO_DIST ,dsMock, generator);
		super.setUp();
	}
	
	public void testMakeRequest() throws Exception {
		getContent();
	}
	
	public void testHereAreServers() {
		long reqid = getContent();
		
		WebServer serverMock1 = EasyMock.createMock(WebServer.class);
		WebServer serverMock2 = EasyMock.createMock(WebServer.class);
		
		Set<WebServer> servers = new LinkedHashSet<WebServer>();
		servers.add(serverMock1);
		servers.add(serverMock2);
		
		serverMock1.sendMessage(new GetContentRequest(reqid, URL, proxy));
		EasyMock.replay(serverMock1);
		
		proxy.hereAreServers(reqid, servers);
		EasyMock.verify(serverMock1);
	}

	//proxy used to throw excpetion when an empty set was given 
	public void testHereAreServersNoServersForUrl() {
		long reqid = getContent();
		
		Set<WebServer> servers = new HashSet<WebServer>();
		proxy.hereAreServers(reqid, servers);
	}
	
	//mocks must not be called
	public void testHereAreServersRequestDoesNotExist() {
		WebServer serverMock1 = EasyMock.createMock(WebServer.class);
		WebServer serverMock2 = EasyMock.createMock(WebServer.class);
		
		Set<WebServer> servers = new HashSet<WebServer>();
		servers.add(serverMock1);
		servers.add(serverMock2);
		
		proxy.hereAreServers(12345, servers);
	}
	
	private long getContent() {
		long id = generator.peekNextID();
		Browser browserMock = EasyMock.createNiceMock(Browser.class);
		dsMock.sendMessage(EasyMock.eq(new GetServersForURLRequest(new Request(id,URL,proxy))));
		EasyMock.replay(dsMock);
		proxy.getContent(new Request(id,URL,browserMock));
		EasyMock.verify(dsMock);
		
		assertEquals(proxy.getRequestsMap().get(id).getUrl(),URL);
		
		EasyMock.reset(dsMock);
		return id;
	}
	
	public void testHereIsContent() {
		long reqId = getContent();
		
		NetworkEntity browserMock = this.proxy.getRequestsMap().get(reqId).getBrowser();
		EasyMock.reset(browserMock);
		((Browser) browserMock).sendMessage(new HereIsContentMessage(reqId,1));
		EasyMock.replay(browserMock);
		proxy.hereIsContent(reqId, 1);
		EasyMock.verify(browserMock);
	}
}
