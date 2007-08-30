package webp2p_sim.core.entity;

import webp2p_sim.core.Clock;

public abstract class AbstractApplicationMessage implements ApplicationMessage {

	private final double PRECISON = 1000;
	private double processTime;
	protected NetworkEntity entity;

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
	
	public final void setEntity(NetworkEntity entity) {
		this.entity = entity;
	}
	
	public double getProcessTime() {
		return processTime;
	}
	
	//FIXME
	public long size() {
		return 0;
	}
}
