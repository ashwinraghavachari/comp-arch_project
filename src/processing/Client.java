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

    private static final long GPU_REQUESTS = 1000;
    private static final long CPU_REQUESTS = 1000;
    private static final long REQUEST_SEPARATION = 1;
        
    public Client(Simulator sim, ProcessingUnit systemEntry) {
        this.sim = sim;
        for(long i = 0; i < GPU_REQUESTS; i++)
        {
            workload.add(new GenericGPURequest(i*REQUEST_SEPARATION, systemEntry));
        }
        for(long i = 0; i < CPU_REQUESTS; i++)
        {
            workload.add(new GenericCPURequest(i*REQUEST_SEPARATION, systemEntry));
        }

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
    
    public void printSummary()
    {
        int total = successfulReqs.size() + failedReqs.size();
        System.out.println("Total reqs:      " + total);
        System.out.println("Successful reqs: " + successfulReqs.size());
        System.out.println("failed reqs:     " + failedReqs.size());
        System.out.println("average success time:        " + 
                successfulReqs.stream().map(r -> r.getTotalRunTime()).reduce(Long::sum));
        System.out.println("average failure time:        " + 
                failedReqs.stream().map(r -> r.getTotalRunTime()).reduce(Long::sum));
        System.out.println("end time:        " + sim.getCurrentTime());
    }

    
}
