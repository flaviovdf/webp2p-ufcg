package webp2p_sim.proxy;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.easymock.classextension.EasyMock;

import webp2p_sim.core.network.Host;
import webp2p_sim.core.network.Network;
import webp2p_sim.ds.GetServersForURLRequest;
import webp2p_sim.server.GetContentRequest;
import webp2p_sim.util.RandomLongGenerator;
import webp2p_sim.util.SmartTestCase;

public class ProxyTest extends SmartTestCase {

	private static final String URL = "url1";
	private Proxy proxy;
	private RandomLongGenerator generator;
	private Host dsMock;
	private Network network;
	
	
	@Override
	protected void setUp() throws Exception {
		this.generator = new RandomLongGenerator();
		this.dsMock = createRandomHost();
		this.network = EasyMock.createMock(Network.class);
		this.proxy = new Proxy(createRandomHost(), ZERO_DIST, network, dsMock, generator);
		super.setUp();
	}
	
	public void testMakeRequest() throws Exception {
		getContent(createRandomHost());
	}
	
	public void testHereAreServers() {
		long reqid = getContent(createRandomHost());
		
		Host serverMock1 = createRandomHost();
		Host serverMock2 = createRandomHost();
		
		Set<Host> servers = new LinkedHashSet<Host>();
		servers.add(serverMock1);
		servers.add(serverMock2);
		
		network.sendMessage(proxy.getHost(), serverMock1, new GetContentRequest(reqid, URL, proxy.getHost()));
		
		EasyMock.replay(network);
		proxy.hereAreServers(reqid, servers);
		EasyMock.verify(network);
	}

	//proxy used to throw excpetion when an empty set was given 
	public void testHereAreServersNoServersForUrl() {
		long reqid = getContent(createRandomHost());
		
		Set<Host> servers = new HashSet<Host>();
		proxy.hereAreServers(reqid, servers);
	}
	
	//mocks must not be called
	public void testHereAreServersRequestDoesNotExist() {
		Host serverMock1 = createRandomHost();
		Host serverMock2 = createRandomHost();
		
		Set<Host> servers = new HashSet<Host>();
		servers.add(serverMock1);
		servers.add(serverMock2);
		
		proxy.hereAreServers(12345, servers);
	}
	
	private long getContent(Host browserMock) {
		long id = generator.peekNextID();
		network.sendMessage(proxy.getHost(), dsMock, new GetServersForURLRequest(new Request(id,URL,proxy.getHost())));
		EasyMock.replay(network);
		proxy.getContent(new Request(id,URL,browserMock));
		EasyMock.verify(network);
		
		assertEquals(proxy.getRequestsMap().get(id).getUrl(),URL);
		
		EasyMock.reset(network);
		return id;
	}
	
	public void testHereIsContent() {
		Host browserMock = createRandomHost();
		long reqId = getContent(browserMock);
		
		network.sendMessage(proxy.getHost(), browserMock, new HereIsContentMessage(reqId,1));
		
		EasyMock.replay(network);
		proxy.hereIsContent(reqId, 1);
		EasyMock.verify(network);
	}
}
