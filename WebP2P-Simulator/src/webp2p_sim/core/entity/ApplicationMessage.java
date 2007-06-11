package webp2p_sim.core.entity;

public interface ApplicationMessage extends TimedEntity {

	public void process();

	public boolean isDone();

	public void setEntity(NetworkEntity entity);

	public void setProcessTime(double processTime);

	public long size();
}
