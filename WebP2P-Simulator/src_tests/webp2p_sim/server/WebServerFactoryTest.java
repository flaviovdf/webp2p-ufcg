package webp2p_sim.server;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import webp2p_sim.core.network.Network;
import webp2p_sim.core.network.Network.Type;
import webp2p_sim.util.SmartTestCase;

public class WebServerFactoryTest extends SmartTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCreateServers() {
		Network network = new Network(Type.TEST);
		WebServerFactory factory = new WebServerFactory(createRandomHost(), network);
		List<WebServer> servers = new LinkedList<WebServer>(factory.createServers(new File("topology.xml"), false));
		
		assertEquals(4, servers.size());
		
		WebServer server1 = servers.get(0);
		WebServer server2 = servers.get(1);
		WebServer server3 = servers.get(2);
		WebServer server4 = servers.get(3);
		
		Set<Integer> sizes = new HashSet<Integer>();
		sizes.add(server1.getFiles().size());
		sizes.add(server2.getFiles().size());
		sizes.add(server3.getFiles().size());
		sizes.add(server4.getFiles().size());

		assertTrue(sizes.contains(3));
		assertTrue(sizes.contains(4));
		assertTrue(sizes.contains(5));
		assertTrue(sizes.contains(1));
	}

}
