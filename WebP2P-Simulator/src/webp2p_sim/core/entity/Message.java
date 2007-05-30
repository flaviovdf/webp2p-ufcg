package webp2p_sim.core.entity;

public interface Message extends TimedEntity {

	public void process();

	public boolean isDone();

	public void setEntity(QueuedEntity entity);

	public void setProcessTime(double processTime);

}
