package webp2p_sim.proxy;

import webp2p_sim.core.entity.NetworkEntity;

public interface RequestCallBack extends NetworkEntity {
	void hereIsContent(long request, int result);
}
