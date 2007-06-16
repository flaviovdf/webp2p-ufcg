package webp2p_sim.server;

import org.apache.log4j.Logger;

public class DefaultReplicationStrategy implements ReplicationStrategy {

	private static final Logger LOG = Logger.getLogger( DefaultReplicationStrategy.class );
	
	public void performReplication(WebServer server, ReplicationInfo info) {
		for (WebServer adj : server.getAdj()) {
			if (!info.hasReplica(adj)) {
				LOG.debug( "Replication for " + info.getUrl() + " requested to " + adj );
				info.replicationRequested(adj);
				adj.sendMessage(new CreateReplicaOfUrlMessage(info.getUrl(), server));
				break;
			}
		}
	}
}
