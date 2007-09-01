package webp2p_sim.core.network;

import org.easymock.classextension.EasyMock;

import webp2p_sim.util.SmartTestCase;

public class SimpleDistanceDelayTest extends SmartTestCase {

	public void testDelay() {
		int maxDown = 256;
		int maxUp = 64;
		double factor = 0.1;
		SimpleDistanceDelay delay = new SimpleDistanceDelay(factor);
		
		AsymetricBandwidth band1 = new AsymetricBandwidth(maxUp, maxDown);
		AsymetricBandwidth band2 = new AsymetricBandwidth(maxUp, maxDown);
		
		Host senderHost = new Host(new Address(254,254,254,254), band1);
		Host receiverHost = new Host(new Address(1,1,1,1), band2);
		
		Connection connection = new Connection(senderHost, receiverHost);
		//adding some messages
		NetworkMessage message1 = EasyMock.createNiceMock(NetworkMessage.class);
		NetworkMessage message2 = EasyMock.createNiceMock(NetworkMessage.class);
		
		EasyMock.expect(message1.dataLeft()).andReturn(300d).anyTimes();
		EasyMock.expect(message2.dataLeft()).andReturn(200d).anyTimes();
		
		EasyMock.replay(message1);
		EasyMock.replay(message2);
		
		connection.transmitMessage(delay, message1);
		connection.transmitMessage(delay, message2);
		
		long xor = senderHost.getAddress().getNumericHost() ^ receiverHost.getAddress().getNumericHost();
		assertEquals((500f / maxUp) + (factor * Long.bitCount(xor)), delay.getDelayBetweenConnection(connection));
	}
	
	public void testDelay2() {
		int maxDown = 256;
		int maxUp = 64;
		double factor = 0.1;
		SimpleDistanceDelay delay = new SimpleDistanceDelay(factor);
		
		AsymetricBandwidth band1 = new AsymetricBandwidth(maxUp, maxDown);
		AsymetricBandwidth band2 = new AsymetricBandwidth(maxUp, maxDown);
		
		Host senderHost = new Host(new Address(254,254,254,254), band1);
		Host receiverHost = new Host(new Address(254,254,254,254), band2);
		
		Connection connection = new Connection(senderHost, receiverHost);
		//adding some messages
		NetworkMessage message1 = EasyMock.createNiceMock(NetworkMessage.class);
		NetworkMessage message2 = EasyMock.createNiceMock(NetworkMessage.class);
		
		EasyMock.expect(message1.dataLeft()).andReturn((300d)).anyTimes();
		EasyMock.expect(message2.dataLeft()).andReturn((200d)).anyTimes();
		
		EasyMock.replay(message1);
		EasyMock.replay(message2);
		
		connection.transmitMessage(delay, message1);
		connection.transmitMessage(delay, message2);
		
		assertEquals(0d, delay.getDelayBetweenConnection(connection));
	}
	
	public void testDelay3() {
		int maxDown = 256;
		int maxUp = 64;
		double factor = 0.1;
		SimpleDistanceDelay delay = new SimpleDistanceDelay(factor);
		
		AsymetricBandwidth band1 = new AsymetricBandwidth(maxUp, maxDown);
		AsymetricBandwidth band2 = new AsymetricBandwidth(maxUp, maxDown);
		
		Host senderHost = new Host(new Address(1,1,1,1), band1);
		Host receiverHost = new Host(new Address(2,2,2,2), band2);
		
		Connection connection = new Connection(senderHost, receiverHost);
		//adding some messages
		NetworkMessage message1 = EasyMock.createNiceMock(NetworkMessage.class);
		NetworkMessage message2 = EasyMock.createNiceMock(NetworkMessage.class);
		
		EasyMock.expect(message1.dataLeft()).andReturn((300d)).anyTimes();
		EasyMock.expect(message2.dataLeft()).andReturn((200d)).anyTimes();
		
		EasyMock.replay(message1);
		EasyMock.replay(message2);
		
		connection.transmitMessage(delay, message1);
		connection.transmitMessage(delay, message2);
		
		assertEquals((500f / maxUp) + (factor * 8), delay.getDelayBetweenConnection(connection));
	}
	
	public void testDelay4() {
		int maxDown = 56;
		int maxUp = 64;
		double factor = 0.1;
		SimpleDistanceDelay delay = new SimpleDistanceDelay(factor);
		
		AsymetricBandwidth band1 = new AsymetricBandwidth(maxUp, maxDown);
		AsymetricBandwidth band2 = new AsymetricBandwidth(maxUp, maxDown);
		
		Host senderHost = new Host(new Address(254,254,254,254), band1);
		Host receiverHost = new Host(new Address(1,1,1,1), band2);
		
		Connection connection = new Connection(senderHost, receiverHost);
		//adding some messages
		NetworkMessage message1 = EasyMock.createNiceMock(NetworkMessage.class);
		NetworkMessage message2 = EasyMock.createNiceMock(NetworkMessage.class);
		
		EasyMock.expect(message1.dataLeft()).andReturn((300d)).anyTimes();
		EasyMock.expect(message2.dataLeft()).andReturn((200d)).anyTimes();
		
		EasyMock.replay(message1);
		EasyMock.replay(message2);
		
		connection.transmitMessage(delay, message1);
		connection.transmitMessage(delay, message2);
		
		long xor = senderHost.getAddress().getNumericHost() ^ receiverHost.getAddress().getNumericHost();
		assertEquals((500d / maxDown) + (factor * Long.bitCount(xor)), delay.getDelayBetweenConnection(connection));
	}
}
