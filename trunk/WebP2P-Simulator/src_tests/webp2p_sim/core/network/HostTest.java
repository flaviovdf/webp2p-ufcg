package webp2p_sim.core.network;

import webp2p_sim.util.SmartTestCase;

public class HostTest extends SmartTestCase {

	public void testAll() {
		int maxDown = 256;
		int maxUp = 64;
		
		AsymetricBandwidth band = new AsymetricBandwidth(maxUp, maxDown);
		
		Host host = new Host(new Address(254,254,254,254, 80), band);
		
		assertEquals(new Address(254,254,254,254, 80), host.getAddress());
		assertEquals(new Host(new Address(254,254,254,254, 80), new AsymetricBandwidth(maxUp, maxDown)), host);
		assertEquals(new Host(new Address(254,254,254,254, 80), null), host);
		
		assertFalse(host.equals(new Host(new Address(253,253,253,253, 80), new AsymetricBandwidth(maxUp, maxDown))));
	}
	
}
