package workloads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import processing.ProcessingUnit;
import requests.GenericCPURequest;
import requests.GenericGPURequest;
import requests.Request;

public class PUUniform extends Workload{

    //Larger number: more CPU
    private static final double CPU_GPU_RATIO = .5;
    
    @Override
    public List<Request> newWorkload(ProcessingUnit systemEntry) {
        load = new ArrayList<>();
        gpuReqs = new ArrayList<>();
        cpuReqs = new ArrayList<>();

        Random rng = new Random(RANDOM_SEED);
        
        for(long i = 0; i < TOTAL_REQUESTS; i++)
        {
            if(rng.nextDouble() >= CPU_GPU_RATIO)
                gpuReqs.add(new GenericGPURequest(i*REQUEST_SEPARATION, systemEntry));
            else
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
