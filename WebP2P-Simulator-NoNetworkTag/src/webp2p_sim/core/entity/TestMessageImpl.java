package webp2p_sim.core.entity;

public class TestMessageImpl extends AbstractApplicationMessage {

	private boolean processed = false;
	
	@Override
	/**
	 * This method will throw a runtime exception if process is called more than once.
	 */
	public void process() {
		if (processed) {
			throw new RuntimeException("Process cannot be called more than once.");
		}
		
		processed = true;
	}
	
	public boolean isProcessed() {
		return processed;
	}
	
}
