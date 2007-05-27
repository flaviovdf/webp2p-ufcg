package common;

import util.SmartTestCase;

public class InfinitTimeToLiveTest extends SmartTestCase {

	public void testTLL() {
		InfinitTimeToLive ittl = new InfinitTimeToLive();
		
		assertEquals(Integer.MAX_VALUE, ittl.remaining());
		assertEquals(Integer.MAX_VALUE, ittl.decrease());
		assertEquals(Integer.MAX_VALUE, ittl.remaining());
	}
	
}
