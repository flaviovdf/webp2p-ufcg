package webp2p_sim.core.network;

/**
 * Exceptions send when a network error occurs.
 */
@SuppressWarnings("serial")
public class NetworkException extends RuntimeException {

	public NetworkException(String msg) {
		super(msg);
	}

}
