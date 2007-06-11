package webp2p_sim.ds;

import java.util.HashSet;
import java.util.Set;

import org.easymock.classextension.EasyMock;

import webp2p_sim.core.entity.NetworkEntity;
import webp2p_sim.proxy.GetResponse;
import webp2p_sim.server.WebServer;
import webp2p_sim.util.SmartTestCase;

public class DiscoveryServiceTest extends SmartTestCase {

	private DiscoveryService ds;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.ds = new DiscoveryService("DS", ZERO_DIST);
	}
	
	public void testPut() throws Exception {
		long requestID1 = 123;
		long requestID2 = 456;
		
		WebServer webServerMock1  = EasyMock.createNiceMock(WebServer.class);
		WebServer webServerMock2  = EasyMock.createNiceMock(WebServer.class);
		WebServer webServerMock3  = EasyMock.createNiceMock(WebServer.class);
		
		ds.putRequest("url1",webServerMock1);
		ds.putRequest("url1",webServerMock1);
		
		ds.putRequest("url1",webServerMock2);
		ds.putRequest("url2",webServerMock2);
		
		ds.putRequest("url3",webServerMock3);
		
		NetworkEntity queued = EasyMock.createStrictMock(NetworkEntity.class);

		Set<WebServer> responseServer1 = new HashSet<WebServer>();
		responseServer1.add(webServerMock1);
		responseServer1.add(webServerMock2);
				
		queued.sendMessage(EasyMock.eq(new GetResponse(responseServer1,requestID1)));
		
		Set<WebServer> responseServer2 = new HashSet<WebServer>();
		responseServer2.add(webServerMock2);
		queued.sendMessage(EasyMock.eq(new GetResponse(responseServer2,requestID2)));
		
		Set<WebServer> responseServer3 = new HashSet<WebServer>();
		responseServer3.add(webServerMock3);
		queued.sendMessage(EasyMock.eq(new GetResponse(responseServer3,requestID2)));
		
		EasyMock.replay(queued);
		ds.getRequest(requestID1, "url1", queued);
		ds.getRequest(requestID2, "url2", queued);
		ds.getRequest(requestID2, "url3", queued);

		EasyMock.verify(queued);
	}
	
}
