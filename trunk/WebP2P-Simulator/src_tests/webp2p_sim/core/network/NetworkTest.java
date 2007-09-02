package webp2p_sim.core.network;

import org.easymock.classextension.EasyMock;

import webp2p_sim.core.entity.NetworkEntity;
import webp2p_sim.core.network.Network.Type;
import webp2p_sim.util.SmartTestCase;

public class NetworkTest extends SmartTestCase {

	public void testBind() {
		Network network = new Network(Type.TEST);
		Address address = new Address(254,254,254,254);
		AsymetricBandwidth band = new AsymetricBandwidth(10, 10);
		Host host = new Host(address, band);
		
		NetworkEntity networkEntity = EasyMock.createMock(NetworkEntity.class);
		network.bind(host, networkEntity);
		
		try {
			network.bind(host, networkEntity);
			fail();
		} catch (NetworkException e) { }
	}

	public void testUnbind() {
		Network network = new Network(Type.TEST);
		Address address = new Address(254,254,254,254);
		AsymetricBandwidth band = new AsymetricBandwidth(10, 10);
		Host host = new Host(address, band);
		
		NetworkEntity networkEntity = EasyMock.createMock(NetworkEntity.class);
		
		try {
			network.unbind(host);
			fail();
		} catch (NetworkException e) { }
		
		network.bind(host, networkEntity);
		network.unbind(host);
	}

	public void testSendMessage() {
		Network network = new Network(Type.TEST);
		Address address = new Address(254,254,254,254);
		AsymetricBandwidth band = new AsymetricBandwidth(10, 10);
		Host host = new Host(address, band);
		
		Address otherAddress = new Address(250,250,250,250);
		AsymetricBandwidth otherBand = new AsymetricBandwidth(20, 10);
		Host otherHost = new Host(otherAddress, otherBand);
		
		Address yetAnotherAddress = new Address(252,252,252,252);
		AsymetricBandwidth yetAnotherBand = new AsymetricBandwidth(20, 10);
		Host yetAnotherHost = new Host(yetAnotherAddress, yetAnotherBand);
		
		NetworkEntity networkEntity = EasyMock.createMock(NetworkEntity.class);
		NetworkEntity otherNetworkEntity = EasyMock.createMock(NetworkEntity.class);
		NetworkEntity yetAotherNetworkEntity = EasyMock.createMock(NetworkEntity.class);
		
		ApplicationMessage message = EasyMock.createNiceMock(ApplicationMessage.class);
		ApplicationMessage message2 = EasyMock.createNiceMock(ApplicationMessage.class);
		ApplicationMessage message3 = EasyMock.createNiceMock(ApplicationMessage.class);
		
		network.bind(host, networkEntity);
		network.bind(otherHost, otherNetworkEntity);
		network.bind(yetAnotherHost, yetAotherNetworkEntity);
		
		assertNull(network.getConnection(host, otherHost));
		
		network.sendMessage(host, otherHost, message);
		assertNotNull(network.getConnection(host, otherHost));
		assertEquals(1, network.getConnection(host, otherHost).getAmountMessagesBeingTransfered());
		
		network.sendMessage(host, otherHost, message2);
		assertEquals(2, network.getConnection(host, otherHost).getAmountMessagesBeingTransfered());
		
		network.sendMessage(host, yetAnotherHost, message3);
		assertEquals(2, network.getConnection(host, otherHost).getAmountMessagesBeingTransfered());
		assertEquals(1, network.getConnection(host, yetAnotherHost).getAmountMessagesBeingTransfered());
		
		//Unknown receiver
		network.sendMessage(host, new Host(new Address(1, 1, 1, 1), new SymetricBandwidth(1)), message3);
		assertEquals(2, network.getConnection(host, otherHost).getAmountMessagesBeingTransfered());
		assertEquals(1, network.getConnection(host, yetAnotherHost).getAmountMessagesBeingTransfered());
		
		//Unknown sender
		network.sendMessage(new Host(new Address(1, 1, 1, 1), new SymetricBandwidth(1)), yetAnotherHost, message3);
		assertEquals(2, network.getConnection(host, otherHost).getAmountMessagesBeingTransfered());
		assertEquals(1, network.getConnection(host, yetAnotherHost).getAmountMessagesBeingTransfered());
	}

	public void testTickOcurredOneMessage() {
		Network network = new Network(Type.TEST);
		Address address = new Address(254,254,254,254);
		AsymetricBandwidth band = new AsymetricBandwidth(10, 10);
		Host host = new Host(address, band);
		
		Address otherAddress = new Address(250,250,250,250);
		AsymetricBandwidth otherBand = new AsymetricBandwidth(20, 10);
		Host otherHost = new Host(otherAddress, otherBand);
		
		NetworkEntity networkEntity = EasyMock.createMock(NetworkEntity.class);
		NetworkEntity otherNetworkEntity = EasyMock.createMock(NetworkEntity.class);
		
		ApplicationMessage message = EasyMock.createNiceMock(ApplicationMessage.class);
		
		EasyMock.expect(message.size()).andReturn(1l);
		
		network.bind(host, networkEntity);
		network.bind(otherHost, otherNetworkEntity);
		
		otherNetworkEntity.receiveMessage(message);
		EasyMock.replay(message, otherNetworkEntity);
		
		network.sendMessage(host, otherHost, message);
		assertEquals(1, network.getConnection(host, otherHost).getAmountMessagesBeingTransfered());
		
		EndToEndDelay delay = network.getDelay();
		double delayBetweenConnection = delay.getDelayBetweenConnection(network.getConnection(host, otherHost));
		for (int i = 1; i<= Math.round(delayBetweenConnection); i++) {
			network.tickOcurred();
		}
		assertNull(network.getConnection(host, otherHost));
		
		EasyMock.verify(otherNetworkEntity);
	}
	
	public void testTickOcurredManyMessages() {
		Network network = new Network(Type.TEST);
		Address address = new Address(254,254,254,254);
		AsymetricBandwidth band = new AsymetricBandwidth(10, 10);
		Host host = new Host(address, band);
		
		Address otherAddress = new Address(250,250,250,250);
		AsymetricBandwidth otherBand = new AsymetricBandwidth(20, 10);
		Host otherHost = new Host(otherAddress, otherBand);
		
		NetworkEntity networkEntity = EasyMock.createMock(NetworkEntity.class);
		NetworkEntity otherNetworkEntity = EasyMock.createMock(NetworkEntity.class);
		
		ApplicationMessage message = EasyMock.createNiceMock(ApplicationMessage.class);
		ApplicationMessage message2 = EasyMock.createNiceMock(ApplicationMessage.class);
		
		EasyMock.expect(message.size()).andReturn(1l);
		EasyMock.expect(message2.size()).andReturn(10l);
		
		network.bind(host, networkEntity);
		network.bind(otherHost, otherNetworkEntity);
		
		otherNetworkEntity.receiveMessage(message);
		otherNetworkEntity.receiveMessage(message2);
		
		EasyMock.replay(message, message2, otherNetworkEntity);
		
		network.sendMessage(host, otherHost, message);
		network.sendMessage(host, otherHost, message2);
		assertEquals(2, network.getConnection(host, otherHost).getAmountMessagesBeingTransfered());
		
		EndToEndDelay delay = network.getDelay();
		double delayBetweenConnection = delay.getDelayBetweenConnection(network.getConnection(host, otherHost));
		for (int i = 1; i<= Math.round(delayBetweenConnection); i++) {
			network.tickOcurred();
		}
		assertNull(network.getConnection(host, otherHost));
		
		EasyMock.verify(otherNetworkEntity);
	}
	
	public void testTickOcurredManyMessages2() {
		Network network = new Network(Type.TEST);
		Address address = new Address(254,254,254,254);
		AsymetricBandwidth band = new AsymetricBandwidth(10, 10);
		Host host = new Host(address, band);
		
		Address otherAddress = new Address(250,250,250,250);
		AsymetricBandwidth otherBand = new AsymetricBandwidth(20, 10);
		Host otherHost = new Host(otherAddress, otherBand);
		
		NetworkEntity networkEntity = EasyMock.createMock(NetworkEntity.class);
		NetworkEntity otherNetworkEntity = EasyMock.createMock(NetworkEntity.class);
		
		ApplicationMessage message = EasyMock.createNiceMock(ApplicationMessage.class);
		ApplicationMessage message2 = EasyMock.createNiceMock(ApplicationMessage.class);
		
		EasyMock.expect(message.size()).andReturn(1l);
		EasyMock.expect(message2.size()).andReturn(10l);
		
		network.bind(host, networkEntity);
		network.bind(otherHost, otherNetworkEntity);
		
		otherNetworkEntity.receiveMessage(message);
		otherNetworkEntity.receiveMessage(message2);
		
		EasyMock.replay(message, message2, otherNetworkEntity);
		
		network.sendMessage(host, otherHost, message);
		assertEquals(1, network.getConnection(host, otherHost).getAmountMessagesBeingTransfered());
		
		EndToEndDelay delay = network.getDelay();
		double delayBetweenConnection = delay.getDelayBetweenConnection(network.getConnection(host, otherHost));
		for (int i = 1; i<= Math.round(delayBetweenConnection / 2); i++) {
			network.tickOcurred();
		}
		assertEquals(1, network.getConnection(host, otherHost).getAmountMessagesBeingTransfered());
		
		network.sendMessage(host, otherHost, message2);
		assertEquals(2, network.getConnection(host, otherHost).getAmountMessagesBeingTransfered());
		
		double newDelayBetwenn = delay.getDelayBetweenConnection(network.getConnection(host, otherHost));
		for (int i = 1; i<= Math.round(newDelayBetwenn); i++) {
			network.tickOcurred();
		}
		assertNull(network.getConnection(host, otherHost));
		
		EasyMock.verify(otherNetworkEntity);
	}
	
	public void testTickOcurredManyMessagesFailures() {
		Network network = new Network(Type.TEST);
		Address address = new Address(254,254,254,254);
		AsymetricBandwidth band = new AsymetricBandwidth(10, 10);
		Host host = new Host(address, band);
		
		Address otherAddress = new Address(250,250,250,250);
		AsymetricBandwidth otherBand = new AsymetricBandwidth(20, 10);
		Host otherHost = new Host(otherAddress, otherBand);
		
		NetworkEntity networkEntity = EasyMock.createMock(NetworkEntity.class);
		NetworkEntity otherNetworkEntity = EasyMock.createMock(NetworkEntity.class);
		
		ApplicationMessage message = EasyMock.createNiceMock(ApplicationMessage.class);
		
		EasyMock.expect(message.size()).andReturn(1l);
		
		network.bind(host, networkEntity);
		network.bind(otherHost, otherNetworkEntity);
		
		EasyMock.replay(message);
		
		network.sendMessage(host, otherHost, message);
		assertEquals(1, network.getConnection(host, otherHost).getAmountMessagesBeingTransfered());
		network.tickOcurred();
		assertEquals(1, network.getConnection(host, otherHost).getAmountMessagesBeingTransfered());
		network.unbind(host);
		assertEquals(1, network.getConnection(host, otherHost).getAmountMessagesBeingTransfered());
		network.tickOcurred();
		assertNull(network.getConnection(host, otherHost));
	}
	
	public void testTickOcurredManyMessagesFailures2() {
		Network network = new Network(Type.TEST);
		Address address = new Address(254,254,254,254);
		AsymetricBandwidth band = new AsymetricBandwidth(10, 10);
		Host host = new Host(address, band);
		
		Address otherAddress = new Address(250,250,250,250);
		AsymetricBandwidth otherBand = new AsymetricBandwidth(20, 10);
		Host otherHost = new Host(otherAddress, otherBand);
		
		NetworkEntity networkEntity = EasyMock.createMock(NetworkEntity.class);
		NetworkEntity otherNetworkEntity = EasyMock.createMock(NetworkEntity.class);
		
		ApplicationMessage message = EasyMock.createNiceMock(ApplicationMessage.class);
		
		EasyMock.expect(message.size()).andReturn(1l);
		
		network.bind(host, networkEntity);
		network.bind(otherHost, otherNetworkEntity);
		
		EasyMock.replay(message);
		
		network.sendMessage(host, otherHost, message);
		assertEquals(1, network.getConnection(host, otherHost).getAmountMessagesBeingTransfered());
		network.tickOcurred();
		assertEquals(1, network.getConnection(host, otherHost).getAmountMessagesBeingTransfered());
		network.unbind(otherHost);
		assertEquals(1, network.getConnection(host, otherHost).getAmountMessagesBeingTransfered());
		network.tickOcurred();
		assertNull(network.getConnection(host, otherHost));
	}
}
