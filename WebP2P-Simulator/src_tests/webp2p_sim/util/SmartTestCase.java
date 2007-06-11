package webp2p_sim.util;

import junit.framework.TestCase;
import webp2p_sim.core.Clock;
import webp2p_sim.core.network.Network;
import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.uah.math.distributions.Distribution;

public abstract class SmartTestCase extends TestCase {

	protected static final Distribution ZERO_DIST = new ContinuousUniformDistribution(0, 0);
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Clock.reset();
		Network.reset();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		Clock.reset();
		Network.reset();
	}
}
