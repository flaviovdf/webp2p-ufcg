package webp2p_sim.core.network;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;

public class ConnectionTest extends TestCase {

	public void testGets() {
		int maxDown = 256;
		int maxUp = 64;
		
		AsymetricBandwidth band1 = new AsymetricBandwidth(maxUp, maxDown);
		AsymetricBandwidth band2 = new AsymetricBandwidth(maxUp, maxDown);
		
		Host senderHost = new Host(new Address(254,254,254,254, 80), band1);
		Host receiverHost = new Host(new Address(1,1,1,1, 80), band2);
		
		Connection connection = new Connection(senderHost, receiverHost);
		assertEquals(new Address(254,254,254,254, 80), connection.getSenderAddress());
		assertEquals(new Address(1,1,1,1, 80), connection.getReceiverAddress());
		
		try {
			connection.getAllocatedReceiverDownloadBandwidth();
			fail();
		} catch (NetworkException e) {}
		
		try {
			connection.getAllocatedSenderUploadBandwidth();
			fail();
		} catch (NetworkException e) {}
		
		assertTrue(connection.noMoreMessages());
	}

	public void testTransmitMessage() {
		int maxDown = 256;
		int maxUp = 64;
		
		AsymetricBandwidth band1 = new AsymetricBandwidth(maxUp, maxDown);
		AsymetricBandwidth band2 = new AsymetricBandwidth(maxUp, maxDown);
		
		Host senderHost = new Host(new Address(254,254,254,254, 80), band1);
		Host receiverHost = new Host(new Address(1,1,1,1, 80), band2);
		
		Connection connection = new Connection(senderHost, receiverHost);
		//adding some messages
		NetworkMessage message1 = EasyMock.createNiceMock(NetworkMessage.class);
		NetworkMessage message2 = EasyMock.createNiceMock(NetworkMessage.class);
		
		EasyMock.expect(message1.dataLeft()).andReturn(new Long(300)).anyTimes();
		EasyMock.expect(message2.dataLeft()).andReturn(new Long(200)).anyTimes();
		
		EasyMock.replay(message1);
		EasyMock.replay(message2);
		
		connection.transmitMessage(message1);
		connection.transmitMessage(message2);
		
		assertEquals(500, connection.getAmountDataBeingTransfered());
		
		assertEquals((float)maxDown, connection.getAllocatedReceiverDownloadBandwidth());
		assertEquals((float)maxUp, connection.getAllocatedSenderUploadBandwidth());
		
		assertFalse(connection.noMoreMessages());
	}

	public void testFlushMessage() {
		int maxDown = 256;
		int maxUp = 64;
		
		AsymetricBandwidth band1 = new AsymetricBandwidth(maxUp, maxDown);
		AsymetricBandwidth band2 = new AsymetricBandwidth(maxUp, maxDown);
		
		Host senderHost = new Host(new Address(254,254,254,254, 80), band1);
		Host receiverHost = new Host(new Address(1,1,1,1, 80), band2);
		
		Connection connection = new Connection(senderHost, receiverHost);
		//adding some messages
		ApplicationMessage appMsg1 = EasyMock.createNiceMock(ApplicationMessage.class);
		ApplicationMessage appMsg2 = EasyMock.createNiceMock(ApplicationMessage.class);
		
		EasyMock.expect(appMsg1.size()).andReturn(new Long(300)).anyTimes();
		EasyMock.expect(appMsg2.size()).andReturn(new Long(200)).anyTimes();
		
		EasyMock.replay(appMsg1);
		EasyMock.replay(appMsg2);
		
		NetworkMessage message1 = new NetworkMessage(appMsg1);
		NetworkMessage message2 = new NetworkMessage(appMsg2);
		connection.transmitMessage(message1);
		connection.transmitMessage(message2);
		assertEquals(300, message1.dataLeft());
		assertEquals(200, message2.dataLeft());
		assertEquals(500, connection.getAmountDataBeingTransfered());
		
		assertEquals((float)maxDown, connection.getAllocatedReceiverDownloadBandwidth());
		assertEquals((float)maxUp, connection.getAllocatedSenderUploadBandwidth());
		
		assertFalse(connection.noMoreMessages());
		
		EndToEndDelay delay = EasyMock.createMock(EndToEndDelay.class);
		EasyMock.expect(delay.getDelayBetweenConnection(connection)).andReturn(new Double(2)).andReturn(new Double(2)).andReturn(new Double(2)).andReturn(new Double(1));
		
		EasyMock.replay(delay);
		
		connection.flushData(delay, 1);
		assertEquals(175, message1.dataLeft());
		assertEquals(75, message2.dataLeft());
		assertFalse(connection.noMoreMessages());
		
		connection.flushData(delay, 1);
		assertEquals(112, message1.dataLeft());
		assertEquals(12, message2.dataLeft());
		assertFalse(connection.noMoreMessages());
		
		connection.flushData(delay, 1);
		assertEquals(62, message1.dataLeft());
		assertEquals(0, message2.dataLeft());
		assertFalse(connection.noMoreMessages());
		
		connection.flushData(delay, 1);
		assertEquals(0, message1.dataLeft());
		assertEquals(0, message2.dataLeft());
		assertTrue(connection.noMoreMessages());
		
		EasyMock.verify(delay);
	}


}
