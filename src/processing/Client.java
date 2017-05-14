package processing;
import java.util.ArrayList;
import java.util.List;

import requests.Request;
import simulator.Simulator;
import workloads.Workload;


public class Client extends ProcessingUnit{

    Simulator sim;
    List<Request> successfulReqs = new ArrayList<>();
    List<Request> failedReqs = new ArrayList<>();
    
    private final ProcessingUnit systemEntry;
    private final Workload workload;
        
    public Client(Simulator sim, ProcessingUnit systemEntry, Workload workload) {
        this.sim = sim;
        this.systemEntry = systemEntry;
        
        this.workload = workload;
        sim.addWorkload(workload.newWorkload(systemEntry));
    }

    @Override
    public void processRequest(Request req) {
        long sla = req.getSLA();
        long finishTime = sim.getCurrentTime();
        long entryTime = req.getEntryTime();

        long runTime = finishTime - entryTime;
        req.setTotalRunTime(runTime);
        
        if(runTime <= sla)
        {
            successfulReqs.add(req);
        }
        else
        {
            failedReqs.add(req);
        }
    }

	@Override
	public double getTotalEnergyUsage(){
		return 0;
	}
    
    @Override
    public void printSummary()
    {
        System.out.println("Total reqs sent:\t" + workload.workload().size());
        System.out.println("Successful sla reqs:\t" + successfulReqs.size());
        System.out.println("failed sla reqs:\t" + failedReqs.size());
        System.out.println("average +sla time(ns):\t" + 
                successfulReqs.stream().map(r -> r.getTotalRunTime()).reduce(Long::sum).get()
                /(long)successfulReqs.size());
        System.out.println("average -sla time(ns):\t" + 
                failedReqs.stream().map(r -> r.getTotalRunTime()).reduce(Long::sum).get()
                /(long)failedReqs.size());
        System.out.println("average cpu req time(ns):\t" + 
                workload.cpuWorkload().stream().map(r -> r.getTotalRunTime()).reduce(Long::sum).get()
                /(long)failedReqs.size());
        System.out.println("average gpu req time(ns):\t" + 
                workload.gpuWorkload().stream().map(r -> r.getTotalRunTime()).reduce(Long::sum).get()
                /(long)failedReqs.size());

        System.out.println("end time:\t" + sim.getCurrentTime());
    }

    @Override
    public long getDelayTime()
    {
        return sim.getCurrentTime();
    }
    
}
