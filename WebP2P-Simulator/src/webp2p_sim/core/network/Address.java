package webp2p_sim.core.network;

/**
 * Represents an IP address and a port. 
 */
public class Address {

	private final String host;
	private final long numeric;

	public Address(int firstByte, int secondByte, int thirdByte, int fourthByte) {
		this.host = firstByte + "." + secondByte + "." + thirdByte + "." + fourthByte;
		this.numeric = Long.parseLong(firstByte + "" + secondByte + "" + thirdByte + "" + fourthByte);
	}

	public String getHost() {
		return host;
	}

	public long getNumericHost() {
		return numeric;
	}
	
	@Override
	public String toString() {
		return getHost();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + (int) (numeric ^ (numeric >>> 32));
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
		return true;
	}



}