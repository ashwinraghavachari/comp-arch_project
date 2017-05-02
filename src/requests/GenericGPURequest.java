package requests;

import processing.ProcessingUnit;

public class GenericGPURequest extends Request {

    private static final long GPU_CYCLES = 1000;
    private static final long CPU_CYCLES = 1000;
    private static final long PRE_GPU_CYCLES = 1000;

    private static final long SLA = 1000;

    public GenericGPURequest(long entryTime, ProcessingUnit dest) {
        this.entryTime = entryTime;
        startTime = entryTime;

        needGPU = true;

        GPUCycles = GPU_CYCLES;
        CPUCycles = CPU_CYCLES;
        preGPUCycles = PRE_GPU_CYCLES;
        cyclecount = GPU_CYCLES + CPU_CYCLES + PRE_GPU_CYCLES;
        
        destination = dest;
        
        sla = SLA;
    }

    @Override
    public void processRequest() {
        destination.processRequest(this);
    }

}
