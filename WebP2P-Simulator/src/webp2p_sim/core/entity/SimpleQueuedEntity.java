package webp2p_sim.core.entity;

import java.util.LinkedList;
import java.util.Queue;

import webp2p_sim.core.Clock;
import webp2p_sim.core.network.ApplicationMessage;
import webp2p_sim.core.network.Host;
import webp2p_sim.core.network.Network;
import edu.uah.math.distributions.Distribution;
import edu.uah.math.distributions.RandomVariable;

public class SimpleQueuedEntity implements NetworkEntity, TimedEntity {

	private LinkedList<ApplicationMessage> queue;

	private RandomVariable rv;

	private ApplicationMessage currentMessage;

	private Host myHostAddress;

	private final Network network;

	public SimpleQueuedEntity(Host host, Distribution distribution, Network network) {
		this.myHostAddress = host;
		this.network = network;
		this.rv = new RandomVariable(distribution);
		this.queue = new LinkedList<ApplicationMessage>();
		this.currentMessage = null;
	}

	public void receiveMessage(ApplicationMessage applicationMessage) {
		double simulate = rv.simulate();
		applicationMessage.setProcessTime(simulate);
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
	
	public Host getHost() {
		return this.myHostAddress;
	}
	
	@Override
	public String toString() {
		return this.myHostAddress.getAddress().toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myHostAddress == null) ? 0 : myHostAddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SimpleQueuedEntity other = (SimpleQueuedEntity) obj;
		if (myHostAddress == null) {
			if (other.myHostAddress != null)
				return false;
		} else if (!myHostAddress.equals(other.myHostAddress))
			return false;
		return true;
	}

	public void unbindSelf() {
		network.unbind(getHost());
	}
	
	public void bindSelf() {
		network.bind(getHost(), this);
	}
	
	public Queue<ApplicationMessage> getMessageQueue() {
		return queue;
	}
	
	protected void sendMessage(Host receiver, ApplicationMessage message) {
		network.sendMessage(getHost(), receiver, message);
	}
}
