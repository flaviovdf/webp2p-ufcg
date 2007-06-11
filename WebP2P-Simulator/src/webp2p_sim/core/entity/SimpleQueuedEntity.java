package webp2p_sim.core.entity;

import java.util.LinkedList;
import java.util.Queue;

import edu.uah.math.distributions.Distribution;
import edu.uah.math.distributions.RandomVariable;

public class SimpleQueuedEntity implements NetworkEntity, TimedEntity {

	private Queue<ApplicationMessage> queue;

	private RandomVariable rv;

	private ApplicationMessage currentMessage;

	private String name;

	public SimpleQueuedEntity(String name, Distribution distribution) {
		this.name = name;
		this.rv = new RandomVariable(distribution);
		this.queue = new LinkedList<ApplicationMessage>();
		this.currentMessage = null;
	}

	public void sendMessage(ApplicationMessage applicationMessage) {
		applicationMessage.setProcessTime(rv.simulate());
		applicationMessage.setEntity(this);
		queue.offer(applicationMessage);
	}

	public void tickOcurred() {
		if (currentMessage == null) {
			currentMessage = queue.poll();
		} else {
			currentMessage.tickOcurred();
		}
		while (currentMessage != null && currentMessage.isDone()) {
			currentMessage.process();
			currentMessage = queue.poll();
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return this.getName();
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SimpleQueuedEntity)) return false;
		SimpleQueuedEntity parameter = (SimpleQueuedEntity) obj;
		return this.name.equals(parameter.getName());
	}
}
