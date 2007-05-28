package core;


import org.easymock.EasyMock;

import util.SmartTestCase;
import core.entity.TimedEntity;

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
		assertEquals(0, this.clock.getTicks());
		
		this.clock.countTick();
		assertEquals(1, this.clock.getTicks());
		
		this.clock.countTick();
		assertEquals(2, this.clock.getTicks());
		
		TimedEntity entityMock1 = EasyMock.createStrictMock(TimedEntity.class);
		
		entityMock1.tickOcurred();
		
		EasyMock.replay(entityMock1);
		this.clock.addEntities(entityMock1);
		this.clock.countTick();
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
		this.clock.countTick();
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
		this.clock.countTick();
		EasyMock.verify(entityMock1, entityMock2);
	}
}