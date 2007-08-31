package webp2p_sim.server;

import org.apache.log4j.Logger;

import webp2p_sim.core.entity.NetworkEntity;

public class DefaultReplicationStrategy implements ReplicationStrategy {

	private static final Logger LOG = Logger.getLogger( DefaultReplicationStrategy.class );
	
	public void performReplication(WebServer server, ReplicationInfo info) {
		for (NetworkEntity adj : server.getAdj()) {
			if (!info.hasReplica(adj)) {
				LOG.debug( "Replication for " + info.getUrl() + " requested to " + adj );
				info.replicationRequested(adj);
				adj.sendMessage(new CreateReplicaOfUrlMessage(info.getUrl(), server));
				break;
			}
		}
	}
}
