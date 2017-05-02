package requests;

public class GenericCPURequest extends Request {
    
    private static final long GPU_CYCLES = 1000;
    private static final long CPU_CYCLES = 1000;
    private static final long PRE_GPU_CYCLES = 1000;

    public GenericCPURequest(long entryTime) {
        this.entryTime = entryTime;
        GPUCycles = GPU_CYCLES;
        CPUCycles = CPU_CYCLES;
        preGPUCycles = PRE_GPU_CYCLES;
    }

    @Override
    public void processRequest() {
        destination.processRequest(this);
    }

}
