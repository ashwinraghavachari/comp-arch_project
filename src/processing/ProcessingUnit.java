package processing;

import java.util.List;

import requests.Request;
import utils.FREQ_STATE;

public abstract class ProcessingUnit {

	public abstract void processRequest(Request req);
	public abstract long getDelayTime();
	
	ProcessingUnit inboundPU;
	ProcessingUnit outboundPU;
	protected double freq;

    public void addInbound(ProcessingUnit pu) {
        inboundPU = pu;
    }

    public void addOutbound(ProcessingUnit pu) {
        outboundPU = pu;
    }
    
    public void setFreq(FREQ_STATE f){
		this.freq = f.getFreq();
	}

    public abstract void printSummary();
    public abstract List<String> getHeaders();
    public abstract List<String> getSummary();
	public abstract double getTotalEnergyUsage();
}
