package webp2p_sim.core;


import org.easymock.classextension.EasyMock;

import webp2p_sim.core.entity.TimedEntity;
import webp2p_sim.util.SmartTestCase;

public class ClockTest extends SmartTestCase {

	private Clock clock;

	protected void setUp() throws Exception {
		super.setUp();
		
		this.clock = Clock.getInstance();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCountTick1() {
		assertEquals(0, this.clock.getCurrentTick());
		
		this.clock.countToTick(1);
		assertEquals(1, this.clock.getCurrentTick());
		
		this.clock.countToTick(2);
		assertEquals(2, this.clock.getCurrentTick());
		
		TimedEntity entityMock1 = EasyMock.createStrictMock(TimedEntity.class);
		
		entityMock1.tickOcurred();
		
		EasyMock.replay(entityMock1);
		this.clock.addEntities(entityMock1);
		this.clock.countToTick(3);
		EasyMock.verify(entityMock1);
	}
	
	public void testCountTick2() {
		TimedEntity entityMock1 = EasyMock.createStrictMock(TimedEntity.class);
		TimedEntity entityMock2 = EasyMock.createStrictMock(TimedEntity.class);
		
		entityMock1.tickOcurred();
		entityMock2.tickOcurred();
		
		EasyMock.replay(entityMock1, entityMock2);
		this.clock.addEntities(entityMock1);
		this.clock.addEntities(entityMock2);
		this.clock.countToTick(1);
		EasyMock.verify(entityMock1, entityMock2);
	}
	
	public void testCountTick3() {
		TimedEntity entityMock1 = EasyMock.createStrictMock(TimedEntity.class);
		TimedEntity entityMock2 = EasyMock.createStrictMock(TimedEntity.class);
		
		entityMock2.tickOcurred();
		
		EasyMock.replay(entityMock1, entityMock2);
		this.clock.addEntities(entityMock1);
		this.clock.addEntities(entityMock2);
		this.clock.removeEntity(entityMock1);
		this.clock.countToTick(1);
		EasyMock.verify(entityMock1, entityMock2);
	}
}
