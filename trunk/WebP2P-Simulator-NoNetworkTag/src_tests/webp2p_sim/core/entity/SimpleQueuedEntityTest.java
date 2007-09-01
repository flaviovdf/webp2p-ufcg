package webp2p_sim.core.entity;

import webp2p_sim.core.Clock;
import webp2p_sim.util.SmartTestCase;
import edu.uah.math.distributions.ContinuousUniformDistribution;

public class SimpleQueuedEntityTest extends SmartTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testSendMessage() {
		final int NUMBER_OF_TICKS = 5;
		SimpleQueuedEntity simpleQueue = new SimpleQueuedEntity("SQE", new ContinuousUniformDistribution(NUMBER_OF_TICKS, NUMBER_OF_TICKS));
		simpleQueue.tickOcurred();
		
		TestMessageImpl message = new TestMessageImpl();
		simpleQueue.sendMessage(message);
		
		assertEquals((double)NUMBER_OF_TICKS, message.getProcessTime());
		
		//no exception should be thrown
		for (int i = 0; i <= NUMBER_OF_TICKS + 2; i++) {
			simpleQueue.tickOcurred();
		}
		
		assertTrue(message.isProcessed());
	}
	
	public void testSendMessage2() {
		double processTime = 0.4;
		SimpleQueuedEntity simpleQueue = new SimpleQueuedEntity("SQE", new ContinuousUniformDistribution(processTime, processTime));
		
		//10 messages of length 0.4 take excatly 4 units of time to complete (each tick process 2.5 messages).
		TestMessageImpl[] testMessageImpls = new TestMessageImpl[10];
		for (int i = 0; i < testMessageImpls.length; i++) {
			testMessageImpls[i] = new TestMessageImpl();
			simpleQueue.sendMessage(testMessageImpls[i]);
			assertEquals(processTime, testMessageImpls[i].getProcessTime());
		}

		assertEquals(10, simpleQueue.getMessageQueue().size());
		
		Clock.getInstance().addEntities(simpleQueue);
		Clock.getInstance().countToTick(4);
		assertEquals(0, simpleQueue.getMessageQueue().size());
		assertEquals(4, Clock.getInstance().getCurrentTick());
		
		for (int i = 0; i < testMessageImpls.length; i++) {
			assertTrue(testMessageImpls[i].isDone());
			assertTrue(testMessageImpls[i].isProcessed());
		}
	}
	
	public void testSendMessage3() {
		double processTime = 0.4;
		SimpleQueuedEntity simpleQueue = new SimpleQueuedEntity("SQE", new ContinuousUniformDistribution(processTime, processTime));
		
		//10 messages of length 0.4 take excatly 4 units of time to complete (each tick process 2.5 messages).
		TestMessageImpl[] testMessageImpls = new TestMessageImpl[3];
		for (int i = 0; i < testMessageImpls.length; i++) {
			testMessageImpls[i] = new TestMessageImpl();
			simpleQueue.sendMessage(testMessageImpls[i]);
			assertEquals(processTime, testMessageImpls[i].getProcessTime());
		}

		assertEquals(3, simpleQueue.getMessageQueue().size());
		Clock.getInstance().addEntities(simpleQueue);
		
		Clock.getInstance().countToTick(1);
		assertEquals(1, simpleQueue.getMessageQueue().size());
		assertTrue(testMessageImpls[0].isProcessed());
		assertTrue(testMessageImpls[1].isProcessed());
		
		Clock.getInstance().countToTick(2);
		assertEquals(0, simpleQueue.getMessageQueue().size());
		assertTrue(testMessageImpls[2].isProcessed());
	}
}
