package webp2p_sim.server;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import webp2p_sim.ds.DiscoveryService;
import webp2p_sim.util.SmartTestCase;
import edu.uah.math.distributions.ExponentialDistribution;
import edu.uah.math.distributions.NormalDistribution;

public class WebServerFactoryTest extends SmartTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCreateServers() {
		WebServerFactory factory = new WebServerFactory(new ExponentialDistribution(2), new DiscoveryService("DS", new NormalDistribution(0, 0)));
		List<WebServer> servers = new LinkedList<WebServer>(factory.createServers(new File("topology.xml")));
		
		assertEquals(3, servers.size());
		
		WebServer server1 = servers.get(0);
		WebServer server2 = servers.get(1);
		WebServer server3 = servers.get(2);
		
		Set<Integer> sizes = new HashSet<Integer>();
		sizes.add(server1.getFiles().size());
		sizes.add(server2.getFiles().size());
		sizes.add(server3.getFiles().size());
		
		assertTrue(sizes.contains(3));
		assertTrue(sizes.contains(4));
		assertTrue(sizes.contains(5));
	}

}
