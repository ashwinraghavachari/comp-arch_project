
public abstract class Request implements Comparable<Request>{
	private boolean needGPU;
	//private boolean needCPU;
	private int SLA;

	protected int startTime;
	private int cyclecount;
	private int startGPUCycles;
	private int CPUCycles;
	protected ProcessingUnit destination;
	
	public boolean isNeedGPU() {
		return needGPU;
	}
	public int getSLA() {
		return SLA;
	}
	public int getCyclecount() {
		return cyclecount;
	}
	public int getStartGPUCycles() {
		return startGPUCycles;
	}
	public int getCPUCycles() {
		return CPUCycles;
	}

	public ProcessingUnit getDestination() {
		return destination;
	}
	public void setDestination(ProcessingUnit destination) {
		this.destination = destination;
	}
	
	@Override
	public int compareTo(Request req)
	{
		return Integer.compare(startTime, req.startTime);
	}
	
	public abstract void processRequest();
	
}
