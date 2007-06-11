package webp2p_sim.core.network;

/**
 * Represents an IP address and a port. 
 */
public class Address {

	private final String host;
	private final long numeric;
	private final int port;

	public Address(int firstByte, int secondByte, int thirdByte, int fourthByte, int port) {
		this.host = firstByte + "." + secondByte + "." + thirdByte + "." + fourthByte;
		this.numeric = Long.parseLong(firstByte + "" + secondByte + "" + thirdByte + "" + fourthByte);
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
	
	public long getNumericHost() {
		return numeric;
	}
	
	@Override
	public String toString() {
		return getHost() + ":" + getPort();
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((host == null) ? 0 : host.hashCode());
		result = PRIME * result + (int) (numeric ^ (numeric >>> 32));
		result = PRIME * result + port;
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
		final Address other = (Address) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (numeric != other.numeric)
			return false;
		if (port != other.port)
			return false;
		return true;
	}


	
}