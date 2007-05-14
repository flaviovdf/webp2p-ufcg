package core;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import core.entity.TimedEntity;

public class ClockTest extends TestCase {

	private Clock clock;

	protected void setUp() throws Exception {
		super.setUp();
		
		this.clock = new Clock();
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
		this.clock.addEntity(entityMock1);
		this.clock.countTick();
		EasyMock.verify(entityMock1);
	}
	
	public void testCountTick2() {
		TimedEntity entityMock1 = EasyMock.createStrictMock(TimedEntity.class);
		TimedEntity entityMock2 = EasyMock.createStrictMock(TimedEntity.class);
		
		entityMock1.tickOcurred();
		entityMock2.tickOcurred();
		
		EasyMock.replay(entityMock1, entityMock2);
		this.clock.addEntity(entityMock1);
		this.clock.addEntity(entityMock2);
		this.clock.countTick();
		EasyMock.verify(entityMock1, entityMock2);
	}
	
	public void testCountTick3() {
		TimedEntity entityMock1 = EasyMock.createStrictMock(TimedEntity.class);
		TimedEntity entityMock2 = EasyMock.createStrictMock(TimedEntity.class);
		
		entityMock2.tickOcurred();
		
		EasyMock.replay(entityMock1, entityMock2);
		this.clock.addEntity(entityMock1);
		this.clock.addEntity(entityMock2);
		this.clock.removeEntity(entityMock1);
		this.clock.countTick();
		EasyMock.verify(entityMock1, entityMock2);
	}
}
