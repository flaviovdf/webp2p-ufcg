package webp2p_sim.core.network;

import webp2p_sim.core.Clock;
import webp2p_sim.core.entity.NetworkEntity;

public abstract class AbstractApplicationMessage implements ApplicationMessage {

	//2KB in bytes
	private static final long DEFAULT_XML_RPC_SIZE = 2 * 1024 * 8;
	
	private final double PRECISON = 100000;
	private double processTime;
	
	protected NetworkEntity receiverEntity;
	protected NetworkEntity callerEntity;
	
	private final long size;

	/**
	 * Creates a new Application Message.
	 * @param size In Bits
	 */
	public AbstractApplicationMessage(long size) {
		this.size = DEFAULT_XML_RPC_SIZE + size;
		this.processTime = 0;
	}
	
	/**
	 * Creates a new Application Message.
	 * @param size In Bits
	 */
	public AbstractApplicationMessage() {
		this.size = DEFAULT_XML_RPC_SIZE;
		this.processTime = 0;
	}
	
	public final boolean isDone() {
		return processTime <= 0;
	}
	
	public final void tickOcurred() {
		double newProcessTime = Math.min(processTime - Clock.getInstance().getCurrentTick(), 0);
		setProcessTime(newProcessTime);
	}

	public final void setProcessTime(double processTime) {
		this.processTime = ((long)(processTime * PRECISON ))/PRECISON;
	}
	
	public final void setReceiverEntity(NetworkEntity entity) {
		this.receiverEntity = entity;
	}
	
	public final void setCallerEntity(NetworkEntity entity) {
		this.callerEntity = entity;
	}
	
	public final double getProcessTime() {
		return processTime;
	}
	
	public final void process() {
		assert receiverEntity != null;
		realProcess();
	}
	
	public abstract void realProcess();

	public long size() {
		return size;
	}
}
