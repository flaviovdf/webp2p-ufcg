package webp2p_sim.server;

import webp2p_sim.core.entity.NetworkEntity;
import webp2p_sim.proxy.Request;

public interface ContentIF extends NetworkEntity {
	public void getContent(Request request);
}
