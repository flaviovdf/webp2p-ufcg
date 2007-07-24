package webp2p.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.metadata.Util;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory;

/**
 * A factory that creates a unique <code>RequestProcessorFactory</code> per class type.
 */
public class StatefulProcessorFactoryFactory implements RequestProcessorFactoryFactory {

	private Map<Class, RequestProcessorFactory> requestProcessorFactoriesMap;
	
	public StatefulProcessorFactoryFactory() {
		this.requestProcessorFactoriesMap = new HashMap<Class, RequestProcessorFactory>();
	}

	public RequestProcessorFactory getRequestProcessorFactory(final Class pClass) throws XmlRpcException {
		if (! this.requestProcessorFactoriesMap.containsKey(pClass)) {
			this.requestProcessorFactoriesMap.put(pClass, new RequestProcessorFactory() {
				private Object instance = null;
				public Object getRequestProcessor(XmlRpcRequest pRequest) throws XmlRpcException {
					if (instance == null) instance = Util.newInstance(pClass);
					return instance;
				}
			});
		}
		return this.requestProcessorFactoriesMap.get(pClass);
	}

}
