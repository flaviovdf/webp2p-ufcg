package webp2p_sim.proxy;

import webp2p_sim.core.entity.NetworkEntity;


public class Request {

	private long id;
	private String url;
	private NetworkEntity callBack;
	
	public Request(long id, String url, NetworkEntity callBack) {
		this.id = id;
		this.callBack = callBack;
		this.url = url;
	}
	
	
	public long getId() {
		return id;
	}

	public NetworkEntity getCallBack() {
		return callBack;
	}

	public String getUrl() {
		return url;
	}


	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((callBack == null) ? 0 : callBack.hashCode());
		result = PRIME * result + (int) (id ^ (id >>> 32));
		result = PRIME * result + ((url == null) ? 0 : url.hashCode());
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
		final Request other = (Request) obj;
		if (callBack == null) {
			if (other.callBack != null)
				return false;
		} else if (!callBack.equals(other.callBack))
			return false;
		if (id != other.id)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
}
