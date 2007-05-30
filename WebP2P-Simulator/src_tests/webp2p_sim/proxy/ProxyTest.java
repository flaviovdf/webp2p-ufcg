package webp2p_sim.proxy;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.easymock.classextension.EasyMock;

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
		makeRequest();
	}
	
	public void testHereAreServers() {
		long reqid = makeRequest();
		
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
		long reqid = makeRequest();
		
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
	
	public void testHereIsContent() {
		long reqid = makeRequest();
		
		
	}
	
	private long makeRequest() {
		long id = generator.peekNextID();
		
		dsMock.sendMessage(EasyMock.eq(new GetServersForURLRequest(id,URL,proxy)));
		EasyMock.replay(dsMock);
		proxy.sendRequest(URL);
		EasyMock.verify(dsMock);
		
		assertEquals(proxy.getRequestsMap().get(id), URL);
		
		EasyMock.reset(dsMock);
		
		return id;
	}
}
