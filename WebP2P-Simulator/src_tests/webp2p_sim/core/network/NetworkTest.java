package webp2p_sim.core.network;

import org.easymock.classextension.EasyMock;

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
		
	}
	
	public void testTickOcurred() {
		Network network = Network.getInstance();
	}

}
