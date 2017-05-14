package workloads;

import java.util.List;

import processing.ProcessingUnit;
import requests.Request;

public abstract class Workload {
    protected static final long GPU_REQUESTS = 1000;
    protected static final long CPU_REQUESTS = 1000;
    protected static final long REQUEST_SEPARATION = 1;

    protected List<Request> load;
    protected List<Request> gpuReqs;
    protected List<Request> cpuReqs;

    public abstract List<Request> newWorkload(ProcessingUnit systemEntry);
    public abstract List<Request> workload();
    public abstract List<Request> cpuWorkload();
    public abstract List<Request> gpuWorkload();
}
