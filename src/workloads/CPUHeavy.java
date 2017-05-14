package workloads;

import java.util.ArrayList;
import java.util.List;

import processing.ProcessingUnit;
import requests.GenericCPURequest;
import requests.GenericGPURequest;
import requests.Request;

public class CPUHeavy extends Workload{
    
    @Override
    public List<Request> newWorkload(ProcessingUnit systemEntry) {
        load = new ArrayList<>();
        gpuReqs = new ArrayList<>();
        cpuReqs = new ArrayList<>();
        
        for(long i = 0; i < GPU_REQUESTS; i++)
        {
            gpuReqs.add(new GenericGPURequest(i*REQUEST_SEPARATION, systemEntry));
        }
        for(long i = 0; i < CPU_REQUESTS; i++)
        {
            cpuReqs.add(new GenericCPURequest(i*REQUEST_SEPARATION, systemEntry));
        }

        load.addAll(gpuReqs);
        load.addAll(cpuReqs);
        
        return load;
    }

    @Override
    public List<Request> workload() {
        return load;
    }

    @Override
    public List<Request> cpuWorkload() {
        return cpuReqs;
    }

    @Override
    public List<Request> gpuWorkload() {
        return gpuReqs;
    }

}
