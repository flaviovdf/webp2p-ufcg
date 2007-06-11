package webp2p_sim.core.network;

public interface Bandwidth {

	public void allocateUpBand(Connection connection);
	
	public void allocateDownBand(Connection connection);

	public long getAllocatedUpBand(Connection connection);

	public long getAllocatedDownBand(Connection connection);

	public void deallocateDownBand(Connection connection);

	public void deallocateUpBand(Connection connection);
	
}
