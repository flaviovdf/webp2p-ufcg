package webp2p_sim.core.network;

import webp2p_sim.util.SmartTestCase;

public class AddressTest extends SmartTestCase {

	public void testAll() {
		Address address = new Address(192, 158, 254, 1, 9999);
		assertEquals("192.158.254.1", address.getHost());
		assertEquals(1921582541, address.getNumericHost());
		assertEquals(9999, address.getPort());
		assertEquals("192.158.254.1:9999", address.toString());
		
		Address address1 = new Address(192, 158, 254, 1, 9990);
		Address address2 = new Address(192, 158, 254, 2, 9999);
		Address address3 = new Address(192, 158, 254, 1, 9999);
		
		assertFalse(address.equals(address1));
		assertFalse(address.equals(address2));
		assertEquals(address, address3);
	}
	
}
