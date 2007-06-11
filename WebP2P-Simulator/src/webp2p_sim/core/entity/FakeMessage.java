package webp2p_sim.core.entity;

public class FakeMessage implements Message {

	private int numberOfTicks;
	private int numberOfProcess;
	private int ticks;
	private int process;
	
	public FakeMessage(int numberOfTicks, int numberOfProcess) {
		this.numberOfTicks = numberOfTicks;
		this.numberOfProcess = numberOfProcess;
		
		this.ticks = 0;
		this.process = 0;
	}

	public void setProcessTime(double processTime) {
	}
	
	public void process() {
		this.process++;
	}
	
	public int getProcessed() {
		return this.process;
	}

	public boolean isDone() {
		return this.ticks >= this.numberOfTicks;
	}

	public void setEntity(NetworkEntity entity) {
	}

	public void tickOcurred() {
		this.ticks++;
	}

	public void verify() {
		if (this.ticks != this.numberOfTicks) throw new RuntimeException("The number of calls to 'tickOcurred()' was " + this.ticks + ". Expected: " + this.numberOfTicks + ".");
		if (this.process != this.numberOfProcess) throw new RuntimeException("The number of calls to 'process()' was " + this.process + ". Expected: " + this.numberOfProcess + ".");
	}
}
