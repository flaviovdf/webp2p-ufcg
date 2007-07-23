package webp2p.discoveryservice;

import java.util.List;

import junit.framework.TestCase;

public class DiscoveryServiceTest extends TestCase {

	private DiscoveryService ds;

	protected void setUp() throws Exception {
		this.ds = new DiscoveryService();
	}

	public void testOperationsWithoutFiles() {
		List<String> list = this.ds.get("http://1.2.3.4:8080/file1.txt");
		assertNotNull(list);
		assertEquals(0, list.size());
	}
	
	public void testOperationsWithThreeFiles() {
		this.ds.put("http://1.2.3.4:8080", "http://1.2.3.4:8080/file1.txt");
		List<String> list = this.ds.get("http://1.2.3.4:8080/file1.txt");
		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals("http://1.2.3.4:8080", list.get(0));
		
		this.ds.put("http://100.2.3.4:8081", "http://1.2.3.4:8080/file1.txt");
		list = this.ds.get("http://1.2.3.4:8080/file1.txt");
		assertNotNull(list);
		assertEquals(2, list.size());
		
		this.ds.put("http://100.2.3.4:8081", "http://1.2.3.4:8080/robots.txt");
		list = this.ds.get("http://1.2.3.4:8080/file1.txt");
		assertNotNull(list);
		assertEquals(2, list.size());
		
		list = this.ds.get("http://1.2.3.4:8080/robots.txt");
		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals("http://100.2.3.4:8081", list.get(0));
	}
	
	public void testOperationsWithRepeatedFiles() {
		this.ds.put("http://1.2.3.4:8080", "http://1.2.3.4:8080/file1.txt");
		this.ds.put("http://1.2.3.4:8080", "http://1.2.3.4:8080/file2.txt");
		this.ds.put("http://166.2.3.4:8080", "http://1.2.3.4:8080/file1.txt");
		this.ds.put("http://1.2.3.4:8080", "http://1.2.3.4:8080/file2.txt");
		this.ds.put("http://166.2.3.4:8080", "http://1.2.3.4:8080/file1.txt");
		
		List<String> list = this.ds.get("http://1.2.3.4:8080/file1.txt");
		assertNotNull(list);
		assertEquals(2, list.size());
		
		list = this.ds.get("http://1.2.3.4:8080/file2.txt");
		assertNotNull(list);
		assertEquals(1, list.size());
	}

	public void testDeleteInexistentFile() {
		this.ds.put("http://1.1.1.1", "http://1.1.1.1/hello.world");
		this.ds.delete("http://1.1.1.1", "http://1.1.1.1/null");
		this.ds.delete("http://2.2.2.2", "http://1.1.1.1/hello.world");
		
		List<String> list = this.ds.get("http://1.1.1.1/null");
		assertNotNull(list);
		assertEquals(0, list.size());
		
		list = this.ds.get("http://1.1.1.1/hello.world");
		assertNotNull(list);
		assertEquals(1, list.size());
	}
	
	public void testDelete() {
		this.ds.put("http://1.1.1.1", "http://1.1.1.1/hello.world");
		this.ds.put("http://1.1.1.1", "http://2.2.2.2/hello.world");
		this.ds.put("http://2.2.2.2", "http://1.1.1.1/hello.world");
		this.ds.put("http://2.2.2.2", "http://2.2.2.2/hello.world");
		
		List<String> list = this.ds.get("http://1.1.1.1/hello.world");
		assertNotNull(list);
		assertEquals(2, list.size());
		
		list = this.ds.get("http://2.2.2.2/hello.world");
		assertNotNull(list);
		assertEquals(2, list.size());
		
		this.ds.delete("http://2.2.2.2", "http://1.1.1.1/hello.world");
		
		list = this.ds.get("http://1.1.1.1/hello.world");
		assertNotNull(list);
		assertEquals(1, list.size());
		
		list = this.ds.get("http://2.2.2.2/hello.world");
		assertNotNull(list);
		assertEquals(2, list.size());
		
		this.ds.delete("http://2.2.2.2", "http://2.2.2.2/hello.world");
		
		list = this.ds.get("http://1.1.1.1/hello.world");
		assertNotNull(list);
		assertEquals(1, list.size());
		
		list = this.ds.get("http://2.2.2.2/hello.world");
		assertNotNull(list);
		assertEquals(1, list.size());
		
		this.ds.delete("http://1.1.1.1", "http://2.2.2.2/hello.world");
		
		list = this.ds.get("http://1.1.1.1/hello.world");
		assertNotNull(list);
		assertEquals(1, list.size());
		
		list = this.ds.get("http://2.2.2.2/hello.world");
		assertNotNull(list);
		assertEquals(0, list.size());
		
		this.ds.delete("http://1.1.1.1", "http://1.1.1.1/hello.world");
		
		list = this.ds.get("http://1.1.1.1/hello.world");
		assertNotNull(list);
		assertEquals(0, list.size());
		
		list = this.ds.get("http://2.2.2.2/hello.world");
		assertNotNull(list);
		assertEquals(0, list.size());
	}

}
