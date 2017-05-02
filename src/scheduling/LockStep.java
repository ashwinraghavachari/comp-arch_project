package scheduling;

import java.util.ArrayList;
import java.util.List;

import processing.ProcessingUnit;
import requests.Request;
import utils.FREQ_STATE;

public class LockStep implements SchedulingPolicy {

    private ProcessingUnit cpu;
    private ProcessingUnit gpu;
    
    private final static int THRESHOLD_LOW = SCHEDULING_THRESHOLDS.LOW.getThreshold();
    private final static int THRESHOLD_MID = SCHEDULING_THRESHOLDS.MID.getThreshold();
    private final static int THRESHOLD_HIGH = SCHEDULING_THRESHOLDS.HIGH.getThreshold();
    
    List<Request> reqsInSystem = new ArrayList<>();

    @Override
    public void setFrequencies(Request req) {
        if(reqsInSystem.remove(req))
        {
            //Leaving system
        }
        else
        {
            //Entering System 
            reqsInSystem.add(req);
        }

        if(reqsInSystem.size() < THRESHOLD_LOW){
            cpu.setFreq(FREQ_STATE.CPU0);
            gpu.setFreq(FREQ_STATE.GPU0);
        }
        else if (reqsInSystem.size() < THRESHOLD_MID){
            cpu.setFreq(FREQ_STATE.CPU1);
            gpu.setFreq(FREQ_STATE.GPU1);
        }
        else if (reqsInSystem.size() < THRESHOLD_HIGH){
            cpu.setFreq(FREQ_STATE.CPU2);
            gpu.setFreq(FREQ_STATE.GPU2);
        }
        else {
            cpu.setFreq(FREQ_STATE.CPU3);
            gpu.setFreq(FREQ_STATE.GPU2);
        }
    }

    public void setCPU(ProcessingUnit cpu){
        this.cpu = cpu;
    }

    public void setGPU(ProcessingUnit gpu){
        this.gpu = gpu;
    }

}
