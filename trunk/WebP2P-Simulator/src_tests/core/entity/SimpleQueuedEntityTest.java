package core.entity;

import util.SmartTestCase;
import edu.uah.math.distributions.ContinuousUniformDistribution;
import junit.framework.TestCase;

public class SimpleQueuedEntityTest extends SmartTestCase {

	private SimpleQueuedEntity simpleQueue;

	protected void setUp() throws Exception {
		super.setUp();
		this.simpleQueue = new SimpleQueuedEntity("SQE", new ContinuousUniformDistribution(0, 0));
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testAddRequest1() {
		final int NUMBER_OF_TICKS = 5;
		this.simpleQueue.tickOcurred();
		
		FakeMessage request = new FakeMessage(NUMBER_OF_TICKS, 1);
		this.simpleQueue.sendMessage(request);
		assertFalse(request.isDone());
		assertEquals(0, request.getProcessed());
		
		for (int i = 0; i < NUMBER_OF_TICKS; i++) {
			this.simpleQueue.tickOcurred();
			assertFalse(request.isDone());
			assertEquals(0, request.getProcessed());
		}
		
		this.simpleQueue.tickOcurred();
		request.verify();
	}
	
	public void testAddRequest2() {
		FakeMessage request1 = new FakeMessage(0, 1);
		FakeMessage request2 = new FakeMessage(0, 1);
		this.simpleQueue.sendMessage(request1);
		this.simpleQueue.sendMessage(request2);
		
		this.simpleQueue.tickOcurred();
		request1.verify();
		request2.verify();
		
		this.simpleQueue.tickOcurred();
		request1.verify();
		request2.verify();
	}

}
