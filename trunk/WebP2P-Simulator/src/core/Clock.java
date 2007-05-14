package core;

import java.util.LinkedList;
import java.util.List;

import core.entity.TimedEntity;

public class Clock {

	private List<TimedEntity> entities;
	private long ticks;
	
	public Clock() {
		this.entities = new LinkedList<TimedEntity>();
		this.ticks = 0;
	}
	
	public void addEntity(TimedEntity... timedEntity) {
		for (TimedEntity entity : timedEntity) {
			entities.add(entity);
		}
	}
	
	public void removeEntity(TimedEntity timedEntity) {
		entities.remove(timedEntity);
	}
	
	public void countTick() {
		this.ticks++;
		
		for (TimedEntity timedEntity : entities) {
			timedEntity.tickOcurred();
		}
	}

	public long getTicks() {
		return ticks;
	}
}
