package webp2p.webserver;

import java.io.File;
import java.net.MalformedURLException;

import junit.framework.TestCase;

public class DataManagerTest extends TestCase {

	private DataManager dataManager;

	protected void setUp() throws Exception {
		super.setUp();
		
		this.dataManager = new DataManager();
		this.dataManager.loadLocalSharedFiles("resources" + File.separator + "webserverp2p.sharedfiles");
	}
	
	public void testGetData() throws MalformedURLException {
		File file = new File("resources" + File.separator + "Lenna.png");
		assertTrue(file.exists());
		byte[] data = this.dataManager.getData("file:resources/Lenna.png");
		assertNotNull(data);
		assertEquals(file.length(), data.length);
	}
	
	public void testStoreLocalDataWithErros() {
		try {
			this.dataManager.storeLocalData("file:inexistent");
			fail("An RuntimeException should be thrown");
		} catch (RuntimeException e) {
			// nothing
		}
		
		try {
			this.dataManager.storeLocalData("file!/invalidurl.com");
			fail("An IllegalArgumentException should be thrown");
		} catch (IllegalArgumentException e) {
			// nothing
		}
	}
	
	public void testStoreLocalData() {
		File file = new File("resources" + File.separator + "valladolid_10986.html");
		assertTrue(file.exists());
		this.dataManager.storeLocalData("file:resources/valladolid_10986.html");
		byte[] data = this.dataManager.getData("file:resources/valladolid_10986.html");
		assertNotNull(data);
		assertEquals(file.length(), data.length);
	}

}
