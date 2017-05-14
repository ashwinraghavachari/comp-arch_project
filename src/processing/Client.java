package processing;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        long workloadSize = workload.workload().size();
        System.out.println("Total reqs sent:\t" + workloadSize);
        System.out.println("Successful sla reqs:\t" + successfulReqs.size());
        System.out.println("failed sla reqs:\t" + failedReqs.size());
        if(!successfulReqs.isEmpty())
            System.out.println("average +sla time(ns):\t" + 
                    successfulReqs.stream().map(r -> r.getTotalRunTime()).reduce(0l, Long::sum)
                    /(long)successfulReqs.size());
        if(!failedReqs.isEmpty())
            System.out.println("average -sla time(ns):\t" + 
                    failedReqs.stream().map(r -> r.getTotalRunTime()).reduce(0l, Long::sum)
                    /(long)failedReqs.size());

        System.out.println("average cpu req time(ns):\t" + 
                workload.cpuWorkload().stream().collect(Collectors.averagingLong(r -> r.getTotalRunTime())));
        System.out.println("average gpu req time(ns):\t" + 
                workload.gpuWorkload().stream().collect(Collectors.averagingLong(r -> r.getTotalRunTime())));

        System.out.println("end time:\t" + sim.getCurrentTime());
    }

    @Override
    public List<String> getHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add("Total Sent");
        headers.add("sla+ Requests");
        headers.add("sla- Requests");
        headers.add("sla+ time");
        headers.add("sla- time");
        headers.add("Average CPU req time");
        headers.add("Average GPU req time");
        headers.add("End time");
        return headers;
    }

    @Override
    public List<String> getSummary() {
        List<String> output = new ArrayList<>();
        output.add(Integer.toString(workload.workload().size()));
        output.add(Integer.toString(successfulReqs.size()));
        output.add(Integer.toString(failedReqs.size()));
        output.add(Double.toString(successfulReqs.stream().collect(Collectors.averagingLong(r -> r.getTotalRunTime()))));
        output.add(Double.toString(failedReqs.stream().collect(Collectors.averagingLong(r -> r.getTotalRunTime()))));

        output.add(Double.toString(workload.cpuWorkload().stream().collect(Collectors.averagingLong(r -> r.getTotalRunTime()))));
        output.add(Double.toString(workload.gpuWorkload().stream().collect(Collectors.averagingLong(r -> r.getTotalRunTime()))));
        output.add(Long.toString(sim.getCurrentTime()));
        return output;
    }

    @Override
    public long getDelayTime()
    {
        return sim.getCurrentTime();
    }
    
}
