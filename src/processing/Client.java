package processing;
import java.util.ArrayList;
import java.util.List;

import requests.Request;
import simulator.Simulator;


public class Client extends ProcessingUnit{

    List<Request> workload = new ArrayList<>();
    Simulator sim;

    public Client(Simulator sim) {
        this.sim = sim;
        sim.addReqs(workload);
    }

    @Override
    public void processRequest(Request req) {
        long sla = req.getSLA();
        long finishTime = sim.getCurrentTime();
        long entryTime = req.getEntryTime();

        long runTime = finishTime - entryTime;
        
        if(runTime <= sla)
        {
            
        }
        else
        {
            
        }
    }

    
}
