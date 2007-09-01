package webp2p_sim.server;

import org.apache.log4j.Logger;

import webp2p_sim.core.network.Host;

public class DefaultReplicationStrategy implements ReplicationStrategy {

	private static final Logger LOG = Logger.getLogger( DefaultReplicationStrategy.class );
	
	public void performReplication(WebServer sender, ReplicationInfo info) {
		for (Host adj : sender.getAdj()) {
			if (!info.hasReplica(adj)) {
				LOG.info( "Replication for " + info.getUrl() + " requested to " + adj );
				info.replicationRequested(adj);
				sender.staSendMessage(adj, new CreateReplicaOfUrlMessage(info.getUrl(), sender.getHost()));
				break;
			}
		}
	}
}
