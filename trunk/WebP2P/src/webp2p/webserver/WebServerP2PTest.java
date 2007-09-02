package webp2p.webserver;

import org.easymock.classextension.EasyMock;

import junit.framework.TestCase;

public class WebServerP2PTest extends TestCase {

	static {
		WebServerP2P.init(null,null);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testGetContent() {
		DataManager dataManagerMock = EasyMock.createStrictMock(DataManager.class);
		Replicator replicatorMock = EasyMock.createStrictMock(Replicator.class);
		
		
		EasyMock.replay(dataManagerMock, replicatorMock);
		
		WebServerP2P ws = new WebServerP2P(dataManagerMock, replicatorMock);
		
		
		
		
		EasyMock.verify(dataManagerMock, replicatorMock);
	}

}
