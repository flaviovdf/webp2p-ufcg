package webp2p_sim.server;

import org.easymock.classextension.EasyMock;

import webp2p_sim.ds.DiscoveryService;
import webp2p_sim.ds.PutFileRequest;
import webp2p_sim.util.SmartTestCase;
import webp2p_sim.util.TimeToLive;


public class WebServerTest extends SmartTestCase {

	private DiscoveryService dsMock;
	private WebServer ws;

	protected void setUp() throws Exception {
		super.setUp();
		
		dsMock = EasyMock.createStrictMock(DiscoveryService.class);
		this.ws = new WebServer("ws1", ZERO_DIST, dsMock);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testLoadFile1() {
		this.dsMock.sendMessage( new PutFileRequest("http://1.2.3.4/file1.txt", this.ws) );
		
		EasyMock.replay( this.dsMock );
		assertFalse(this.ws.getFiles().contains( "http://1.2.3.4/file1.txt" ));
		this.ws.loadFile( "http://1.2.3.4/file1.txt", 123, new TimeToLive(10) );
		assertTrue(this.ws.getFiles().contains( "http://1.2.3.4/file1.txt" ));
		assertFalse(this.ws.getFiles().contains( "http://1.2.3.4/file1.exe" ));
		EasyMock.verify( this.dsMock );
	}
	
	public void testLoadFile2() {
		this.dsMock.sendMessage( new PutFileRequest("http://1.2.3.4/file.txt", this.ws) );
		
		EasyMock.replay( this.dsMock );
		assertFalse(this.ws.getFiles().contains( "http://1.2.3.4/file.txt" ));
		this.ws.loadFile( "http://1.2.3.4/file.txt", 123, new TimeToLive(10) );
		assertTrue(this.ws.getFiles().contains( "http://1.2.3.4/file.txt" ));
		this.ws.loadFile( "http://1.2.3.4/file.txt", 123, new TimeToLive(1) );
		assertTrue(this.ws.getFiles().contains( "http://1.2.3.4/file.txt" ));
		EasyMock.verify( this.dsMock );
	}
	
	public void testLoadFile3() {
		this.dsMock.sendMessage( new PutFileRequest("http://1.2.3.4/file.txt", this.ws) );
		this.dsMock.sendMessage( new PutFileRequest("http://1.2.3.4/file.txt", this.ws) );
		
		EasyMock.replay( this.dsMock );
		assertFalse(this.ws.getFiles().contains( "http://1.2.3.4/file.txt" ));
		this.ws.loadFile( "http://1.2.3.4/file.txt", 123, new TimeToLive(10) );
		assertTrue(this.ws.getFiles().contains( "http://1.2.3.4/file.txt" ));
		this.ws.loadFile( "http://1.2.3.4/file.txt", 123, new TimeToLive(11) );
		assertTrue(this.ws.getFiles().contains( "http://1.2.3.4/file.txt" ));
		EasyMock.verify( this.dsMock );
	}
	
	public void testAdjacents() {
		assertEquals(0, this.ws.getAdj().size());
		this.ws.addAdj( new WebServer("ws2", ZERO_DIST, this.dsMock) );
		assertEquals(1, this.ws.getAdj().size());
		this.ws.addAdj( new WebServer("ws2", ZERO_DIST, this.dsMock) );
		assertEquals(2, this.ws.getAdj().size());
	}
	
	public void testTTLDecrease() {
		this.ws.loadFile( "http://1.2.3.4/file1.txt", 1, new TimeToLive(3) );
		this.ws.loadFile( "http://1.2.3.4/file2.txt", 1, new TimeToLive(2) );
		this.ws.loadFile( "http://1.2.3.4/file3.txt", 1, new TimeToLive(1) );
		assertEquals(3, this.ws.getFiles().size());
		
		this.ws.tickOcurred();
		assertEquals(2, this.ws.getFiles().size());
		
		this.ws.tickOcurred();
		assertEquals(1, this.ws.getFiles().size());
		
		this.ws.loadFile( "http://1.2.3.4/file1.txt", 1, new TimeToLive(2) );
		
		this.ws.tickOcurred();
		assertEquals(1, this.ws.getFiles().size());
		
		this.ws.tickOcurred();
		assertEquals(0, this.ws.getFiles().size());
		
		this.ws.tickOcurred();
		assertEquals(0, this.ws.getFiles().size());
	}
	
	public void testReplication1() {
		WebServer wsMock = EasyMock.createStrictMock( WebServer.class );
		wsMock.sendMessage( new GetContentForReplicationMessage("http://4.3.2.1/f.txt", this.ws) );
		
		EasyMock.replay( wsMock );
		this.ws.createReplicaOfUrl( "http://4.3.2.1/f.txt", wsMock );
		EasyMock.verify( wsMock );
	}

	public void testReplication2() {
		WebServer wsMock = EasyMock.createStrictMock( WebServer.class );
		wsMock.sendMessage( new HereIsReplicaOfContent("http://4.3.2.1/f.txt", WebServer.DEFAULT_REPLICATION_TTL, 77) );
		
		EasyMock.replay( wsMock );
		this.ws.loadFile("http://4.3.2.1/f.txt", 77, new TimeToLive(13));
		this.ws.getContentForReplication( "http://4.3.2.1/f.txt", wsMock );
		EasyMock.verify( wsMock );
	}
	
	public void testReplication3() {
		this.dsMock.sendMessage( new PutFileRequest("http://1.2.3.4/robots.txt", this.ws) );
		
		EasyMock.replay( this.dsMock );
		assertEquals(0, this.ws.getFiles().size());
		this.ws.hereIsReplicaOfUrl(  "http://1.2.3.4/robots.txt", 77, 123);
		assertEquals(1, this.ws.getFiles().size());
		EasyMock.verify( this.dsMock );
	}
	
}
