package webp2p_sim.util;


public class InfinitTimeToLiveTest extends SmartTestCase {

	public void testTLL() {
		InfinitTimeToLive ittl = new InfinitTimeToLive();
		
		assertEquals(Integer.MAX_VALUE, ittl.remaining());
		assertEquals(Integer.MAX_VALUE, ittl.decrease());
		assertEquals(Integer.MAX_VALUE, ittl.remaining());
	}
	
}
