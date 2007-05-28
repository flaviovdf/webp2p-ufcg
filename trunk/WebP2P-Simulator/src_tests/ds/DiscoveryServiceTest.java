package ds;

import util.SmartTestCase;
import edu.uah.math.distributions.ContinuousUniformDistribution;

public class DiscoveryServiceTest extends SmartTestCase {

	private DiscoveryService ds;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.ds = new DiscoveryService("DS", new ContinuousUniformDistribution(0, 0));
	}
	
	public void testTODO() throws Exception {
		
	}
	
}
