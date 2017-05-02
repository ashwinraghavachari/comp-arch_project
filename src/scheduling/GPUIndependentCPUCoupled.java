package scheduling;

import java.util.ArrayList;
import java.util.List;

import processing.ProcessingUnit;
import requests.Request;
import utils.FREQ_STATE;

public class GPUIndependentCPUCoupled implements SchedulingPolicy {

    private ProcessingUnit cpu;
    private ProcessingUnit gpu;

    private final static int THRESHOLD_LOW = SCHEDULING_THRESHOLDS.LOW.getThreshold();
    private final static int THRESHOLD_MID = SCHEDULING_THRESHOLDS.MID.getThreshold();
    private final static int THRESHOLD_HIGH = SCHEDULING_THRESHOLDS.HIGH.getThreshold();

    List<Request> cpuReqsInSystem = new ArrayList<>();
    List<Request> gpuReqsInSystem = new ArrayList<>();

    @Override
    public void setFrequencies(Request req) {

        if(req.needsGPU() || gpuReqsInSystem.contains(req))
        {
            //Uses gpu
            if(!gpuReqsInSystem.remove(req))
            {
                gpuReqsInSystem.add(req);
            }
            updateGPU();
        }

        //Uses cpu
        if(!cpuReqsInSystem.remove(req))
        {
            cpuReqsInSystem.add(req);
        }
        updateCPU();
    }
    
    private void updateGPU()
    {
        if(gpuReqsInSystem.size() < THRESHOLD_LOW){
            gpu.setFreq(FREQ_STATE.GPU0);
        }
        else if (gpuReqsInSystem.size() < THRESHOLD_MID){
            gpu.setFreq(FREQ_STATE.GPU1);
        }
        else if (gpuReqsInSystem.size() < THRESHOLD_HIGH){
            gpu.setFreq(FREQ_STATE.GPU2);
        }
        else {
            gpu.setFreq(FREQ_STATE.GPU2);
        }
    }

    private void updateCPU()
    {
        
        if(cpuReqsInSystem.size() < THRESHOLD_LOW){
            cpu.setFreq(FREQ_STATE.CPU0);
        }
        else if (cpuReqsInSystem.size() < THRESHOLD_MID){
            cpu.setFreq(FREQ_STATE.CPU1);
        }
        else if (cpuReqsInSystem.size() < THRESHOLD_HIGH){
            cpu.setFreq(FREQ_STATE.CPU2);
        }
        else {
            cpu.setFreq(FREQ_STATE.CPU3);
        }
    }

    public void setCPU(ProcessingUnit cpu){
        this.cpu = cpu;
    }

    public void setGPU(ProcessingUnit gpu){
        this.gpu = gpu;
    }

}
