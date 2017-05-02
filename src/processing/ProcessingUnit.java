package processing;

import requests.Request;
import utils.FREQ_STATE;

public abstract class ProcessingUnit {

	public abstract void processRequest(Request req);
	
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
}
