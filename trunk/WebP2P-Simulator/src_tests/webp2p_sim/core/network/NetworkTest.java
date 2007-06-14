package webp2p_sim.core.network;

import org.easymock.classextension.EasyMock;

import webp2p_sim.core.entity.ApplicationMessage;
import webp2p_sim.core.entity.NetworkEntity;
import webp2p_sim.util.SmartTestCase;

public class NetworkTest extends SmartTestCase {

	public void testBind() {
		Network network = Network.getInstance();
		Address address = new Address(254,254,254,254, 80);
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
		Network network = Network.getInstance();
		Address address = new Address(254,254,254,254, 80);
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
		Network network = Network.getInstance();
		Address address = new Address(254,254,254,254, 80);
		AsymetricBandwidth band = new AsymetricBandwidth(10, 10);
		Host host = new Host(address, band);
		
		Address otherAddress = new Address(250,250,250,250, 80);
		AsymetricBandwidth otherBand = new AsymetricBandwidth(20, 10);
		Host otherHost = new Host(otherAddress, otherBand);
		
		Address yetAnotherAddress = new Address(252,252,252,252, 80);
		AsymetricBandwidth yetAnotherBand = new AsymetricBandwidth(20, 10);
		Host yetAnotherHost = new Host(yetAnotherAddress, yetAnotherBand);
		
		NetworkEntity networkEntity = EasyMock.createMock(NetworkEntity.class);
		NetworkEntity otherNetworkEntity = EasyMock.createMock(NetworkEntity.class);
		NetworkEntity yetAotherNetworkEntity = EasyMock.createMock(NetworkEntity.class);
		
		ApplicationMessage message = EasyMock.createNiceMock(ApplicationMessage.class);
		ApplicationMessage message2 = EasyMock.createNiceMock(ApplicationMessage.class);
		ApplicationMessage message3 = EasyMock.createNiceMock(ApplicationMessage.class);
		
		EasyMock.expect(message.size()).andReturn(10l).anyTimes();
		
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
	}

}
