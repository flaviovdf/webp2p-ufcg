package webp2p_sim.core.network;

import java.util.HashSet;
import java.util.Set;

/**
 * A Bandwidth allocation algorithm that used the same channel for
 * upload and download.
 * <br>
 * This algorithm distributes bandwidth equally between connections.
 */
public class SymetricBandwidth implements Bandwidth {

	private final double maxTotal;
	
	private Set<Connection> upConnnections;
	private Set<Connection> downConnnections;
	
	/**
	 * Creates a new SymetricBandwidth.
	 * 
	 * @param maxTotal In bits per second
	 */
	public SymetricBandwidth(long maxTotal) {
		this.maxTotal = maxTotal;
		this.upConnnections = new HashSet<Connection>();
		this.downConnnections = new HashSet<Connection>();
	}
	
	public void allocateDownBand(Connection connection) {
		this.downConnnections.add(connection);
	}

	public void allocateUpBand(Connection connection) {
		this.upConnnections.add(connection);
	}

	public long getAllocatedDownBand(Connection connection) {
		if (!downConnnections.contains(connection)) {
			throw new NetworkException("Connection " + connection + " not allocated for Bandwitdh " + this);
		}
		
		return Math.round(maxTotal / (downConnnections.size() + upConnnections.size()));
	}

	public long getAllocatedUpBand(Connection connection) {
		if (!upConnnections.contains(connection)) {
			throw new NetworkException("Connection " + connection + " not allocated for Bandwitdh " + this);
		}
		
		return Math.round(maxTotal / (downConnnections.size() + upConnnections.size()));
	}

	public void deallocateDownBand(Connection connection) {
		if (!downConnnections.contains(connection)) {
			throw new NetworkException("Connection " + connection + " not allocated for Bandwitdh " + this);
		}
		
		this.downConnnections.remove(connection);
	}

	public void deallocateUpBand(Connection connection) {
		if (!upConnnections.contains(connection)) {
			throw new NetworkException("Connection " + connection + " not allocated for Bandwitdh " + this);
		}
		
		this.upConnnections.remove(connection);
	}

	@Override
	public long getTotalDownBand() {
		return (long) maxTotal;
	}

	@Override
	public long getTotalUpBand() {
		return (long) maxTotal;
	}

}
