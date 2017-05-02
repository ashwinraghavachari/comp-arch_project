package processing;
import java.util.ArrayList;
import java.util.List;

import requests.GenericCPURequest;
import requests.GenericGPURequest;
import requests.Request;
import simulator.Simulator;


public class Client extends ProcessingUnit{

    List<Request> workload = new ArrayList<>();
    Simulator sim;
    List<Request> successfulReqs = new ArrayList<>();
    List<Request> failedReqs = new ArrayList<>();
    List<Request> gpuReqs = new ArrayList<>();
    List<Request> cpuReqs = new ArrayList<>();

    private static final long GPU_REQUESTS = 1000;
    private static final long CPU_REQUESTS = 1000;
    private static final long REQUEST_SEPARATION = 1;
        
    public Client(Simulator sim, ProcessingUnit systemEntry) {
        this.sim = sim;
        for(long i = 0; i < GPU_REQUESTS; i++)
        {
            gpuReqs.add(new GenericGPURequest(i*REQUEST_SEPARATION, systemEntry));
        }
        for(long i = 0; i < CPU_REQUESTS; i++)
        {
            cpuReqs.add(new GenericCPURequest(i*REQUEST_SEPARATION, systemEntry));
        }

        workload.addAll(gpuReqs);
        workload.addAll(cpuReqs);

        sim.addReqs(workload);
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
        System.out.println("Total reqs sent:\t" + workload.size());
        System.out.println("Successful reqs:\t" + successfulReqs.size());
        System.out.println("failed reqs:\t" + failedReqs.size());
        System.out.println("average +sla time:\t" + 
                successfulReqs.stream().map(r -> r.getTotalRunTime()).reduce(Long::sum).get()
                /(long)successfulReqs.size());
        System.out.println("average -sla time:\t" + 
                failedReqs.stream().map(r -> r.getTotalRunTime()).reduce(Long::sum).get()
                /(long)failedReqs.size());
        System.out.println("average cpu time:\t" + 
                cpuReqs.stream().map(r -> r.getTotalRunTime()).reduce(Long::sum).get()
                /(long)failedReqs.size());
        System.out.println("average gpu time:\t" + 
                gpuReqs.stream().map(r -> r.getTotalRunTime()).reduce(Long::sum).get()
                /(long)failedReqs.size());

        System.out.println("end time:\t" + sim.getCurrentTime());
    }

    @Override
    public long getDelayTime()
    {
        return sim.getCurrentTime();
    }
    
}
