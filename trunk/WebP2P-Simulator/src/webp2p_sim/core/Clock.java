package webp2p_sim.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import webp2p_sim.core.entity.TimedEntity;

public class Clock {

	private List<TimedEntity> entities;
	private long ticks;
	
	private static Clock instance = null;
	
	private Clock() {
		this.entities = new LinkedList<TimedEntity>();
		this.ticks = 0;
	}
	
	public static Clock getInstance() {
		if (instance == null) {
			instance = new Clock();
		}
		
		return instance;
	}
	
	public static void reset() {
		Clock.instance = null;
	}
	
	public void addEntities(TimedEntity... timedEntity) {
		addEntities(Arrays.asList(timedEntity));
	}
	
	public void addEntities(Collection<TimedEntity> timedEntities) {
		entities.addAll(timedEntities);
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
