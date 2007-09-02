package webp2p_sim.common;

import webp2p_sim.core.entity.NetworkEntity;

public interface RequestCallBack extends NetworkEntity {
	
	void hereIsContent(long request, int result, long size);
	
}
