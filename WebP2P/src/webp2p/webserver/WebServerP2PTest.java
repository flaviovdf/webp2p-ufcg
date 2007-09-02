package webp2p.webserver;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;

import webp2p.discoveryservice.DiscoveryServiceStub;

public class WebServerP2PTest extends TestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testGetContent() {
		DiscoveryServiceStub discoveryServiceMock = EasyMock.createMock(DiscoveryServiceStub.class);
		DataManager dataManagerMock = EasyMock.createStrictMock(DataManager.class);
		Replicator replicatorMock = EasyMock.createStrictMock(Replicator.class);
		
		
//		EasyMock.replay(discoveryServiceMock, dataManagerMock, replicatorMock);
//		
//		WebServerP2P ws = new WebServerP2P(dataManagerMock, replicatorMock);
//		
//		
//		
//		
//		EasyMock.verify(discoveryServiceMock, dataManagerMock, replicatorMock);
	}

}
