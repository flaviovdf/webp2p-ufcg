package webp2p_sim.util;



public class Connection {

	private final Address clientAddress;
	private final Address hostAddress;

	public Connection(Address clientAddress, Address hostAddress) {
		this.clientAddress = clientAddress;
		this.hostAddress = hostAddress;
	}

	@Override
	public String toString() {
		return "Client: " + clientAddress.toString() + " - Host:" + hostAddress.toString();
	}
	
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((clientAddress == null) ? 0 : clientAddress.hashCode());
		result = PRIME * result + ((hostAddress == null) ? 0 : hostAddress.hashCode());
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
		final Connection other = (Connection) obj;
		if (clientAddress == null) {
			if (other.clientAddress != null)
				return false;
		} else if (!clientAddress.equals(other.clientAddress))
			return false;
		if (hostAddress == null) {
			if (other.hostAddress != null)
				return false;
		} else if (!hostAddress.equals(other.hostAddress))
			return false;
		return true;
	}


	
}
