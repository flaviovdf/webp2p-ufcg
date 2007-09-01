package webp2p_sim.util;

import junit.framework.TestCase;
import webp2p_sim.core.Clock;
import webp2p_sim.core.network.Address;
import webp2p_sim.core.network.AsymetricBandwidth;
import webp2p_sim.core.network.Host;
import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.uah.math.distributions.Distribution;

public abstract class SmartTestCase extends TestCase {

	protected static final Distribution ZERO_DIST = new ContinuousUniformDistribution(0, 0);
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Clock.reset();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		Clock.reset();
	}
	
	public Host createRandomHost() {
		int byte1 = (int) (Math.random() * 253 + 1);
		int byte2 = (int) (Math.random() * 253 + 1);
		int byte3 = (int) (Math.random() * 253 + 1);
		int byte4 = (int) (Math.random() * 253 + 1);
		
		return new Host(new Address(byte1, byte2, byte3, byte4), new AsymetricBandwidth(10, 10));
	}
	
}
