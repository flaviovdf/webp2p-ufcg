package webp2p.webserver;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;

public class WebServerP2PTest extends TestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testGetContent() {
		DataManager dataManagerMock = EasyMock.createMock(DataManager.class);
		
		EasyMock.expect(dataManagerMock.getData("file:resources/Lenna.png")).andReturn(new byte[] {});
		
		EasyMock.replay(dataManagerMock);
		
		WebServerP2P ws = new WebServerP2P(dataManagerMock, null);
		ws.getContent("file:resources/Lenna.png");
		
		EasyMock.verify(dataManagerMock);
	}
	
	public void testStoreReplica() {
		DataManager dataManagerMock = EasyMock.createMock(DataManager.class);
		
		EasyMock.expect(dataManagerMock.storeRemoteData("file:resources/Lenna.png")).andReturn(true);
		
		EasyMock.replay(dataManagerMock);
		
		WebServerP2P ws = new WebServerP2P(dataManagerMock, null);
		ws.storeReplica("file:resources/Lenna.png");
		
		EasyMock.verify(dataManagerMock);
	}

}
