package webp2p_sim.core.network;

import java.util.HashSet;
import java.util.Set;

/**
 * A Bandwidth allocation algorithm that separates upload and download
 * channels.
 * <br>
 * This algorithm distributes bandwidth equally between connections.
 */
public class AsymetricBandwidth implements Bandwidth {

	private final double maxUp;
	private final double maxDown;
	
	private Set<Connection> upConnnections;
	private Set<Connection> downConnnections;
	
	public AsymetricBandwidth(long maxUp, long maxDown) {
		this.maxUp = maxUp;
		this.maxDown = maxDown;
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
		
		return Math.round(maxDown / downConnnections.size());
	}

	public long getAllocatedUpBand(Connection connection) {
		if (!upConnnections.contains(connection)) {
			throw new NetworkException("Connection " + connection + " not allocated for Bandwitdh " + this);
		}
		
		return Math.round(maxUp / upConnnections.size());
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

}
