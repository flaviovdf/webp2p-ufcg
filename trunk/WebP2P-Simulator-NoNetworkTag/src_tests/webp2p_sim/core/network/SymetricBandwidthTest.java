package webp2p_sim.core.network;

import org.easymock.classextension.EasyMock;

import webp2p_sim.util.SmartTestCase;

public class SymetricBandwidthTest extends SmartTestCase {

	public void testAllocateDownBand() {
		int max = 300;
		SymetricBandwidth band = new SymetricBandwidth(max);
		
		Connection connection1 = EasyMock.createNiceMock(Connection.class);
		Connection connection2 = EasyMock.createNiceMock(Connection.class);
		Connection connection3 = EasyMock.createNiceMock(Connection.class);
		
		
		try {
			band.getAllocatedDownBand(connection1);
			fail();
		} catch(NetworkException e) {}
		
		try {
			band.getAllocatedDownBand(connection2);
			fail();
		} catch(NetworkException e) {}

		try {
			band.getAllocatedDownBand(connection3);
			fail();
		} catch(NetworkException e) {}
		
		//allocating
		band.allocateDownBand(connection1);
		assertEquals(max, band.getAllocatedDownBand(connection1));
		
		band.allocateDownBand(connection2);
		assertEquals(max / 2, band.getAllocatedDownBand(connection1));
		assertEquals(max / 2, band.getAllocatedDownBand(connection2));
		
		band.allocateDownBand(connection3);
		assertEquals(max / 3, band.getAllocatedDownBand(connection1));
		assertEquals(max / 3, band.getAllocatedDownBand(connection2));
		assertEquals(max / 3, band.getAllocatedDownBand(connection3));
		
		
		//deallocating
		band.deallocateDownBand(connection1);
		assertEquals(max / 2, band.getAllocatedDownBand(connection2));
		assertEquals(max / 2, band.getAllocatedDownBand(connection3));
		
		band.deallocateDownBand(connection2);
		assertEquals(max, band.getAllocatedDownBand(connection3));
	}
	
	public void testAllocateUpBand() {
		int max = 300;
		SymetricBandwidth band = new SymetricBandwidth(max);
		
		Connection connection1 = EasyMock.createNiceMock(Connection.class);
		Connection connection2 = EasyMock.createNiceMock(Connection.class);
		Connection connection3 = EasyMock.createNiceMock(Connection.class);
		
		
		try {
			band.getAllocatedUpBand(connection1);
			fail();
		} catch(NetworkException e) {}
		
		try {
			band.getAllocatedUpBand(connection2);
			fail();
		} catch(NetworkException e) {}

		try {
			band.getAllocatedUpBand(connection3);
			fail();
		} catch(NetworkException e) {}
		
		band.allocateUpBand(connection1);
		assertEquals(max, band.getAllocatedUpBand(connection1));
		
		band.allocateUpBand(connection2);
		assertEquals(max / 2, band.getAllocatedUpBand(connection1));
		assertEquals(max / 2, band.getAllocatedUpBand(connection2));
		
		band.allocateUpBand(connection3);
		assertEquals(max / 3, band.getAllocatedUpBand(connection1));
		assertEquals(max / 3, band.getAllocatedUpBand(connection2));
		assertEquals(max / 3, band.getAllocatedUpBand(connection3));
		
		//deallocating
		band.deallocateUpBand(connection1);
		assertEquals(max / 2, band.getAllocatedUpBand(connection2));
		assertEquals(max / 2, band.getAllocatedUpBand(connection3));
		
		band.deallocateUpBand(connection2);
		assertEquals(max, band.getAllocatedUpBand(connection3));
	}
	
	public void testAllocationOfUpBandDoesAffectDown() {
		int max = 300;
		SymetricBandwidth band = new SymetricBandwidth(max);
		
		Connection connection1 = EasyMock.createNiceMock(Connection.class);

		band.allocateUpBand(connection1);
		band.allocateDownBand(connection1);
		
		assertEquals(max / 2, band.getAllocatedUpBand(connection1));
		assertEquals(max / 2, band.getAllocatedDownBand(connection1));
	}
}
