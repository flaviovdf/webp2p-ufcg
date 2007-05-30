package webp2p_sim.core.entity;

public abstract class AbstractMessage implements Message {

	private long processTime;
	protected QueuedEntity entity;

	public AbstractMessage() {
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
	
	public final void setEntity(QueuedEntity entity) {
		this.entity = entity;
	}
}
