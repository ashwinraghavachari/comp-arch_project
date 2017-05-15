package requests;

import processing.ProcessingUnit;

public class GenericCPURequest extends Request {
    
    private static final long CPU_CYCLES = 1000;

    public GenericCPURequest(long entryTime, ProcessingUnit dest) {
        this.entryTime = entryTime;
        startTime = entryTime;

        needGPU = false;
        CPUCycles = CPU_CYCLES;
        cyclecount = CPU_CYCLES;
        
        destination = dest;
        
        sla = SLA;
    }

    @Override
    public void processRequest() {
        destination.processRequest(this);
    }

}
