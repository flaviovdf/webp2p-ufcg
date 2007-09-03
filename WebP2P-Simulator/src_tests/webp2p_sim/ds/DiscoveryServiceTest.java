package webp2p_sim.ds;

import java.util.ArrayList;

import org.easymock.classextension.EasyMock;

import webp2p_sim.core.network.Host;
import webp2p_sim.core.network.Network;
import webp2p_sim.proxy.GetResponse;
import webp2p_sim.proxy.Request;
import webp2p_sim.util.SmartTestCase;

public class DiscoveryServiceTest extends SmartTestCase {

	private DiscoveryService ds;
	private Network network;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.network = EasyMock.createMock(Network.class);
		this.ds = new DiscoveryService(createRandomHost(), ZERO_DIST, this.network, false);
	}
	
	public void testPut() throws Exception {
		long requestID1 = 123;
		long requestID2 = 456;
		
		Host webServerMock1 = createRandomHost();
		Host webServerMock2 = createRandomHost();
		Host webServerMock3 = createRandomHost();
		
		ds.putRequest("url1",webServerMock1);
		ds.putRequest("url1",webServerMock2);
		
		ds.putRequest("url2",webServerMock2);
		
		ds.putRequest("url3",webServerMock3);
		
		Host queued = createRandomHost();

		ArrayList<Host> responseServer1 = new ArrayList<Host>();
		responseServer1.add(webServerMock1);
		responseServer1.add(webServerMock2);
				
		network.sendMessage(ds.getHost(), queued, new GetResponse(responseServer1,requestID1));
		
		ArrayList<Host> responseServer2 = new ArrayList<Host>();
		responseServer2.add(webServerMock2);
		network.sendMessage(ds.getHost(), queued, new GetResponse(responseServer2,requestID2));
		
		ArrayList<Host> responseServer3 = new ArrayList<Host>();
		responseServer3.add(webServerMock3);
		network.sendMessage(ds.getHost(), queued, new GetResponse(responseServer3,requestID2));
		
		EasyMock.replay(network);
		ds.getRequest(new Request(requestID1, "url1", queued));
		ds.getRequest(new Request(requestID2, "url2", queued));
		ds.getRequest(new Request(requestID2, "url3", queued));

		EasyMock.verify(network);
	}
	
}
