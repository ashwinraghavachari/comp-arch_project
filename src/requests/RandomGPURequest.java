package requests;

import java.util.Random;

import processing.ProcessingUnit;

public class RandomGPURequest extends Request {

    private static final long GPU_CYCLES_MIN = 750;
    private static final long CPU_CYCLES_MIN = 750;
    private static final long PRE_GPU_CYCLES_MIN = 750;

    private static final long MAX_ADDITIONAL_CYCLES = 500;

    public RandomGPURequest(long entryTime, ProcessingUnit dest) {
        this.entryTime = entryTime;
        startTime = entryTime;

        needGPU = true;

        Random rng = new Random();
        GPUCycles = GPU_CYCLES_MIN + rng.nextLong() % MAX_ADDITIONAL_CYCLES;
        CPUCycles = CPU_CYCLES_MIN + rng.nextLong() % MAX_ADDITIONAL_CYCLES;
        preGPUCycles = PRE_GPU_CYCLES_MIN + rng.nextLong() % MAX_ADDITIONAL_CYCLES;
        cyclecount = preGPUCycles + CPUCycles + GPUCycles;
        
        destination = dest;
        
        sla = SLA;
    }

    @Override
    public void processRequest() {
        destination.processRequest(this);
    }

}
