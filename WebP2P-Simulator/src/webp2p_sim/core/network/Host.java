package webp2p_sim.core.network;

public class Host {

	private final Address address;
	private final Bandwidth bandwidth;

	public Host(Address address, Bandwidth bandwidth) {
		this.address = address;
		this.bandwidth = bandwidth;
	}

	public Address getAddress() {
		return address;
	}

	public Bandwidth getBandwidth() {
		return bandwidth;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((address == null) ? 0 : address.hashCode());
		result = PRIME * result + ((bandwidth == null) ? 0 : bandwidth.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Host other = (Host) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (bandwidth == null) {
			if (other.bandwidth != null)
				return false;
		} else if (!bandwidth.equals(other.bandwidth))
			return false;
		return true;
	}
	
}
