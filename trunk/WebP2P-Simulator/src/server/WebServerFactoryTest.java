package server;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

public class WebServerFactoryTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCreateServers() {
		WebServerFactory factory = new WebServerFactory(null, null);
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
