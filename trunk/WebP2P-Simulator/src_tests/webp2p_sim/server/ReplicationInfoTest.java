package webp2p_sim.server;

import org.easymock.classextension.EasyMock;

import junit.framework.TestCase;

public class ReplicationInfoTest extends TestCase {
	
	public void testMustReplicate() {
		ReplicationInfo replicationInfo = new ReplicationInfo(1,1,"www.anything.com");
		assertFalse(replicationInfo.mustReplicate());
		replicationInfo.urlRequested();
		assertTrue(replicationInfo.mustReplicate());
	}
	
	public void testHasReplica() {
		ReplicationInfo replicationInfo = new ReplicationInfo(1,1,"www.anything.com");
		WebServer webServerMock = EasyMock.createMock(WebServer.class);
		replicationInfo.replicationRequested(webServerMock);
		assertTrue(replicationInfo.hasReplica(webServerMock));
	}
	
	public void testRemovingReplica() {
		ReplicationInfo replicationInfo = new ReplicationInfo(1,1,"www.anything.com");
		WebServer webServerMock = EasyMock.createMock(WebServer.class);
		
		replicationInfo.replicationRequested(webServerMock);
		replicationInfo.replicationDone(webServerMock,1);
		replicationInfo.tickOcurred();
		
		assertFalse(replicationInfo.hasReplica(webServerMock));

		replicationInfo.urlRequested();
		
		assertEquals(1,replicationInfo.getRequestsInThisWindow());
		
		replicationInfo.tickOcurred();
		
		assertEquals(0,replicationInfo.getRequestsInThisWindow());
	}
}
