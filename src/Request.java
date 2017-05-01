
public abstract class Request implements Comparable<Request>{

    private boolean needGPU;
	private int SLA;

	protected long entryTime;
	protected long startTime;
	private int cyclecount;
	//if(needGPU)
	private int preGPUCycles;
	private int GPUCycles;

	private int CPUCycles;
	protected ProcessingUnit destination;
	
	public boolean needsGPU() {
		return needGPU;
	}
	public int getSLA() {
		return SLA;
	}
	public int getCyclecount() {
		return cyclecount;
	}
	public int getCPUCycles() {
		return CPUCycles;
	}

	public long getStartTime() {
        return startTime;
    }
    public int getPreGPUCycles() {
        return preGPUCycles;
    }
    public int getGPUCycles() {
        return GPUCycles;
    }

	public ProcessingUnit getDestination() {
		return destination;
	}
	public void setDestination(ProcessingUnit destination) {
		this.destination = destination;
	}
    public void setStart(long startTime) {
        this.startTime = startTime;
    }
	
	@Override
	public int compareTo(Request req)
	{
		return Long.compare(startTime, req.startTime);
	}
	
	public abstract void processRequest();
	
}
