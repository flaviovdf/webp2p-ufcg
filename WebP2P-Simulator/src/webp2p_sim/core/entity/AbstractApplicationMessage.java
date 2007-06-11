package webp2p_sim.core.entity;

public abstract class AbstractApplicationMessage implements ApplicationMessage {

	private long processTime;
	protected NetworkEntity entity;

	public AbstractApplicationMessage() {
		this.processTime = 0;
	}
	
	public final boolean isDone() {
		return processTime <= 0;
	}
	
	public final void tickOcurred() {
		processTime--;
	}

	public final void setProcessTime(double processTime) {
		this.processTime = Math.round(processTime);
	}
	
	public final void setEntity(NetworkEntity entity) {
		this.entity = entity;
	}
	
	//FIXME
	public long size() {
		return 0;
	}
}
