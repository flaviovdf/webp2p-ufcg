package webp2p_sim.core;

import java.lang.reflect.Constructor;

public class Params {

	protected long simTime;
	protected int seed;
	
	@SuppressWarnings("unchecked")
	protected <T> T extractObject(String[] definitionArray) {
		try {
			Class[] paramTypes = new Class[definitionArray.length - 1];
			Object[] oracleParams = new Object[definitionArray.length - 1];
			for (int i = 1; i < definitionArray.length; i++) {
				String param = definitionArray[i];
				String[] tokens = param.split("\\s+");
				
				String paramValue = tokens[1];
				Class<?> paramClass = double.class;
				paramTypes[i - 1] = paramClass;
				
				Object objectParam = paramClass.getConstructor(new Class[] {String.class}).newInstance(new Object[] {paramValue});
				oracleParams[i - 1] = objectParam;
			}
			
			Class<T> clazz = (Class<T>) Class.forName(definitionArray[0]);
			
			Constructor<T> constructor = clazz.getConstructor(paramTypes);
			return constructor.newInstance(oracleParams);
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}
	
	public long getSimTime() {
		return simTime;
	}

	public int getSeed() {
		return seed;
	}
}
