package webp2p_sim.core.entity;

public interface ApplicationMessage extends TimedEntity {

	public void process();

	public boolean isDone();

	public void setReceiverEntity(NetworkEntity entity);
	
	public void setCallerEntity(NetworkEntity entity);

	public void setProcessTime(double processTime);

	public double getProcessTime();
	
	public long size();
}
