package core.entity;

import java.util.LinkedList;
import java.util.Queue;

import edu.uah.math.distributions.Distribution;
import edu.uah.math.distributions.RandomVariable;

public class SimpleQueuedEntity implements QueuedEntity, TimedEntity {

	private Queue<Message> queue;

	private RandomVariable rv;

	private Message currentMessage;

	public SimpleQueuedEntity(Distribution distribution) {
		this.rv = new RandomVariable(distribution);
		this.queue = new LinkedList<Message>();
		this.currentMessage = null;
	}

	public void sendMessage(Message message) {
		message.setProcessTime(rv.simulate());
		message.setEntity(this);
		queue.offer(message);
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
}
