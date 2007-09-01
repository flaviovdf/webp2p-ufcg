package webp2p_sim.core.network;

import org.easymock.classextension.EasyMock;

import webp2p_sim.core.entity.ApplicationMessage;
import webp2p_sim.util.SmartTestCase;

public class ConnectionTest extends SmartTestCase {

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
		
		EndToEndDelay endToEndDelay = EasyMock.createNiceMock(EndToEndDelay.class);
		EasyMock.expect(endToEndDelay.getDelayBetweenConnection((Connection) EasyMock.anyObject())).andReturn(10d).anyTimes();
		EasyMock.replay(endToEndDelay);
		
		AsymetricBandwidth band1 = new AsymetricBandwidth(maxUp, maxDown);
		AsymetricBandwidth band2 = new AsymetricBandwidth(maxUp, maxDown);
		
		Host senderHost = new Host(new Address(254,254,254,254, 80), band1);
		Host receiverHost = new Host(new Address(1,1,1,1, 80), band2);
		
		Connection connection = new Connection(senderHost, receiverHost);
		//adding some messages
		NetworkMessage message1 = EasyMock.createNiceMock(NetworkMessage.class);
		NetworkMessage message2 = EasyMock.createNiceMock(NetworkMessage.class);
		
		EasyMock.expect(message1.dataLeft()).andReturn(300d).anyTimes();
		EasyMock.expect(message2.dataLeft()).andReturn(200d).anyTimes();
		
		EasyMock.replay(message1);
		EasyMock.replay(message2);
		
		connection.transmitMessage(endToEndDelay, message1);
		connection.transmitMessage(endToEndDelay, message2);
		
		assertEquals(500d, connection.getAmountDataBeingTransfered());
		
		assertEquals((float)maxDown, connection.getAllocatedReceiverDownloadBandwidth());
		assertEquals((float)maxUp, connection.getAllocatedSenderUploadBandwidth());
		
		assertFalse(connection.noMoreMessages());
	}

	public void testFlushMessage() {
		int maxDown = 256;
		int maxUp = 64;
		
		EndToEndDelay endToEndDelay = EasyMock.createNiceMock(EndToEndDelay.class);
		EasyMock.expect(endToEndDelay.getDelayBetweenConnection((Connection) EasyMock.anyObject())).andReturn(10d).anyTimes();
		EasyMock.replay(endToEndDelay);
		
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
		
		EndToEndDelay delay = EasyMock.createMock(EndToEndDelay.class);
		EasyMock.expect(delay.getDelayBetweenConnection(connection)).andReturn(new Double(2)).andReturn(new Double(3));
		EasyMock.replay(delay);
		
		NetworkMessage message1 = new NetworkMessage(appMsg1);
		NetworkMessage message2 = new NetworkMessage(appMsg2);
		connection.transmitMessage(delay, message1);
		connection.transmitMessage(delay, message2);
		assertEquals(300d, message1.dataLeft());
		assertEquals(200d, message2.dataLeft());
		assertEquals(500d, connection.getAmountDataBeingTransfered());
		
		assertEquals((float)maxDown, connection.getAllocatedReceiverDownloadBandwidth());
		assertEquals((float)maxUp, connection.getAllocatedSenderUploadBandwidth());
		
		assertFalse(connection.noMoreMessages());
		
		
		connection.flushData();
		assertEquals(217, message1.dataLeft(), 1);
		assertEquals(116, message2.dataLeft(), 1);
		assertFalse(connection.noMoreMessages());
		
		connection.flushData();
		assertEquals(133, message1.dataLeft(), 1);
		assertEquals(33, message2.dataLeft(), 1);
		assertFalse(connection.noMoreMessages());
		
		connection.flushData();
		assertEquals(1d, message1.dataLeft());
		assertEquals(0d, message2.dataLeft());
		assertFalse(connection.noMoreMessages());
		
		EasyMock.verify(delay);
	}


}
