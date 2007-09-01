package webp2p_sim.core.entity;

import java.util.LinkedList;
import java.util.Queue;

import webp2p_sim.core.Clock;
import edu.uah.math.distributions.Distribution;
import edu.uah.math.distributions.RandomVariable;

public class SimpleQueuedEntity implements NetworkEntity, TimedEntity {

	private LinkedList<ApplicationMessage> queue;

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
		double simulate = rv.simulate();
		applicationMessage.setProcessTime(simulate);
		applicationMessage.setEntity(this);
		queue.addLast(applicationMessage);
	}

	public void tickOcurred() {
		//Get next message from queue
		if (currentMessage == null && !queue.isEmpty()) {
			currentMessage = queue.removeFirst();
		}

		//Decreasing message time
		if (currentMessage != null) {
			double sumTime = currentMessage.getProcessTime();
			currentMessage.tickOcurred();
		
			//Acquiring messages to process with time < 1
			if (currentMessage.isDone()) {
				LinkedList<ApplicationMessage> toProcess = new LinkedList<ApplicationMessage>();
				toProcess.add(currentMessage);
				
				while (!queue.isEmpty() && sumTime <= Clock.getInstance().getTickSize()) {
					ApplicationMessage poll = queue.removeFirst();
					sumTime += poll.getProcessTime();
					toProcess.add(poll);
				}
		
				//Remove last message if time exceeds tick size
				if (!toProcess.isEmpty() && sumTime > Clock.getInstance().getTickSize()) {
					ApplicationMessage last = toProcess.removeLast();
					
					last.setProcessTime(sumTime - Clock.getInstance().getTickSize());
					queue.addFirst(last);
				}
				
				for(ApplicationMessage message : toProcess) {
					message.setProcessTime(0);
					message.process();
				}
				
				currentMessage = null;
			}
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

	public Queue<ApplicationMessage> getMessageQueue() {
		return queue;
	}
}
