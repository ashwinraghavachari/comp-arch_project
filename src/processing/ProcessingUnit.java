package processing;

import requests.Request;

public abstract class ProcessingUnit {

	public abstract void processRequest(Request req);
	
	ProcessingUnit inboundPU;
	ProcessingUnit outboundPU;

    public void addInbound(ProcessingUnit pu) {
        inboundPU = pu;
    }

    public void addOutbound(ProcessingUnit pu) {
        outboundPU = pu;
    }
}
