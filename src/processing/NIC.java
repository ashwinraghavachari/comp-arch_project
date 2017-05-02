package processing;
import java.util.ArrayList;
import java.util.List;

import requests.Request;
import simulator.Simulator;
import utils.FREQ_STATE;

public class NIC extends ProcessingUnit{

    private List<Request> seen = new ArrayList<>();
    private Simulator sim;
    private static final long NIC_TO_CPU_DELAY = 0;

	public NIC(Simulator sim) {
	    this.sim = sim;
    }

    @Override
	public void processRequest(Request req) {
        //if successfully removed, the request has been seen
        if(seen.remove(req))
        {
            //outgoing -> send to Client
            req.setDestination(outboundPU);
            req.setStart(sim.getCurrentTime());
            sim.addReq(req);
        }
        else
        {
            //incoming -> send to CPU
            seen.add(req);
            req.setDestination(inboundPU);
            req.setStart(sim.getCurrentTime() + NIC_TO_CPU_DELAY);
            sim.addReq(req);
        }
        
        sim.getPolicy().setFrequencies(req);
	}

	@Override
	public double getTotalEnergyUsage(){
		return 0;
	}

    @Override
    public long getDelayTime() {
        return sim.getCurrentTime();
    }

    @Override
    public void printSummary() {
        System.out.println("***NIC***");
        System.out.println("");
    }
}
