package webp2p_sim.core.entity;

import webp2p_sim.core.Clock;

public abstract class AbstractApplicationMessage implements ApplicationMessage {

	private final double PRECISON = 1000;
	private double processTime;
	protected NetworkEntity receiverEntity;
	protected NetworkEntity callerEntity;

	public AbstractApplicationMessage() {
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

	//FIXME
	public long size() {
		return 0;
	}
}
