package webp2p_sim.server;

public interface ReplicationStrategy {

	public void performReplication(WebServer server, ReplicationInfo info);

}
