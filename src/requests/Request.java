package requests;

import processing.ProcessingUnit;

public abstract class Request implements Comparable<Request>{

    private boolean needGPU;
	private long SLA;

	protected long entryTime;
	private long startTime;
	private long cyclecount;
	//if(needGPU)
	protected long preGPUCycles;
	protected long GPUCycles;

	protected long CPUCycles;
	protected ProcessingUnit destination;
	
	public boolean needsGPU() {
		return needGPU;
	}
	public long getSLA() {
		return SLA;
	}
	public long getCyclecount() {
		return cyclecount;
	}
	public long getCPUCycles() {
		return CPUCycles;
	}

	public long getStartTime() {
        return startTime;
    }
    public long getPreGPUCycles() {
        return preGPUCycles;
    }
    public long getGPUCycles() {
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

    public long getEntryTime() {
        return entryTime;
    }
	
}
