package webp2p_sim.core.network;

import webp2p_sim.core.Clock;
import webp2p_sim.core.entity.SimpleQueuedEntity;
import webp2p_sim.core.network.Network.Type;
import webp2p_sim.util.SmartTestCase;
import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.uah.math.distributions.Distribution;

public class MessageSendingTest extends SmartTestCase {

	public void testMessageExchange() {
		Network network = new Network(Type.TCP);
		
		Sender s = new Sender(new Host(new Address(192,168,254,1), new AsymetricBandwidth(256 * 1024, 512 * 1024)), new ContinuousUniformDistribution(0, 0), network, true);
		Receiver r = new Receiver(new Host(new Address(192,168,254,2), new AsymetricBandwidth(256 * 1024, 512 * 1024)), new ContinuousUniformDistribution(0, 0), network, true);
		
		Clock.getInstance().addEntities(s, r, network);
		s.sendTestMessage(r.getHost(), 300 * 1024);
		
		assertFalse(r.received);
		
		Clock.getInstance().countToTick(1);
		assertFalse(r.received);
		
		Clock.getInstance().countToTick(2);
		assertTrue(r.received);
	}
	
	private class Sender extends SimpleQueuedEntity {

		public Sender(Host host, Distribution distribution, Network network, boolean bindSelf) {
			super(host, distribution, network, bindSelf);
		}
		
		public void sendTestMessage(Host host, long size) {
			sendMessage(host, new SimpleMessage(size));
		}
	}
	
	private class Receiver extends SimpleQueuedEntity {

		private boolean received = false;

		public Receiver(Host host, Distribution distribution, Network network, boolean bindSelf) {
			super(host, distribution, network, bindSelf);
		}
		
		public void hereIsMessage() {
			this.received = true;
		}
	}
	
	private class SimpleMessage extends AbstractApplicationMessage {

		public SimpleMessage(long size) {
			super(size);
		}
		
		@Override
		public void realProcess() {
			((Receiver) receiverEntity).hereIsMessage();
		}
		
	}
}
