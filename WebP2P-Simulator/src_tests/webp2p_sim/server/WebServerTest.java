package webp2p_sim.server;

import org.easymock.classextension.EasyMock;

import webp2p_sim.core.network.Host;
import webp2p_sim.core.network.Network;
import webp2p_sim.ds.PutFileRequest;
import webp2p_sim.util.SmartTestCase;
import webp2p_sim.util.TimeToLive;


public class WebServerTest extends SmartTestCase {

	private Host dsMock;
	private WebServer ws;
	private Network network;

	protected void setUp() throws Exception {
		super.setUp();
		
		this.network = EasyMock.createMock(Network.class);
		this.dsMock = createRandomHost();
		this.ws = new WebServer(createRandomHost(), ZERO_DIST, network, dsMock);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testLoadFile1() {
		this.network.sendMessage(ws.getHost(), dsMock, new PutFileRequest("http://1.2.3.4/file1.txt", this.ws.getHost()) );
		
		EasyMock.replay( network );
		assertFalse(this.ws.getFiles().contains( "http://1.2.3.4/file1.txt" ));
		this.ws.loadFile( "http://1.2.3.4/file1.txt", 123, new TimeToLive(10) );
		assertTrue(this.ws.getFiles().contains( "http://1.2.3.4/file1.txt" ));
		assertFalse(this.ws.getFiles().contains( "http://1.2.3.4/file1.exe" ));
		EasyMock.verify( network );
	}
	
	public void testLoadFile2() {
		this.network.sendMessage(ws.getHost(), dsMock, new PutFileRequest("http://1.2.3.4/file.txt", this.ws.getHost()) );
		
		EasyMock.replay( network );
		assertFalse(this.ws.getFiles().contains( "http://1.2.3.4/file.txt" ));
		this.ws.loadFile( "http://1.2.3.4/file.txt", 123, new TimeToLive(10) );
		assertTrue(this.ws.getFiles().contains( "http://1.2.3.4/file.txt" ));
		this.ws.loadFile( "http://1.2.3.4/file.txt", 123, new TimeToLive(1) );
		assertTrue(this.ws.getFiles().contains( "http://1.2.3.4/file.txt" ));
		EasyMock.verify( network );
	}
	
	public void testLoadFile3() {
		this.network.sendMessage(ws.getHost(), dsMock, new PutFileRequest("http://1.2.3.4/file.txt", this.ws.getHost()) );
		this.network.sendMessage(ws.getHost(), dsMock, new PutFileRequest("http://1.2.3.4/file.txt", this.ws.getHost()) );
		
		EasyMock.replay( network );
		assertFalse(this.ws.getFiles().contains( "http://1.2.3.4/file.txt" ));
		this.ws.loadFile( "http://1.2.3.4/file.txt", 123, new TimeToLive(10) );
		assertTrue(this.ws.getFiles().contains( "http://1.2.3.4/file.txt" ));
		this.ws.loadFile( "http://1.2.3.4/file.txt", 123, new TimeToLive(11) );
		assertTrue(this.ws.getFiles().contains( "http://1.2.3.4/file.txt" ));
		EasyMock.verify( network );
	}
	
	public void testAdjacents() {
		assertEquals(0, this.ws.getAdj().size());
		this.ws.addAdj( createRandomHost() );
		assertEquals(1, this.ws.getAdj().size());
		this.ws.addAdj( createRandomHost() );
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
		Host wsMock = createRandomHost();
		network.sendMessage(ws.getHost(), wsMock, new GetContentForReplicationMessage("http://4.3.2.1/f.txt", this.ws.getHost()) );
		
		EasyMock.replay( network );
		this.ws.createReplicaOfUrl( "http://4.3.2.1/f.txt", wsMock );
		EasyMock.verify( network );
	}

	public void testReplication2() {
		Host wsMock = createRandomHost();
		
		network.sendMessage(ws.getHost(), dsMock, new PutFileRequest("http://4.3.2.1/f.txt", this.ws.getHost()) );
		network.sendMessage(ws.getHost(), wsMock, new HereIsReplicaOfContent("http://4.3.2.1/f.txt", WebServer.DEFAULT_REPLICATION_TTL, 77) );
		
		EasyMock.replay( network );
		this.ws.loadFile("http://4.3.2.1/f.txt", 77, new TimeToLive(13));
		this.ws.getContentForReplication( "http://4.3.2.1/f.txt", wsMock );
		EasyMock.verify( network );
	}
	
	public void testReplication3() {
		this.network.sendMessage(ws.getHost(), dsMock, new PutFileRequest("http://1.2.3.4/robots.txt", this.ws.getHost()) );
		
		EasyMock.replay( network );
		assertEquals(0, this.ws.getFiles().size());
		this.ws.hereIsReplicaOfUrl(  "http://1.2.3.4/robots.txt", 77, 123);
		assertEquals(1, this.ws.getFiles().size());
		EasyMock.verify( network );
	}
	
}
